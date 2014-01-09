package org.it4y.net.protocols.IP.TCP;

import org.it4y.net.protocols.IP.IpPacket;
import org.it4y.net.protocols.IP.TCP.*;
import org.it4y.util.Hexdump;

import java.nio.ByteBuffer;

/**
 * Created by luc on 12/26/13.
 */
public class TCPPacket extends IpPacket {

    public TCPPacket(ByteBuffer buffer, int size) {
        super(buffer, size);
    }

    @Override
    public int getHeaderSize() {
        int size=getDataOffset();
        return size;
    }

    @Override
    public int getPayLoadSize() {
        return getIPLenght() - getIpHeaderSize() - getHeaderSize();
    }

    public void swapSourceDestinationPort() {
        final int ipheadersize = getIpHeaderSize();
        final short srcPort = rawPacket.getShort(ipheadersize);
        rawPacket.putInt(ipheadersize, rawPacket.getInt(ipheadersize + 2));
        rawPacket.putInt(ipheadersize + 2, srcPort);
    }

    public short getSourcePort() {
        return rawPacket.getShort(getIpHeaderSize());
    }

    public short getDestinationPort() {
        return rawPacket.getShort(getIpHeaderSize() + 2);
    }

    public int getSequenceNumber() {
        return rawPacket.getInt(getIpHeaderSize() + 4);
    }

    public int getAckNumber() {
        return rawPacket.getInt(getIpHeaderSize() + 8);
    }

    public int getDataOffset() {
        //first 4bits=number of 4bytes header size
         int size=(((int)rawPacket.getShort(getIpHeaderSize() + 12) &0xf000) >>10);
        return size;
    }

    public boolean isNS() {
        return ((rawPacket.get(getIpHeaderSize() + 12) & (byte) 0x01) > (byte) 0);
    }

    public boolean isCWR() {
        return ((rawPacket.get(getIpHeaderSize() + 13) & (byte) 0x80) > (byte) 0);
    }

    public boolean isECE() {
        return ((rawPacket.get(getIpHeaderSize() + 13) & (byte) 0x40) > (byte) 0);
    }

    public boolean isURG() {
        return ((rawPacket.get(getIpHeaderSize() + 13) & (byte) 0x20) > (byte) 0);
    }

    public boolean isACK() {
        return ((rawPacket.get(getIpHeaderSize() + 13) & (byte) 0x10) > (byte) 0);
    }

    public boolean isPSH() {
        return ((rawPacket.get(getIpHeaderSize() + 13) & (byte) 0x08) > (byte) 0);
    }

    public boolean isRST() {
        return ((rawPacket.get(getIpHeaderSize() + 13) & (byte) 0x04) > (byte) 0);
    }

    public boolean isSYN() {
        return ((rawPacket.get(getIpHeaderSize() + 13) & (byte) 0x02) > (byte) 0);
    }

    public boolean isFIN() {
        return ((rawPacket.get(getIpHeaderSize() + 13) & (byte) 0x01) > (byte) 0);
    }

    public short getWindowSize() {
        return rawPacket.getShort(getIpHeaderSize() + 14);
    }

    public short getChecksumSize() {
        return rawPacket.getShort(getIpHeaderSize() + 16);
    }

    public short getUrgentPointer() {
        return rawPacket.getShort(getIpHeaderSize() + 18);
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
            System.out.println("Ignoring tcp option " + ((int) type));
        }
        return null;
    }

    public TCPOption getOptionByType(byte type) {
        resetBuffer();
        //position first option byte
        int i = getIpHeaderSize() + 20;
        while (i < getIpHeaderSize() + getHeaderSize()) {
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
        int i = getIpHeaderSize() + 20;
        while (i < getIpHeaderSize() + getHeaderSize()) {
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
}
