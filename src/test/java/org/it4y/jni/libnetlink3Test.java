package org.it4y.jni;

import junit.framework.Assert;
import org.it4y.jni.linux.netlink;
import org.it4y.jni.linux.rtnetlink;
import org.it4y.util.Counter;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.it4y.jni.libnetlink3.rtnl_accept;

/**
 * Created by luc on 1/9/14.
 */
public class libnetlink3Test {

    private ByteBuffer messageBuffer = ByteBuffer.allocateDirect(8129);

    @Test
    public void testRtnlOpenClose() throws Exception {

        libnetlink3.rtnl_handle handle;
        ByteBuffer buffer;

        handle=new libnetlink3.rtnl_handle();
        Assert.assertNotNull(handle);

        //open netlink3 socket
        int result=libnetlink3.rtnl_open(handle,(short)0xffff) ;
        Assert.assertTrue(result==0);
        //Check the rtnl handle structure , from netlink c code:
        //struct rtnl_handle
        //{
        //	int			fd;
        //	struct sockaddr_nl	local;
        // 	struct sockaddr_nl	peer;
        //	uint32_t		seq;
        //	uint32_t		dump;
        //};
        buffer=ByteBuffer.wrap(handle.handle);
        Assert.assertNotNull(buffer);
        Assert.assertTrue(buffer.getShort() != 0); //fd field
        Assert.assertTrue(buffer.getInt() != 0);   //local
        Assert.assertTrue(buffer.getInt() !=0);   //peer
        Assert.assertTrue(buffer.getShort() ==0); //seq

        libnetlink3.rtnl_close(handle);
        buffer=ByteBuffer.wrap(handle.handle);
        Assert.assertTrue(buffer.getShort() ==0); //fd field
        Assert.assertTrue(buffer.getInt() ==0);   //local
        Assert.assertTrue(buffer.getInt() ==0);   //peer
        Assert.assertTrue(buffer.getShort() == 0); //seq
    }

    @Test
    public void testRtnlListen() throws Exception {

        libnetlink3.rtnl_handle handle;
        ByteBuffer buffer;
        int result=0;
        final Counter cnt=new Counter();

        //open netlink3 socket
        handle=new libnetlink3.rtnl_handle();
        int groups = rtnetlink.RTMGRP_IPV4_IFADDR |
                     rtnetlink.RTMGRP_IPV4_ROUTE |
                     rtnetlink.RTMGRP_IPV4_MROUTE |
                     rtnetlink.RTMGRP_LINK;
        result=libnetlink3.rtnl_open_byproto(handle, groups,netlink.NETLINK_ROUTE);
        Assert.assertTrue(result == 0);
        //Request addres information
        result=libnetlink3.rtnl_wilddump_request(handle, 0, rtnetlink.RTM_GETADDR);
        int retry=0;
        //this runs async so retry 10 times
        while(cnt.getCount()==0 & retry<10) {
            result=libnetlink3.rtnl_listen(handle, messageBuffer, new rtnl_accept() {
                @Override
                public int accept(ByteBuffer message) {
                    cnt.inc();
                    return libnetlink3.rtl_accept_STOP;
                }
            });
            Thread.sleep(100);
            retry++;
        }
        Assert.assertTrue(cnt.getCount()==1);
        //close it
        libnetlink3.rtnl_close(handle);
    }

}