/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.protocols.IP.ICMP;

import org.it4y.jni.linux.jhash;
import org.it4y.net.protocols.IP.IpPacket;
import org.it4y.util.Hexdump;

import java.nio.ByteBuffer;

public class ICMPPacket extends IpPacket {

    public static final byte ICMP_HEADER_SIZE = 4;
    public static final byte ECHO_REPLY = (byte) 0;
    public static final byte ECHO_REQUEST = (byte) 8;

    public static final int header_icmp_type=0;
    public static final int header_icmp_code=1;
    public static final int header_icmp_checksum=2;
    public static final int icmp_identifier=4;
    public static final int icmp_seqnumber=6;
    public static final int icmp_timestamp=8;

    private int ip_header_offset=0;

    public ICMPPacket(ByteBuffer buffer, int size) {
        super(buffer, size);
        //get IP header size
        ip_header_offset=super.getHeaderSize();
    }

    public ICMPPacket(IpPacket ip) {
        super(ip.getRawPacket(),ip.getRawSize());
        //get IP header size
        ip_header_offset=ip.getHeaderSize();
    }

    public int getHeaderSize() {
        return ICMP_HEADER_SIZE;
    }

    public int getPayLoadSize() {
        return rawLimit - ip_header_offset - ICMP_HEADER_SIZE;
    }

    public ByteBuffer getHeader() {
        //get IP header size
        resetBuffer();
        rawPacket.position(ip_header_offset);
        rawPacket.limit(ip_header_offset + ICMP_HEADER_SIZE);
        return rawPacket.slice();
    }

    public ByteBuffer getPayLoad() {
        //get IP header size
        resetBuffer();
        rawPacket.position(ip_header_offset);
        rawPacket.limit(rawSize);
        return rawPacket.slice();
    }

    public byte getType() {
        return rawPacket.get(ip_header_offset + header_icmp_type);
    }

    public void setType(byte type) {
        rawPacket.put(ip_header_offset + header_icmp_type, type);
    }

    public byte getCode() {
        return rawPacket.get(ip_header_offset + header_icmp_code);
    }

    public void setCode(byte code) {
        rawPacket.put(ip_header_offset + header_icmp_code, code);
    }

    public short getICMPChecksum() {
        return rawPacket.getShort(ip_header_offset + header_icmp_checksum);
    }

    public void resetICMPChecksum() {
       rawPacket.putShort(ip_header_offset + header_icmp_checksum, (short)0x0000);
    }
    public short updateICMPChecksum() {
        resetICMPChecksum();
        //checksum calculation for Ip header
        final short checksum = rfc1071Checksum(ip_header_offset, ICMP_HEADER_SIZE + getPayLoadSize());
        rawPacket.putShort(ip_header_offset + header_icmp_checksum,checksum);
        return checksum;
    }

    public short getIdentifier() {
        return rawPacket.getShort(ip_header_offset + icmp_identifier);
    }

    public void setIdentifier(short ident) {
        rawPacket.putShort(ip_header_offset + icmp_identifier, ident);
    }

    public short getSequenceNumber() {
        return rawPacket.getShort(ip_header_offset + icmp_seqnumber);
    }
    public void putSequenceNumber(short number) {
        rawPacket.putShort(ip_header_offset + icmp_seqnumber, number);
    }

    public long getTimeStamp() {
        return rawPacket.getLong(ip_header_offset + icmp_timestamp);
    }

    public void setTimeStamp(long epochTime) {
        rawPacket.putLong(ip_header_offset + icmp_timestamp, epochTime);
    }

    public boolean isEchoRequest() {
        return getType() == ECHO_REQUEST;
    }

    public boolean isEchoReply() {
        return getType() == ECHO_REPLY;
    }

    /*
     * this will convert a ICMP request to ICMP reply without copying data :-)
     *
     */
    public void convertToEchoReply() {
        if (getType() != ECHO_REQUEST) {
            throw new RuntimeException("Not ICMP request");
        }
        resetBuffer();
        setTTL((byte) 64);      //TTL
        //swap src/dst address
        swapSourceDestination();
        updateChecksum();
        //change it to a reply, preserve data
        setType(ECHO_REPLY);
        updateICMPChecksum();
    }

    @Override
    public int getDstRoutingHash() {
        int dst=rawPacket.getInt(header_dst);  //32 dest address
        int src=rawPacket.getInt(header_src);  //32 src address
        int port=0;
        int proto=(int)rawPacket.get(header_protocol);
        return jhash.jhash_3words(dst, port, proto, src);
    }

    @Override
    public int getFlowHash() {
        int dst=rawPacket.getInt(header_dst);  //32 dest address
        int src=rawPacket.getInt(header_src);  //32 src address
        int proto=((int)rawPacket.get(header_protocol)) &0xff;
        int code=((int)rawPacket.get(ip_header_offset + header_icmp_code)) & 0xff;
        int msgtype=((int)rawPacket.get(ip_header_offset+header_icmp_type))& 0xff;
        //match flow for request/reply
        if (msgtype==(int)ECHO_REPLY || msgtype==(int) ECHO_REQUEST) {
            //pair ping based on there identity
            short msgIdent=getIdentifier();
            proto=proto<<16+((int)msgIdent&0xffff);
        }else {
            proto=proto<<16+code<<8+msgtype;
        }
        return jhash.jhash_3words(dst, src, proto, 0);
    }

    @Override
    public String toString() {
        StringBuilder s=new StringBuilder(128);
        s.append(super.toString()).append("[c:").append(getCode()).append("t:").append(getType()).append("]");
        if (getType() == ECHO_REQUEST || getType() == ECHO_REPLY) {
          s.append("i: ").append(String.format("0x%04x",getIdentifier())).append(" c:").append((int)getSequenceNumber()&0xffff);
        } else {
            if (getPayLoadSize()>0) {
                s.append(Hexdump.bytesToHex(getPayLoad(), Math.min(getPayLoadSize(), 20)));
            }
        }
        return s.toString();

    }

}
