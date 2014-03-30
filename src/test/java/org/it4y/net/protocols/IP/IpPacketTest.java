package org.it4y.net.protocols.IP;

import org.it4y.net.protocols.IP.UDP.UDPPacket;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Created by luc on 3/21/14.
 */
public class IpPacketTest {

   @Test
    public void testIpPacket() {
       ByteBuffer rawBytes=ByteBuffer.allocate(60);
       IpPacket ipPacket = new IpPacket(rawBytes,60);

       Assert.assertNotNull(ipPacket);
       ipPacket.initIpHeader();
       ipPacket.updateChecksum();
       Assert.assertEquals(20, ipPacket.getHeaderSize());
       Assert.assertEquals(0, ipPacket.getTOS());
       Assert.assertEquals(64, ipPacket.getTTL());
       Assert.assertEquals(64, ipPacket.getTTL());
       Assert.assertEquals(20, ipPacket.getHeaderSize());
       Assert.assertEquals(40, ipPacket.getPayLoadSize());
       Assert.assertEquals(60, ipPacket.getRawSize());
       Assert.assertEquals(0, ipPacket.getIdentification());
       Assert.assertEquals(0, ipPacket.getFlags());
       Assert.assertEquals(0,ipPacket.getFragmentOffset());
       Assert.assertNotNull(ipPacket.getRawPacket());
       ipPacket.getDstRoutingHash();
       ipPacket.getFlowHash();
       Assert.assertNotNull(ipPacket.toString());
       Assert.assertFalse(ipPacket.hasOptions());
       Assert.assertNotNull(ipPacket.getHeader());
       Assert.assertNotNull(ipPacket.getIpHeader());
       Assert.assertNotNull(ipPacket.getPayLoad());
       Assert.assertNotNull(ipPacket.getIpPayLoad());
       ipPacket.release();
       Assert.assertNull(ipPacket.getRawPacket());
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

   @Test
   public void testNotWordBoundRfc1071Checksum() {
       ByteBuffer rawBytes=ByteBuffer.allocate(3);
       //this is based on rfc1071 examples
       rawBytes.put((byte)0x00);
       rawBytes.put((byte)0x01);
       rawBytes.put((byte)0xf2);
       IpPacket packet = new IpPacket(rawBytes,rawBytes.limit());
       Assert.assertEquals((short)0xFF0C,packet.rfc1071Checksum(0,3));
   }

    @Test
    public void testCreateRawIp(){
        ByteBuffer rawBytes=ByteBuffer.allocate(60);
        IpPacket packet = new IpPacket(rawBytes,60);
        Assert.assertNotNull(packet);
        packet.resetBuffer();
        packet.initIpHeader();
        packet.setSourceAddress(0x08080808);
        packet.setDestinationAddress(0x08080404);
        packet.setTOS((byte)0x01);
        packet.setTTL((byte)0x02);
        packet.setProtocol(UDPPacket.PROTOCOL);
        packet.setFragmentOffset((short)0x1fff);
        packet.setIdentification((short)0x04);
        //set some flags
        Assert.assertFalse(packet.isDF());
        Assert.assertFalse(packet.isMF());
        packet.setDF(true);
        Assert.assertTrue(packet.isDF());
        Assert.assertFalse(packet.isMF());
        packet.setMF(true);
        Assert.assertTrue(packet.isDF());
        Assert.assertTrue(packet.isMF());
        packet.updateChecksum();
        //validate
        Assert.assertEquals(0x08080808,packet.getSourceAddress());
        Assert.assertEquals(0x08080404,packet.getDestinationAddress());
        Assert.assertEquals(0x01,packet.getTOS());
        Assert.assertEquals(0x02,packet.getTTL());
        Assert.assertEquals(UDPPacket.PROTOCOL,packet.getProtocol());
        Assert.assertEquals(0x1fff,packet.getFragmentOffset());
        Assert.assertEquals(0x04,packet.getIdentification());

        packet.setDF(false);
        packet.setMF(false);
        Assert.assertEquals(0x1fff,packet.getFragmentOffset());
        Assert.assertFalse(packet.isDF());
        Assert.assertFalse(packet.isMF());
        packet.setFragmentOffset((short)0x1234);
        Assert.assertEquals((short)0x1234,packet.getFragmentOffset());
    }
}
