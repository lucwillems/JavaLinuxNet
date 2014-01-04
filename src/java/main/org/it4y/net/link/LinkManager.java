package org.it4y.net.link;

import org.it4y.jni.libnetlink;
import org.it4y.jni.linuxutils;
import org.it4y.net.netlink.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by luc on 1/3/14.
 */
public class LinkManager extends Thread implements libnetlink.rtnl_accept {

    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock rlock = rwl.readLock();
    private final Lock wlock = rwl.writeLock();
    private Logger log = LoggerFactory.getLogger(LinkManager.class);
    private HashMap<String, NetworkInterface> interfaceList;
    private libnetlink.rtnl_handle handle;
    private ByteBuffer messageBuffer = ByteBuffer.allocateDirect(8129);
    private int groups = 0;
    private int initstate = 0;
    private boolean prevActiveState = false;

    public LinkManager() {
        super("netlink-linkmanager");
        log.debug("init linkmanager");
        interfaceList = new HashMap<String, NetworkInterface>();
        handle = new libnetlink.rtnl_handle();
        this.setDaemon(true);
    }

    public void InitNetLink() {
        //we are intrested in Routing/link/address events
        groups = libnetlink.linux.rtnetlink.RTMGRP_IPV4_IFADDR |
                libnetlink.linux.rtnetlink.RTMGRP_IPV4_ROUTE |
                libnetlink.linux.rtnetlink.RTMGRP_IPV4_MROUTE |
                libnetlink.linux.rtnetlink.RTMGRP_LINK;
        linuxutils.rtnl_open_byproto(handle, groups, libnetlink.linux.netlink.NETLINK_ROUTE);
        log.debug("linkmanager started");
    }

    public void run() {

        //init netlink
        InitNetLink();

        //wait for netlink messages
        while (true) {
            if (initstate < 4) {
                //we can handle only 1 wilddump at the same time, so we have a little stepping program here
                switch (initstate) {
                    case 0:
                        log.debug("dump link");
                        linuxutils.rtnl_wilddump_request(handle, 0, libnetlink.linux.rtnetlink.RTM_GETLINK);
                        initstate++;
                        break;
                    case 1:
                        log.debug("dump addresses");
                        linuxutils.rtnl_wilddump_request(handle, 0, libnetlink.linux.rtnetlink.RTM_GETADDR);
                        initstate++;
                        break;
                    case 2:
                        log.debug("dump routing");
                        linuxutils.rtnl_wilddump_request(handle, 0, libnetlink.linux.rtnetlink.RTM_GETROUTE);
                        initstate++;
                        break;
                    default:
                        log.debug("link manager ready");
                        initstate = 4; //finished
                }
            }
            //rtnl_listen is blocking until rtnl_accept interface returns rtl_accept_STOP.
            //when stop, the listen will return and thread can continue...
            messageBuffer.rewind();
            messageBuffer.order(ByteOrder.LITTLE_ENDIAN);
            linuxutils.rtnl_listen(handle, messageBuffer, this);
        }

    }

    public int accept(ByteBuffer message) {
        NlMessage msg = NetlinkMsgFactory.processRawPacket(message);
        if (msg != null) {
            log.debug("message:{} type={}", msg.getClass().getSimpleName(), msg.getNlMsgType());
            log.trace("{}", msg);
            wlock.lock();
            try {
                if (msg instanceof interfaceInfoMsg) {
                    handleLinkMessages((interfaceInfoMsg) msg);
                } else if (msg instanceof interfaceAddressMsg) {
                    handleAddressMessages((interfaceAddressMsg) msg);
                } else if (msg instanceof routeMsg) {
                    handleRoutingMessage((routeMsg) msg);
                }
            } finally {
                wlock.unlock();
            }
            //continue or stop listening ?
            return msg.moreMessages() ? libnetlink.rtl_accept_CONTINUE : libnetlink.rtl_accept_STOP;
        }
        return libnetlink.rtl_accept_STOP;
    }

