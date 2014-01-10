/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.protocols.IP.ICMP;

import org.it4y.net.protocols.IP.IpPacket;

import java.nio.ByteBuffer;

public class ICMPPacket extends IpPacket {

    public static final byte ICMP_HEADER_SIZE = 4;
    public static final byte ECHO_REPLY = (byte) 0;
    public static final byte ECHO_REQUEST = (byte) 8;

    public ICMPPacket(ByteBuffer buffer, int size) {
        super(buffer, size);
    }

    public int getHeaderSize() {
        return ICMP_HEADER_SIZE;
    }

    public int getPayLoadSize() {
        return rawLimit - super.getHeaderSize() - ICMP_HEADER_SIZE;
    }

    public ByteBuffer getHeader() {
        //get IP header size
        int headersize = super.getHeaderSize();
        resetBuffer();
        rawPacket.position(headersize);
        rawPacket.limit(headersize + ICMP_HEADER_SIZE);
        return rawPacket.slice();
    }

    public ByteBuffer getPayLoad() {
        //get IP header size
        int headersize = super.getHeaderSize() + ICMP_HEADER_SIZE;
        resetBuffer();
        rawPacket.position(headersize);
        rawPacket.limit(rawSize);
        return rawPacket.slice();
    }

    public byte getType() {
        return rawPacket.get(super.getHeaderSize() + 0);
    }

    public void setType(byte type) {
        rawPacket.put(super.getHeaderSize() + 0, type);
    }

    public byte getCode() {
        return rawPacket.get(super.getHeaderSize() + 1);
    }

    public void setCode(byte code) {
        rawPacket.put(super.getHeaderSize() + 1, code);
    }

    public short getChecksum() {
        return rawPacket.getShort(super.getHeaderSize() + 2);
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

}
