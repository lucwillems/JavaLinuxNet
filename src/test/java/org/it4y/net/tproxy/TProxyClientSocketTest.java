package org.it4y.net.tproxy;

import junit.framework.Assert;
import org.it4y.jni.libc;
import org.it4y.jni.linux.socket;
import org.junit.Test;

import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by luc on 1/10/14.
 */
public class TProxyClientSocketTest {

    @Test
    public void testTPRoxyClientSocket() throws Exception {
        Socket client=new Socket();
        libc.sockaddr_in remote=new libc.sockaddr_in(0x01020304,0x1234, socket.AF_INET);
        TProxyInterceptedSocket tps=new TProxyInterceptedSocket(client,remote);
        Assert.assertNotNull(tps);
        Assert.assertEquals(client, tps.getSocket());
        Assert.assertEquals(0x01020304, tps.getRemoteAddress());
        Assert.assertEquals(0x1234,tps.getRemotePort());
        InetAddress ia=tps.getRemoteAddressAsInetAddress();
        Assert.assertEquals("/1.2.3.4",ia.toString());
        Assert.assertEquals("local:null:0 remote:/1.2.3.4:4660",tps.toString());
        SocketAddress sa=tps.getRemoteAsSocketAddress();
        Assert.assertEquals("/1.2.3.4:4660",sa.toString());
    }
}
