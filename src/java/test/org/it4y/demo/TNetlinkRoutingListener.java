package org.it4y.demo;

import org.it4y.jni.libnetlink;
import org.it4y.jni.linuxutils;
import org.it4y.util.Hexdump;

import java.net.InetAddress;
import java.nio.ByteBuffer;

/**
 * Created by luc on 1/1/14.
 */
public class TNetlinkRoutingListener extends TestRunner {
    private libnetlink.rtnl_handle handle;
    private ByteBuffer messageBuffer=ByteBuffer.allocate(8192);
    private int groups=0;

    public TNetlinkRoutingListener() {
        super("tnetlinkrouting-listener");
        handle=new libnetlink.rtnl_handle();

    }

    @Override
    public void run() {
        //we are intrested in Routing events
        groups = 0xffffffff;
        groups |= libnetlink.utils.nl_mgrp(libnetlink.linux.rtnetlink.RTNLGRP_IPV4_ROUTE);
        groups |= libnetlink.utils.nl_mgrp(libnetlink.linux.rtnetlink.RTNLGRP_IPV6_ROUTE);
        System.out.println("Groups: "+Integer.toHexString(groups));
        linuxutils.rtnl_open_byproto(handle, groups, libnetlink.linux.netlink.NETLINK_ROUTE);
        System.out.println(Hexdump.bytesToHex(handle.handle, 4));
        while(true) {
            linuxutils.rtnl_listen(handle,messageBuffer,new libnetlink.rtnl_accept() {
                @Override
                public int accept(ByteBuffer message) {
                    System.out.println("netlink message recieved");
                    return 0;
                }
            });
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignore) {
                break;
            }
        }
        linuxutils.rtnl_close(handle);
    }
}
