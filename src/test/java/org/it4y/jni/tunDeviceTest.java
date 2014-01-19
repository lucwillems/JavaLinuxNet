package org.it4y.jni;

import junit.framework.Assert;
import org.it4y.net.tuntap.TunDevice;
import org.junit.Test;

/**
 * Created by luc on 1/18/14.
 * This test can not do mutch when no admin rights have been given.
 * It willhow ever :
 * - load / call jni stuff and make sure this is ok
 * - check security in case it is run with security not active
 * - incase setcap is used, we can test jni calls
 *
 * the integration test is more pratical, but needs additional setup .
 */

public class tunDeviceTest {

    /* create and open a tunnel device */
    private TunDevice openTun(final String device) throws libc.ErrnoException {
        final TunDevice tun;
        if (device != null) {
            tun=new TunDevice(device);
        } else {
            tun=new TunDevice();
        }
        Assert.assertNotNull(tun);
        return tun;
    }

    @Test
    public void testOpenTunDevice() throws Exception {
        TunDevice tun=null;
        try {
            tun=openTun("test");
            try {
                tun.open();
            } catch (final libc.ErrnoException errno) {
                //must be "Operation not permitted
                Assert.assertEquals(1,errno.getErrno());
            }
        } finally {
            if (tun != null) {
                tun.close();
                Assert.assertEquals(0,tun.getFd());
            }
        }
    }

    @Test
    public void testOpenTun() throws Exception {
        TunDevice tun=null;
        try {
            tun=openTun(null);
            try {
                tun.open();
            } catch (final libc.ErrnoException errno) {
                //must be "Operation not permitted
                Assert.assertEquals(1,errno.getErrno());
            }
        } finally {
            if (tun != null) {
                tun.close();
                Assert.assertEquals(0,tun.getFd());
            }
        }
    }


    @Test
    public void testSetNonBlocking() throws Exception {
        TunDevice tun=null;
        try {
            tun=openTun(null);
            try {
                tun.open();
                tun.setNonBlocking(true);
             } catch (final libc.ErrnoException errno) {
                //must be "Operation not permitted
                Assert.assertEquals(1,errno.getErrno());
            }
        } finally {
            if (tun != null) {
                tun.close();
                Assert.assertEquals(0,tun.getFd());
            }
        }

    }

    @Test
    public void testIsDataReady() throws Exception {
            TunDevice tun=null;
            try {
                tun=openTun(null);
                try {
                    tun.open();
                    tun.isDataReady(100);
                } catch (final libc.ErrnoException errno) {
                    //must be "Operation not permitted
                    Assert.assertEquals(1,errno.getErrno());
                }
            } finally {
                if (tun != null) {
                    tun.close();
                    Assert.assertEquals(0,tun.getFd());
                }
            }
    }

}
