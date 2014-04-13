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

import org.it4y.net.protocols.IP.IpPacket;
import org.it4y.util.PCAPFileReader;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by luc on 4/13/14.
 */
public class TCPStreamtest {
    private Logger logger= LoggerFactory.getLogger(TCPStreamtest.class);

    @Test
    public void testTCPStream() throws IOException {
        long firstAck=0;
        long firstSeqNr=0;
        long lastAck=0;
        long dupAckcount=0;
        PCAPFileReader reader = new PCAPFileReader("src/test/pcap/http-10m-random.pcap");
        IpPacket ipPacket;
        while((ipPacket=reader.readPacket())!=null) {
            TCPPacket tcpPacket = (TCPPacket)ipPacket;
            long ack=((long)tcpPacket.getAckNumber()) & 0xffffffff;
            long seqnr=((long)tcpPacket.getSequenceNumber()) & 0xffffffff;
            if (tcpPacket.isSYN() & tcpPacket.isACK()) {
                firstSeqNr=ack;
                firstAck=seqnr;
                lastAck=seqnr;
                logger.info("First SYNC ACK {} {}",firstAck,firstSeqNr);
            }
            if (tcpPacket.getDestinationPort()==80) {
                if (lastAck == ack) {
                    dupAckcount++;
                    if (dupAckcount > 1) {
                        logger.info("dup ack : {} cnt={}", ack, dupAckcount);
                    }
                } else {
                    dupAckcount = 0;
                    lastAck = ack;
                }
                logger.info("{} {} {} {} {} {} {}",(seqnr-firstSeqNr),(ack-firstAck),tcpPacket.getRawSize(),tcpPacket.getPayLoadSize(),((TCPPacket) ipPacket).isSYN(),((TCPPacket) ipPacket).isACK(),((TCPPacket) ipPacket).isFIN());
            }
        }
    }
}