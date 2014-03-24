package org.it4y.net.protocols.IP.IPIP;

import org.it4y.net.protocols.IP.IpPacket;
import org.it4y.util.Hexdump;
import java.nio.ByteBuffer;

/**
 * Created by luc on 3/24/14.
 * bassed on RFC1853
 */
public class IPIPPacket extends IpPacket{
    public static final int IPIP_HEADER_SIZE = 0;
    private int ip_header_offset;

    public static final int header_gre_flags=0;
    public static final int header_gre_protocol=2;
    public static final int header_gre_checksum=4;
    public static final int header_gre_reserved=6;
    public static final int header_gre_key=8;
    public static final int header_gre_seq=12;

    public IPIPPacket(ByteBuffer buffer, int size) {
        super(buffer, size);
        ip_header_offset=super.getHeaderSize();
    }
    public IPIPPacket(IpPacket ip) {
        super(ip.getRawPacket(),ip.getRawSize());
        //get IP header size
        ip_header_offset=ip.getHeaderSize();
    }

    public int getHeaderSize() {
        return IPIP_HEADER_SIZE;
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


    @Override
    public String toString() {
        final StringBuilder s=new StringBuilder(128);
        if (getPayLoadSize()>0) {
            s.append(Hexdump.bytesToHex(getPayLoad(), Math.min(getPayLoadSize(), 20)));
        }
        return s.toString();
    }

}
