package org.it4y.net.netlink;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Created by luc on 3/23/14.
 */
public class NetlinkMsgFactoryTest {

    @Test
    public void testNetLinkMsgFactory() {
        ByteBuffer rawData=ByteBuffer.allocate(16);
        NlMessage msg=NetlinkMsgFactory.processRawPacket(rawData);
        //invalid message
        Assert.assertNull(msg);
    }
}
