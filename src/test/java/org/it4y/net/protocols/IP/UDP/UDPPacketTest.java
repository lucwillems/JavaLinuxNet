package org.it4y.net.protocols.IP.UDP;

import org.it4y.net.protocols.IP.ICMP.ICMPPacket;
import org.it4y.net.protocols.IP.IPFactory;
import org.it4y.net.protocols.IP.IpPacket;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Created by luc on 3/21/14.
 */
public class UDPPacketTest {
    /* Frame (71 bytes) */
    static final byte[] DNSrequest = {
            (byte)0x45, (byte)0x00, (byte)/* 8/....E. */
            (byte)0x00, (byte)0x39, (byte)0xf5, (byte)0x10, (byte)0x00, (byte)0x00, (byte)0x40, (byte)0x11, (byte)/* .9....@. */
            (byte)0xb4, (byte)0x5b, (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x90, (byte)0x08, (byte)0x08, (byte)/* .[...... */
            (byte)0x08, (byte)0x08, (byte)0xca, (byte)0xfd, (byte)0x00, (byte)0x35, (byte)0x00, (byte)0x25, (byte)/* .....5.% */
            (byte)0xd5, (byte)0x7e, (byte)0x32, (byte)0x14, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x01, (byte)/* .~2..... */
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x03, (byte)0x77, (byte)/* .......w */
            (byte)0x77, (byte)0x77, (byte)0x03, (byte)0x6b, (byte)0x64, (byte)0x65, (byte)0x03, (byte)0x6f, (byte)/* ww.kde.o */
            (byte)0x72, (byte)0x67, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x01        /* rg..... */
    };

/* Frame (108 bytes) */
    static final byte[] DNSresponse = {
            (byte)0x45, (byte)0x00, (byte)/* Kn;...E. */
            (byte)0x00, (byte)0x5e, (byte)0xb5, (byte)0x7b, (byte)0x00, (byte)0x00, (byte)0x30, (byte)0x11, (byte)/* .^.{..0. */
            (byte)0x03, (byte)0xcc, (byte)0x08, (byte)0x08, (byte)0x08, (byte)0x08, (byte)0xc0, (byte)0xa8, (byte)/* ........ */
            (byte)0x00, (byte)0x90, (byte)0x00, (byte)0x35, (byte)0xca, (byte)0xfd, (byte)0x00, (byte)0x4a, (byte)/* ...5...J */
            (byte)0xc3, (byte)0x41, (byte)0x32, (byte)0x14, (byte)0x81, (byte)0x80, (byte)0x00, (byte)0x01, (byte)/* .A2..... */
            (byte)0x00, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x03, (byte)0x77, (byte)/* .......w */
            (byte)0x77, (byte)0x77, (byte)0x03, (byte)0x6b, (byte)0x64, (byte)0x65, (byte)0x03, (byte)0x6f, (byte)/* ww.kde.o */
            (byte)0x72, (byte)0x67, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x01, (byte)0xc0, (byte)/* rg...... */
            (byte)0x0c, (byte)0x00, (byte)0x05, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x01, (byte)/* ........ */
            (byte)0x23, (byte)0x00, (byte)0x09, (byte)0x06, (byte)0x73, (byte)0x70, (byte)0x69, (byte)0x64, (byte)/* #...spid */
            (byte)0x65, (byte)0x72, (byte)0xc0, (byte)0x10, (byte)0xc0, (byte)0x29, (byte)0x00, (byte)0x01, (byte)/* er...).. */
            (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x23, (byte)0x00, (byte)0x04, (byte)/* .....#.. */
            (byte)0x2e, (byte)0x04, (byte)0x60, (byte)0xfa                          /* ..`. */
    };

    @Test
    public void testICMP_DNSRequest() {

        ByteBuffer rawData = ByteBuffer.allocate(DNSrequest.length);
        rawData.put(DNSrequest);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, DNSrequest.length);
        Assert.assertNotNull(packet);
        Assert.assertTrue(packet instanceof UDPPacket);
        Assert.assertEquals(((UDPPacket) packet).getSourceAddress(),0xc0a80090);
        Assert.assertEquals(((UDPPacket) packet).getDestinationAddress(),0x08080808);
        Assert.assertEquals(((UDPPacket) packet).getHeaderSize(),UDPPacket.UDP_HEADER_SIZE);
        Assert.assertEquals(((UDPPacket) packet).getIpHeaderSize(),20);
        Assert.assertEquals(((UDPPacket) packet).getTOS(),0);
        Assert.assertEquals(((UDPPacket) packet).getTTL(),64);
        Assert.assertEquals(((UDPPacket) packet).getProtocol(),17);
        //Test payload
        ByteBuffer payload=packet.getPayLoad();
        Assert.assertEquals(8,((UDPPacket)packet).getHeaderSize());
        Assert.assertEquals(29,((UDPPacket) packet).getPayLoadSize());
        Assert.assertEquals(((UDPPacket)packet).getPayLoadSize(),payload.limit());
        Assert.assertEquals(packet.getRawPacket().get(packet.getIpHeaderSize() + UDPPacket.UDP_HEADER_SIZE), payload.get(0));

        //check ICMP header
        ByteBuffer header=(((UDPPacket)packet).getHeader());
        Assert.assertEquals(((UDPPacket)packet).getHeaderSize(),header.limit());
        Assert.assertEquals(packet.getRawPacket().getInt(packet.getIpHeaderSize()),header.getInt(0));

        //Check IP Header
        ByteBuffer Ipheader=(((UDPPacket)packet).getIpHeader());
        Assert.assertEquals(((UDPPacket)packet).getIpHeaderSize(),Ipheader.limit());
        Assert.assertEquals(packet.getRawPacket().getInt(0),Ipheader.getInt(0));

        ((UDPPacket)packet).swapSourceDestinationPort();
        ((UDPPacket)packet).swapSourceDestination();
        ((UDPPacket)packet).updateChecksum();
        ((UDPPacket)packet).getDstRoutingHash();
        ((UDPPacket)packet).getFlowHash();

        Assert.assertNotNull(((UDPPacket)packet).toString());
    }

    @Test
    public void testICMP_DNSResponse() {

        ByteBuffer rawData = ByteBuffer.allocate(DNSresponse.length);
        rawData.put(DNSresponse);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, DNSresponse.length);
        Assert.assertNotNull(packet);
        Assert.assertTrue(packet instanceof UDPPacket);
    }

}
