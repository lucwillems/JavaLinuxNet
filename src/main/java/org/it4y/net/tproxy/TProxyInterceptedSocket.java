/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.tproxy;

import org.it4y.jni.libc;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * TProxyInterceptedSocket wrappes the local and remote Socket Addresses from a TProxy intercepted TCP connection
 * Created by luc on 12/27/13.
 */
public class TProxyInterceptedSocket {

    private final Socket socket;
    private final libc.sockaddr_in remote;

    /**
     * create a client proxy Socket pair
     * @param socket ; local part of the connection
     * @param remote : remote part of the connection
     * @throws UnknownHostException
     */
    public TProxyInterceptedSocket(final Socket socket, final libc.sockaddr_in remote) throws UnknownHostException {
        this.socket = socket;
        this.remote = remote;
    }

    /**
     * get local part of the connection
     * @return
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * get Remote part of the connection a SocketAddress
     * @return
     */
    public InetSocketAddress getRemoteAsSocketAddress() {
        return remote.toInetSocketAddress();
    }

    /**
     * get Remote IPV4 in network notation
     * @return
     */
    public int getRemoteAddress() {
        return remote.address;
    }

    /**
     * get Remote IPV4 address as InetAddress
     * @return
     */
    public InetAddress getRemoteAddressAsInetAddress() {
        return remote.toInetAddress();
    }

    /**
     * return remote port of the connection
     * @return
     */
    public int getRemotePort() {
        return remote.port;
    }

    public String toString() {
        final StringBuilder s = new StringBuilder(128);
        s.append("local:").append(socket.getInetAddress()).append(':').append(socket.getPort()).append(' ');
        if (remote != null) {
            s.append("remote:").append(remote.toInetAddress()).append(':').append(remote.port);
        }
        return s.toString();
    }
}
