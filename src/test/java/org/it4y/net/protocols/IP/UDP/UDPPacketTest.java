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

package org.it4y.net.protocols.IP.UDP;

import org.it4y.net.protocols.IP.IPFactory;
import org.it4y.net.protocols.IP.IpPacket;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by luc on 3/21/14.
 */
public class UDPPacketTest {
    private Logger logger = LoggerFactory.getLogger(UDPPacket.class);

    /* Frame (71 bytes) */
    static final int udpFlowHash=0xd7e93316;
    static final int udpReverseFlowHash=0x256d58ec;

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
    public void testUDP_DNSRequest() {

        ByteBuffer rawData = ByteBuffer.allocate(DNSrequest.length);
        rawData.put(DNSrequest);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, DNSrequest.length);
        Assert.assertNotNull(packet);
        Assert.assertTrue(packet instanceof UDPPacket);
        Assert.assertEquals(packet.getSourceAddress(),0xc0a80090);
        Assert.assertEquals(packet.getDestinationAddress(),0x08080808);
        Assert.assertEquals(packet.getHeaderSize(),UDPPacket.UDP_HEADER_SIZE);
        Assert.assertEquals(packet.getIpHeaderSize(),20);
        Assert.assertEquals(packet.getTOS(),0);
        Assert.assertEquals(packet.getTTL(),64);
        Assert.assertEquals(packet.getProtocol(),17);
        //Test payload
        ByteBuffer payload=packet.getPayLoad();
        Assert.assertEquals(8, packet.getHeaderSize());
        Assert.assertEquals(29, packet.getPayLoadSize());
        Assert.assertEquals(packet.getPayLoadSize(),payload.limit());
        Assert.assertEquals(packet.getRawPacket().get(packet.getIpHeaderSize() + UDPPacket.UDP_HEADER_SIZE), payload.get(0));

        //check ICMP header
        ByteBuffer header=(packet.getHeader());
        Assert.assertEquals(packet.getHeaderSize(),header.limit());
        Assert.assertEquals(packet.getRawPacket().getInt(packet.getIpHeaderSize()),header.getInt(0));

        //Check IP Header
        ByteBuffer Ipheader=(packet.getIpHeader());
        Assert.assertEquals(packet.getIpHeaderSize(),Ipheader.limit());
        Assert.assertEquals(packet.getRawPacket().getInt(0),Ipheader.getInt(0));

        //Check flow hash
        logger.info("flow: {}",Integer.toHexString(packet.getFlowHash()));
        logger.info("reverse flow: {}",Integer.toHexString(packet.getReverseFlowHash()));
        Assert.assertEquals(udpFlowHash,packet.getFlowHash());
        Assert.assertEquals(udpReverseFlowHash,packet.getReverseFlowHash());

        //replay
        ((UDPPacket)packet).swapSourceDestinationPort();
        packet.swapSourceDestination();
        packet.updateChecksum();
        packet.getDstRoutingHash();
        packet.getFlowHash();

        Assert.assertNotNull(packet.toString());
    }

    @Test
    public void testUDP_DNSResponse() {

        ByteBuffer rawData = ByteBuffer.allocate(DNSresponse.length);
        rawData.put(DNSresponse);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, DNSresponse.length);
        Assert.assertNotNull(packet);
        Assert.assertTrue(packet instanceof UDPPacket);

        //Check flow hash
        logger.info("flow: {}",Integer.toHexString(packet.getFlowHash()));
        logger.info("reverse flow: {}",Integer.toHexString(packet.getReverseFlowHash()));
        //reverse as this is answer
        Assert.assertEquals(udpReverseFlowHash,packet.getFlowHash());
        Assert.assertEquals(udpFlowHash,packet.getReverseFlowHash());

    }

}
