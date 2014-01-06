package org.it4y.net.protocols.IP.UDP;

import org.it4y.net.protocols.IP.IpPacket;
import org.it4y.util.Hexdump;

import java.nio.ByteBuffer;

/**
 * Created by luc on 12/26/13.
 */
public class UDPPacket extends IpPacket {

    private static final int UDP_HEADER_SIZE = 4;

    public UDPPacket(ByteBuffer buffer, int size) {
        super(buffer, size);
    }

    public int getHeaderSize() {
        return UDP_HEADER_SIZE;
    }

    public int getPayLoadSize() {
        return getLength() - UDP_HEADER_SIZE;
    }

    public ByteBuffer getHeader() {
        //get IP header size
        int headersize = getIpHeaderSize();
        resetBuffer();
        int oposition=rawPacket.position();
        int olimit=rawPacket.limit();
        try {
            rawPacket.position(headersize);
            rawPacket.limit(headersize + UDP_HEADER_SIZE);
            return rawPacket.slice();
        } finally {
            rawPacket.limit(olimit);
            rawPacket.position(oposition);
        }
    }

    public ByteBuffer getPayLoad() {
        //get IP header size
        int headersize = getIpHeaderSize() + UDP_HEADER_SIZE;
        resetBuffer();
        int oposition=rawPacket.position();
        int olimit=rawPacket.limit();
        try {
            rawPacket.position(headersize);
            return rawPacket.slice();
        } finally {
            rawPacket.limit(olimit);
            rawPacket.position(oposition);
        }
    }

    public short getLength() {
        return rawPacket.getShort(super.getIpHeaderSize() + 4);
    }

    public short getChecksum() {
        return rawPacket.getShort(super.getIpHeaderSize() + 6);
    }

    public void resetChecksum() {
        rawPacket.putShort(super.getHeaderSize() + 6, (short) 0x0000); //16checksum must be 0 before calculation
    }

    @Override
    public short updateChecksum() {
        final int ipheadersize = getIpHeaderSize();
        rawPacket.putShort(ipheadersize + 6, (short) 0x0000); //16checksum must be 0 before calculation
        final short checksum = rfc1071Checksum(ipheadersize, rawLimit - ipheadersize);
        rawPacket.putShort(ipheadersize + 6, checksum);
        return checksum;
    }

    public void swapSourceDestinationPort() {
        final int ipheadersize = getIpHeaderSize();
        final short srcPort = rawPacket.getShort(ipheadersize);
        rawPacket.putShort(ipheadersize, rawPacket.getShort(ipheadersize + 2));
        rawPacket.putShort(ipheadersize + 2, srcPort);
    }

    public short getSourcePort() {
        return rawPacket.getShort(getIpHeaderSize());
    }

    public short getDestinationPort() {
        return rawPacket.getShort(getIpHeaderSize() + 2);
    }

    public String toString() {
        StringBuffer s=new StringBuffer();
        s.append(super.toString());
        s.append("UDP[");
        s.append("sport:").append((int)getSourcePort()&0xffff).append(",");
        s.append("dport:").append((int)getDestinationPort()&0xffff).append(",");
        s.append("h:").append(getHeaderSize()).append(",");
        s.append("d:").append(getPayLoadSize());
        s.append("] ");
        s.append(Hexdump.bytesToHex(getPayLoad(),Math.min(getLength(),128)));
        return s.toString();
    };
}
