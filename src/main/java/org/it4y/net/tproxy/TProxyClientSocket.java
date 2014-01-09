package org.it4y.net.tproxy;

import org.it4y.jni.libc;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by luc on 12/27/13.
 */
public class TProxyClientSocket {

    private Socket socket = null;
    private libc.sockaddr_in remote;

    /*
     * TODO : can't we do this better than getByAddress ?
     */
    public TProxyClientSocket(Socket socket, libc.sockaddr_in remote) throws UnknownHostException {
        this.socket = socket;
        this.remote = remote;
    }

    public Socket getSocket() {
        return socket;
    }

    public InetSocketAddress getRemote() {
        return remote.toInetSocketAddress();
    }

    public int getRemoteAddressAsInt() {
        return remote.address;
    }

    public InetAddress getRemoteAddress() {
        return remote.toInetAddress();
    }

    public int getRemotePort() {
        return remote.port;
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("local:").append(socket.getInetAddress()).append(":").append(socket.getPort()).append(" ");
        if (remote != null) {
            s.append("Remote:").append(remote.toInetAddress()).append(":").append(remote.port);
        }
        return s.toString();
    }
}
