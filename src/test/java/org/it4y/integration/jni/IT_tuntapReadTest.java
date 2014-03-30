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

package org.it4y.integration.jni;

import junit.framework.Assert;
import org.it4y.integration.utils;
import org.it4y.jni.libc;
import org.it4y.net.tuntap.TunDevice;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by luc on 3/21/14.
 */
public class IT_tuntapReadTest {
    Logger log= LoggerFactory.getLogger(IT_tuntapReadTest.class);

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
    public void testTunReadNoIPAvailable() throws Exception{
        boolean thrownexception=false;
        TunDevice tun=null;
        log.info("test tunnel write no IP");
        try {
            tun=openTun("test");
            tun.open();
            Assert.assertTrue(tun.getFd() > 0);
            log.info("device: {} fd: {}",tun.getDevice(),tun.getFd());
            //we need to write some bytes to tun device
            ByteBuffer buf=ByteBuffer.allocateDirect(1500);
            //this will cause the read not to block
            tun.setNonBlocking(true);
            int result=tun.readByteBuffer(buf,true); //no exception here because reading non blocking
            //We should not have any result here
            log.info("read result: {}",result);
            //we should get -EAGAIN
            Assert.assertEquals(-11,result);
        } finally {
            if (tun != null) {
                tun.close();
                Assert.assertEquals(0, tun.getFd());
            }
        }
    }

    @Test
    public void testTunReadError() throws Exception{
        boolean thrownexception=false;
        TunDevice tun=null;
        log.info("test tunnel read error");
        try {
            tun=openTun("test");
            tun.open();
            Assert.assertTrue(tun.getFd() > 0);
            log.info("device: {} fd: {}",tun.getDevice(),tun.getFd());
            //we need to write some bytes to tun device
            ByteBuffer buf=ByteBuffer.allocateDirect(1500);
            //this will cause the read not to block
            tun.setNonBlocking(true);
            int result=tun.readByteBuffer(buf,false); //this will cause ErrnoException
            //We should not have any result here
            log.info("read result: {}", result);
            Assert.assertEquals(-1,result);
        } catch (libc.ErrnoException errno) {
            //invalid argument
            log.info("got exeception (OK) : {}",errno.getMessage());
            Assert.assertEquals(11,errno.getErrno());
            thrownexception=true;
        } finally {
            if (tun != null) {
                tun.close();
                Assert.assertEquals(0, tun.getFd());
                Assert.assertTrue(thrownexception);
            }
        }
    }

    @Test
    public void testTunIsDataReady() throws Exception{
        boolean thrownexception=false;
        TunDevice tun=null;
        log.info("test tunnel data ready");
        try {
            tun=openTun("test");
            tun.open();
            Assert.assertTrue(tun.getFd() > 0);
            log.info("device: {} fd: {}",tun.getDevice(),tun.getFd());
            //we need to send data so it will be routed into the tunnel
            //todo : make it route into tunnel withouth setup script
            int size=1000;
            utils.sendTestUDP(size);

            //we need to write some bytes to tun device
            ByteBuffer buf=ByteBuffer.allocateDirect(1500);
            //this will cause the read not to block
            tun.setNonBlocking(true);
            //now wait for data
            long start=System.currentTimeMillis();
            if (tun.isDataReady(100))  {
                log.info("data is there");
            }
            long delta=System.currentTimeMillis()-start;
            log.info("data after {} msec",delta);
            //we should less than 2msec
            Assert.assertTrue(delta<10);
            int result=tun.readByteBuffer(buf,true);
            //We should not have any result here
            log.info("read result: {}", result);
            Assert.assertEquals(size+28,result);
            //lets check some ip headers to be sure
            Assert.assertEquals(0x45,buf.get(0x00)); //should be IPv4 header , 5=size in 32 bits
        } finally {
            if (tun != null) {
                tun.close();
                Assert.assertEquals(0, tun.getFd());
            }
        }
    }


    @Test
    public void testTunPing() throws Exception {
        boolean thrownexception=false;
        TunDevice tun=null;
        log.info("test tunnel ping");
        try {
            tun=openTun("test");
            tun.open();
            Assert.assertTrue(tun.getFd() > 0);
            log.info("device: {} fd: {}",tun.getDevice(),tun.getFd());
            //we need to send data so it will be routed into the tunnel
            //todo : make it route into tunnel withouth setup script
            int size=1000;
            utils.sendTestUDP(size);

            //we need to write some bytes to tun device
            ByteBuffer buf=ByteBuffer.allocateDirect(1500);
            //this will cause the read not to block
            tun.setNonBlocking(true);

            //now wait for data
            long start=System.currentTimeMillis();
            if (tun.isDataReady(100))  {
                log.info("data is there");
            }
            long delta=System.currentTimeMillis()-start;
            log.info("data after {} msec",delta);
            //we should less than 2msec
            Assert.assertTrue(delta<10);
            int result=tun.readByteBuffer(buf,true);
            //We should not have any result here
            log.info("read result: {}", result);
            Assert.assertEquals(size+28,result);
            //lets check some ip headers to be sure
            Assert.assertEquals(0x45,buf.get(0x00)); //should be IPv4 header , 5=size in 32 bits
            Assert.assertEquals(17,buf.get(0x09)); //should be UDP protocol
        } finally {
            if (tun != null) {
                tun.close();
                Assert.assertEquals(0, tun.getFd());
            }
        }
    }
}
