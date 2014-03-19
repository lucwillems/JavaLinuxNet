/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.protocols.IP.TCP;

import org.it4y.jni.linux.jhash;
import org.it4y.net.protocols.IP.IpPacket;
import org.it4y.util.Hexdump;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class TCPPacket extends IpPacket {
    private Logger log= LoggerFactory.getLogger(TCPPacket.class);

    private int ip_header_size;

    public static final int header_tcp_sport=0;
    public static final int header_tcp_dport=2;
    public static final int header_tcp_sequence_number=4;
    public static final int header_tcp_ack_number=8;
    public static final int header_tcp_data_offset=12;
    public static final int header_tcp_flags1=12;
    public static final int header_tcp_flags2=13;
    public static final int header_tcp_window=14;
    public static final int header_tcp_checksum=16;
    public static final int header_tcp_urgentpointer=18;

    public TCPPacket(ByteBuffer buffer, int size) {
        super(buffer, size);
        ip_header_size=super.getIpHeaderSize();
    }

    public TCPPacket(IpPacket ip) {
        super(ip.getRawPacket(),ip.getRawSize());
        ip_header_size=ip.getIpHeaderSize();
    }

    @Override
    public int getHeaderSize() {
        int size=getDataOffset();
        return size;
    }

    @Override
    public int getPayLoadSize() {
        return getIPLenght() - ip_header_size - getHeaderSize();
    }

    public void swapSourceDestinationPort() {
        final short srcPort = rawPacket.getShort(ip_header_size);
        rawPacket.putInt(ip_header_size, rawPacket.getInt(ip_header_size + header_tcp_dport));
        rawPacket.putInt(ip_header_size + header_tcp_dport, srcPort);
    }

    public short getSourcePort() {
        return rawPacket.getShort(ip_header_size);
    }
    public void getSourcePort(short port) {
        rawPacket.putShort(ip_header_size, port);
    }

    public short getDestinationPort() {
        return rawPacket.getShort(ip_header_size + header_tcp_dport);
    }
    public void setDestinationPort(short port) {
       rawPacket.putShort(ip_header_size + header_tcp_dport, port);
    }

    public int getSequenceNumber() {
        return rawPacket.getInt(ip_header_size + header_tcp_sequence_number);
    }
    public void setSequenceNumber(int seq) {
       rawPacket.putInt(ip_header_size + header_tcp_sequence_number, seq);
    }

    public int getAckNumber() {
        return rawPacket.getInt(ip_header_size + header_tcp_ack_number);
    }
    public void setAckNumber(int ack) {
        rawPacket.putInt(ip_header_size + header_tcp_ack_number, ack);
    }

    public int getDataOffset() {
       //first 4bits=number of 4bytes header size
       int size=(((int)rawPacket.getShort(ip_header_size + header_tcp_data_offset) &0xf000) >>10);
       return size;
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

    public void setWindowSize(short window) {
        rawPacket.putShort(ip_header_size + header_tcp_window, window);
    }

    public short getChecksumSize() {
        return rawPacket.getShort(ip_header_size + header_tcp_checksum);
    }
    public void setChecksumSize(short checksum) {
        rawPacket.putShort(ip_header_size + header_tcp_checksum, checksum);
    }

    public short getUrgentPointer() {
        return rawPacket.getShort(ip_header_size+header_tcp_urgentpointer);
    }
    public void setUrgentPointer(short pointer) {
        rawPacket.putShort(ip_header_size + header_tcp_urgentpointer, pointer);
    }

    public boolean hasOptions() {
        return getHeaderSize() > 20;
    }

    public TCPOption convertToTCPOption(byte type, int i) {
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

    public TCPOption getOptionByType(byte type) {
        resetBuffer();
        //position first option byte
        int i = ip_header_size + 20;
        while (i < ip_header_size + getHeaderSize()) {
            TCPOption option = null;
            byte optionId = rawPacket.get(i);
            byte length = rawPacket.get(i + 1);
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

    public TCPOption getOption(int indx) {
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
            TCPOption option = null;
            byte optionId = rawPacket.get(i);
            byte length = rawPacket.get(i + 1);
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
        StringBuffer s = new StringBuffer();
        s.append(super.toString());
        s.append("TCP[");
        s.append("h:").append(getHeaderSize()).append(",");
        s.append("p:").append(getPayLoadSize()).append(",");
        s.append("sport:").append((int) (getSourcePort()) & 0xffff).append(",");
        s.append("dport:").append((int) (getDestinationPort()) & 0xffff).append(",");
        s.append("seq:").append((long) (getSequenceNumber()) & 0xffffffffL).append(",");
        s.append("ack:").append((long) (getAckNumber()) & 0xffffffffL).append(",");
        if (isSYN()) {
            s.append("S");
        }
        if (isACK()) {
            s.append("A");
        }
        if (isRST()) {
            s.append("R");
        }
        if (isFIN()) {
            s.append("F");
        }
        s.append(",");
        s.append("wnd:").append((int)getWindowSize()&0xffff).append(",");
        s.append("urg:").append((int)getUrgentPointer()&0xffff).append(",");
        //TCP options
        int x = 0;
        TCPOption o;
        while ((o = getOption(x)) != null) {
            x++;
            s.append(o.toString()).append(",");
        }
        s.setLength(s.length()-1);
        s.append("]");
        if (getPayLoadSize()>0) {
            s.append("\n payload:").append(Hexdump.bytesToHex(getPayLoad(),Math.min(getPayLoadSize(),500)));
        }
        return s.toString();
    }

    @Override
    public int getDstRoutingHash() {
            int dst=rawPacket.getInt(header_dst);  //32 dest address
            int src=rawPacket.getInt(header_src);  //32 src address
            int port=(int)rawPacket.getShort(ip_header_size+header_tcp_sport)<<16 + (int)rawPacket.getShort(ip_header_size+header_tcp_dport);
            int proto=(int)rawPacket.get(header_protocol);
            return jhash.jhash_3words(dst, port, proto, src);
    }

    @Override
    public int getFlowHash() {
        int dst=rawPacket.getInt(header_dst);  //32 dest address
        int src=rawPacket.getInt(header_src);  //32 src address
        int proto=((int)rawPacket.get(header_protocol)&0xff)<<16;
        int sport=((int)rawPacket.getShort(ip_header_size+header_tcp_sport)) &0xffff;
        int dport=((int)rawPacket.getShort(ip_header_size+header_tcp_dport)) &0xffff;
        int port=sport<<16+dport;
        return jhash.jhash_3words(dst, src, port,proto);
    }

}
