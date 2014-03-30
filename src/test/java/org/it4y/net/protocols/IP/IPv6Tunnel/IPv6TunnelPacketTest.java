/*
 * Copyright 20124 Luc Willems (T.M.M.)
 *
 * We licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.it4y.net.protocols.IP.IPv6Tunnel;

import junit.framework.Assert;
import org.it4y.net.protocols.IP.IPFactory;
import org.it4y.net.protocols.IP.IpPacket;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by luc on 3/24/14.
 */
public class IPv6TunnelPacketTest {

    private Logger logger = LoggerFactory.getLogger(IPv6TunnelPacket.class);

    //actually this is not real uipv6 in ipv4 , only ipv4 header is ok ,and first nibble of ipv6=6
    static final byte[] ipv6Tunnel_1 = {
            (byte)0x45, (byte)0x00, (byte)0x00, (byte)0x68, (byte)0xe6, (byte)0xe4, (byte)0x40, (byte)0x00, /* E..h..@. */
            (byte)0x40, (byte)0x29, (byte)0xd6, (byte)0x5f, (byte)0xac, (byte)0x10, (byte)0x12, (byte)0x96, /* @.._.... */
            (byte)0xac, (byte)0x10, (byte)0x12, (byte)0x97, (byte)0x65, (byte)0x00, (byte)0x00, (byte)0x54, /* ....E..T */
            (byte)0x77, (byte)0xff, (byte)0x40, (byte)0x00, (byte)0x40, (byte)0x01, (byte)0x1c, (byte)0xa6, /* w.@.@... */
            (byte)0x0a, (byte)0x00, (byte)0xc9, (byte)0x02, (byte)0x0a, (byte)0x00, (byte)0xc9, (byte)0x01, /* ........ */
            (byte)0x08, (byte)0x00, (byte)0xa6, (byte)0x3b, (byte)0x0b, (byte)0x7a, (byte)0x00, (byte)0x0d, /* ...;.z.. */
            (byte)0x02, (byte)0x1e, (byte)0x30, (byte)0x53, (byte)0x24, (byte)0xc9, (byte)0x04, (byte)0x00, /* ..0S$... */
            (byte)0x08, (byte)0x09, (byte)0x0a, (byte)0x0b, (byte)0x0c, (byte)0x0d, (byte)0x0e, (byte)0x0f, /* ........ */
            (byte)0x10, (byte)0x11, (byte)0x12, (byte)0x13, (byte)0x14, (byte)0x15, (byte)0x16, (byte)0x17, /* ........ */
            (byte)0x18, (byte)0x19, (byte)0x1a, (byte)0x1b, (byte)0x1c, (byte)0x1d, (byte)0x1e, (byte)0x1f, /* ........ */
            (byte)0x20, (byte)0x21, (byte)0x22, (byte)0x23, (byte)0x24, (byte)0x25, (byte)0x26, (byte)0x27, /*  !"#$%&' */
            (byte)0x28, (byte)0x29, (byte)0x2a, (byte)0x2b, (byte)0x2c, (byte)0x2d, (byte)0x2e, (byte)0x2f, /* ()*+,-./ */
            (byte)0x30, (byte)0x31, (byte)0x32, (byte)0x33, (byte)0x34, (byte)0x35, (byte)0x36, (byte)0x37  /* 01234567 */
    };

    @Test
    public void testIPv6TunnelPacket() {
        ByteBuffer rawData = ByteBuffer.allocate(ipv6Tunnel_1.length);
        rawData.put(ipv6Tunnel_1);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, (byte) rawData.limit());
        Assert.assertNotNull(packet);
        logger.info("IPv6Tunnel packet: {}", packet.toString());
        IPv6TunnelPacket ipv6 = (IPv6TunnelPacket) packet;
        ByteBuffer payload = ipv6.getPayLoad();
        Assert.assertNotNull(ipv6.getHeader());
        Assert.assertEquals(0, ipv6.getHeaderSize());
        Assert.assertEquals(84, ipv6.getPayLoadSize());
    }

}
