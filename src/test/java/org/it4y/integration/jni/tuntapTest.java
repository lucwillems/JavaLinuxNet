package org.it4y.integration.jni;

import junit.framework.Assert;
import org.it4y.integration.utils;
import org.it4y.jni.libc;
import org.it4y.net.protocols.IP.IpPacket;
import org.it4y.net.tuntap.TunDevice;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by luc on 1/17/14.
 * Please run setup-test.sh before running this test
 * This can only be run on linux
 */
public class tuntapTest {
    Logger log= LoggerFactory.getLogger(tuntapTest.class);

    /* create and open a tunnel device */
    private TunDevice openTun(String device) throws libc.ErrnoException {
        TunDevice tun=null;
        if (device != null) {
            tun=new TunDevice(device);
        } else {
            tun=new TunDevice();
        }
        Assert.assertNotNull(tun);
        tun.open();
        Assert.assertTrue(tun.getFd() > 0);
        if (device == null) {
            Assert.assertTrue(tun.getDevice().startsWith("tun"));
        } else {
            Assert.assertEquals(device,tun.getDevice());
        }
        return tun;
    }

    @Test
    public void testTunDevice() throws Exception {
        TunDevice tun=null;
        try {
            tun=openTun(null);
        } finally {
            if (tun != null) {
                tun.close();
                Assert.assertEquals(0,tun.getFd());
            }
        }
    }

    @Test
    public void testTunDeviceOnName() throws Exception{
        TunDevice tun=null;
        try {
            tun=openTun("test");
        } finally {
            if (tun != null) {
                tun.close();
                Assert.assertEquals(0, tun.getFd());
            }
        }
    }

    @Test
    public void testTunDeviceDoubleOpen() throws Exception{
        boolean thrownexception=false;
        TunDevice tun=null;
        try {
            tun=openTun("test");
            //this will cause exception
            tun.open();
        } catch (libc.ErrnoException errno) {
            log.info("got exception: {}",errno.getMessage());
            thrownexception=true;
            Assert.assertEquals(16,errno.getErrno());
            Assert.assertEquals("Device or resource busy",errno.getMessage());
        } finally {
            if (tun != null) {
                tun.close();
                Assert.assertEquals(0, tun.getFd());
                Assert.assertTrue(thrownexception);
            }
        }
    }

    @Test
    public void testTunWrite() throws Exception{
        boolean thrownexception=false;
        TunDevice tun=null;
        try {
            tun=openTun("test");

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
        try {
            tun=openTun("test");
            int size=1000;
            ByteBuffer ipPacket=utils.getBadIpPacket(IpPacket.ICMP,size);
            tun.writeByteBuffer(ipPacket,size);
        } catch (libc.ErrnoException errno) {
            //invalid argument
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

    @Test
    public void testTunReadNoIPAvailable() throws Exception{
        boolean thrownexception=false;
        TunDevice tun=null;
        try {
            tun=openTun("test");
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
        try {
            tun=openTun("test");
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
    public void testTunIsDataReadyTimeout() throws Exception{
        boolean thrownexception=false;
        TunDevice tun=null;
        try {
            tun=openTun("test");
            //we need to write some bytes to tun device
            ByteBuffer buf=ByteBuffer.allocateDirect(1500);
            //this will cause the read not to block
            tun.setNonBlocking(true);
            int result=tun.readByteBuffer(buf,true);
            //We should not have any result here
            log.info("read result: {}", result);
            Assert.assertEquals(-11,result);

            //now wait for data
            long start=System.currentTimeMillis();
            tun.isDataReady(100); //100msec
            long delta=System.currentTimeMillis()-start;
            //we should have more than 90msec
            Assert.assertTrue(delta>90);
            log.info("no data after {} msec",delta);
        } finally {
            if (tun != null) {
                tun.close();
                Assert.assertEquals(0, tun.getFd());
            }
        }
    }

    @Test
    public void testTunIsDataReady() throws Exception{
        boolean thrownexception=false;
        TunDevice tun=null;
        try {
            tun=openTun("test");
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
        try {
            tun=openTun("test");
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
}