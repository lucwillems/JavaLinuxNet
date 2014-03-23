package org.it4y.net.protocols.IP.TCP;

import org.it4y.net.protocols.IP.ICMP.ICMPPacket;
import org.it4y.net.protocols.IP.IPFactory;
import org.it4y.net.protocols.IP.IpPacket;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by luc on 3/23/14.
 */
public class TCPPacketTest {
    Logger logger = LoggerFactory.getLogger(TCPPacket.class);
    /* Frame (74 bytes)
     * wget http://8.8.8.8 from 192.168.0.144
     * */
    static final byte[] tcp_sync = {
            (byte)0x45, (byte)0x00, /* 8/....E. */
            (byte)0x00, (byte)0x3c, (byte)0xd9, (byte)0xa5, (byte)0x40, (byte)0x00, (byte)0x40, (byte)0x06, /* .<..@.@. */
            (byte)0x8f, (byte)0xce, (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x90, (byte)0x08, (byte)0x08, /* ........ */
            (byte)0x08, (byte)0x08, (byte)0xdd, (byte)0x60, (byte)0x00, (byte)0x50, (byte)0xf5, (byte)0x73, /* ...`.P.s */
            (byte)0xd7, (byte)0xd3, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xa0, (byte)0x02, /* ........ */
            (byte)0x72, (byte)0x10, (byte)0x4a, (byte)0x39, (byte)0x00, (byte)0x00, (byte)0x02, (byte)0x04, /* r.J9.... */
            (byte)0x05, (byte)0xb4, (byte)0x04, (byte)0x02, (byte)0x08, (byte)0x0a, (byte)0x00, (byte)0xfd, /* ........ */
            (byte)0x0e, (byte)0x76, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x03, /* .v...... */
            (byte)0x03, (byte)0x0a                                      /* .. */
    };

    @Test
    public void testTCPPacket() {
        ByteBuffer rawData=ByteBuffer.allocate(tcp_sync.length);
        rawData.put(tcp_sync);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, rawData.limit());
        Assert.assertNotNull(packet);
        Assert.assertTrue(packet instanceof TCPPacket);
        Assert.assertEquals(0, ((TCPPacket) packet).getAckNumber());
        Assert.assertEquals(0xf573d7d3, ((TCPPacket) packet).getSequenceNumber());
        Assert.assertEquals(0, ((TCPPacket) packet).getUrgentPointer());
        Assert.assertEquals(29200, ((TCPPacket) packet).getWindowSize());
        Assert.assertEquals((short) 0xdd60, ((TCPPacket) packet).getSourcePort());
        Assert.assertEquals(80, ((TCPPacket) packet).getDestinationPort());
        Assert.assertNotNull(((TCPPacket) packet).getOption(0));
        Assert.assertTrue(((TCPPacket) packet).isSYN());
        Assert.assertFalse(((TCPPacket) packet).isACK());
        Assert.assertFalse(((TCPPacket) packet).isCWR());
        Assert.assertFalse(((TCPPacket) packet).isECE());
        Assert.assertFalse(((TCPPacket)packet).isFIN());
        Assert.assertFalse(((TCPPacket)packet).isNS());
        Assert.assertFalse(((TCPPacket)packet).isPSH());
        Assert.assertFalse(((TCPPacket)packet).isRST());
        Assert.assertFalse(((TCPPacket)packet).isURG());
        Assert.assertEquals(0,((TCPPacket)packet).getPayLoadSize());
        Assert.assertEquals(40,((TCPPacket)packet).getHeaderSize());
        Assert.assertEquals(20,((TCPPacket)packet).getIpHeaderSize());

        ((TCPPacket)packet).swapSourceDestinationPort();
        Assert.assertEquals((short) 0xdd60, ((TCPPacket) packet).getDestinationPort());
        Assert.assertEquals(80, ((TCPPacket) packet).getSourcePort());
        Assert.assertTrue(((TCPPacket)packet).hasOptions());
        Assert.assertEquals(19001,((TCPPacket)packet).getTCPChecksum());
        Assert.assertNotNull(((TCPPacket)packet).toString());
        Assert.assertEquals(0xd8388ee8,packet.getFlowHash());
        Assert.assertEquals(0x35a4919f,packet.getDstRoutingHash());


    }

    @Test
    public void testTCPOptionMSS() {
        ByteBuffer rawData=ByteBuffer.allocate(tcp_sync.length);
        rawData.put(tcp_sync);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, rawData.limit());
        Assert.assertNotNull(packet);
        Assert.assertTrue(packet instanceof TCPPacket);
        TCPOption o=((TCPPacket) packet).getOptionByType(TCPOption.MSS);
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TCPoptionMSS);
        Assert.assertNotNull(o.toString());
        Assert.assertEquals(1460, ((TCPoptionMSS) o).getMss());
    }

    @Test
    public void testTCPOptionWindowScale() {
        ByteBuffer rawData=ByteBuffer.allocate(tcp_sync.length);
        rawData.put(tcp_sync);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, rawData.limit());
        Assert.assertNotNull(packet);
        Assert.assertTrue(packet instanceof TCPPacket);
        TCPOption o=((TCPPacket) packet).getOptionByType(TCPOption.WSCALE);
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TCPoptionWindowScale);
        Assert.assertNotNull(o.toString());
        Assert.assertEquals(10, ((TCPoptionWindowScale) o).getScale());
    }

    @Test
    public void testTCPOptionSACK() {
        ByteBuffer rawData=ByteBuffer.allocate(tcp_sync.length);
        rawData.put(tcp_sync);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, rawData.limit());
        Assert.assertNotNull(packet);
        Assert.assertTrue(packet instanceof TCPPacket);
        TCPOption o=((TCPPacket) packet).getOptionByType(TCPOption.SACK);
        Assert.assertNull(o);
    }

    @Test
    public void testTCPOptionTimeStamp() {
        ByteBuffer rawData=ByteBuffer.allocate(tcp_sync.length);
        rawData.put(tcp_sync);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, rawData.limit());
        Assert.assertNotNull(packet);
        Assert.assertTrue(packet instanceof TCPPacket);
        TCPOption o=((TCPPacket) packet).getOptionByType(TCPOption.TIMESTAMP);
        Assert.assertNotNull(o);
        Assert.assertNotNull(o.toString());
        Assert.assertEquals(0, ((TCPoptionTimeStamp) o).getTsecr());
        Assert.assertEquals(0x00fd0e76, ((TCPoptionTimeStamp) o).getTsval());
    }

    @Test
    public void testTCPOptionNOP() {
        ByteBuffer rawData=ByteBuffer.allocate(tcp_sync.length);
        rawData.put(tcp_sync);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, rawData.limit());
        Assert.assertNotNull(packet);
        Assert.assertTrue(packet instanceof TCPPacket);
        TCPOption o=((TCPPacket) packet).getOptionByType(TCPOption.NOP);
        Assert.assertNotNull(o);
        Assert.assertNotNull(o.toString());
    }

    @Test
    public void testTCPOptionEnd() {
        ByteBuffer rawData=ByteBuffer.allocate(tcp_sync.length);
        rawData.put(tcp_sync);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, rawData.limit());
        Assert.assertNotNull(packet);
        Assert.assertTrue(packet instanceof TCPPacket);
        TCPOption o=((TCPPacket) packet).getOptionByType(TCPOption.END);
        Assert.assertNull(o);
    }

}
