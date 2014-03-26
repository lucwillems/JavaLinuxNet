package org.it4y.net.protocols.IP.IPIP;

import org.it4y.net.protocols.IP.IpPacket;
import org.it4y.util.Hexdump;
import java.nio.ByteBuffer;

/**
 * Created by luc on 3/24/14.
 * bassed on RFC1853
 */
public class IPIPPacket extends IpPacket{
    public static final byte PROTOCOL=4;
    public static final int IPIP_HEADER_SIZE = 0;

    private int ip_header_offset;

    public IPIPPacket(ByteBuffer buffer, int size) {
        super(buffer, size);
        ip_header_offset=super.getHeaderSize();
    }
    public IPIPPacket(IpPacket ip) {
        super(ip.getRawPacket(),ip.getRawSize());
        //get IP header size
        ip_header_offset=ip.getHeaderSize();
    }

    @Override
    public void initIpHeader() {
        super.initIpHeader();
        setProtocol(IPIPPacket.PROTOCOL);
        ip_header_offset=super.getIpHeaderSize();
    }

    @Override
    public int getHeaderSize() {
        return IPIP_HEADER_SIZE;
    }

    @Override
    public int getPayLoadSize() {
        return rawLimit - ip_header_offset - getHeaderSize();
    }

    @Override
    public ByteBuffer getHeader() {
        //get IP header size
        resetBuffer();
        rawPacket.position(ip_header_offset);
        rawPacket.limit(ip_header_offset + getHeaderSize());
        return rawPacket.slice();
    }

    @Override
    public ByteBuffer getPayLoad() {
        //get IP header size
        resetBuffer();
        rawPacket.position(ip_header_offset+getHeaderSize());
        rawPacket.limit(rawSize);
        return rawPacket.slice();
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
