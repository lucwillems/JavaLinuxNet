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

package org.it4y.util;

import org.it4y.jni.linux.pcap;
import org.it4y.net.protocols.IP.IPFactory;
import org.it4y.net.protocols.IP.IpPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * based on http://wiki.wireshark.org/Development/LibpcapFileFormat
 * Created by luc on 4/12/14.
 */
public class PCAPFileReader {
    private final Logger logger=LoggerFactory.getLogger(PCAPFileReader.class);
    public static final int PCAP_HEADER_SIZE=24;
    public static final int PCAP_PACKETHEADER_SIZE=16;
    public static final int PCAP_MAGIC=0xa1b2c3d4;
    public static final int PCAP_MAGIC_REVERSE=0xd4c3b2a1;

    private InputStream inputFile;
    private long pointer;
    private ByteBuffer header;
    private int magic;
    private int snaplength;
    private int dataLinkType;
    private int dataLinkHeaderSize=0;


    private ByteOrder fileFormat=ByteOrder.nativeOrder();

    public PCAPFileReader(String filename) throws IOException {
        inputFile=new FileInputStream(filename);
        logger.info("File: {}",filename);
        readHeader();
    }
    public PCAPFileReader(InputStream input) throws IOException {
        inputFile=input;
        readHeader();
    }

    private void readHeader() throws IOException {
        header=ByteBuffer.allocate(PCAP_HEADER_SIZE);
        inputFile.read(header.array());
        inputFile.mark(Integer.MAX_VALUE);
        //check little/big indean
        magic=header.getInt(0);
        if (magic != PCAP_MAGIC) {
            if (magic == PCAP_MAGIC_REVERSE) {
                logger.debug("Packet format: {}",ByteOrder.BIG_ENDIAN);
                header=header.order(ByteOrder.LITTLE_ENDIAN);
                magic=header.getInt(0);
                if (magic==PCAP_MAGIC) {
                    fileFormat=ByteOrder.LITTLE_ENDIAN;
                }
            }
        }
        dataLinkType=header.getInt(20);
        snaplength=header.getInt(16);
        switch (dataLinkType) {
            case pcap.DLT_EN10MB: dataLinkHeaderSize=14;break; //Ethernet frame
            case pcap.DLT_IPV4:dataLinkHeaderSize=0;break; //IPv4 frame
            case pcap.DLT_PPP:dataLinkHeaderSize=0;break;//TODO : ppp frame
            default: throw new UnsupportedEncodingException("unsupported DATALINK type "+dataLinkType);
        }
        logger.info("header magic: 0x{}",Integer.toHexString(magic));
        logger.info("DataLink type: 0x{}",Integer.toHexString(dataLinkType));
        logger.info("snapLength: 0x{}",snaplength);
    }

    /**
     * Read IP Packet or return NULL in case of
     *  - end of file
     *  - packet to small to retrieve IP header
     *  - unsupported datalink type , could not find ip header location
     * @return
     * @throws IOException
     */
    public IpPacket readPacket() throws IOException {
        ByteBuffer packetHeader=ByteBuffer.allocate(PCAP_PACKETHEADER_SIZE);
        try {
            inputFile.read(packetHeader.array());
            packetHeader = packetHeader.order(fileFormat);
            //get length of packet.
            int captureLength = packetHeader.getInt(8);
            logger.debug("packet Size: {}", captureLength);
            if (captureLength < dataLinkHeaderSize + 20) {  //we need atleast ip header + some extra data
                if (captureLength!=0) { //size=0 means end of stream
                    logger.warn("captured packet to small: {}", captureLength);
                }
                return null;
            }

            ByteBuffer rawPacket = ByteBuffer.allocate(captureLength);
            inputFile.read(rawPacket.array());
            rawPacket = rawPacket.order(fileFormat);
            //remove to ip header
            rawPacket.position(dataLinkHeaderSize);
            IpPacket packet = IPFactory.processRawPacket(rawPacket.slice(), captureLength - dataLinkHeaderSize);
            return packet;
        }finally {
            packetHeader=null;
        }
    }

    public int getMagic() {
        return magic;
    }

    public int getDataLinkType() {
        return dataLinkType;
    }

    public int getSnaplength() {
        return snaplength;
    }

    public void close() throws IOException {
        inputFile.close();
        header=null;
    }
}
