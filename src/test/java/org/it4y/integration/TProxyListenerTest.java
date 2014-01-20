package org.it4y.integration;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import junit.framework.Assert;
import org.it4y.net.link.LinkManager;
import org.it4y.net.link.NetworkInterface;
import org.it4y.net.tproxy.TProxyInterceptedSocket;
import org.it4y.net.tproxy.TProxyListener;
import org.it4y.util.Counter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * You need to run src/test/scripts/setup-test.sh to be able to run this test
 * Created by luc on 1/10/14.
 */
public class TProxyListenerTest {
    final private Logger log = LoggerFactory.getLogger(TProxyListenerTest.class);

    //check this values with setup-test.sh
    private String bind = "Localhost";
    private int port = 1800;
    private int backlog = 10;

    private TProxyListener startTProxyListener(TProxyListener proxy) throws Exception {

        Assert.assertNotNull(proxy);
        proxy.initServer();
        proxy.start();
        //start it
        int cnt = 0;
        while (!proxy.isRunning() & cnt < 10) {
            Thread.sleep(10);
            cnt++;
        }
        Assert.assertTrue(proxy.isRunning());
        Assert.assertTrue(!proxy.isHalted());
        Assert.assertNotNull(proxy.getSocket());
        Assert.assertNotNull(proxy.getSocket().isBound());
        return proxy;
    }

    private void stopTProxyListener(TProxyListener proxy) throws Exception {
        proxy.halt();
        int cnt = 0;
        while (proxy.isRunning() & cnt < 10) {
            Thread.sleep(10);
            cnt++;
        }
        org.junit.Assert.assertTrue("TProxy Listener must be stopped", !proxy.isRunning());
        org.junit.Assert.assertTrue("TProxy Listener must be stopped", !proxy.isHalted());
        Assert.assertNull(proxy.getSocket());
    }

    private LinkManager startLinkManager() throws Exception {
        LinkManager lm = new LinkManager();
        int retry = 0;
        lm.start();
        //do something here
        while (!lm.isReady() & retry < 10) {
            Thread.sleep(10);
            retry++;
        }
        Assert.assertTrue(lm.isReady());

        //get interface with default gateway. this will be our source IP
        final NetworkInterface defaultGW = lm.getDefaultGateway();
        Assert.assertNotNull(defaultGW);
        log.info("Default GW: {}", defaultGW);

        //our test device calls test
        final NetworkInterface tunnel = lm.findByInterfaceName("test");
        Assert.assertNotNull("Please run setup-test.sh",tunnel);

        return lm;
    }

    private void stopLinkManager(LinkManager lm) throws Exception {
        lm.halt();
        int cnt = 0;
        while (!lm.isHalted() & cnt < 10) {
            Thread.sleep(10);
            cnt++;
        }
        lm.shutDown();
        org.junit.Assert.assertTrue(!lm.isRunning());
    }


    @Test
    public void testTProxyListener() throws Exception {

        TProxyListener listener = startTProxyListener(new TProxyListener(InetAddress.getByName(bind), port, backlog) {
            @Override
            public void newClient(TProxyInterceptedSocket client) throws IOException {
            }

            @Override
            public void onIOError(IOException io) {
            }
        });
        try {
            //do something here
            Thread.sleep(1);
        } finally {
            listener.halt();
        }
    }

