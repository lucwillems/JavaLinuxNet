package org.it4y.integration.jni;

import junit.framework.Assert;
import org.it4y.jni.libc;
import org.it4y.net.tuntap.TunDevice;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by luc on 1/17/14.
 * Please run setup-test.sh before running this test
 * This can only be run on linux
 */
public class IT_tuntapTest {
    Logger log= LoggerFactory.getLogger(IT_tuntapTest.class);

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
    public void testAnonymousTunDevice() throws Exception {
        TunDevice tun=null;
        log.info("test anonymous tunnel device");
        try {
            tun=openTun(null);
            tun.open();
            Assert.assertTrue(tun.getFd() > 0);
            log.info("device: {} fd: {}",tun.getDevice(),tun.getFd());
            Assert.assertTrue(tun.getDevice().startsWith("tun"));
        } finally {
            if (tun != null) {
                tun.close();
                Assert.assertEquals(0,tun.getFd());
            }
        }
    }

    @Test
    public void testNamedTunDeviceOnName() throws Exception{
        TunDevice tun=null;
        log.info("test named tunnel device");
        try {
            tun=openTun("test");
            tun.open();
            Assert.assertTrue(tun.getFd() > 0);
            log.info("device: {} fd: {}",tun.getDevice(),tun.getFd());
            Assert.assertEquals("test",tun.getDevice());
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
        log.info("test double open");
        TunDevice tun=null;
        try {
            tun=openTun("test");
            //this will cause exception
            tun.open();
            Assert.assertTrue(tun.getFd() > 0);
            log.info("device: {} fd: {}",tun.getDevice(),tun.getFd());
            tun.open();
        } catch (libc.ErrnoException errno) {
            log.info("got exception (OK): {}",errno.getMessage());
            thrownexception=true;
            Assert.assertEquals(16,errno.getErrno());
            //Assert.assertEquals("Device or resource busy",errno.getMessage());
        } finally {
            if (tun != null) {
                tun.close();
                Assert.assertEquals(0, tun.getFd());
                Assert.assertTrue(thrownexception);
            }
        }
    }

}