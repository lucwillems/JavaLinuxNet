package org.it4y.jni;

import junit.framework.Assert;
import org.it4y.jni.linux.netlink;
import org.it4y.jni.linux.rtnetlink;
import org.it4y.util.Counter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

import static org.it4y.jni.libnetlink3.rtnl_accept;

/**
 * Created by luc on 1/9/14.
 */
public class libnetlink3Test {

    private Logger logger = LoggerFactory.getLogger(libnetlink3Test.class);
    private ByteBuffer messageBuffer = ByteBuffer.allocateDirect(8129);

    @Test
    public void testRtnlOpenClose() throws Exception {

        logger.info("rtnl open/close");
        libnetlink3.rtnl_handle handle;
        ByteBuffer buffer;

        handle=new libnetlink3.rtnl_handle();
        Assert.assertNotNull(handle);

        //open netlink3 socket
        int result=libnetlink3.rtnl_open(handle,(short)0xffff) ;
        Assert.assertTrue(result==0);
        logger.info("rtnl fd: {}",handle.getFd());
        logger.info("rtnl seq: {}",handle.getSeq());

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

        libnetlink3.rtnl_close(handle);
    }

    @Test
    public void testRtnlListen() throws Exception {

        libnetlink3.rtnl_handle handle;
        ByteBuffer buffer;
        int result=0;
        final Counter cnt=new Counter();

        logger.info("rtnl listen...");
        //open netlink3 socket
        handle=new libnetlink3.rtnl_handle();
        int groups = rtnetlink.RTMGRP_IPV4_IFADDR |
                     rtnetlink.RTMGRP_IPV4_ROUTE |
                     rtnetlink.RTMGRP_IPV4_MROUTE |
                     rtnetlink.RTMGRP_LINK;
        result=libnetlink3.rtnl_open_byproto(handle, groups,netlink.NETLINK_ROUTE);
        Assert.assertTrue(result == 0);
        //Request addres information
        logger.info("rtnl dump request");
        result=libnetlink3.rtnl_wilddump_request(handle, 0, rtnetlink.RTM_GETADDR);
        int retry=0;
        //this runs async so retry 10 times
        while(cnt.getCount()==0 & retry<10) {
            result=libnetlink3.rtnl_listen(handle, messageBuffer, new rtnl_accept() {
                @Override
                public int accept(ByteBuffer message) {
                    logger.info("rtnl got message, stopping");
                    cnt.inc();
                    return libnetlink3.rtl_accept_STOP;
                }
            });
            Thread.sleep(100);
            retry++;
        }
        //we recieved a message ?
        Assert.assertTrue(cnt.getCount()==1);
        //close it
        libnetlink3.rtnl_close(handle);
    }

    @Test
    public void testLibnetlink3Utils() {
        org.junit.Assert.assertEquals(0,libnetlink3.utils.nl_mgrp(0));
        org.junit.Assert.assertEquals(1,libnetlink3.utils.nl_mgrp(1));
        org.junit.Assert.assertEquals(2,libnetlink3.utils.nl_mgrp(2));
        org.junit.Assert.assertEquals(4,libnetlink3.utils.nl_mgrp(3));
        org.junit.Assert.assertEquals(8,libnetlink3.utils.nl_mgrp(4));
        org.junit.Assert.assertEquals(16,libnetlink3.utils.nl_mgrp(5));
        org.junit.Assert.assertEquals(0x40000000,libnetlink3.utils.nl_mgrp(31));
    }

    public void testRTNL_Handle(){
        libnetlink3.rtnl_handle handle = new libnetlink3.rtnl_handle();
        org.junit.Assert.assertNotNull(handle);

    }
}