/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.protocols.IP.UDP;

import org.it4y.jni.linux.jhash;
import org.it4y.net.protocols.IP.IpPacket;
import org.it4y.util.Hexdump;

import java.nio.ByteBuffer;

public class UDPPacket extends IpPacket {

    public static final byte PROTOCOL=17;
    public static final int UDP_HEADER_SIZE = 4;

    private int ip_header_size;

    private static final int header_udp_sport=0;
    private static final int header_udp_dport=2;
    private static final int header_udp_length=4;
    private static final int header_udp_checksum=6;

    public UDPPacket(ByteBuffer buffer, int size) {
        super(buffer, size);
        ip_header_size=super.getHeaderSize();
    }

    public UDPPacket(IpPacket ip) {
        super(ip.getRawPacket(),ip.getRawSize());
        ip_header_size=ip.getHeaderSize();
    }

    public int getHeaderSize() {
        return UDP_HEADER_SIZE;
    }

    public int getPayLoadSize() {
        return getLength() - UDP_HEADER_SIZE;
    }

    public ByteBuffer getHeader() {
        resetBuffer();
        int oposition=rawPacket.position();
        int olimit=rawPacket.limit();
        try {
            rawPacket.position(ip_header_size);
            rawPacket.limit(ip_header_size + UDP_HEADER_SIZE);
            return rawPacket.slice();
        } finally {
            rawPacket.limit(olimit);
            rawPacket.position(oposition);
        }
    }

    public ByteBuffer getPayLoad() {
        resetBuffer();
        int oposition=rawPacket.position();
        int olimit=rawPacket.limit();
        try {
            rawPacket.position(ip_header_size);
            return rawPacket.slice();
        } finally {
            rawPacket.limit(olimit);
            rawPacket.position(oposition);
        }
    }

    public short getLength() {
        return rawPacket.getShort(ip_header_size + header_udp_length);
    }
    public void setLength(short length) {
        rawPacket.putShort(ip_header_size + header_udp_length, length);
    }

    public short getChecksum() {
        return rawPacket.getShort(ip_header_size + header_udp_checksum);
    }

    public void resetChecksum() {
        rawPacket.putShort(ip_header_size + header_udp_checksum, (short) 0x0000); //16checksum must be 0 before calculation
    }

    @Override
    public short updateChecksum() {
        rawPacket.putShort(ip_header_size + header_udp_checksum, (short) 0x0000); //16checksum must be 0 before calculation
        final short checksum = rfc1071Checksum(ip_header_size, rawLimit - ip_header_size);
        rawPacket.putShort(ip_header_size + header_udp_checksum, checksum);
        return checksum;
    }

    public void swapSourceDestinationPort() {
        final short srcPort = rawPacket.getShort(ip_header_size);
        rawPacket.putShort(ip_header_size, rawPacket.getShort(ip_header_size + header_udp_dport));
        rawPacket.putShort(ip_header_size + header_udp_dport, srcPort);
    }

    public short getSourcePort() {
        return rawPacket.getShort(ip_header_size);
    }
    public void setSourcePort(short port) {
        rawPacket.putShort(ip_header_size, port);
    }

    public short getDestinationPort() {
        return rawPacket.getShort(ip_header_size + header_udp_dport);
    }
    public void setDestinationPort(short port) {
        rawPacket.putShort(ip_header_size + header_udp_dport, port);
    }

    public String toString() {
        StringBuffer s=new StringBuffer();
        s.append(super.toString());
        s.append("UDP[");
        s.append("sport:").append((int)getSourcePort()&0xffff).append(",");
        s.append("dport:").append((int)getDestinationPort()&0xffff).append(",");
        s.append("h:").append(getHeaderSize()).append(",");
        s.append("d:").append(getPayLoadSize());
        s.append("] ");
        s.append(Hexdump.bytesToHex(getPayLoad(),Math.min(getLength(),128)));
        return s.toString();
    };

    @Override
    public int getDstRoutingHash() {
        int dst=rawPacket.getInt(header_dst);  //32 dest address
        int src=rawPacket.getInt(header_src);  //32 src address
        int port=(int)rawPacket.getShort(ip_header_size+header_udp_sport)<<16 + (int)rawPacket.getShort(ip_header_size+header_udp_dport);
        int proto=(int)rawPacket.get(header_protocol);
        return jhash.jhash_3words(dst, port, proto, src);
    }

    @Override
    public int getFlowHash() {
        int dst=rawPacket.getInt(header_dst);  //32 dest address
        int src=rawPacket.getInt(header_src);  //32 src address
        int proto=((int)rawPacket.get(header_protocol)&0xff)<<16;
        int sport=((int)rawPacket.getShort(ip_header_size+header_udp_sport)) &0xffff;
        int dport=((int)rawPacket.getShort(ip_header_size+header_udp_dport)) &0xffff;
        int port=sport<<16+dport;
        return jhash.jhash_3words(dst, src, port,proto);
    }

}
