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

package org.it4y.net.protocols.IP;

import org.it4y.net.protocols.IP.GRE.GREPacket;
import org.it4y.net.protocols.IP.ICMP.ICMPPacket;
import org.it4y.net.protocols.IP.IPIP.IPIPPacket;
import org.it4y.net.protocols.IP.IPv6Tunnel.IPv6TunnelPacket;
import org.it4y.net.protocols.IP.TCP.TCPPacket;
import org.it4y.net.protocols.IP.UDP.UDPPacket;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class IPFactory {

    private interface ipv4Factory {
        IpPacket create(ByteBuffer buffer, int size);
    }

    public static final Map<Byte, ipv4Factory> ipv4FactoryMap = Collections.unmodifiableMap(new HashMap<Byte, ipv4Factory>() {{
        put(ICMPPacket.PROTOCOL, new ipv4Factory() {
            public IpPacket create(final ByteBuffer buffer, final int size) {
                return new ICMPPacket(buffer, size);
            }
        });
        put(IPIPPacket.PROTOCOL, new ipv4Factory() {
            public IpPacket create(final ByteBuffer buffer, final int size) {
                return new IPIPPacket(buffer, size);
            }
        });
        put(TCPPacket.PROTOCOL, new ipv4Factory() {
            public IpPacket create(final ByteBuffer buffer, final int size) {
                return new TCPPacket(buffer, size);
            }
        });
        put(UDPPacket.PROTOCOL, new ipv4Factory() {
            public IpPacket create(final ByteBuffer buffer, final int size) {
                return new UDPPacket(buffer, size);
            }
        });
        put(IPv6TunnelPacket.PROTOCOL, new ipv4Factory() {
            public IpPacket create(final ByteBuffer buffer, final int size) {
                return new IPv6TunnelPacket(buffer, size);
            }
        });
        put(GREPacket.PROTOCOL, new ipv4Factory() {
            public IpPacket create(final ByteBuffer buffer, final int size) {
                return new GREPacket(buffer, size);
            }
        });
    }});

    public static IpPacket processRawPacket(final ByteBuffer buffer, final int size) {
        //check for IPv4 and protocol
        if (buffer != null & size >0) {
            final byte ipVersion = (byte) (buffer.get(IpPacket.header_version) >> 4);
            if (ipVersion == 4) {
                final byte ipProtocol=buffer.get(IpPacket.header_protocol);
                final ipv4Factory factory = ipv4FactoryMap.get(ipProtocol);
                if (factory == null) {
                    return new IpPacket(buffer, size);
                } else {
                    return factory.create(buffer, size);
                }
             }
        }
        return null;
    }
}
