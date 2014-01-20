/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.protocols.IP;

import org.it4y.jni.linux.jhash;
import org.it4y.net.RawPacket;

import java.nio.ByteBuffer;

public class IpPacket extends RawPacket {

    public static final byte ICMP = (byte) 1;
    public static final byte UDP = (byte) 17;
    public static final byte TCP = (byte) 6;

    //byte position of IP header
    public static final int header_version=0;
    public static final int header_tos=1;
    public static final int header_len=2;
    public static final int header_identification=4;
    public static final int header_flags=6;
    public static final int header_fragments=6;
    public static final int header_ttl=8;
    public static final int header_protocol=9;
    public static final int header_checksum=10;
    public static final int header_src=12;
    public static final int header_dst=16;

    public IpPacket(final ByteBuffer buffer, final int size) {
        super(buffer, size);
    }

    public int getHeaderSize() {
        return getIpHeaderSize();
    }

    public int getPayLoadSize() {
        return rawSize - getHeaderSize();
    }

    public ByteBuffer getHeader() {
        resetBuffer();
        final int opositiorn=rawPacket.position();
        final int olimit=rawPacket.limit();
        try {
            rawPacket.position(getIpHeaderSize());
            rawPacket.limit(getHeaderSize());
            return rawPacket.slice();
        } finally {
            rawPacket.limit(olimit);
            rawPacket.position(opositiorn);
        }
    }

    public ByteBuffer getPayLoad() {
        resetBuffer();
        final int opositiorn=rawPacket.position();
        final int olimit=rawPacket.limit();
        try {
            final int headerSize=getIpHeaderSize()+getHeaderSize();
            rawPacket.position(headerSize);
            rawPacket.limit(getIPLenght()-headerSize);
            return rawPacket.slice();
        } finally {
            rawPacket.limit(olimit);
            rawPacket.position(opositiorn);
        }
    }

    //IP specific header size
    public int getIpHeaderSize() {
        return (byte) ((rawPacket.get(header_version) & (byte) 0x0f) * (byte) 4) &0xff;
    }

    //IP specific header
    public ByteBuffer getIpHeader() {
        resetBuffer();
        rawPacket.position(0);
        rawPacket.limit(getIpHeaderSize());
        return rawPacket.slice();
    }

    //IP specific payload
    public ByteBuffer getIpPayLoad() {
        resetBuffer();
        rawPacket.position(getIpHeaderSize());
        rawPacket.limit(rawSize - getIpHeaderSize());
        return rawPacket.slice();
    }

    public byte getTOS() {
        return rawPacket.get(header_tos);
    }

    public void setTOS(final byte tos) {
        rawPacket.put(header_tos, tos);
    }

    public short getIPLenght() {
        return rawPacket.getShort(header_len);
    }

    public short getIdentification() {
        return rawPacket.getShort(header_identification);
    }

    public byte getFlags() {
        return (byte) (rawPacket.get(header_flags) & (byte) 0xe0 >> (byte) 5);
    }

    public short getFragmentOffset() {
        return rawPacket.getShort(header_fragments);
    }

    public byte getTTL() {
        return rawPacket.get(header_ttl);
    }

    public void setTTL(final byte ttl) {
        rawPacket.put(header_ttl, ttl);
    }

    public byte getProtocol() {
        return rawPacket.get(header_protocol);
    }
    public void setProtocol(final byte protocol) {
        rawPacket.put(header_protocol, protocol);
    }

    public short getChecksum() {
        return rawPacket.getShort(header_checksum);
    }

    public void resetChecksum() {
        rawPacket.putShort(header_checksum, (short) 0x0000); //16checksum must be 0 before calculation
    }

    public short updateChecksum() {
        resetChecksum();
        //checksum calculation for Ip header
        final short checksum = rfc1071Checksum(0, getIpHeaderSize());
        rawPacket.putShort(header_checksum, checksum);
        return checksum;
    }

    public int getSourceAddress() {
        return rawPacket.getInt(header_src);
    }

    public void setSourceAddress(final int address) {
        rawPacket.putInt(header_src, address);
    }

    public int getDestinationAddress() {
        return rawPacket.getInt(header_dst);
    }

    public void setDestinationAddress(final int address) {
        rawPacket.putInt(header_dst, address);
    }

    public void swapSourceDestination() {
        final int src = rawPacket.getInt(header_src);
        rawPacket.putInt(header_src, rawPacket.getInt(header_dst));
        rawPacket.putInt(header_dst, src);
    }

    //how to handle options ?
    public boolean hasOptions() {
        return getHeaderSize() > 20;
    }


    public short rfc1071Checksum(final int offset, final int size) {
        int sum = 0;
        int data;

        // Handle all pairs
        for (int i = 0; i < size; i = i + 2) {
            // Corrected to include @Andy's edits and various comments on Stack Overflow
            data = rawPacket.get(offset + i) << 8 & 0xFF00 | rawPacket.get(offset + i + 1) & 0xFF;
            sum += data;
            // 1's complement carry bit correction in 16-bits (detecting sign extension)
            if ((sum & 0xFFFF0000) > 0) {
                sum = sum & 0xFFFF;
                sum += 1;
            }
        }

        // Final 1's complement value correction to 16-bits
        sum = ~sum;
        sum = sum & 0xFFFF;
        return (short) sum;
    }

    public static String ipToString(final int ip) {
        final StringBuilder s = new StringBuilder(128);
        s.append(ip >> 24 & 0x00ff).append('.');
        s.append(ip >> 16 & 0x00ff).append('.');
        s.append(ip >> 8 & 0x00ff).append('.');
        s.append(ip & 0x00ff);
        return s.toString();
    }

    @Override
    public String toString() {
        final StringBuilder s=new StringBuilder(128);
        s.append("IP@").append(String.format("%08x",getFlowHash()));
        s.append('[').append("len:").append(getRawSize()).append(',');
        s.append("src:").append(ipToString(getSourceAddress())).append(',');
        s.append("dst:").append(ipToString(getDestinationAddress())).append(',');
        s.append("tos:").append(getTOS()).append(',');
        s.append("ttl:").append(getTTL()).append(']');
        return s.toString();
    }


    public void initIpHeader() {
        rawPacket.put((byte) 0x45);  //IPv4 + header size
        rawPacket.put((byte) 0x00);        //dscp
        rawPacket.putShort((short)rawSize); //size
        rawPacket.putShort((byte) 0x00); //identification
        rawPacket.putShort((byte) 0x00); //flags fragments
        rawPacket.put((byte) 0x40); //TTL
    }

    @Override
    public int getDstRoutingHash() {
        final int dst=rawPacket.getInt(header_dst);  //32 dest address
        final int src=rawPacket.getInt(header_src);  //32 src address
        final int port=0x0;
        final int proto=(int)rawPacket.get(header_protocol);
        return jhash.jhash_3words(dst, port, proto, src);
    }

    @Override
    public int getFlowHash() {
        final int dst=rawPacket.getInt(header_dst);  //32 dest address
        final int src=rawPacket.getInt(header_src);  //32 src address
        final int proto=((int)rawPacket.get(header_protocol) &0xff)<<16;
        return jhash.jhash_3words(dst, src, proto, 0);
    }

}
