/*
 * Copyright 20124 Luc Willems (T.M.M.)
 *
 * We licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.it4y.integration.jni;

import junit.framework.Assert;
import org.it4y.integration.utils;
import org.it4y.jni.libc;
import org.it4y.net.protocols.IP.ICMP.ICMPPacket;
import org.it4y.net.protocols.IP.IpPacket;
import org.it4y.net.tuntap.TunDevice;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by luc on 3/21/14.
 */
public class IT_tuntapWriteTest {
    Logger log= LoggerFactory.getLogger(IT_tuntapWriteTest.class);

    /* create and open a tunnel device */
    private TunDevice openTun(String device) throws libc.ErrnoException {
        TunDevice tun=null;
        if (device != null) {
            tun=new TunDevice(device);
        } else {
            tun=new TunDevice();
        }
        Assert.assertNotNull(tun);
        log.info("tun: {}",tun);
        return tun;
    }


    @Test
    public void testTunWrite() throws Exception{
        boolean thrownexception=false;
        TunDevice tun=null;
        log.info("test tunnel write");
        try {
            tun=openTun("test");
            tun.open();
            Assert.assertTrue(tun.getFd() > 0);
            log.info("device: {} fd: {}",tun.getDevice(),tun.getFd());
            //we need to write some bytes to tun device
            ByteBuffer buf=ByteBuffer.allocateDirect(60);
            //create dummy ICMP packet,
            buf.clear();
            buf.put((byte) 0x45);  //IPv4 + header size
            buf.put((byte) 0x00);  //dscp
            buf.put((byte)60);    //size
            buf.putShort((byte) 0x00);
            buf.putShort((byte) 0x00);
            buf.put((byte) 0x40); //TTL
            buf.put((byte)0x01); //protocol
            int x=tun.writeByteBuffer(buf,60);
            Assert.assertEquals(60,x);
        } finally {
            if (tun != null) {
                tun.close();
                Assert.assertEquals(0, tun.getFd());
            }
        }
    }

    @Test
    public void testTunWriteInvalidIP() throws Exception{
        boolean thrownexception=false;
        TunDevice tun=null;
        log.info("test tunnel write invalid IP");
        try {
            tun=openTun("test");
            tun.open();
            Assert.assertTrue(tun.getFd() > 0);
            log.info("device: {} fd: {}",tun.getDevice(),tun.getFd());

            int size=1000;
            ByteBuffer ipPacket= utils.getBadIpPacket(ICMPPacket.PROTOCOL, size);
            tun.writeByteBuffer(ipPacket,size);
        } catch (libc.ErrnoException errno) {
            //invalid argument
            log.info("got exeception (OK) : {}",errno.getMessage());
            Assert.assertEquals(22,errno.getErrno());
            thrownexception=true;
        } finally {
            if (tun != null) {
                tun.close();
                Assert.assertEquals(0, tun.getFd());
                Assert.assertTrue(thrownexception);
            }
        }
    }

}
