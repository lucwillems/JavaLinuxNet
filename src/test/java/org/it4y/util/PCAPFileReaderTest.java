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

import org.it4y.net.protocols.IP.IpPacket;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by luc on 4/12/14.
 */
public class PCAPFileReaderTest {
    private Logger logger= LoggerFactory.getLogger(PCAPFileReaderTest.class);
    @Test
    public void testPCapReader() throws IOException {

        PCAPFileReader reader=new PCAPFileReader("src/test/pcap/gre.pcap");
        Assert.assertEquals(PCAPFileReader.PCAP_MAGIC,reader.getMagic());
        Assert.assertTrue(reader.getDataLinkType() > 0);
        Assert.assertTrue(reader.getSnaplength()>0);
        try {
            IpPacket ip;
            while ((ip = reader.readPacket()) != null) {
                logger.info("{}", ip);
                ip.release();
            }
        } finally {
            reader.close();
        }
    }
}
