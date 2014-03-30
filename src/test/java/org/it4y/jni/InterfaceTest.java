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

package org.it4y.jni;

import org.it4y.jni.linux.ioctl;
import org.it4y.jni.linux.socket;
import org.it4y.net.link.LinkManager;
import org.it4y.net.link.LinkNotification;
import org.it4y.net.link.NetworkInterface;
import org.it4y.util.Counter;
import org.it4y.util.Hexdump;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * Created by luc on 3/29/14.
 */
public class InterfaceTest {
    private String device="testnotused";
    private String doesntexist="doesntexist";
    private Logger logger= LoggerFactory.getLogger(InterfaceTest.class);
    private short originalState=0;

    @Test
    public void testInterfaceState() throws libc.ErrnoException {
        short flags=linuxutils.ioctl_SIOCGIFFLAGS(device);
        logger.info("State: 0x{}",Integer.toHexString(flags));
    }

    @Test
    public void testInterfaceDownUp() throws libc.ErrnoException, InterruptedException {
        short originalFlags=linuxutils.ioctl_SIOCGIFFLAGS(device);
        logger.info("current: 0x{}",Integer.toHexString(originalFlags));
        //Doing this will cause the routing be lost to this interface
        //so use one that is not required for good working
        Assert.assertEquals(0, linuxutils.ioctl_ifupdown(device, false));
        short flags=linuxutils.ioctl_SIOCGIFFLAGS(device);
        logger.info("down: 0x{}", Integer.toHexString(flags));
        Assert.assertEquals(0,flags & ioctl.IFF_UP);
        Assert.assertEquals(0, linuxutils.ioctl_ifupdown(device, true));
        flags=linuxutils.ioctl_SIOCGIFFLAGS(device);
        logger.info("up: 0x{}", Integer.toHexString(flags));
        //flags should be the same after test
        Assert.assertEquals(originalFlags, flags);
    }

    @Test
    public void testInterfaceDoestExist()  {
        //Doing this will cause the routing be lost to this interface
        //so use one that is not required for good working
        boolean gotException=false;
        try {
            linuxutils.ioctl_ifupdown(doesntexist,true);
        } catch (libc.ErrnoException ok) {
            logger.info("got Exception (OK): {}", ok.getMessage());
            gotException=true;
        }
        Assert.assertTrue(gotException);
    }

    @Test
    public void testInterfaceWithLinkManager() throws InterruptedException, libc.ErrnoException {
        LinkManager linkMng=new LinkManager();
        final Counter cnt=new Counter();
        linkMng.start();
        Thread.sleep(100);
        //register a listener so we see changes
        linkMng.registerListener(LinkNotification.EventAction.All, LinkNotification.EventType.All, new LinkNotification() {
            @Override
            public void onEvent(EventAction action, EventType type, NetworkInterface network) {
                logger.info("onEvent: {} {} {}", action, type, network);
                cnt.inc();
            }

            @Override
            public void onStateChanged(NetworkInterface network) {
                logger.info("onState: {} {} {}", network);
            }
        });
        NetworkInterface networkInterface = linkMng.findByInterfaceName(device);
        logger.info("state before: {}",networkInterface);
        Assert.assertEquals(0, linuxutils.ioctl_ifupdown(device, false));
        Thread.sleep(100);
        logger.info("state after: {}", networkInterface);
        Assert.assertEquals(1,cnt.getCount()); //did linkManager saw it ?
        Assert.assertEquals(0, linuxutils.ioctl_ifupdown(device, true));
        Thread.sleep(100);
        logger.info("state end: {}", networkInterface);
        linkMng.halt();
        Thread.sleep(100);
        linkMng.shutDown();
    }

    @Test
    public void testInterfacegetAddress() throws libc.ErrnoException {
        libc.sockaddr_in address=linuxutils.ioctl_SIOCGIFADDR(device);
        logger.info("IPv4 sockaddr_in: {}", Hexdump.bytesToHex(address.array(), address.array().length));
        logger.info("{}", address);
        Assert.assertEquals(0, address.port);
        Assert.assertEquals(0x7f000003, address.address);
        Assert.assertEquals(0x02, address.family);
        logger.info("{}", address.toInetAddress());
        logger.info("{}", address.toInetSocketAddress());
    }

    @Test
    public void testInterfacesetAddress() throws libc.ErrnoException, UnknownHostException {
        libc.sockaddr_in originalAddress= linuxutils.ioctl_SIOCGIFADDR(device);
        logger.info("original {}", originalAddress);
        InetAddress ipv4= Inet4Address.getByName("127.0.0.4");
        libc.sockaddr_in address= new libc.sockaddr_in(ipv4);
        linuxutils.ioctl_SIOCSIFADDR(device,address);
        address= linuxutils.ioctl_SIOCGIFADDR(device);
        logger.info("IPv4 sockaddr_in: {}", Hexdump.bytesToHex(address.array(), address.array().length));
        logger.info("{}", address);
        Assert.assertEquals(0, address.port);
        Assert.assertEquals(0x7f000004, address.address);
        Assert.assertEquals(0x02,address.family);
        logger.info("{}", address.toInetAddress());
        logger.info("{}", address.toInetSocketAddress());
        linuxutils.ioctl_SIOCSIFADDR(device,new libc.sockaddr_in(0x7f000003,(short)0,socket.AF_INET));
    }

    @Test
    public void testInterfacegetNetmask() throws libc.ErrnoException {
        libc.sockaddr_in address=linuxutils.ioctl_SIOCGIFNETMASK(device);
        logger.info("IPv4 sockaddr_in: {}", Hexdump.bytesToHex(address.array(), address.array().length));
        logger.info("{}", address);
        Assert.assertEquals(0, address.port);
        Assert.assertEquals(0xffffffff, address.address);
        Assert.assertEquals(0x02,address.family);
        logger.info("{}", address.toInetAddress());
        logger.info("{}", address.toInetSocketAddress());
    }

    @Test
    public void testInterfacesetNetMask() throws libc.ErrnoException, UnknownHostException {
        libc.sockaddr_in originalNetmask= linuxutils.ioctl_SIOCGIFNETMASK(device);
        logger.info("original {}", originalNetmask);
        libc.sockaddr_in address= new libc.sockaddr_in(0xffffff00,(short)0,socket.AF_INET);
        linuxutils.ioctl_SIOCSIFNETMASK(device, address);
        address= linuxutils.ioctl_SIOCGIFNETMASK(device);
        logger.info("IPv4 sockaddr_in: {}", Hexdump.bytesToHex(address.array(), address.array().length));
        logger.info("{}", address);
        Assert.assertEquals(0, address.port);
        Assert.assertEquals(0xffffff00, address.address);
        Assert.assertEquals(0x02,address.family);
        logger.info("{}", address.toInetAddress());
        logger.info("{}", address.toInetSocketAddress());
        Assert.assertEquals(0,linuxutils.ioctl_SIOCSIFNETMASK(device,new libc.sockaddr_in(0xffffffff, (short) 0, socket.AF_INET)));

    }

}
