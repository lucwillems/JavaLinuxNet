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

package org.it4y.demo;

import org.it4y.jni.SocketOptions;
import org.it4y.jni.linuxutils;
import org.it4y.net.tproxy.TProxyInterceptedSocket;
import org.it4y.net.tproxy.TProxyServerSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;


/**
 * Created by luc on 12/28/13.
 */
public class TProxyListener extends TestRunner {

    private final Logger log= LoggerFactory.getLogger(TProxyListener.class);
    private InetAddress listenIp;
    private int listenPort;
    private TProxyServerSocket server;

    public TProxyListener() {
        super("tproxy-listener");
        try {
            listenIp = InetAddress.getByName("127.0.0.1");
            listenPort = 1800;
        } catch (final Throwable ignore) {
        }
    }

    public int getFd() {
        return server.getFd();
    }


    public void run() {
        try {
            server = new TProxyServerSocket();
            server.initTProxy(listenIp, listenPort,500);
            running = true;
            while (running) {
                final TProxyInterceptedSocket client = server.accepProxy();

                //set client user transmit timeout
                linuxutils.setintSockOption(client.getSocket(), SocketOptions.SOL_TCP, SocketOptions.TCP_USER_TIMEOUT, 10000);

                log.info("TCP timout:",linuxutils.getintSockOption(client.getSocket(), SocketOptions.SOL_TCP, SocketOptions.TCP_USER_TIMEOUT));
                //back to the future: switch back to reno :-)
                linuxutils.setstringSockOption(client.getSocket(), SocketOptions.SOL_TCP, SocketOptions.TCP_CONGESTION, "reno");
                log.info("{} : {}",System.currentTimeMillis(),client);
//                TProxyInterceptedSocket lastclient = client;
                //do http proxy
                new HttpProxyThread(client).start();
            }
        } catch (final Throwable t) {
            log.error("ooeps:", t);
        }
    }
}
