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

package org.it4y.net.link;

import junit.framework.Assert;
import org.it4y.util.Counter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Created by luc on 1/9/14.
 */
public class LinkManagerTest {
    private final Logger log= LoggerFactory.getLogger(LinkManager.class);

    private LinkManager startLinkManager(LinkNotification.EventType aType,LinkNotification.EventAction aAction,LinkNotification aListener)  throws Exception {
        LinkManager lm=new LinkManager();
        Assert.assertNotNull(lm);
        //register event listener if we want
        if (aListener != null) {
            LinkNotification x=lm.registerListener(aAction,aType,aListener);
            Assert.assertEquals(x,aListener);
        }
        //run it
        lm.start();
        //wait so thread can start
        Thread.sleep(100);
        Assert.assertTrue(lm.isRunning());
        Assert.assertTrue(lm.isDaemon());
        Assert.assertTrue(lm.isAlive());
        int retry=0;
        //we need to wait until linkmanager has all the data
        while(!lm.isReady() & retry < 20) {
            Thread.sleep(100);
            retry++;
        }
        Assert.assertTrue("Timeout waiting link manager",lm.isReady());
        return lm;
    }

    private LinkManager startLinkManager()  throws Exception {
        return startLinkManager(null,null,null);
    }

    private void stopLinkManager(LinkManager lm) throws Exception {
        if (lm != null) {
            lm.halt();
            //wait so thread can stop
            Thread.sleep(100);
            Assert.assertTrue("link manager must be stopped",lm.isHalted());
            lm.shutDown();
            Assert.assertTrue(!lm.isReady());
            Assert.assertTrue(!lm.isRunning());
            lm=null;
        }
    }


    @Test
    public void testLinkManager() throws Exception {
        LinkManager lm=startLinkManager();
        try {
          Assert.assertNotNull(lm);
        } finally {
            stopLinkManager(lm);
        }
    }

    @Test
    public void testDefaulGateway() throws Exception {
        //this test should always work when there is a normal network setup
        LinkManager lm=startLinkManager();
        //Get default gateway
        try {
            Assert.assertNotNull(lm.getDefaultGateway());
        } finally {
            stopLinkManager(lm);
        }
    }

    @Test
    public void testfindbyInterfaceName() throws Exception {
        //this test should always work when there is a normal network setup
        LinkManager lm=startLinkManager();
        try {
            //Get lo interface
            NetworkInterface lo=lm.findByInterfaceName("lo");
            Assert.assertNotNull(lo);
            log.info("{}",lo);
            //this is only correct for lo interface
            Assert.assertNotNull(lo.getIpv4AddressAsInetAddress());
            Assert.assertTrue(lo.getMtu() > 0);
            Assert.assertEquals("lo", lo.getName());
            Assert.assertEquals(0x0100007f,lo.getIpv4Address()&0xffffffff);
            Assert.assertTrue(lo.getMtu()>0);
            Assert.assertTrue(lo.isLowerUP());
            Assert.assertTrue(lo.isUP());
            Assert.assertTrue(lo.isLoopBack());
            Assert.assertTrue(!lo.isPoint2Point());
            Assert.assertTrue(lo.isActive());
        } finally {
            stopLinkManager(lm);
        }
    }

    @Test
    public void testfindbyInterfaceIndex() throws Exception {
        //this test should always work when there is a normal network setup
        LinkManager lm=startLinkManager();
        try {
            //Get lo interface
            NetworkInterface lo=lm.findByInterfaceIndex(1);
            Assert.assertNotNull(lo);
            Assert.assertEquals(1,lo.getIndex());
        } finally {
            stopLinkManager(lm);
        }
    }

    @Test
    public void testConcurrentFindbyName() throws Exception{
        final Counter readcnt=new Counter();
        final Counter wrtcnt=new Counter();
        final LinkManager lm=startLinkManager();
        final ExecutorService es= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*4);

