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

package org.it4y.net.protocols.IP.TCP;

import org.it4y.jni.linux.jhash;
import org.it4y.net.protocols.IP.IpPacket;
import org.it4y.util.Hexdump;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class TCPPacket extends IpPacket {
    public static final byte PROTOCOL=6;

    private static final int header_tcp_sport=0;
    private static final int header_tcp_dport=2;
    private static final int header_tcp_sequence_number=4;
    private static final int header_tcp_ack_number=8;
    private static final int header_tcp_data_offset=12;
    private static final int header_tcp_flags1=12;
    private static final int header_tcp_flags2=13;
    private static final int header_tcp_window=14;
    private static final int header_tcp_checksum=16;
    private static final int header_tcp_urgentpointer=18;

    private final Logger log= LoggerFactory.getLogger(TCPPacket.class);
    private int ip_header_size;


    public TCPPacket(final ByteBuffer buffer, final int size) {
        super(buffer, size);
        ip_header_size= getIpHeaderSize();
    }

    public TCPPacket(final IpPacket ip) {
        super(ip.getRawPacket(),ip.getRawSize());
        ip_header_size=ip.getIpHeaderSize();
    }

    @Override
    public void initIpHeader() {
        super.initIpHeader();
        setProtocol(PROTOCOL);
        ip_header_size= getIpHeaderSize();
    }

    @Override
    public int getHeaderSize() {
        return getDataOffset();
    }

    @Override
    public int getPayLoadSize() {
        return rawLimit - getIpHeaderSize() - getHeaderSize();
    }

    public ByteBuffer getHeader() {
        resetBuffer();
        final int oposition=rawPacket.position();
        final int olimit=rawPacket.limit();
        try {
            rawPacket.position(ip_header_size);
            rawPacket.limit(ip_header_size + getHeaderSize());
            return rawPacket.slice();
        } finally {
            rawPacket.limit(olimit);
            rawPacket.position(oposition);
        }
    }

    public ByteBuffer getPayLoad() {
        resetBuffer();
        final int oposition=rawPacket.position();
        final int olimit=rawPacket.limit();
        try {
            rawPacket.position(ip_header_size+getHeaderSize());
            return rawPacket.slice();
        } finally {
            rawPacket.limit(olimit);
            rawPacket.position(oposition);
        }
    }

    public void swapSourceDestinationPort() {
        final short srcPort = rawPacket.getShort(ip_header_size);
        rawPacket.putShort(ip_header_size, rawPacket.getShort(ip_header_size + header_tcp_dport));
        rawPacket.putShort(ip_header_size + header_tcp_dport, srcPort);
    }

    public short getSourcePort() {
        return rawPacket.getShort(ip_header_size);
    }
    public void setSourcePort(final short port) {
        rawPacket.putShort(ip_header_size, port);
    }

    public short getDestinationPort() {
        return rawPacket.getShort(ip_header_size + header_tcp_dport);
    }
    public void setDestinationPort(final short port) {
       rawPacket.putShort(ip_header_size + header_tcp_dport, port);
    }

    public int getSequenceNumber() {
        return rawPacket.getInt(ip_header_size + header_tcp_sequence_number);
    }
    public void setSequenceNumber(final int seq) {
       rawPacket.putInt(ip_header_size + header_tcp_sequence_number, seq);
    }

    public int getAckNumber() {
        return rawPacket.getInt(ip_header_size + header_tcp_ack_number);
    }
    public void setAckNumber(final int ack) {
        rawPacket.putInt(ip_header_size + header_tcp_ack_number, ack);
    }

    public int getDataOffset() {
       //first 4bits=number of 4bytes header size
       return (((int)rawPacket.getShort(ip_header_size + header_tcp_data_offset) &0xf000) >>10);
    }

    public boolean isNS() {
        return ((rawPacket.get(ip_header_size + header_tcp_flags1) & (byte) 0x01) > (byte) 0);
    }

    public boolean isCWR() {
        return ((rawPacket.get(ip_header_size + header_tcp_flags2) & (byte) 0x80) > (byte) 0);
    }

    public boolean isECE() {
        return ((rawPacket.get(ip_header_size + header_tcp_flags2) & (byte) 0x40) > (byte) 0);
    }

    public boolean isURG() {
        return ((rawPacket.get(ip_header_size + header_tcp_flags2) & (byte) 0x20) > (byte) 0);
    }

    public boolean isACK() {
        return ((rawPacket.get(ip_header_size + header_tcp_flags2) & (byte) 0x10) > (byte) 0);
    }

    public boolean isPSH() {
        return ((rawPacket.get(ip_header_size + header_tcp_flags2) & (byte) 0x08) > (byte) 0);
    }

    public boolean isRST() {
        return ((rawPacket.get(ip_header_size + header_tcp_flags2) & (byte) 0x04) > (byte) 0);
    }

    public boolean isSYN() {
        return ((rawPacket.get(ip_header_size + header_tcp_flags2) & (byte) 0x02) > (byte) 0);
    }

    public boolean isFIN() {
        return ((rawPacket.get(ip_header_size + header_tcp_flags2) & (byte) 0x01) > (byte) 0);
    }

    public short getWindowSize() {
        return rawPacket.getShort(ip_header_size + header_tcp_window);
    }

    public void setWindowSize(final short window) {
        rawPacket.putShort(ip_header_size + header_tcp_window, window);
    }

    public short getTCPChecksum() {
        return rawPacket.getShort(ip_header_size + header_tcp_checksum);
    }
    public void setTCPChecksum(final short checksum) {
        rawPacket.putShort(ip_header_size + header_tcp_checksum, checksum);
    }

    public short getUrgentPointer() {
        return rawPacket.getShort(ip_header_size+header_tcp_urgentpointer);
    }
    public void setUrgentPointer(final short pointer) {
        rawPacket.putShort(ip_header_size + header_tcp_urgentpointer, pointer);
    }

    public boolean hasOptions() {
        return getHeaderSize() > 20;
    }

    public TCPOption convertToTCPOption(final byte type, final int i) {
        if (type == TCPOption.END) {
            return new TCPoptionEnd();
        } else if (type == TCPOption.NOP) {
            return new TCPoptionNOP();
        } else if (type == TCPOption.MSS) {
            return new TCPoptionMSS(rawPacket.getShort(i + 2));
        } else if (type == TCPOption.WSCALE) {
            return new TCPoptionWindowScale(rawPacket.get(i + 2));
        } else if (type == TCPOption.SACK_ENABLED) {
            return new TCPoptionSACK();
        } else if (type == TCPOption.TIMESTAMP) {
            return new TCPoptionTimeStamp(rawPacket.getInt(i + 2), rawPacket.getInt(i + 6));
        } else {
            log.debug("Ignoring tcp option {}", ((int) type));
        }
        return null;
    }

    public TCPOption getOptionByType(final byte type) {
        resetBuffer();
        //position first option byte
        int i = ip_header_size + 20;
        while (i < ip_header_size + getHeaderSize()) {
            final byte optionId = rawPacket.get(i);
            final byte length = rawPacket.get(i + 1);
            if (optionId == type) {
                return convertToTCPOption(optionId, i);
            }
            //options are stranges :-)
            if (optionId > 1) {
                i = i + (int) length;
            } else i++;
        }
        return null;
    }

    public TCPOption getOption(final int indx) {
        //We need to walk the option bytes,
        //options always start with
        //1) byte : option nr
        //2) byte : length (including this field and first field
        //3) byte... : n bytes data (depending on length )
        resetBuffer();
        //position first option byte
        int cnt = 0;
        int i = ip_header_size + 20;
        while (i < ip_header_size + getHeaderSize()) {
            final byte optionId = rawPacket.get(i);
            final byte length = rawPacket.get(i + 1);
            if (cnt == indx) {
                return convertToTCPOption(optionId, i);
            }
            //options are stranges :-)
            if (optionId > 1) {
                i = i + (int) length;
            } else i++;
            cnt++;
        }
        return null;
    }

    @Override
    public String toString() {
        //incase we have a truncated packet from pcap file we should prepare for this
        final StringBuilder s = new StringBuilder(128);
        s.append(super.toString());
        try {
            s.append("TCP[");
            s.append("h:").append(getHeaderSize()).append(',');
            s.append("p:").append(getPayLoadSize()).append(',');
            s.append("sport:").append((int) (getSourcePort()) & 0xffff).append(',');
            s.append("dport:").append((int) (getDestinationPort()) & 0xffff).append(',');
            s.append("seq:").append((long) (getSequenceNumber()) & 0xffffffffL).append(',');
            s.append("ack:").append((long) (getAckNumber()) & 0xffffffffL).append(',');
            if (isSYN()) {
                s.append('S');
            }
            if (isACK()) {
                s.append('A');
            }
            if (isRST()) {
                s.append('R');
            }
            if (isFIN()) {
                s.append('F');
            }
            s.append(',');
            s.append("wnd:").append((int)getWindowSize()&0xffff).append(',');
            s.append("urg:").append((int)getUrgentPointer()&0xffff).append(',');
            //TCP options
            int x = 0;
            TCPOption o;
            while ((o = getOption(x)) != null) {
                x++;
                s.append(o.toString()).append(',');
            }
            s.setLength(s.length()-1);
            s.append(']');
            if (getPayLoadSize()>0) {
                s.append("\n payload:").append(Hexdump.bytesToHex(getPayLoad(),Math.min(getPayLoadSize(),10)));
            }
        } catch (IndexOutOfBoundsException ignore){
            s.append("...(truncated)");
        }
        return s.toString();
    }

    @Override
    public int getDstRoutingHash() {
            final int dst=rawPacket.getInt(header_dst);  //32 dest address
            final int src=rawPacket.getInt(header_src);  //32 src address
            final int port=(int)rawPacket.getShort(ip_header_size+header_tcp_sport)<<16 + (int)rawPacket.getShort(ip_header_size+header_tcp_dport);
            final int proto=(int)rawPacket.get(header_protocol);
            return jhash.jhash_3words(dst, port, proto, src);
    }

    @Override
    public int getFlowHash() {
        final int dst=rawPacket.getInt(header_dst);  //32 dest address
        final int src=rawPacket.getInt(header_src);  //32 src address
        final int proto=((int)rawPacket.get(header_protocol)&0xff)<<16;
        final int sport=((int)rawPacket.getShort(ip_header_size+header_tcp_sport)) &0xffff;
        final int dport=((int)rawPacket.getShort(ip_header_size+header_tcp_dport)) &0xffff;
        final int port=sport<<16+dport;
        return jhash.jhash_3words(dst, src, port,proto);
    }

    @Override
    public int getReverseFlowHash() {
        final int src=rawPacket.getInt(header_dst);  //32 dest address
        final int dst=rawPacket.getInt(header_src);  //32 src address
        final int proto=((int)rawPacket.get(header_protocol)&0xff)<<16;
        final int dport=((int)rawPacket.getShort(ip_header_size+header_tcp_sport)) &0xffff;
        final int sport=((int)rawPacket.getShort(ip_header_size+header_tcp_dport)) &0xffff;
        final int port=sport<<16+dport;
        return jhash.jhash_3words(dst, src, port,proto);
    }

}
