package org.it4y.net.protocols.IP.GRE;

import org.it4y.net.protocols.IP.IpPacket;
import org.it4y.util.Hexdump;

import java.nio.ByteBuffer;

/**
 * Created by luc on 3/24/14.
 */
public class GREPacket extends IpPacket {

    public static final int GRE_HEADER_SIZE = 4;
    private int ip_header_offset;

    public static final int header_gre_flags=0;
    public static final int header_gre_protocol=2;
    public static final int header_gre_checksum=4;
    public static final int header_gre_reserved=6;
    public static final int header_gre_key=8;
    public static final int header_gre_seq=12;

    public GREPacket(ByteBuffer buffer, int size) {
        super(buffer, size);
        ip_header_offset=super.getHeaderSize();
    }
    public GREPacket(IpPacket ip) {
        super(ip.getRawPacket(),ip.getRawSize());
        //get IP header size
        ip_header_offset=ip.getHeaderSize();
    }

    public int getHeaderSize() {
        int size=GRE_HEADER_SIZE;
        if (hasGreChecksum())   { size=size+4;}
        if (hasGreKeys() || hasGreSequenceNumbers() ) {size=size+8;}
        return size;
    }

    public int getPayLoadSize() {
        return rawLimit - ip_header_offset - getHeaderSize();
    }

    public ByteBuffer getHeader() {
        //get IP header size
        resetBuffer();
        rawPacket.position(ip_header_offset);
        rawPacket.limit(ip_header_offset + getHeaderSize());
        return rawPacket.slice();
    }

    public ByteBuffer getPayLoad() {
        //get IP header size
        resetBuffer();
        rawPacket.position(ip_header_offset+getHeaderSize());
        rawPacket.limit(rawSize);
        return rawPacket.slice();
    }

    public short getGreFlags() {
        return rawPacket.getShort(ip_header_offset + header_gre_flags);
    }

    public boolean hasGreChecksum() {
        short flags=getGreFlags();
        return (flags & (short)0x8000) == (short)0x8000;
    }
    public boolean hasGreKeys() {
        short flags=getGreFlags();
        return (flags & (short)0x2000) == (short)0x2000;
    }
    public boolean hasGreSequenceNumbers() {
        short flags=getGreFlags();
        return (flags & (short)0x1000) == (short)0x1000;
    }

    public short getGreChecksum() {
        return  rawPacket.getShort(ip_header_offset + header_gre_checksum);
    }

    public int getGreKey() {
        return  rawPacket.getInt(ip_header_offset + header_gre_key);
    }
    public int getGreSeqNumber() {
        return  rawPacket.getInt(ip_header_offset + header_gre_seq);
    }

    public void setGreFlags(short flags) {
        rawPacket.putShort(ip_header_offset + header_gre_flags, flags);
    }

    public short getEmbeddedProtocol() {
        return rawPacket.getShort(ip_header_offset + header_gre_protocol);
    }

    public void setEmbeddedProtocol(short protocol) {
        rawPacket.putShort(ip_header_offset + header_gre_protocol,protocol);
    }

    @Override
    public String toString() {
        final StringBuilder s=new StringBuilder(128);
        s.append(super.toString()).append("[");
        if (hasGreChecksum()) {s.append("C,"); };
        if (hasGreKeys()) {s.append("K=").append(Integer.toHexString(getGreKey())).append(",");}
        if (hasGreSequenceNumbers()) {s.append("S=").append(Integer.toHexString(getGreSeqNumber())).append(",");}
        s.deleteCharAt(s.lastIndexOf(","));
        s.append("]");
        if (getPayLoadSize()>0) {
                s.append(Hexdump.bytesToHex(getPayLoad(), Math.min(getPayLoadSize(), 20)));
        }
        return s.toString();
    }

}
