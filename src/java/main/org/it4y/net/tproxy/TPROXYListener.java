package org.it4y.net.tproxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * Created by luc on 12/27/13.
 */
public class TPROXYListener extends Thread {

    private int port = 0;
    private int backlog = 0;
    private InetAddress bind = null;
    private ServerSocket socket;

    private boolean running = false;

    public TPROXYListener(int port, int backlog, InetAddress bind) {
        this.port = port;
        this.backlog = backlog;
        this.bind = bind;
    }

    public void halt() {
        running = false;
    }

    public void run() {

        try {
            socket = new TProxyServerSocket();
            socket.setReuseAddress(true);
        } catch (IOException io) {
            System.out.println("Ooeeps :" + io.getMessage());
            return;
        }
        //set some important socket options
        System.out.println("TPROXY socket created..");
        running = true;


    }
}
