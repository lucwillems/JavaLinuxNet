package org.it4y.demo;

import org.it4y.net.tproxy.TProxyClientSocket;
import org.it4y.net.tproxy.TProxyServerSocket;

import java.net.InetAddress;

/**
 * Created by luc on 12/28/13.
 */
public class TProxyListener extends TestRunner {

    private InetAddress listenIp;
    private int listenPort;
    private TProxyServerSocket server;

    public TProxyListener() {
        super("tproxy-listener");
        try {
        this.listenIp=InetAddress.getByName("127.0.0.1");
        this.listenPort=1800;
        } catch (Throwable ignore) {}
    }

    public void run() {
        try {
         server=new TProxyServerSocket();
         server.initTProxy(listenIp,listenPort);
         while(true) {
            TProxyClientSocket client=server.accepProxy();
            System.out.println(System.currentTimeMillis()+": "+client);
         }
        } catch (Throwable t) {
            System.err.println("ooeps:"+t.getMessage());
            t.printStackTrace();
        }
    }
}