    private void handleLinkMessages(interfaceInfoMsg msg) {
        //get interface name
        String name = msg.getRTAMessage("ifname") != null ? msg.getRTAMessage("ifname").getString() : null;
        if (name == null)
            return;
        //do we know this interface ?
        NetworkInterface x = interfaceList.get(name);

        switch (msg.getNlMsgType()) {
            case libnetlink.linux.rtnetlink.RTM_NEWLINK: //add or update interface to list
                if (x == null) { //new interface
                    x = new NetworkInterface(name, msg.getInterfaceIndex(), msg.getInterfaceFlags());
                    prevActiveState=false;
                    if (msg.getRTAMessage(libnetlink.linux.if_link.IFLA_ADDRESS) != null) {
                        x.setMacAddress(msg.getRTAMessage(libnetlink.linux.if_link.IFLA_ADDRESS).getHexString());
                    }
                    if (msg.getRTAMessage(libnetlink.linux.if_link.IFLA_MTU) != null) {
                        x.setmtu(msg.getRTAMessage(libnetlink.linux.if_link.IFLA_MTU).getInt());
                    }
                    if (msg.getRTAMessage(libnetlink.linux.if_link.IFLA_OPERSTATE) != null) {
                        x.setState((int) msg.getRTAMessage(libnetlink.linux.if_link.IFLA_OPERSTATE).getByte() & 0xff);
                    }
                    interfaceList.put(name, x);
                    log.info("new interface {}", x);
                    sendLinkNotification(LinkNotification.EventAction.New, LinkNotification.EventType.Link, x);
                } else {
                    //update state
                    prevActiveState=x.isActive();
                    boolean doNotification=false;
                    if (msg.getRTAMessage("operstate") != null) {
                        int newState = (int) msg.getRTAMessage(libnetlink.linux.if_link.IFLA_OPERSTATE).getByte() & 0xff;
                        if (x.getState() != newState) {
                            x.setState(newState);
                            doNotification=true;
                        }
                    }
                    if (msg.getInterfaceFlags() != x.getInterfaceFlag()) {
                        x.setInterfaceFlag(msg.getInterfaceFlags());
                        doNotification=true;
                    }
                    log.debug("update interface {} : {}", name,msg.getNlMsgType());
                    if (doNotification) {
                        sendLinkNotification(LinkNotification.EventAction.Update, LinkNotification.EventType.Link, x);
                    }
                }
                break;
            case libnetlink.linux.rtnetlink.RTM_DELLINK: //remove interface from list
                log.info("remove interface {}", name);
                prevActiveState=x.isActive();
                interfaceList.remove(name);
                sendLinkNotification(LinkNotification.EventAction.Remove, LinkNotification.EventType.Link, x);
                break;
        }
    }

    private void handleAddressMessages(interfaceAddressMsg msg) {
        //get interface name
        NetworkInterface x = findByInterfaceIndex(msg.getInterfaceIndex());
        if (x == null) {
            log.debug("no interface found.");
            return;
        }
        prevActiveState=x.isActive();
        log.trace("interface {}", x);
        switch (msg.getNlMsgType()) {
            case libnetlink.linux.rtnetlink.RTM_NEWADDR: //add or update interface to list
                if (msg.getRTAMessage(libnetlink.linux.if_address.IFA_ADDRESS) != null) {
                    x.setIpv4Address(msg.getRTAMessage(libnetlink.linux.if_address.IFA_ADDRESS).getInt());
                }
                //a P2P link store other Peer address in IFA_BROADCAST so get it
                if (x.isPoint2Point() & msg.getRTAMessage(libnetlink.linux.if_address.IFA_BROADCAST) != null) {
                    x.setIpv4P2Paddress(msg.getRTAMessage(libnetlink.linux.if_address.IFA_BROADCAST).getInt());
                }
                sendLinkNotification(LinkNotification.EventAction.Update, LinkNotification.EventType.Address, x);
                break;
            case libnetlink.linux.rtnetlink.RTM_DELADDR: //remove interface from list
                x.setIpv4Address(0);
                x.setIpv4P2Paddress(0);
                sendLinkNotification(LinkNotification.EventAction.Remove, LinkNotification.EventType.Address, x);
                break;
        }
    }

