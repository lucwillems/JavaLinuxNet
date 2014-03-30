/*
 * Copyright 2014 Luc Willems (T.M.M.)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.it4y.net.protocols.IP.GRE;

import org.it4y.net.protocols.IP.ICMP.ICMPPacket;
import org.it4y.net.protocols.IP.IPFactory;
import org.it4y.net.protocols.IP.IpPacket;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by luc on 3/24/14.
 */
public class grePacketTest {
    private Logger logger = LoggerFactory.getLogger(grePacketTest.class);

    //GRE tunnel without keys/sequence and checksum. inside a icmp request/reply
    static final byte[] gre1_1 = {
         (byte)0x45, (byte)(byte)0x00, (byte)(byte)/* V.....E. */
         (byte)0x00, (byte)(byte)0x6c, (byte)(byte)0xd3, (byte)(byte)0x71, (byte)(byte)0x40, (byte)(byte)0x00, (byte)(byte)0xe1, (byte)(byte)0x2f, (byte)(byte)/* .l.q@../ */
         (byte)0x48, (byte)(byte)0xa3, (byte)(byte)0xac, (byte)(byte)0x10, (byte)(byte)0x12, (byte)(byte)0x96, (byte)(byte)0xac, (byte)(byte)0x10, (byte)(byte)/* H....... */
         (byte)0x12, (byte)(byte)0x97, (byte)(byte)0x00, (byte)(byte)0x00, (byte)(byte)0x08, (byte)(byte)0x00, (byte)(byte)0x45, (byte)(byte)0x00, (byte)(byte)/* ......E. */
         (byte)0x00, (byte)(byte)0x54, (byte)(byte)0x64, (byte)(byte)0x51, (byte)(byte)0x40, (byte)(byte)0x00, (byte)(byte)0x40, (byte)(byte)0x01, (byte)(byte)/* .TdQ@.@. */
         (byte)0x30, (byte)(byte)0x54, (byte)(byte)0x0a, (byte)(byte)0x00, (byte)(byte)0xc9, (byte)(byte)0x02, (byte)(byte)0x0a, (byte)(byte)0x00, (byte)(byte)/* 0T...... */
         (byte)0xc9, (byte)(byte)0x01, (byte)(byte)0x08, (byte)(byte)0x00, (byte)(byte)0x38, (byte)(byte)0xe4, (byte)(byte)0x0a, (byte)(byte)0xea, (byte)(byte)/* ....8... */
         (byte)0x00, (byte)(byte)0x16, (byte)(byte)0x01, (byte)(byte)0x06, (byte)(byte)0x30, (byte)(byte)0x53, (byte)(byte)0x8e, (byte)(byte)0xbf, (byte)(byte)/* ....0S.. */
         (byte)0x09, (byte)(byte)0x00, (byte)(byte)0x08, (byte)(byte)0x09, (byte)(byte)0x0a, (byte)(byte)0x0b, (byte)(byte)0x0c, (byte)(byte)0x0d, (byte)(byte)/* ........ */
         (byte)0x0e, (byte)(byte)0x0f, (byte)(byte)0x10, (byte)(byte)0x11, (byte)(byte)0x12, (byte)(byte)0x13, (byte)(byte)0x14, (byte)(byte)0x15, (byte)(byte)/* ........ */
         (byte)0x16, (byte)(byte)0x17, (byte)(byte)0x18, (byte)(byte)0x19, (byte)(byte)0x1a, (byte)(byte)0x1b, (byte)(byte)0x1c, (byte)(byte)0x1d, (byte)(byte)/* ........ */
         (byte)0x1e, (byte)(byte)0x1f, (byte)(byte)0x20, (byte)(byte)0x21, (byte)(byte)0x22, (byte)(byte)0x23, (byte)(byte)0x24, (byte)(byte)0x25, (byte)(byte)/* .. !"#$% */
         (byte)0x26, (byte)(byte)0x27, (byte)(byte)0x28, (byte)(byte)0x29, (byte)(byte)0x2a, (byte)(byte)0x2b, (byte)(byte)0x2c, (byte)(byte)0x2d, (byte)(byte)/* &'()*+,- */
         (byte)0x2e, (byte)(byte)0x2f, (byte)(byte)0x30, (byte)(byte)0x31, (byte)(byte)0x32, (byte)(byte)0x33, (byte)(byte)0x34, (byte)(byte)0x35, (byte)(byte)/* ./012345 */
         (byte)0x36, (byte)(byte)0x37                                      /* 67 */
    };