        try {
            //we need access to internal wlock lock for simulating write locks
            Field privateLock = LinkManager.class.getDeclaredField("wlock");
            privateLock.setAccessible(true);
            final Lock wlock=(Lock)privateLock.get(lm);

            log.info("running concurrent access...");
            log.info("# of cpu: {}",Runtime.getRuntime().availableProcessors());
            //Run 40 jobs on the linkmanager requesting all links concurrently
            for (int i=0;i<100;i++) {
                //start some read access
                for (final String net : lm.getInterfaceList()) {
                    es.submit(new Runnable() {
                    @Override
                    public void run() {
                        readcnt.inc();
                        NetworkInterface x=lm.findByInterfaceName(net);
                        Assert.assertNotNull(x);
                        Assert.assertEquals(net,x.getName());
                         }
                });
                 //and long locking write access
                 if ((i % 3) == 0) {
                    es.submit(new Runnable() {
                    @Override
                    public void run() {
                        wrtcnt.inc();
                        wlock.lock();
                        try {
                            //do something usefull while holding the lock
                            Thread.sleep(20);
                        } catch (InterruptedException ignore) {
                        } finally {
                            wlock.unlock();
                        }
                        }
                });
                   }
                }
            }
            Thread.sleep(500);
            //All read/write locks are executed multible times
            log.info("Read locks: {}",readcnt.getCount());
            log.info("Write locks: {}",wrtcnt.getCount());
            Assert.assertTrue(readcnt.getCount()>1);
            Assert.assertTrue(wrtcnt.getCount()>1);
        } finally {
            es.shutdown();
            es.awaitTermination(10,TimeUnit.SECONDS);
            stopLinkManager(lm);
        }
    }

    @Test
    public void testLinkMessagesNotification() throws Exception {

        final Counter cnt=new Counter();

        final LinkManager lm=startLinkManager(LinkNotification.EventType.Link, LinkNotification.EventAction.All, new LinkNotification() {
            @Override
            public void onEvent(EventAction action, EventType type, NetworkInterface network) {
                    cnt.inc();
                    Assert.assertEquals(action, EventAction.New);
                    Assert.assertEquals(type, EventType.Link);
            }

            @Override
            public void onStateChanged(NetworkInterface network) {
                    cnt.inc();
            }
        });
        try {
            Assert.assertTrue(cnt.getCount() > 0);
        } finally {
           stopLinkManager(lm);
        }
    }

    @Test
    public void testAddressMessagesNotification() throws Exception {

        final Counter cnt=new Counter();

        final LinkManager lm=startLinkManager(LinkNotification.EventType.Address, LinkNotification.EventAction.All, new LinkNotification() {
            @Override
            public void onEvent(EventAction action, EventType type, NetworkInterface network) {
                cnt.inc();
                Assert.assertEquals(action, EventAction.Update);
                Assert.assertEquals(type, EventType.Address);
            }

            @Override
            public void onStateChanged(NetworkInterface network) {
                cnt.inc();
            }
        });
        try {
            Assert.assertTrue(cnt.getCount() > 0);
        } finally {
            stopLinkManager(lm);
        }
    }

    @Test
    public void testRouteMessagesNotification() throws Exception {

        final Counter cnt=new Counter();

        final LinkManager lm=startLinkManager(LinkNotification.EventType.Routing, LinkNotification.EventAction.All, new LinkNotification() {
            @Override
            public void onEvent(EventAction action, EventType type, NetworkInterface network) {
                cnt.inc();
                Assert.assertEquals(action, EventAction.Update);
                Assert.assertEquals(type, EventType.Routing);
            }

            @Override
            public void onStateChanged(NetworkInterface network) {
            }
        });
        try {
            Assert.assertTrue(cnt.getCount() > 0);
            //if it is not thread save, we don't get here
        } finally {
            lm.halt();
        }
    }

    @Test
    public void testLinkStateMessagesNotification() throws Exception {

        final Counter cnt=new Counter();

        final LinkManager lm=startLinkManager(LinkNotification.EventType.All, LinkNotification.EventAction.All, new LinkNotification() {

            @Override
            public void onEvent(EventAction action, EventType type, NetworkInterface network) {
            }

            @Override
            public void onStateChanged(NetworkInterface network) {
                cnt.inc();
                Assert.assertTrue(network.isUP());
                Assert.assertTrue(network.isActive());
            }
        });
        try {
            //we should always have lo up
            Assert.assertTrue(cnt.getCount() >= 1);
        } finally {
            //if it is not thread save, we don't get here
            stopLinkManager(lm);
        }
    }

    @Test
    public void testUnregisterNotification() throws Exception {
        final LinkManager lm=startLinkManager();
        LinkNotification noti=new LinkNotification() {
            @Override
            public void onEvent(EventAction action, EventType type, NetworkInterface network) {

            }

            @Override
            public void onStateChanged(NetworkInterface network) {
            }
        };
        try {
            lm.registerListener(LinkNotification.EventAction.All, LinkNotification.EventType.All, noti);
            lm.unRegisterListener(noti);
        } finally {
            stopLinkManager(lm);
        }
    }

}
