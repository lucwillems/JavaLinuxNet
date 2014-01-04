package org.it4y.demo;

import org.it4y.jni.libc;
import org.it4y.jni.linuxutils;
import org.it4y.net.linux.SocketOptions;
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
    private TProxyClientSocket lastclient = null;
    private libc.tcp_info info = new libc.tcp_info();

    public TProxyListener() {
        super("tproxy-listener");
        try {
            this.listenIp = InetAddress.getByName("127.0.0.1");
            this.listenPort = 1800;
        } catch (Throwable ignore) {
        }
    }

    public int getFd() {
        return server.getFd();
    }


    public void run() {
        try {
            server = new TProxyServerSocket();
            server.initTProxy(listenIp, listenPort);
            while (true) {
                TProxyClientSocket client = server.accepProxy();

                //set client user transmit timeout
                linuxutils.setintSockOption(client.getSocket(), SocketOptions.SOL_TCP, SocketOptions.TCP_USER_TIMEOUT, 10000);
                System.out.println(linuxutils.getintSockOption(client.getSocket(), SocketOptions.SOL_TCP, SocketOptions.TCP_USER_TIMEOUT));
                //back to the future: switch back to reno :-)
                linuxutils.setstringSockOption(client.getSocket(), SocketOptions.SOL_TCP, SocketOptions.TCP_CONGESTION, "reno");
                String con = linuxutils.getstringSockOption(client.getSocket(), SocketOptions.SOL_TCP, SocketOptions.TCP_CONGESTION);
                //System.out.println(con);

                System.out.println(System.currentTimeMillis() + ": " + client);
                lastclient = client;
                //do http proxy
                new HttpProxyThread(client).start();
            }
        } catch (Throwable t) {
            System.err.println("ooeps:" + t.getMessage());
            t.printStackTrace();
        }
    }
}