    static final byte[] gre1_2 = {
        (byte)0x45, (byte)(byte)0x00, (byte)(byte)/* V..<..E. */
        (byte)0x00, (byte)(byte)0x6c, (byte)(byte)0xe4, (byte)(byte)0xb4, (byte)(byte)0x40, (byte)(byte)0x00, (byte)(byte)0xe1, (byte)(byte)0x2f, (byte)(byte)/* .l..@../ */
        (byte)0x37, (byte)(byte)0x60, (byte)(byte)0xac, (byte)(byte)0x10, (byte)(byte)0x12, (byte)(byte)0x97, (byte)(byte)0xac, (byte)(byte)0x10, (byte)(byte)/* 7`...... */
        (byte)0x12, (byte)(byte)0x96, (byte)(byte)0x00, (byte)(byte)0x00, (byte)(byte)0x08, (byte)(byte)0x00, (byte)(byte)0x45, (byte)(byte)0x00, (byte)(byte)/* ......E. */
        (byte)0x00, (byte)(byte)0x54, (byte)(byte)0xaf, (byte)(byte)0xd6, (byte)(byte)0x00, (byte)(byte)0x00, (byte)(byte)0x40, (byte)(byte)0x01, (byte)(byte)/* .T....@. */
        (byte)0x24, (byte)(byte)0xcf, (byte)(byte)0x0a, (byte)(byte)0x00, (byte)(byte)0xc9, (byte)(byte)0x01, (byte)(byte)0x0a, (byte)(byte)0x00, (byte)(byte)/* $....... */
        (byte)0xc9, (byte)(byte)0x02, (byte)(byte)0x00, (byte)(byte)0x00, (byte)(byte)0x40, (byte)(byte)0xe4, (byte)(byte)0x0a, (byte)(byte)0xea, (byte)(byte)/* ....@... */
        (byte)0x00, (byte)(byte)0x16, (byte)(byte)0x01, (byte)(byte)0x06, (byte)(byte)0x30, (byte)(byte)0x53, (byte)(byte)0x8e, (byte)(byte)0xbf, (byte)(byte)/* ....0S.. */
        (byte)0x09, (byte)(byte)0x00, (byte)(byte)0x08, (byte)(byte)0x09, (byte)(byte)0x0a, (byte)(byte)0x0b, (byte)(byte)0x0c, (byte)(byte)0x0d, (byte)(byte)/* ........ */
        (byte)0x0e, (byte)(byte)0x0f, (byte)(byte)0x10, (byte)(byte)0x11, (byte)(byte)0x12, (byte)(byte)0x13, (byte)(byte)0x14, (byte)(byte)0x15, (byte)(byte)/* ........ */
        (byte)0x16, (byte)(byte)0x17, (byte)(byte)0x18, (byte)(byte)0x19, (byte)(byte)0x1a, (byte)(byte)0x1b, (byte)(byte)0x1c, (byte)(byte)0x1d, (byte)(byte)/* ........ */
        (byte)0x1e, (byte)(byte)0x1f, (byte)(byte)0x20, (byte)(byte)0x21, (byte)(byte)0x22, (byte)(byte)0x23, (byte)(byte)0x24, (byte)(byte)0x25, (byte)(byte)/* .. !"#$% */
        (byte)0x26, (byte)(byte)0x27, (byte)(byte)0x28, (byte)(byte)0x29, (byte)(byte)0x2a, (byte)(byte)0x2b, (byte)(byte)0x2c, (byte)(byte)0x2d, (byte)(byte)/* &'()*+,- */
        (byte)0x2e, (byte)(byte)0x2f, (byte)(byte)0x30, (byte)(byte)0x31, (byte)(byte)0x32, (byte)(byte)0x33, (byte)(byte)0x34, (byte)(byte)0x35, (byte)(byte)/* ./012345 */
        (byte)0x36, (byte)(byte)0x37                                      /* 67 */
    };