    @Test
    public void testTProxyConnection() throws Exception {
        final Counter msgcnt = new Counter();

        LinkManager linkManager = null;
        TProxyListener proxy = null;

        try {
            linkManager = startLinkManager();

            //get interface with default gateway. this will be our source IP
            final NetworkInterface defaultGW = linkManager.getDefaultGateway();

            //Run a proxy to intercept port 80 . this requires setup-test.sh to setup iptables and routing
            proxy = startTProxyListener(new TProxyListener(InetAddress.getByName(bind), port, backlog) {
                public void newClient(TProxyInterceptedSocket client) {
                    log.info("intercept client connection: {}", client);
                    //check client connection parameters
                    Assert.assertNotNull(client);
                    //we should check local and remote address are unchanged (thats transparent proxy)
                    Assert.assertEquals(0x08080404, client.getRemoteAddress());
                    Assert.assertEquals(80, client.getRemotePort());
                    Assert.assertTrue(client.getSocket().toString().contains(defaultGW.getIpv4AddressAsInetAddress().toString()));
                    //close connection on our side
                    try {
                        client.getSocket().close();
                        log.info("client connection closed");
                    } catch (IOException io) {
                    }

                    //this will signal message recieved
                    msgcnt.inc();
                }

                public void onIOError(IOException io) {
                }
            });

            log.info("TProxy running, port {}", port);
            //Now connect to our test IP (8.8.4.4) using HTTP
            Socket clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress("8.8.4.4", 80), 5000);
            Assert.assertTrue("connection must be established", clientSocket.isConnected());
            log.info("Client connected...");
            //now wait until we get this signaled by changed msgcnt
            int retry = 0;
            while (msgcnt.getCount() != 1 & retry < 10) {
                Thread.sleep(100);
                retry++;
            }
            log.info("number of connections seen: {}", msgcnt.getCount());
            clientSocket.close();
            //we recieved 1 connection
            Assert.assertEquals(1, msgcnt.getCount());
        } finally {
            if (linkManager!= null)
                stopLinkManager(linkManager);
            if (proxy != null)
                stopTProxyListener(proxy);
        }
    }


    @Test
    public void testIOError() throws Exception {

        final Counter msgcnt = new Counter();
        final Counter errcnt = new Counter();

        LinkManager linkManager = null;
        TProxyListener proxy = null;

        try {
            linkManager = startLinkManager();
            //get interface with default gateway. this will be our source IP
            final NetworkInterface defaultGW = linkManager.getDefaultGateway();

            //Run a proxy to intercept port 80 . this requires setup-test.sh to setup iptables and routing
            proxy = startTProxyListener(new TProxyListener(InetAddress.getByName(bind), port, backlog) {

                public void newClient(TProxyInterceptedSocket client) throws IOException {
                    log.info("intercept client connection: {}", client);
                    //check client connection parameters
                    Assert.assertNotNull(client);
                    //we should check local and remote address are unchanged (thats transparent proxy)
                    Assert.assertEquals(0x08080404, client.getRemoteAddress());
                    Assert.assertEquals(80, client.getRemotePort());
                    Assert.assertTrue(client.getSocket().toString().contains(defaultGW.getIpv4AddressAsInetAddress().toString()));
                    msgcnt.inc();
                    //this will cause exceptions by design ;-)
                    throw new IOException("shit happens");
                }

                public void onIOError(IOException io) {
                    errcnt.inc();
                    log.info("we got some error: {}", io.getMessage());
                    Assert.assertEquals("shit happens", io.getMessage());
                }
            });
            log.info("TProxy running...");

            //Now connect to our test IP (8.8.4.4) using HTTP
            Socket clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress("8.8.4.4", 80), 5000);
            Assert.assertTrue("connection must be established", clientSocket.isConnected());
            log.info("Client connected...");
            //now wait until client signals connection recieved
            int retry = 0;
            while (msgcnt.getCount() != 1 & retry < 10) {
                Thread.sleep(10);
                retry++;
            }
            //we need some time to throw the exception so wait a bit
            Thread.sleep(100);
            clientSocket.close();
            log.info("Client disconnected");

            //we recieved 1 connection
            Assert.assertEquals(1, msgcnt.getCount());
            Assert.assertEquals(1, errcnt.getCount());
        } finally {
            if (linkManager!= null)
                stopLinkManager(linkManager);
            if (proxy != null)
                stopTProxyListener(proxy);
        }
    }


    @Test
    public void testPerformanceMultibleClients() throws Exception {
        final Counter msgcnt = new Counter();
        final Counter errcnt = new Counter();

        LinkManager linkManager = null;
        TProxyListener proxy = null;

        try {
            linkManager = startLinkManager();
            //get interface with default gateway. this will be our source IP
            final NetworkInterface defaultGW = linkManager.getDefaultGateway();


            //Run a proxy to intercept port 80 . this requires setup-test.sh to setup iptables and routing
            proxy = startTProxyListener(new TProxyListener(InetAddress.getByName(bind), port, backlog) {
                public void newClient(TProxyInterceptedSocket client) throws IOException {
                    //log.info("intercept client connection: {}", client);
                    //check client connection parameters
                    Assert.assertNotNull(client);
                    //we should check local and remote address are unchanged (thats transparent proxy)
                    Assert.assertEquals(0x08080404, client.getRemoteAddress());
                    Assert.assertEquals(80, client.getRemotePort());
                    Assert.assertTrue(client.getSocket().toString().contains(defaultGW.getIpv4AddressAsInetAddress().toString()));
                    msgcnt.inc();
                }

                public void onIOError(IOException io) {
                    errcnt.inc();
                    log.info("oeps some error: {}", io.getMessage(), io);
                }
            });
            log.info("TProxy running...");

            //we use NETTY to generated massive tcp connections
            EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 40);
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            Bootstrap handler = b.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                }
            });

            // Start the client.
            int nrOfConnections = 2000;
            log.info("Starting {} connections ....", nrOfConnections);
            long start = System.currentTimeMillis();
            int retry = 0;
            for (int i = 0; i < nrOfConnections; i++) {
                final ChannelFuture f = b.connect("8.8.4.4", 80);
                f.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        //close when connected
                        if (future.isSuccess()) {
                            future.channel().disconnect();
                        }
                    }
                });
            }

            //log.info("Started: {} msec", System.currentTimeMillis() - start);
            //wait until all are closed
            while (msgcnt.getCount() < nrOfConnections & retry < nrOfConnections) {
                Thread.sleep(10);
                retry++;
            }

            long end = System.currentTimeMillis();
            log.info("Done: {} msec", end - start);
            Assert.assertEquals(nrOfConnections, msgcnt.getCount());
            Assert.assertEquals(0, errcnt.getCount());
        } finally {
            if (linkManager!= null)
                stopLinkManager(linkManager);
            if (proxy != null)
                stopTProxyListener(proxy);
        }
    }


}
