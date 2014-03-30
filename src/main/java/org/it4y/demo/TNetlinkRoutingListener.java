/*
 * Copyright 2014 Luc Willems (T.M.M.)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.it4y.demo;

import org.it4y.jni.libnetlink3;
import org.it4y.jni.linux.netlink;
import org.it4y.jni.linux.rtnetlink;
import org.it4y.net.netlink.NetlinkMsgFactory;
import org.it4y.net.netlink.NlMessage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by luc on 1/1/14.
 */
public class TNetlinkRoutingListener extends TestRunner {
    private final libnetlink3.rtnl_handle handle;
    private final ByteBuffer messageBuffer = ByteBuffer.allocateDirect(8129);
    private int initstate;


    public TNetlinkRoutingListener() {
        super("tnetlinkrouting-listener");
        handle = new libnetlink3.rtnl_handle();

    }

    public void run() {
        //we are interested in Routing/link/address events
        final int groups = rtnetlink.RTMGRP_IPV4_IFADDR |
                           rtnetlink.RTMGRP_IPV4_ROUTE |
                           rtnetlink.RTMGRP_IPV4_MROUTE |
                           rtnetlink.RTMGRP_LINK;
        //System.out.println("Groups: 0x" + Integer.toHexString(groups));
        libnetlink3.rtnl_open_byproto(handle, groups, netlink.NETLINK_ROUTE);
        //System.out.println(Hexdump.bytesToHex(handle.handle, 4));
        running=true;
        while (running) {
            if (initstate < 4) {
                //we can handle only 1 wilddump at the same time, so we have a little stepping program here
                switch (initstate) {
                    case 0:
                        //System.out.println(new Date() + " Requesting link information...");
                        libnetlink3.rtnl_wilddump_request(handle, 0, rtnetlink.RTM_GETLINK);
                        initstate++;
                        break;
                    case 1:
                        //System.out.println(new Date() + " Requesting address information...");
                        libnetlink3.rtnl_wilddump_request(handle, 0, rtnetlink.RTM_GETADDR);
                        initstate++;
                        break;
                    case 2:
                        //System.out.println(new Date() + " Requesting routing information...");
                        libnetlink3.rtnl_wilddump_request(handle, 0, rtnetlink.RTM_GETROUTE);
                        initstate++;
                        break;
                    default:
                        //System.out.println("Init finished");
                        initstate = 4; //finished
                }
            }
            //rtnl_listen is blocking until rtnl_accept interface returns rtl_accept_STOP.
            //when stop, the listen will return and thread can continue...
            messageBuffer.rewind();
            messageBuffer.order(ByteOrder.LITTLE_ENDIAN);
            libnetlink3.rtnl_listen(handle, messageBuffer, new libnetlink3.rtnl_accept() {
                public int accept(final ByteBuffer message) {
                    //make sure ByteBuffer mar/position are set correct.
                    message.rewind();
                    final NlMessage msg = NetlinkMsgFactory.processRawPacket(message);
                    if (msg != null) {
                        //System.out.println(new Date() + " " + msg.toString());
                        //continue or stop listening ?
                        return msg.moreMessages() ? libnetlink3.rtl_accept_CONTINUE : libnetlink3.rtl_accept_STOP;
                    }
                    return libnetlink3.rtl_accept_STOP;
                }
            });
        }
        //libnetlink3.rtnl_close(handle);
    }
}