    //GRE tunnel with keys, sequence number and checksum. inside a icmp request/reply
    static final byte[] gre2_1 = {
                (byte)0x45, (byte)0x00, (byte)0x00, (byte)0x78, (byte)0xe2, (byte)0x4c, (byte)0x40, (byte)0x00, /* E..x.L@. */
                (byte)0xe1, (byte)0x2f, (byte)0x39, (byte)0xbc, (byte)0xac, (byte)0x10, (byte)0x12, (byte)0x96, /* ./9..... */
                (byte)0xac, (byte)0x10, (byte)0x12, (byte)0x97, (byte)0xb0, (byte)0x00, (byte)0x08, (byte)0x00, /* ........ */
                (byte)0x44, (byte)0xc4, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, /* D....... */
                (byte)0x00, (byte)0x00, (byte)0x03, (byte)0x3a, (byte)0x45, (byte)0x00, (byte)0x00, (byte)0x54, /* ...:E..T */
                (byte)0x73, (byte)0x67, (byte)0x40, (byte)0x00, (byte)0x40, (byte)0x01, (byte)0x21, (byte)0x3e, /* sg@.@.!> */
                (byte)0x0a, (byte)0x00, (byte)0xc9, (byte)0x02, (byte)0x0a, (byte)0x00, (byte)0xc9, (byte)0x01, /* ........ */
                (byte)0x08, (byte)0x00, (byte)0x0b, (byte)0x90, (byte)0x0b, (byte)0x5d, (byte)0x00, (byte)0x06, /* .....].. */
                (byte)0x86, (byte)0x16, (byte)0x30, (byte)0x53, (byte)0x3c, (byte)0xa0, (byte)0x03, (byte)0x00, /* ..0S<... */
                (byte)0x08, (byte)0x09, (byte)0x0a, (byte)0x0b, (byte)0x0c, (byte)0x0d, (byte)0x0e, (byte)0x0f, /* ........ */
                (byte)0x10, (byte)0x11, (byte)0x12, (byte)0x13, (byte)0x14, (byte)0x15, (byte)0x16, (byte)0x17, /* ........ */
                (byte)0x18, (byte)0x19, (byte)0x1a, (byte)0x1b, (byte)0x1c, (byte)0x1d, (byte)0x1e, (byte)0x1f, /* ........ */
                (byte)0x20, (byte)0x21, (byte)0x22, (byte)0x23, (byte)0x24, (byte)0x25, (byte)0x26, (byte)0x27, /*  !"#$%&' */
                (byte)0x28, (byte)0x29, (byte)0x2a, (byte)0x2b, (byte)0x2c, (byte)0x2d, (byte)0x2e, (byte)0x2f, /* ()*+,-./ */
                (byte)0x30, (byte)0x31, (byte)0x32, (byte)0x33, (byte)0x34, (byte)0x35, (byte)0x36, (byte)0x37  /* 01234567 */
    };

    static final byte[] gre2_2 = {
                (byte)0x45, (byte)0x00, (byte)0x00, (byte)0x78, (byte)0xf0, (byte)0x5e, (byte)0x40, (byte)0x00, /* E..x.^@. */
                (byte)0xe1, (byte)0x2f, (byte)0x2b, (byte)0xaa, (byte)0xac, (byte)0x10, (byte)0x12, (byte)0x97, /* ./+..... */
                (byte)0xac, (byte)0x10, (byte)0x12, (byte)0x96, (byte)0xb0, (byte)0x00, (byte)0x08, (byte)0x00, /* ........ */
                (byte)0x47, (byte)0xf5, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, /* G....... */
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x09, (byte)0x45, (byte)0x00, (byte)0x00, (byte)0x54, /* ....E..T */
                (byte)0xbb, (byte)0x7a, (byte)0x00, (byte)0x00, (byte)0x40, (byte)0x01, (byte)0x19, (byte)0x2b, /* .z..@..+ */
                (byte)0x0a, (byte)0x00, (byte)0xc9, (byte)0x01, (byte)0x0a, (byte)0x00, (byte)0xc9, (byte)0x02, /* ........ */
                (byte)0x00, (byte)0x00, (byte)0x13, (byte)0x90, (byte)0x0b, (byte)0x5d, (byte)0x00, (byte)0x06, /* .....].. */
                (byte)0x86, (byte)0x16, (byte)0x30, (byte)0x53, (byte)0x3c, (byte)0xa0, (byte)0x03, (byte)0x00, /* ..0S<... */
                (byte)0x08, (byte)0x09, (byte)0x0a, (byte)0x0b, (byte)0x0c, (byte)0x0d, (byte)0x0e, (byte)0x0f, /* ........ */
                (byte)0x10, (byte)0x11, (byte)0x12, (byte)0x13, (byte)0x14, (byte)0x15, (byte)0x16, (byte)0x17, /* ........ */
                (byte)0x18, (byte)0x19, (byte)0x1a, (byte)0x1b, (byte)0x1c, (byte)0x1d, (byte)0x1e, (byte)0x1f, /* ........ */
                (byte)0x20, (byte)0x21, (byte)0x22, (byte)0x23, (byte)0x24, (byte)0x25, (byte)0x26, (byte)0x27, /*  !"#$%&' */
                (byte)0x28, (byte)0x29, (byte)0x2a, (byte)0x2b, (byte)0x2c, (byte)0x2d, (byte)0x2e, (byte)0x2f, /* ()*+,-./ */
                (byte)0x30, (byte)0x31, (byte)0x32, (byte)0x33, (byte)0x34, (byte)0x35, (byte)0x36, (byte)0x37  /* 01234567 */
    };


