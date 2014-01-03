package org.it4y.net.protocols.IP;

import org.it4y.net.protocols.IP.IpPacket;

import java.nio.ByteBuffer;

/**
 * Created by luc on 12/26/13.
 */
public class UDPPacket extends IpPacket {

    private static final int UDP_HEADER_SIZE=4;

    public UDPPacket(ByteBuffer buffer,int size) {
        super(buffer,size);
    }

    public int getHeaderSize() {
        return UDP_HEADER_SIZE;
    }

    public int getPayLoadSize() {
        return rawLimit-getIpHeaderSize()-UDP_HEADER_SIZE;
    }

    public ByteBuffer getHeader() {
        //get IP header size
        int headersize=getIpHeaderSize();
        resetBuffer();
        rawPacket.position(headersize);
        rawPacket.limit(headersize+UDP_HEADER_SIZE);
        return rawPacket.slice();
    }

    public ByteBuffer getPayLoad() {
        //get IP header size
        int headersize=getIpHeaderSize()+UDP_HEADER_SIZE;
        resetBuffer();
        rawPacket.position(headersize);
        return rawPacket.slice();
    }

    public short getLength() {
        return rawPacket.getShort(super.getHeaderSize()+4);
    }
    public short getChecksum() {
        return rawPacket.getShort(super.getHeaderSize()+6);
    }

    public void resetChecksum() {
        rawPacket.putShort(super.getHeaderSize()+6, (short) 0x0000) ; //16checksum must be 0 before calculation
    }

    @Override
    public short updateChecksum() {
        final int ipheadersize=getIpHeaderSize();
        rawPacket.putShort(ipheadersize+6, (short) 0x0000) ; //16checksum must be 0 before calculation
        final short checksum=rfc1071Checksum(ipheadersize,rawLimit-ipheadersize);
        rawPacket.putShort(ipheadersize+6,checksum);
        return checksum;
    }

    public void swapSourceDestinationPort() {
        final int ipheadersize=getIpHeaderSize();
        final short srcPort=rawPacket.getShort(ipheadersize);
        rawPacket.putShort(ipheadersize,rawPacket.getShort(ipheadersize+2));
        rawPacket.putShort(ipheadersize+2,srcPort);
    }

    public short getSourcePort() {
        return rawPacket.getShort(getIpHeaderSize());
    }
    public short getDestinationPort() {
        return rawPacket.getShort(getIpHeaderSize()+2);
    }


}
