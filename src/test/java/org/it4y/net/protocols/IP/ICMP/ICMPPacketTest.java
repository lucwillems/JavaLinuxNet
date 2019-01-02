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

package org.it4y.net.protocols.IP.ICMP;

import org.it4y.net.protocols.IP.IPFactory;
import org.it4y.net.protocols.IP.IpPacket;
import org.it4y.util.Hexdump;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by luc on 3/21/14.
 */
public class ICMPPacketTest {
    private Logger log= LoggerFactory.getLogger(ICMPPacket.class);
    /* this is extracted via wireshark ping 192.168.0.44 -> 8.8.8.8
     * the ethernet frame is removed from the packet
     */
    /* Frame (98 bytes)  : ping request */
    static final int icmpFlowHash=0x6e90cf64;
    static final int icmpReverseFlowHash=0x3bd8f3e0;

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

    /* Frame (98 bytes) : ping reply */
    static final byte[] pingResponse = {
            (byte)0x45, (byte)0x00, /* Kn;...E. */
            (byte)0x00, (byte)0x54, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x30, (byte)0x01, /* .T....0. */
            (byte)0xb9, (byte)0x61, (byte)0x08, (byte)0x08, (byte)0x08, (byte)0x08, (byte)0xc0, (byte)0xa8, /* .a...... */
            (byte)0x00, (byte)0x90, (byte)0x00, (byte)0x00, (byte)0x69, (byte)0x60, (byte)0x42, (byte)0x95, /* ....i`B. */
            (byte)0x00, (byte)0x23, (byte)0x1f, (byte)0x53, (byte)0x2c, (byte)0x53, (byte)0x11, (byte)0x3e, /* .#.S,S.> */
            (byte)0x0c, (byte)0x00, (byte)0x08, (byte)0x09, (byte)0x0a, (byte)0x0b, (byte)0x0c, (byte)0x0d, /* ........ */
            (byte)0x0e, (byte)0x0f, (byte)0x10, (byte)0x11, (byte)0x12, (byte)0x13, (byte)0x14, (byte)0x15, /* ........ */
            (byte)0x16, (byte)0x17, (byte)0x18, (byte)0x19, (byte)0x1a, (byte)0x1b, (byte)0x1c, (byte)0x1d, /* ........ */
            (byte)0x1e, (byte)0x1f, (byte)0x20, (byte)0x21, (byte)0x22, (byte)0x23, (byte)0x24, (byte)0x25, /* .. !"#$% */
            (byte)0x26, (byte)0x27, (byte)0x28, (byte)0x29, (byte)0x2a, (byte)0x2b, (byte)0x2c, (byte)0x2d, /* &'()*+,- */
            (byte)0x2e, (byte)0x2f, (byte)0x30, (byte)0x31, (byte)0x32, (byte)0x33, (byte)0x34, (byte)0x35, /* ./012345 */
            (byte)0x36, (byte)0x37                                      /* 67 */
    };

    @Test
    public void testICMP_ECHOREQUEST() {
        log.info("Test request...");
        ByteBuffer rawData=ByteBuffer.allocate(pingRequest.length);
        rawData.put(pingRequest);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, pingRequest.length);
        Assert.assertNotNull(packet);
        Assert.assertTrue(packet instanceof ICMPPacket);
        log.info("ICMP: {}", packet.toString());
        log.info("Dump: {}",Hexdump.bytesToHex(packet.getRawPacket(),packet.getRawSize()));
        Assert.assertEquals(((ICMPPacket) packet).getType(), ICMPPacket.ECHO_REQUEST);
        Assert.assertEquals(((ICMPPacket) packet).getCode(), 0);
        Assert.assertEquals(packet.getChecksum(),0x7E6D);
        Assert.assertEquals(((ICMPPacket) packet).getSequenceNumber(),35);
        Assert.assertEquals(((ICMPPacket) packet).getTimeStamp(),2257196573525937152L);
        Assert.assertEquals(packet.getSourceAddress(),0xc0a80090);
        Assert.assertEquals(packet.getDestinationAddress(),0x08080808);
        Assert.assertEquals(packet.getHeaderSize(),ICMPPacket.ICMP_HEADER_SIZE);
        Assert.assertEquals(packet.getIpHeaderSize(),20);
        Assert.assertEquals(packet.getTOS(),0);
        Assert.assertEquals(packet.getTTL(),64);
        Assert.assertEquals(packet.getProtocol(),1);
        Assert.assertEquals(packet.getPayLoadSize(),56);
        //check payload
        ByteBuffer payload=(packet.getPayLoad());
        Assert.assertEquals(packet.getPayLoadSize(),payload.limit());
        Assert.assertEquals(packet.getRawPacket().get(packet.getIpHeaderSize()+ICMPPacket.ICMP_HEADER_SIZE),payload.get(0));