    static final byte[][] greStream={gre1_1,gre1_2,gre2_2,gre2_2};

    @Test
    public void testSimpleGrePacket() {

        ByteBuffer rawData=ByteBuffer.allocate(gre1_1.length);
        rawData.put(gre1_1);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, (byte)rawData.limit());
        Assert.assertNotNull(packet);
        logger.info("gre packet: {}", packet.toString());
        GREPacket gre= (GREPacket) packet;
        Assert.assertFalse(gre.hasGreKeys());
        Assert.assertFalse(gre.hasGreChecksum());
        Assert.assertFalse(gre.hasGreSequenceNumbers());
        Assert.assertNotNull(gre.getHeader());
        Assert.assertEquals(4, gre.getHeaderSize());
        Assert.assertEquals(84, gre.getPayLoadSize());
        ByteBuffer payload=gre.getPayLoad();
        Assert.assertEquals(0x0800,gre.getEmbeddedProtocol());
        Assert.assertEquals(0x0,gre.getGreFlags());
        //payload is ICMP packet so lets see if this works
        ICMPPacket icmpPacket = (ICMPPacket) IPFactory.processRawPacket(payload,payload.limit());
        Assert.assertNotNull(icmpPacket);
        logger.info("  gre payload: {}",icmpPacket.toString());
        Assert.assertEquals(ICMPPacket.ECHO_REQUEST,icmpPacket.getType());
    }

    @Test
    public void testFullGrePacket() {

        ByteBuffer rawData=ByteBuffer.allocate(gre2_1.length);
        rawData.put(gre2_1);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, (byte)rawData.limit());
        Assert.assertNotNull(packet);
        logger.info("gre packet: {}", packet.toString());
        GREPacket gre= (GREPacket) packet;
        Assert.assertTrue(gre.hasGreKeys());
        Assert.assertTrue(gre.hasGreChecksum());
        Assert.assertTrue(gre.hasGreSequenceNumbers());
        Assert.assertEquals(1, gre.getGreKey());
        Assert.assertEquals(0x33a, gre.getGreSeqNumber());
        Assert.assertEquals(0x44c4, gre.getGreChecksum());
        Assert.assertEquals(16, gre.getHeaderSize());
        Assert.assertEquals(84, gre.getPayLoadSize());
        Assert.assertEquals(0x0800,gre.getEmbeddedProtocol());
        Assert.assertEquals((short)0xb000,gre.getGreFlags());

        ByteBuffer payload=gre.getPayLoad();
        //payload is ICMP packet so lets see if this works
        ICMPPacket icmpPacket = (ICMPPacket) IPFactory.processRawPacket(payload,payload.limit());
        Assert.assertNotNull(icmpPacket);
        logger.info("  gre payload: {}",icmpPacket.toString());
        Assert.assertEquals(ICMPPacket.ECHO_REQUEST,icmpPacket.getType());
    }

}
