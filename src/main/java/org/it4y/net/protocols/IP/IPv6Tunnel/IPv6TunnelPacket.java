package org.it4y.net.protocols.IP.IPv6Tunnel;

import org.it4y.net.protocols.IP.IpPacket;
import org.it4y.util.Hexdump;

import java.nio.ByteBuffer;

/**
 * Created by luc on 3/24/14.
 */
public class IPv6TunnelPacket extends IpPacket {
    public static final byte PROTOCOL=41;
    public static final int IPv6Tunnel_HEADER_SIZE = 0;

    private int ip_header_offset;

    public IPv6TunnelPacket(ByteBuffer buffer, int size) {
        super(buffer, size);
        ip_header_offset=super.getHeaderSize();
    }

    public IPv6TunnelPacket(IpPacket ip) {
        super(ip.getRawPacket(),ip.getRawSize());
        //get IP header size
        ip_header_offset=ip.getHeaderSize();
    }

    @Override
    public void initIpHeader() {
        super.initIpHeader();
        setProtocol(IPv6TunnelPacket.PROTOCOL);
        ip_header_offset=super.getIpHeaderSize();
    }

    @Override
    public int getHeaderSize() {
        return IPv6Tunnel_HEADER_SIZE;
    }

    @Override
    public int getPayLoadSize() {
        return rawLimit - ip_header_offset - getHeaderSize();
    }

    @Override
    public ByteBuffer getHeader() {
        //get IP header size
        resetBuffer();
        rawPacket.position(ip_header_offset);
        rawPacket.limit(ip_header_offset + getHeaderSize());
        return rawPacket.slice();
    }

    @Override
    public ByteBuffer getPayLoad() {
        //get IP header size
        resetBuffer();
        rawPacket.position(ip_header_offset+getHeaderSize());
        rawPacket.limit(rawSize);
        return rawPacket.slice();
    }

    @Override
    public String toString() {
        final StringBuilder s=new StringBuilder(128);
        if (getPayLoadSize()>0) {
            s.append(Hexdump.bytesToHex(getPayLoad(), Math.min(getPayLoadSize(), 20)));
        }
        return s.toString();
    }


}
