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

import org.it4y.jni.libc;
import org.it4y.jni.linux.jhash;
import org.it4y.net.protocols.IP.IpPacket;
import org.it4y.util.Hexdump;

import java.nio.ByteBuffer;
import java.util.Date;

public class ICMPPacket extends IpPacket {
    public static final byte PROTOCOL=1;

    public static final byte ICMP_HEADER_SIZE = 8;
    public static final byte ECHO_REPLY = (byte) 0;
    public static final byte ECHO_REQUEST = (byte) 8;

    private static final int header_icmp_type=0;
    private static final int header_icmp_code=1;
    private static final int header_icmp_checksum=2;
    private static final int header_icmp_identifier=4;
    private static final int header_icmp_nexthopmtu=6;
    private static final int header_icmp_seqnumber=6;
    private static final int header_icmp_timestamp=8;

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

    @Override
    public void initIpHeader() {
        super.initIpHeader();
        setProtocol(ICMPPacket.PROTOCOL);
        ip_header_offset=super.getIpHeaderSize();
    }

    @Override
    public int getHeaderSize() {
        return ICMP_HEADER_SIZE;
    }

    @Override
    public int getPayLoadSize() {
        return rawLimit - getIpHeaderSize() - ICMP_HEADER_SIZE;
    }

    @Override
    public ByteBuffer getHeader() {
        //get IP header size
        resetBuffer();
        rawPacket.position(ip_header_offset);
        rawPacket.limit(ip_header_offset + ICMP_HEADER_SIZE);
        return rawPacket.slice();
    }

    @Override
    public ByteBuffer getPayLoad() {
        //get IP header size
        resetBuffer();
        rawPacket.position(getIpHeaderSize()+ICMP_HEADER_SIZE);
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

    public short getNextHopMTU() {
        return rawPacket.getShort(ip_header_offset + header_icmp_nexthopmtu);
    }

    public void setNextHopMTU(short mtu) {
        rawPacket.putShort(ip_header_offset + header_icmp_nexthopmtu, mtu);
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
        return rawPacket.getShort(ip_header_offset + header_icmp_identifier);
    }

    public void setIdentifier(short ident) {
        rawPacket.putShort(ip_header_offset + header_icmp_identifier, ident);
    }

    public short getSequenceNumber() {
        return rawPacket.getShort(ip_header_offset + header_icmp_seqnumber);
    }
    public void putSequenceNumber(short number) {
        rawPacket.putShort(ip_header_offset + header_icmp_seqnumber, number);
    }

    public long getTimeStamp() {
        return rawPacket.getLong(ip_header_offset + header_icmp_timestamp);
    }

    public void setTimeStamp(long epochTime) {
        rawPacket.putLong(ip_header_offset + header_icmp_timestamp, epochTime);
    }
    public Date getTimeStampAsDate() {
        //linux ping store date as timeval , so convert it
        int msec=libc.ntohi(rawPacket.getInt(ip_header_offset + header_icmp_timestamp + 4));
        int sec=libc.ntohi(rawPacket.getInt(ip_header_offset + header_icmp_timestamp));
        return libc.Timeval2Date(sec,msec);
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
        final StringBuilder s=new StringBuilder(128);
        s.append(super.toString()).append("[c:").append(getCode()).append("t:").append(getType()).append("]");
        if (getType() == ECHO_REQUEST || getType() == ECHO_REPLY) {
          s.append("i: ").append(String.format("0x%04x", getIdentifier()))
                  .append(" s:").append((int) getSequenceNumber() & 0xffff).append(" t:").append(getTimeStampAsDate());
        } else {
            if (getPayLoadSize()>0) {
                s.append(Hexdump.bytesToHex(getPayLoad(), Math.min(getPayLoadSize(), 20)));
            }
        }
        return s.toString();

    }

}
