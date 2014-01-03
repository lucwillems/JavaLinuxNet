package org.it4y.net.netlink;

import org.it4y.jni.libnetlink;
import org.it4y.jni.linuxutils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by luc on 1/3/14.
 */
public class LinkManager extends Thread implements libnetlink.rtnl_accept {

    private ConcurrentHashMap<String, NetworkInterface> interfaceList;
    private libnetlink.rtnl_handle handle;
    private ByteBuffer messageBuffer = ByteBuffer.allocateDirect(8129);
    private int groups = 0;
    private int initstate = 0;


    public LinkManager() {
        super("netlink-linkmanager");
        interfaceList = new ConcurrentHashMap<String, NetworkInterface>();
        this.setDaemon(true);
    }

    public void InitNetLink() {
        //we are intrested in Routing/link/address events
        groups = libnetlink.linux.rtnetlink.RTMGRP_IPV4_IFADDR |
                libnetlink.linux.rtnetlink.RTMGRP_IPV4_ROUTE |
                libnetlink.linux.rtnetlink.RTMGRP_IPV4_MROUTE |
                libnetlink.linux.rtnetlink.RTMGRP_LINK;
        linuxutils.rtnl_open_byproto(handle, groups, libnetlink.linux.netlink.NETLINK_ROUTE);
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
                        linuxutils.rtnl_wilddump_request(handle, 0, libnetlink.linux.rtnetlink.RTM_GETLINK);
                        initstate++;
                        break;
                    case 1:
                        linuxutils.rtnl_wilddump_request(handle, 0, libnetlink.linux.rtnetlink.RTM_GETADDR);
                        initstate++;
                        break;
                    case 2:
                        linuxutils.rtnl_wilddump_request(handle, 0, libnetlink.linux.rtnetlink.RTM_GETROUTE);
                        initstate++;
                        break;
                    default:
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

    @Override
    public int accept(ByteBuffer message) {
        NlMessage msg = NetlinkMsgFactory.processRawPacket(message);
        if (msg != null) {
            if (msg instanceof interfaceInfoMsg) {
                handleLinkMessages((interfaceInfoMsg)msg);
            }
            //continue or stop listening ?
            return msg.moreMessages() ? libnetlink.rtl_accept_CONTINUE : libnetlink.rtl_accept_STOP;
        }
        return libnetlink.rtl_accept_STOP;
    }


    private void handleLinkMessages(interfaceInfoMsg msg) {
        //get interface name
        String name= msg.getRTAMessage("ifname") != null ? msg.getRTAMessage("ifname").getString() : null;
        if (name==null)
            return;
        //do we know this interface ?
        NetworkInterface x=interfaceList.get(name);

        //add/delete ?
        switch (msg.getNlMsgType()) {
          libnetlink.linux.rtnetlink.RTM_NEWLINK: //add or update interface to list
            if (x==null) { //new interface
                    x=new NetworkInterface(name,msg.ifi_index);
                    if (msg.getRTAMessage("address") != null ) { x.setMacAddress(msg.getRTAMessage("address").getHexString());}
                    if (msg.getRTAMessage("mtu") != null) { x.setmtu(msg.getRTAMessage("mtu").getInt());}
                    if (msg.getRTAMessage("operstate") != null) { x.setState(msg.getRTAMessage("operstate").getInt());}
                    interfaceList.put(name,x);
            } else {
                //update state
                if (msg.getRTAMessage("operstate") != null) { x.setState(msg.getRTAMessage("operstate").getInt());}
            }
            sendUpdateLinkNotification(x);
            break;
          libnetlink.linux.rtnetlink.RTM_DELLINK: //remove interface from list
            interfaceList.remove(name);
            sendRemovedLinkNotification(x);
            break;
        }
    }

    private void sendUpdateLinkNotification(NetworkInterface x) {
        //TODO
    }
    private void sendRemovedLinkNotification(NetworkInterface x) {
        //TODO
    }

}
