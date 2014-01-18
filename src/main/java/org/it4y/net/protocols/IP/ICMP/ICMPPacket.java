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

import java.nio.ByteBuffer;

public class ICMPPacket extends IpPacket {

    public static final byte ICMP_HEADER_SIZE = 4;
    public static final byte ECHO_REPLY = (byte) 0;
    public static final byte ECHO_REQUEST = (byte) 8;

    public static final int header_type=0;
    public static final int header_code=1;
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
        return rawPacket.get(ip_header_offset + header_type);
    }

    public void setType(byte type) {
        rawPacket.put(ip_header_offset + header_type, type);
    }

    public byte getCode() {
        return rawPacket.get(ip_header_offset + header_code);
    }

    public void setCode(byte code) {
        rawPacket.put(ip_header_offset + 1, code);
    }

    public short getChecksum() {
        return rawPacket.getShort(ip_header_offset + 2);
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
        int code=((int)rawPacket.get(ip_header_offset + header_code)) & 0xff;
        int msgtype=((int)rawPacket.get(ip_header_offset+header_type))& 0xff;
        //match flow for request/reply
        if (msgtype==(int)ECHO_REPLY)
            msgtype=(int)ECHO_REQUEST;
        proto=proto<<16+code<<8+msgtype;
        return jhash.jhash_3words(dst, src, proto, 0);
    }

}
