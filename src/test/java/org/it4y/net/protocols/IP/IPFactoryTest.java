package org.it4y.net.protocols.IP;

import junit.framework.Assert;
import org.it4y.net.protocols.IP.ICMP.ICMPPacket;
import org.it4y.net.protocols.IP.TCP.TCPPacket;
import org.it4y.net.protocols.IP.UDP.UDPPacket;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Created by luc on 3/21/14.
 */
public class IPFactoryTest {
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

    /* Frame (98 bytes)  : ping request */
    static final byte[] pingRequest = {
            (byte)0x45, (byte)0x00, /* 8/....E. */
            (byte)0x00, (byte)0x54, (byte)0xea, (byte)0xf3, (byte)0x40, (byte)0x00, (byte)0x40, (byte)0x01, /* .T..@.@. */
            (byte)0x7e, (byte)0x6d, (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x90, (byte)0x08, (byte)0x08, /* ~m...... */
            (byte)0x08, (byte)0x08, (byte)0x08, (byte)0x00, (byte)0x61, (byte)0x60, (byte)0x42, (byte)0x95, /* ....a`B. */
            (byte)0x00, (byte)0x23, (byte)0x1f, (byte)0x53, (byte)0x2c, (byte)0x53, (byte)0x11, (byte)0x3e, /* .#.S,S.> */
            (byte)0x0c, (byte)0x00, (byte)0x08, (byte)0x09, (byte)0x0a, (byte)0x0b, (byte)0x0c, (byte)0x0d, /* ........ */
            (byte)0x0e, (byte)0x0f, (byte)0x10, (byte)0x11, (byte)0x12, (byte)0x13, (byte)0x14, (byte)0x15, /* ........ */
            (byte)0x16, (byte)0x17, (byte)0x18, (byte)0x19, (byte)0x1a, (byte)0x1b, (byte)0x1c, (byte)0x1d, /* ........ */
            (byte)0x1e, (byte)0x1f, (byte)0x20, (byte)0x21, (byte)0x22, (byte)0x23, (byte)0x24, (byte)0x25, /* .. !"#$% */
            (byte)0x26, (byte)0x27, (byte)0x28, (byte)0x29, (byte)0x2a, (byte)0x2b, (byte)0x2c, (byte)0x2d, /* &'()*+,- */
            (byte)0x2e, (byte)0x2f, (byte)0x30, (byte)0x31, (byte)0x32, (byte)0x33, (byte)0x34, (byte)0x35, /* ./012345 */
            (byte)0x36, (byte)0x37                                      /* 67 */
    };

    
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

    /* Frame (74 bytes)
    * some dummy packet , copy from icmp but change protocol number
    * */
    static final byte[] unknownIpProtocol = {
            (byte)0x45, (byte)0x00, (byte)/* 8/....E. */
            (byte)0x00, (byte)0x39, (byte)0xf5, (byte)0x10, (byte)0x00, (byte)0x00, (byte)0x40, (byte)0xff, (byte)/* .9....@. */
            (byte)0xb4, (byte)0x5b, (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x90, (byte)0x08, (byte)0x08, (byte)/* .[...... */
            (byte)0x08, (byte)0x08, (byte)0xca, (byte)0xfd, (byte)0x00, (byte)0x35, (byte)0x00, (byte)0x25, (byte)/* .....5.% */
            (byte)0xd5, (byte)0x7e, (byte)0x32, (byte)0x14, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x01, (byte)/* .~2..... */
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x03, (byte)0x77, (byte)/* .......w */
            (byte)0x77, (byte)0x77, (byte)0x03, (byte)0x6b, (byte)0x64, (byte)0x65, (byte)0x03, (byte)0x6f, (byte)/* ww.kde.o */
            (byte)0x72, (byte)0x67, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x01        /* rg..... */
    };

    @Test
    public void testProcessRawPacket() throws Exception {
        Object result=IPFactory.processRawPacket(null,0);
        Assert.assertNull(result);
    }

    @Test
    public void testProcessUDPPacket() throws Exception {
        ByteBuffer rawData = ByteBuffer.allocate(DNSrequest.length);
        rawData.put(DNSrequest);
        IpPacket result=IPFactory.processRawPacket(rawData,rawData.limit());
        Assert.assertNotNull(result);
        org.junit.Assert.assertTrue(result instanceof UDPPacket);
        org.junit.Assert.assertEquals(UDPPacket.PROTOCOL,result.getProtocol());
    }

    @Test
    public void testProcessICMPPacket() throws Exception {
        ByteBuffer rawData = ByteBuffer.allocate(pingRequest.length);
        rawData.put(pingRequest);
        IpPacket result=IPFactory.processRawPacket(rawData,rawData.limit());
        Assert.assertNotNull(result);
        org.junit.Assert.assertTrue(result instanceof ICMPPacket);
        org.junit.Assert.assertEquals(ICMPPacket.PROTOCOL,result.getProtocol());
    }

    @Test
    public void testProcessTCPPacket() throws Exception {
        ByteBuffer rawData = ByteBuffer.allocate(tcp_sync.length);
        rawData.put(tcp_sync);
        IpPacket result=IPFactory.processRawPacket(rawData,rawData.limit());
        Assert.assertNotNull(result);
        org.junit.Assert.assertTrue(result instanceof TCPPacket);
        org.junit.Assert.assertEquals(TCPPacket.PROTOCOL,result.getProtocol());
    }

    @Test
    public void testProcessUnkownPacket() throws Exception {
        ByteBuffer rawData = ByteBuffer.allocate(unknownIpProtocol.length);
        rawData.put(unknownIpProtocol);
        IpPacket result=IPFactory.processRawPacket(rawData,rawData.limit());
        Assert.assertNotNull(result);
        org.junit.Assert.assertEquals((byte)0xff,result.getProtocol());
    }

}
