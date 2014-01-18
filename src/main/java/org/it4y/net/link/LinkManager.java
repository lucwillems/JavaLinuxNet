/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.link;

import org.it4y.jni.libnetlink3;
import org.it4y.jni.linux.*;
import org.it4y.jni.linuxutils;
import org.it4y.net.netlink.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LinkManager extends Thread {

    /**
     * We us this lock to protect agains multithreaded (mostly) read/write access
     * of the hashmap and content of this map
     */
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    /**
     * the read lock
     */
    private final Lock rlock = rwl.readLock();
    /**
     * the write lock
     */
    private final Lock wlock = rwl.writeLock();
    /**
     * our internal logger
     */
    private Logger log = LoggerFactory.getLogger(LinkManager.class);
    /**
     * A map of known interface, using name as key
     */
    private HashMap<String, NetworkInterface> interfaceList;

    /**
     * A list of LinkNotification listeners
     */
    private ArrayList<NotificationRegister> listeners;

    /**
     * our netlink rtnl_handle handle
     */
    private libnetlink3.rtnl_handle handle;

    /**
     * Byte buffer we use to retrieve the socket data from netlink
     */
    private ByteBuffer messageBuffer = ByteBuffer.allocateDirect(8129);

    /**
     * netlink multicast groups we are intrested in
     */
    private int groups = 0;
    /**
     * During startup , initstate keeps track of the current init state
     *<pre>
     *     0=init
     *     ..
     *     4=initialization done, all information is known.
     *     5=halt in progress
     *     6=halted
     *</pre>
     */
    private int initstate = 0;

    /**
     * internal flag of runnig thread
     */
    private boolean running=false;

    /**
     * previous active state to detect state changes (interface ready/not ready)
     */
    private boolean prevActiveState = false;

    /**
     * LinkManager keeps track of active network interfaces and collect basic interface information.
     * it will send notifications when network interfaces state changes so actions can be done.
     * it use netlink version 3 to retrieve this information from the linux kernel
     */
    public LinkManager() {
        super("netlink-manager");
        log.debug("init netlink-manager");
        interfaceList = new HashMap<String, NetworkInterface>();
        listeners=new ArrayList<NotificationRegister>();
        handle = new libnetlink3.rtnl_handle();
        this.setDaemon(true);
    }

    /**
     * initialize netlink interface
     *
     */
    protected void InitNetLink() {
        //we are intrested in Routing/link/address events
        groups = rtnetlink.RTMGRP_IPV4_IFADDR |
                 rtnetlink.RTMGRP_IPV4_ROUTE |
                 rtnetlink.RTMGRP_IPV4_MROUTE |
                 rtnetlink.RTMGRP_LINK;
        libnetlink3.rtnl_open_byproto(handle, groups, netlink.NETLINK_ROUTE);
        log.debug("linkmanager started");
    }

    public void halt() {
        wlock.lock();
        try {
            log.info("halting link manager state: {}",initstate);
            running=false;
            initstate=5;
            //We trigger a dump,which will cause the blocking listen call to exit so we can exit the the thread cleanly
            libnetlink3.rtnl_wilddump_request(handle, 0, rtnetlink.RTM_GETLINK);
        } finally {
            wlock.unlock();
        }
    }

    public boolean isHalted() {
        return initstate==6;
    }

    public void shutDown() {
        if (initstate != 6) {
            throw new RuntimeException("netlink manager not halted");
        }
        //close our handle
        listeners.clear();
        libnetlink3.rtnl_close(handle);
    }

    /**
     * start the main link manager thread which listen to netlink messages and process them
     */
    public void run() {

        //init netlink
        InitNetLink();

        //wait for netlink messages
        running=true;
        while (running) {
            if (initstate < 4) {
                //we can handle only 1 wilddump at the same time, so we have a little stepping program here
                switch (initstate) {
                    case 0:
                        log.debug("dump link");
                        libnetlink3.rtnl_wilddump_request(handle, 0, rtnetlink.RTM_GETLINK);
                        initstate++;
                        break;
                    case 1:
                        log.debug("dump addresses");
                        libnetlink3.rtnl_wilddump_request(handle, 0, rtnetlink.RTM_GETADDR);
                        initstate++;
                        break;
                    case 2:
                        log.debug("dump routing");
                        libnetlink3.rtnl_wilddump_request(handle, 0, rtnetlink.RTM_GETROUTE);
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
            libnetlink3.rtnl_listen(handle, messageBuffer, new libnetlink3.rtnl_accept() {

                @Override
                public int accept(ByteBuffer message) {
                    NlMessage msg = NetlinkMsgFactory.processRawPacket(message);
                    if (msg != null) {
                        log.debug("message:{} type={} : {}", msg.getClass().getSimpleName(), msg.getNlMsgType(),msg);
                        log.trace("{}", msg);

                        //we are going to modify so set write lock here
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
                        return msg.moreMessages() ? libnetlink3.rtl_accept_CONTINUE : libnetlink3.rtl_accept_STOP;
                    }
                    return libnetlink3.rtl_accept_STOP;
                }
            });
        }
        log.info("halted.");
        initstate=6;
    }

    /**
     * Handle netlink link messages
     * @param msg
     */
    private void handleLinkMessages(interfaceInfoMsg msg) {
        //get interface name
        String name = msg.getRTAMessage("ifname") != null ? msg.getRTAMessage("ifname").getString() : null;
        if (name == null)
            return;
        //do we know this interface ?
        NetworkInterface x = interfaceList.get(name);

        switch (msg.getNlMsgType()) {
            case rtnetlink.RTM_NEWLINK: //add or update interface to list
                if (x == null) { //new interface
                    x = new NetworkInterface(name, msg.getInterfaceIndex(), msg.getInterfaceFlags(),(int)msg.getInterfaceType()&0xffff);
                    prevActiveState=false;
                    if (msg.getRTAMessage(if_link.IFLA_ADDRESS) != null) {
                        x.setMacAddress(msg.getRTAMessage(if_link.IFLA_ADDRESS).getHexString());
                    }
                    if (msg.getRTAMessage(if_link.IFLA_MTU) != null) {
                        x.setmtu(msg.getRTAMessage(if_link.IFLA_MTU).getInt());
                    }
                    if (msg.getRTAMessage(if_link.IFLA_OPERSTATE) != null) {
                        x.setState((int) msg.getRTAMessage(if_link.IFLA_OPERSTATE).getByte() & 0xff);
                    }
                    interfaceList.put(name, x);
                    log.info("new interface {}", x);
                    sendLinkNotification(LinkNotification.EventAction.New, LinkNotification.EventType.Link, x);
                } else {
                    //update state
                    prevActiveState=x.isActive();
                    boolean doNotification=false;
                    if (msg.getRTAMessage("operstate") != null) {
                        int newState = (int) msg.getRTAMessage(if_link.IFLA_OPERSTATE).getByte() & 0xff;
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
            case rtnetlink.RTM_DELLINK: //remove interface from list
                log.info("remove interface {}", name);
                prevActiveState=x.isActive();
                interfaceList.remove(name);
                sendLinkNotification(LinkNotification.EventAction.Remove, LinkNotification.EventType.Link, x);
                break;
        }
    }

    /**
     * Handle netlink address messages
     * @param msg
     */
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
            case rtnetlink.RTM_NEWADDR: //add or update interface to list
                //IPV4 address ?
                if ( msg.getAddresFamily() == socket.AF_INET) {
                    if (msg.getRTAMessage(if_address.IFA_ADDRESS) != null) {
                        log.debug("set address");
                        x.setIpv4Address(msg.getRTAMessage(if_address.IFA_ADDRESS).getInt());
                    }
                    //a P2P link store other Peer address in IFA_BROADCAST so get it
                    if (x.isPoint2Point() & msg.getRTAMessage(if_address.IFA_BROADCAST) != null)  {
                        log.debug("set P2P address");
                        x.setIpv4P2Paddress(msg.getRTAMessage(if_address.IFA_BROADCAST).getInt());
                    }
                    sendLinkNotification(LinkNotification.EventAction.Update, LinkNotification.EventType.Address, x);
                } else {
                    log.trace("ignoring");
                }
                break;
            case rtnetlink.RTM_DELADDR: //remove interface from list
                //IPV4 address ?
                if ( msg.getAddresFamily() == socket.AF_INET) {
                    log.trace("unset P2P address");
                    x.setIpv4Address(0);
                    x.setIpv4P2Paddress(0);
                    sendLinkNotification(LinkNotification.EventAction.Remove, LinkNotification.EventType.Address, x);
                } else {
                    log.trace("ignoring");
                }
                break;
        }
    }

    /**
     * Handle netlink routing messages
     *
     * @param msg
     */
    private void handleRoutingMessage(routeMsg msg) {
        //get interface name
        if (msg.getRTAMessage(rtnetlink.RTA_OIF) != null) {
            NetworkInterface x = findByInterfaceIndex(msg.getRTAMessage(rtnetlink.RTA_OIF).getInt());
            //log.debug(msg.toString());
            if (x == null) {
                log.debug("no interface found");
                return;
            }
            prevActiveState=x.isActive();
            log.trace("interface {}", x);
            //log.info(msg.toString());
            boolean isLinkLocal = ((int) msg.getRouteScope()  & 0xff) == rtnetlink.RT_SCOPE_LINK & ((int)msg.getRouteDestLen() &0xff)>0 && msg.getRouteType()==rtnetlink.RTN_UNICAST;
            boolean isLocalHost = ((int) msg.getRouteScope()  & 0xff) == rtnetlink.RT_SCOPE_HOST &
                                  ((int)msg.getRouteDestLen() & 0xff)>0 && msg.getRouteType()==rtnetlink.RTN_LOCAL &&
                                  ((int)msg.getRouteDestLen() & 0xff)<32;
            boolean isDefaultGateway = msg.getRTAMessage(rtnetlink.RTA_GATEWA) != null &&
                    msg.getRTAMessage(rtnetlink.RTA_DST) == null;
            //log.info("{} {}",isLocal,isDefaultGateway);
            switch (msg.getNlMsgType()) {
                case rtnetlink.RTM_NEWROUTE: //add or update interface to list
                    if (isLinkLocal | isLocalHost) {
                        x.setNetmask(msg.getRouteDestLen());
                    } else if (isDefaultGateway) {
                        x.setIpv4Gateway(msg.getRTAMessage(rtnetlink.RTA_GATEWA).getInt());
                    } else {
                        break;
                    }
                    sendLinkNotification(LinkNotification.EventAction.Update, LinkNotification.EventType.Routing, x);
                    break;
                case rtnetlink.RTM_DELROUTE:
                    if (isDefaultGateway) {
                        x.setIpv4Gateway(0);
                        sendLinkNotification(LinkNotification.EventAction.Remove, LinkNotification.EventType.Routing, x);
                    }
                    break;
            }
        }
    }

    /**
     * Find interface with default gateway
     *
     * @return NetworkInterface
     */
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

    /**
     * Find interface by kernel index number. returns null in case the interface doesn't exist
     * this method is slow.
     *
     * @param index
     * @return NetworkInterface
     */
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

    /**
     * Find interface by name, returns NULL incase the interface doesn't exist
     *
     * @param name
     * @return NetworkInterface
     */
    public NetworkInterface findByInterfaceName(String name) {
        rlock.lock();
        try {
            return interfaceList.get(name);
        } finally {
            rlock.unlock();
        }
    }

    /**
     * returns a set of interface names active. Use ReadLock() and ReadUnlock() to make iteration of this list
     * thread save outside LinkManager
     *
     * do something like this :
     *<pre>
     *{@code
     *  lnkMng.ReadLock();
     *  try {
     *    for (String name:lnkMng.getInterfaceList()) {
     *        NetworkInterface x=lnkMng.findByInterfaceName(name);
     *        ...
     *    }
     *  } finally {
     *    lnkMng.ReadUnLock();
     *  }
     *}
     *</pre>
     * @return Set<String>
     */
    public Set<String> getInterfaceList() {
        rlock.lock();
        try {
            return interfaceList.keySet();
        } finally {
            rlock.unlock();
        }
    }

    /**
     * Set readLock on internal interface list.
     */
    public void ReadLock() {
        log.trace("Lock read");
        rlock.lock();
    }

    /**
     * Release the read lock on the internal interface list
     */
    public void ReadUnLock() {
        log.trace("unLock read");
        rlock.unlock();
    }

    /**
     * true if internal data is up to date after startup.
     * the internal interface list is created asynchronous so during startup we need to wait until it is ready.
     * @return
     */
    public boolean isReady() {
        return initstate==4;
    }

    /**
     * notification register entry
     */
    class NotificationRegister {
        LinkNotification.EventType type;
        LinkNotification.EventAction action;
        LinkNotification listener;

        public NotificationRegister(LinkNotification.EventAction action,LinkNotification.EventType type,LinkNotification listener) {
            this.type=type;
            this.action=action;
            this.listener=listener;
        }

        public boolean isIntrestedIn(LinkNotification.EventType aType,LinkNotification.EventAction aAction) {
            return (this.action== LinkNotification.EventAction.All||this.action==aAction) &&
                   (this.type== LinkNotification.EventType.All || this.type==aType);
        }
    }

    public LinkNotification registerListener(LinkNotification.EventAction aAction,LinkNotification.EventType aType,LinkNotification aListener) {
        wlock.lock();
        try {
            listeners.add(new NotificationRegister(aAction,aType,aListener));
            log.info("{} listener registrated",listeners.size());
            return aListener;
        } finally {
            wlock.unlock();
        }
    }

    public void unRegisterListener(LinkNotification aListener) {
        wlock.lock();
        try {
            Iterator<NotificationRegister> i=listeners.iterator();
            while(i.hasNext()) {
                NotificationRegister x=i.next();
                if (x.listener.equals(aListener)) {
                    i.remove();
                    log.info("removed listener {}",x);
                }
            }
            log.info("{} listener registrated",listeners.size());
        } finally {
            wlock.unlock();
        }
    }

    /**
     * Send link notification and State notification for registrated listeners
     * this is still run when the write lock is active
     * @param action
     * @param event
     * @param x
     */
    private void sendLinkNotification(LinkNotification.EventAction action, LinkNotification.EventType event, NetworkInterface x) {
        for (NotificationRegister listener : listeners) {
            if (listener.isIntrestedIn(event,action)) {
                //call the listener
                log.debug("onEvent {}",listener.listener);
                listener.listener.onEvent(action,event,x);
            }
            if (prevActiveState != x.isActive()) {
                //this notification will only be send when active state changes
                log.debug("onStateChanged {}",listener.listener);
                listener.listener.onStateChanged(x);
            }
        }
    }

    /**
     * is lm thread running
     * @return
     */
    public boolean isRunning() {
        return running;
    };


}

