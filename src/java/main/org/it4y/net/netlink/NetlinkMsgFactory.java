package org.it4y.net.netlink;

import org.it4y.jni.libnetlink;
import org.it4y.util.Hexdump;

import java.nio.ByteBuffer;

/**
 * Created by luc on 1/2/14.
 */
public class NetlinkMsgFactory {
    public static final NlMessage processRawPacket(ByteBuffer buffer) {

        //Read nlmsg header
        int  nlmsg_len=buffer.getInt(0);
        short nlmsg_type=buffer.getShort(4);
        //to make sure position is pointed to begin of data
        buffer.position(0);
        NlMessage result=null;

        switch(nlmsg_type) {
            case libnetlink.linux.rtnetlink.RTM_NEWROUTE: result=handleRoutingMsg(nlmsg_len,nlmsg_type,buffer);break;
            case libnetlink.linux.rtnetlink.RTM_DELROUTE: result=handleRoutingMsg(nlmsg_len,nlmsg_type, buffer);break;
            case libnetlink.linux.rtnetlink.RTM_NEWLINK: result=handleLinkMsg(nlmsg_len,nlmsg_type, buffer);break;
            case libnetlink.linux.rtnetlink.RTM_DELLINK: result=handleLinkMsg(nlmsg_len,nlmsg_type,buffer);break;
            case libnetlink.linux.rtnetlink.RTM_NEWADDR: result=handleAddressMsg(nlmsg_len,nlmsg_type,buffer);break;
            case libnetlink.linux.rtnetlink.RTM_DELADDR: result=handleAddressMsg(nlmsg_len,nlmsg_type,buffer);break;
            case libnetlink.linux.rtnetlink.RTM_NEWADDRLABEL: System.out.println("New Address label:"+nlmsg_type);break;
            case libnetlink.linux.rtnetlink.RTM_DELADDRLABEL: System.out.println("Del Address label:"+nlmsg_type);break;
            case libnetlink.linux.rtnetlink.RTM_NEWNEIGH: System.out.println("New neigh:"+nlmsg_type);break;
            case libnetlink.linux.rtnetlink.RTM_DELNEIGH: System.out.println("Del neigth:"+nlmsg_type);break;
            case libnetlink.linux.rtnetlink.RTM_GETNEIGH: System.out.println("Get neigth:"+nlmsg_type);break;
            case libnetlink.linux.rtnetlink.RTM_NEWPREFIX: System.out.println("New prefix:"+nlmsg_type);break;
            case libnetlink.linux.rtnetlink.RTM_NEWRULE: System.out.println("New route rule:"+nlmsg_type);break;
            case libnetlink.linux.rtnetlink.RTM_DELRULE: System.out.println("Del route rule:"+nlmsg_type);break;
            case libnetlink.linux.rtnetlink.RTM_NEWNETCONF:System.out.println("New netconf:"+nlmsg_type);break;
            default: System.out.println("Netlink message type :"+nlmsg_type);
        }

        return result;
    }

    private static NlMessage handleRoutingMsg(int nlmsg_len, short nlmsg_type,ByteBuffer buffer) {
        switch(nlmsg_type) {
            case libnetlink.linux.rtnetlink.RTM_NEWROUTE: System.out.print("NEW route:");break;
            case libnetlink.linux.rtnetlink.RTM_DELROUTE: System.out.print("DEL route:");break;
        }
       return new routeMsg(buffer);
    }

    private static NlMessage handleLinkMsg(int nlmsg_len,short nlmsg_type,ByteBuffer buffer) {
        //move buffer position to start of ifinfomsg
        switch(nlmsg_type) {
            case libnetlink.linux.rtnetlink.RTM_NEWLINK: System.out.print("NEW link:");break;
            case libnetlink.linux.rtnetlink.RTM_DELLINK: System.out.print("DEL link:");break;
        }
        return new interfaceInfoMsg(buffer);
    }

    private static NlMessage handleAddressMsg(int nlmsg_len,short nlmsg_type,ByteBuffer buffer) {
        switch(nlmsg_type) {
            case libnetlink.linux.rtnetlink.RTM_NEWADDR: System.out.println("New Address["+nlmsg_type +"]: "+ Hexdump.bytesToHex(buffer,nlmsg_len));break;
            case libnetlink.linux.rtnetlink.RTM_DELADDR: System.out.println("Del Address:"+nlmsg_type);break;
        }
        return new interfaceAddressMsg(buffer);
    }

}

