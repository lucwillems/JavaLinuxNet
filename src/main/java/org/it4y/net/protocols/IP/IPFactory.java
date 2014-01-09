package org.it4y.net.protocols.IP;

import org.it4y.net.protocols.IP.ICMP.ICMPPacket;
import org.it4y.net.protocols.IP.TCP.TCPPacket;
import org.it4y.net.protocols.IP.UDP.UDPPacket;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luc on 12/26/13.
 */
public class IPFactory {

    private interface ipv4Factory {
        IpPacket create(ByteBuffer buffer, int size);
    }

    public static final Map<Byte, ipv4Factory> ipv4FactoryMap = Collections.unmodifiableMap(new HashMap<Byte, ipv4Factory>() {{
        put((byte) 1, new ipv4Factory() {
            public IpPacket create(ByteBuffer buffer, int size) {
                return new ICMPPacket(buffer, size);
            }
        });
        put((byte) 17, new ipv4Factory() {
            public IpPacket create(ByteBuffer buffer, int size) {
                return new UDPPacket(buffer, size);
            }
        });
        put((byte) 6, new ipv4Factory() {
            public IpPacket create(ByteBuffer buffer, int size) {
                return new TCPPacket(buffer, size);
            }
        });
    }});

    public static final IpPacket processRawPacket(ByteBuffer buffer, int size) {
        //check for IPv4 and protocol
        byte ipVersion = (byte) (buffer.get(0) >> 4);
        if (ipVersion == 4) {
            ipv4Factory factory = ipv4FactoryMap.get((byte) buffer.get(9));
            if (factory == null) {
                return new IpPacket(buffer, size);
            } else {
                return factory.create(buffer, size);
            }
        }
        return null;
    }
}
