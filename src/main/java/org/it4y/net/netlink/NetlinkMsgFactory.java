/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.netlink;

import org.it4y.jni.linux.netlink;
import org.it4y.jni.linux.rtnetlink;
import org.it4y.util.Hexdump;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by luc on 1/2/14.
 */
public class NetlinkMsgFactory {
    private static final Logger log = LoggerFactory.getLogger(NetlinkMsgFactory.class);
    public static NlMessage processRawPacket(final ByteBuffer buffer) {

        //Read nlmsg header
        final int nlmsg_len = buffer.getInt(0);
        final short nlmsg_type = buffer.getShort(4);
        //to make sure position is pointed to begin of data
        buffer.position(0);
        NlMessage result = null;

        switch (nlmsg_type) {
            case rtnetlink.RTM_NEWROUTE:
                result = handleRoutingMsg(nlmsg_len, nlmsg_type, buffer);
                break;
            case rtnetlink.RTM_DELROUTE:
                result = handleRoutingMsg(nlmsg_len, nlmsg_type, buffer);
                break;
            case rtnetlink.RTM_NEWLINK:
                result = handleLinkMsg(nlmsg_len, nlmsg_type, buffer);
                break;
            case rtnetlink.RTM_DELLINK:
                result = handleLinkMsg(nlmsg_len, nlmsg_type, buffer);
                break;
            case rtnetlink.RTM_NEWADDR:
                result = handleAddressMsg(nlmsg_len, nlmsg_type, buffer);
                break;
            case rtnetlink.RTM_DELADDR:
                result = handleAddressMsg(nlmsg_len, nlmsg_type, buffer);
                break;
            //case libnetlink.linux.rtnetlink.RTM_NEWADDRLABEL: System.out.println("New Address label:"+nlmsg_type);break;
            //case libnetlink.linux.rtnetlink.RTM_DELADDRLABEL: System.out.println("Del Address label:"+nlmsg_type);break;
            case rtnetlink.RTM_NEWNEIGH:
                result = handleNeighbourMsg(nlmsg_len, nlmsg_type, buffer);
                break;
            case rtnetlink.RTM_DELNEIGH:
                result = handleNeighbourMsg(nlmsg_len, nlmsg_type, buffer);
                break;
            case rtnetlink.RTM_GETNEIGH:
                result = handleNeighbourMsg(nlmsg_len, nlmsg_type, buffer);
                break;
            //case libnetlink.linux.rtnetlink.RTM_NEWPREFIX: System.out.println("New prefix:"+nlmsg_type);break;
            //case libnetlink.linux.rtnetlink.RTM_NEWRULE: System.out.println("New route rule:"+nlmsg_type);break;
            //case libnetlink.linux.rtnetlink.RTM_DELRULE: System.out.println("Del route rule:"+nlmsg_type);break;
            //case libnetlink.linux.rtnetlink.RTM_NEWNETCONF:System.out.println("New netconf:"+nlmsg_type);break;
            case netlink.NLMSG_DONE:
                result = new NLDoneMessage(buffer);
                break;
            case netlink.NLMSG_ERROR:
                result = new NlErrorMessage(buffer);
                break;
            default:
                if (log.isDebugEnabled()) {
                    log.debug("UNKNOWN Netlink message type: {} len:{} [{}]",nlmsg_type,nlmsg_len,Hexdump.bytesToHex(buffer, nlmsg_len));
                }
        }
        return result;
    }

    private static NlMessage handleRoutingMsg(final int nlmsg_len, final short nlmsg_type, final ByteBuffer buffer) {
        return new routeMsg(buffer);
    }

    private static NlMessage handleLinkMsg(final int nlmsg_len, final short nlmsg_type, final ByteBuffer buffer) {
        return new interfaceInfoMsg(buffer);
    }

    private static NlMessage handleAddressMsg(final int nlmsg_len, final short nlmsg_type, final ByteBuffer buffer) {
        return new interfaceAddressMsg(buffer);
    }

    private static NlMessage handleNeighbourMsg(final int nlmsg_len, final short nlmsg_type, final ByteBuffer buffer) {
        return new neighbourMsg(buffer);
    }

}

