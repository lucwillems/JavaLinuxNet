/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.protocols.IP;

import org.it4y.net.RawPacket;

import java.nio.ByteBuffer;

public class IpPacket extends RawPacket {

    public static final byte ICMP = (byte) 1;
    public static final byte UDP = (byte) 17;
    public static final byte TCP = (byte) 6;

    public IpPacket(ByteBuffer buffer, int size) {
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
        int opositiorn=rawPacket.position();
        int olimit=rawPacket.limit();
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
        int opositiorn=rawPacket.position();
        int olimit=rawPacket.limit();
        try {
            int headerSize=getIpHeaderSize()+getHeaderSize();
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
        int size= (int)((byte) ((rawPacket.get(0) & (byte) 0x0f) * (byte) 4) &0xff);
        return size;
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
        return rawPacket.get(1);
    }

    public void setTOS(byte tos) {
        rawPacket.put(1, tos);
    }

    public short getIPLenght() {
        return rawPacket.getShort(2);
    }

    public short getIdentification() {
        return rawPacket.getShort(4);
    }

    public byte getFlags() {
        return (byte) (rawPacket.get(6) & (byte) 0xe0 >> (byte) 5);
    }

    public short getFragmentOffset() {
        return rawPacket.getShort(6);
    }

    public byte getTTL() {
        return rawPacket.get(8);
    }

    public void setTTL(byte ttl) {
        rawPacket.put(8, ttl);
    }

    public byte getProtocol() {
        return rawPacket.get(9);
    }

    public short getChecksum() {
        return rawPacket.getShort(10);
    }

    public void resetChecksum() {
        rawPacket.putShort(10, (short) 0x0000); //16checksum must be 0 before calculation
    }

    public short updateChecksum() {
        resetChecksum();
        //checksum calculation for Ip header
        short checksum = rfc1071Checksum(0, getIpHeaderSize());
        rawPacket.putShort(10, checksum);
        return checksum;
    }

    public int getSourceAddress() {
        return rawPacket.getInt(12);
    }

    public void setSourceAddress(int address) {
        rawPacket.putInt(12, address);
    }

    public int getDestinationAddress() {
        return rawPacket.getInt(16);
    }

    public void setDestinationAddress(int address) {
        rawPacket.putInt(16, address);
    }

    public void swapSourceDestination() {
        final int src = rawPacket.getInt(12);
        rawPacket.putInt(12, rawPacket.getInt(16));
        rawPacket.putInt(16, src);
    }

    //TODO how to handle options ?
    public boolean hasOptions() {
        return getHeaderSize() > 20;
    }


    public short rfc1071Checksum(int offset, int size) {
        int sum = 0;
        int data;

        // Handle all pairs
        for (int i = 0; i < size; i = i + 2) {
            // Corrected to include @Andy's edits and various comments on Stack Overflow
            data = (((rawPacket.get(offset + i) << 8) & 0xFF00) | ((rawPacket.get(offset + i + 1)) & 0xFF));
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

    public static StringBuffer ipToString(int ip) {
        StringBuffer s = new StringBuffer();
        s.append((ip >> 24) & 0x00ff).append(".");
        s.append((ip >> 16) & 0x00ff).append(".");
        s.append((ip >> 8) & 0x00ff).append(".");
        s.append((ip) & 0x00ff);
        return s;
    }

    @Override
    public String toString() {
        StringBuffer s=new StringBuffer();
        s.append("IP[").append("len:").append(getRawSize()).append(",");
        s.append("src:").append(ipToString(getSourceAddress())).append(",");
        s.append("dst:").append(ipToString(getDestinationAddress())).append(",");
        s.append("tos:").append(getTOS()).append(",");
        s.append("ttl:").append(getTTL()).append("]");
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
}
