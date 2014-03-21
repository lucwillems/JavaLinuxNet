package org.it4y.net.protocols.IP;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Created by luc on 3/21/14.
 */
public class IpPacketTest {

   @Test
    public void testIpPacket() {
       ByteBuffer rawBytes=ByteBuffer.allocate(0);
       IpPacket ipPacket = new IpPacket(rawBytes,0);

       Assert.assertNotNull(ipPacket);
   }
}
