package org.it4y.jni;

import org.it4y.jni.linux.socket;
import org.junit.Assert;
import org.junit.Test;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteOrder;

/**
 * Created by luc on 1/9/14.
 */
public class libcTest {

    @Test
    public void testNtol() throws Exception {
     System.out.println("System endian: "+ByteOrder.nativeOrder().toString());
     /* convert 32 bits from network to local format */
     int x=0x44332211;
     Assert.assertEquals(0x11223344,libc.ntol(x));
     x=0xffeeddcc;
     Assert.assertEquals(0xccddeeff,libc.ntol(x));

    }

    @Test
    public void testToInetAddress() throws Exception {
        int x=0x01020304;
        InetAddress ip=libc.toInetAddress(x);
        Assert.assertNotNull(ip);
        Assert.assertEquals("/1.2.3.4", ip.toString());
    }

    @Test
    public void testin_addr() throws Exception {
        libc.in_addr a;
        a=new libc.in_addr();
        Assert.assertEquals(0, a.address);
        a=new libc.in_addr(0x01020304);
        Assert.assertEquals(0x01020304, a.address);
        InetAddress ax=a.toInetAddress();
        Assert.assertNotNull(ax);
        Assert.assertEquals("/1.2.3.4", ax.toString());
        //to String will give hex presentartion of address
        Assert.assertEquals("0x01020304",a.toString());
    }

    @Test
    public void testSockaddr_in() throws Exception {
        libc.sockaddr_in sa;

        sa=new libc.sockaddr_in(0,0, socket.AF_INET);
        Assert.assertNotNull(sa);
        Assert.assertEquals(0, sa.address);
        Assert.assertEquals(0, sa.port);
        Assert.assertEquals(2,socket.AF_INET);
        sa=new libc.sockaddr_in(0x01020304,0x1234,socket.AF_INET6);
        Assert.assertEquals(0x01020304, sa.address);
        Assert.assertEquals(0x1234,sa.port);
        Assert.assertEquals(10,socket.AF_INET6);
        InetSocketAddress isa=sa.toInetSocketAddress();
        Assert.assertNotNull(isa);
        Assert.assertEquals("/1.2.3.4:4660",isa.toString());
        Assert.assertEquals("0x01020304:4660",sa.toString());
    }

 }