        //check ICMP header
        ByteBuffer header=(packet.getHeader());
        Assert.assertEquals(packet.getHeaderSize(),header.limit());
        Assert.assertEquals(packet.getRawPacket().getInt(packet.getIpHeaderSize()),header.getInt(0));

        //Check IP Header
        ByteBuffer Ipheader=(packet.getIpHeader());
        Assert.assertEquals(packet.getIpHeaderSize(),Ipheader.limit());
        Assert.assertEquals(packet.getRawPacket().getInt(0),Ipheader.getInt(0));

        ICMPPacket packet1 = (ICMPPacket)packet;
        log.info("flow: {}",Integer.toHexString(packet1.getFlowHash()));
        log.info("reverse flow: {}",Integer.toHexString(packet1.getReverseFlowHash()));
        Assert.assertEquals(icmpFlowHash,packet1.getFlowHash());
        Assert.assertEquals(icmpReverseFlowHash,packet1.getReverseFlowHash());

        //Convert to reply
        ((ICMPPacket) packet).convertToEchoReply();
        Assert.assertEquals(packet.getDestinationAddress(),0xc0a80090);
        Assert.assertEquals(packet.getSourceAddress(),0x08080808);
        Assert.assertEquals(((ICMPPacket) packet).getType(), ICMPPacket.ECHO_REPLY);
        Assert.assertEquals(packet.getChecksum(),0x7E6D);

    }

    @Test
    public void testICMP_ECHOREPLY() {
        log.info("Test reply...");
        ByteBuffer rawData=ByteBuffer.allocate(pingResponse.length);
        rawData.put(pingResponse);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, pingResponse.length);
        Assert.assertNotNull(packet);
        Assert.assertTrue(packet instanceof ICMPPacket);
        log.info("ICMP: {}", packet.toString());
        Assert.assertEquals(((ICMPPacket) packet).getType(), ICMPPacket.ECHO_REPLY);
        Assert.assertEquals(((ICMPPacket) packet).getCode(), 0);
        Assert.assertEquals(packet.getChecksum(),(short)0xb961);
        Assert.assertEquals(((ICMPPacket) packet).getSequenceNumber(),35);
        Assert.assertEquals(((ICMPPacket) packet).getTimeStamp(),2257196573525937152L);
        Assert.assertEquals(packet.getDestinationAddress(),0xc0a80090);
        Assert.assertEquals(packet.getSourceAddress(),0x08080808);
        Assert.assertEquals(packet.getHeaderSize(),ICMPPacket.ICMP_HEADER_SIZE);
        Assert.assertEquals(packet.getIpHeaderSize(),20);
        Assert.assertEquals(packet.getTOS(),0);
        Assert.assertEquals(packet.getTTL(),48);
        Assert.assertEquals(packet.getProtocol(),1);
        Assert.assertEquals(packet.getPayLoadSize(),56);
        ICMPPacket packet2 = (ICMPPacket)packet;
        log.info("flow: {}",Integer.toHexString(packet2.getFlowHash()));
        log.info("reverse flow: {}",Integer.toHexString(packet2.getReverseFlowHash()));
        //this is replay packet so we need to turn around the values
        Assert.assertEquals(icmpReverseFlowHash,packet2.getFlowHash());
        Assert.assertEquals(icmpFlowHash,packet2.getReverseFlowHash());

    }

    @Test
    public void testCreateICMP() {
        //Create ICMP unreachable network packet
        ByteBuffer rawData=ByteBuffer.allocate(pingRequest.length);
        rawData.put(pingRequest);
        rawData.flip();
        IpPacket ping = IPFactory.processRawPacket(rawData, pingRequest.length);
        Assert.assertNotNull(ping);
        ByteBuffer buffer = ByteBuffer.allocate(20+8+20+40); //ip header+icmp header + orig ip header +40 bytes orig data
        ICMPPacket packet = new ICMPPacket(buffer,buffer.limit());
        packet.initIpHeader();
        packet.setType((byte) 3);
        packet.setCode((byte) 0);
        packet.setSourceAddress(0x7f000001);
        packet.setDestinationAddress(0x7f000002);
        packet.setNextHopMTU((short)1200);
        //we need to copy original IP header + 8 data bytes
        ByteBuffer payload=packet.getPayLoad().slice();
        for (int i=0;i<ping.getIpHeaderSize()+40;i++) {
            payload.put(i,ping.getRawPacket().get(i));
        }
        packet.updateChecksum();
        packet.updateICMPChecksum();
        Assert.assertEquals(ICMPPacket.PROTOCOL,packet.getProtocol());
        Assert.assertEquals(60, packet.getPayLoadSize());
        Assert.assertEquals(88, packet.getRawSize());
        Assert.assertEquals(0x45,packet.getRawPacket().get(0)); //should be ip 0x45
        Assert.assertEquals(0x03,packet.getType());
        Assert.assertEquals(0x45,packet.getPayLoad().get(0));//header of original packet
        log.info("Dump: {}",Hexdump.bytesToHex(packet.getRawPacket(),packet.getRawSize()));

    }
}
