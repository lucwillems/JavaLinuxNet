package org.it4y.net.netlink;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by luc on 3/23/14.
 */
public class NlMessageTest {
    private Logger logger= LoggerFactory.getLogger(NlMessageTest.class);

    @Test
    public void testNlErrorMessage() throws Exception {
        logger.info("NlErrorMessage...");
        ByteBuffer rawData=ByteBuffer.allocate(16);
        NlErrorMessage msg=new NlErrorMessage(rawData);
        Assert.assertNotNull(msg);
        Assert.assertEquals(0, msg.getNlMsgType());
        Assert.assertEquals(0, msg.getNlMsgLen());
        Assert.assertEquals(0, msg.getNlMsgPID());
        Assert.assertEquals(0,msg.getNlMsgSequence());
        Assert.assertEquals(0,msg.getNlmsg_flags());
        Assert.assertNotNull(msg.toString());
        logger.info("NlErrorMessage : {}",msg.toString());
    }
    @Test
    public void testNlDoneMessage() throws Exception {
        logger.info("NlDoneMessage...");
        ByteBuffer rawData=ByteBuffer.allocate(16);
        NlDoneMessage msg=new NlDoneMessage(rawData);
        Assert.assertNotNull(msg);
        Assert.assertEquals(0, msg.getNlMsgType());
        Assert.assertEquals(0, msg.getNlMsgLen());
        Assert.assertEquals(0, msg.getNlMsgPID());
        Assert.assertEquals(0,msg.getNlMsgSequence());
        Assert.assertEquals(0,msg.getNlmsg_flags());
        Assert.assertNotNull(msg.toString());
        logger.info("NlErrorMessage : {}",msg.toString());
    }

}
