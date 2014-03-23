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

   @Test
   public void testWordBoundRfc1071Checksum() {
       ByteBuffer rawBytes=ByteBuffer.allocate(8);
       //this is based on rfc1071 examples
       rawBytes.put((byte)0x00);
       rawBytes.put((byte)0x01);
       rawBytes.put((byte)0xf2);
       rawBytes.put((byte)0x03);
       rawBytes.put((byte)0xf4);
       rawBytes.put((byte)0xf5);
       rawBytes.put((byte) 0xf6);
       rawBytes.put((byte)0xf7);
       IpPacket packet = new IpPacket(rawBytes,rawBytes.limit());
       Assert.assertEquals((short)0x220d,packet.rfc1071Checksum(0,8));

   }

   public void testNotWordBoundRfc1071Checksum() {
       ByteBuffer rawBytes=ByteBuffer.allocate(3);
       //this is based on rfc1071 examples
       rawBytes.put((byte)0x00);
       rawBytes.put((byte)0x01);
       rawBytes.put((byte)0xf2);
       IpPacket packet = new IpPacket(rawBytes,rawBytes.limit());
       Assert.assertEquals((short)0x220d,packet.rfc1071Checksum(0,3));

   }
}