    private void handleRoutingMessage(routeMsg msg) {
        //get interface name
        if (msg.getRTAMessage(libnetlink.linux.rtnetlink.RTA_OIF) != null) {
            NetworkInterface x = findByInterfaceIndex(msg.getRTAMessage(libnetlink.linux.rtnetlink.RTA_OIF).getInt());
            //log.debug(msg.toString());
            if (x == null) {
                log.debug("no interface found");
                return;
            }
            prevActiveState=x.isActive();
            log.trace("interface {}", x);
            //log.info(msg.toString());
            boolean isLinkLocal = ((int) msg.getRouteScope() & 0xff) == libnetlink.linux.rtnetlink.RT_SCOPE_LINK &
                    msg.getRouteDestLen() < 32;
            boolean isDefaultGateway = msg.getRTAMessage(libnetlink.linux.rtnetlink.RTA_GATEWA) != null &&
                    msg.getRTAMessage(libnetlink.linux.rtnetlink.RTA_DST) == null;
            //log.info("{} {}",isLocal,isDefaultGateway);
            switch (msg.getNlMsgType()) {
                case libnetlink.linux.rtnetlink.RTM_NEWROUTE: //add or update interface to list
                    if (isLinkLocal) {
                        x.setNetmask(msg.getRouteDestLen());
                    } else if (isDefaultGateway) {
                        x.setIpv4Gateway(msg.getRTAMessage(libnetlink.linux.rtnetlink.RTA_GATEWA).getInt());
                    } else {
                        break;
                    }
                    sendLinkNotification(LinkNotification.EventAction.Update, LinkNotification.EventType.Routing, x);
                    break;
                case libnetlink.linux.rtnetlink.RTM_DELROUTE:
                    if (isDefaultGateway) {
                        x.setIpv4Gateway(0);
                        sendLinkNotification(LinkNotification.EventAction.Remove, LinkNotification.EventType.Routing, x);
                    }
                    break;
            }
        }
    }

    private void sendLinkNotification(LinkNotification.EventAction action, LinkNotification.EventType event, NetworkInterface x) {
        log.debug("notify Event: {} {} {} ", action, event, x);//TODO
        if (prevActiveState != x.isActive()) {
            //this notification will only be send when active state changes
            log.warn("State Event: {} active: {} ", x, x.isActive());//TODO
        }
    }

    public NetworkInterface getDefaultGateway() {
        rlock.lock();
        try {
            for (String name : interfaceList.keySet()) {
                NetworkInterface x = interfaceList.get(name);
                if (x.getIpv4Gateway() != 0) {
                    return x;
                }
            }
            return null;
        } finally {
            rlock.unlock();
        }
    }

    public NetworkInterface findByInterfaceIndex(int index) {
        rlock.lock();
        try {
            for (String name : interfaceList.keySet()) {
                NetworkInterface x = interfaceList.get(name);
                if (x.getIndex() == index)
                    return x;
            }
            return null;
        } finally {
            rlock.unlock();
        }
    }

    public NetworkInterface findByInterfaceName(String name) {
        rlock.lock();
        try {
            return interfaceList.get(name);
        } finally {
            rlock.unlock();
        }
    }

    public Set<String> getInterfaceList() {
        rlock.lock();
        try {
            return interfaceList.keySet();
        } finally {
            rlock.unlock();
        }
    }

    public void ReadLock() {
        log.info("Lock read");
        rlock.lock();
    }

    public void ReadUnLock() {
        log.info("unLock read");
        rlock.unlock();
    }
}

