package org.it4y.net.tproxy;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by luc on 12/27/13.
 */
public class TProxyClientSocket {

    private Socket socket=null;
    public InetAddress remoteIp=null;
    public int remotePort=-1;

    private byte[] toBytes(int i)
    {
        byte[] result = new byte[4];
        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i /*>> 0*/);
        return result;
    }

    /*
     * TODO : can't we do this better than getByAddress ?
     */
    public TProxyClientSocket(Socket socket, int remoteIp, int remotePort) throws UnknownHostException {
        this.socket=socket;
        this.remoteIp=InetAddress.getByAddress(toBytes(remoteIp));
        this.remotePort=remotePort;
    }

    public Socket getSocket() {
        return socket;
    }

    public InetAddress getRemoteIp() {
        return remoteIp;
    }
    public void setRemoteIp(InetAddress a) {
        this.remoteIp=a;
    }

    protected int getRemotePort() {
        return remotePort;
    }
    protected void setRemotePort(int p) {
        this.remotePort=p;
    }

    public String toString() {
        StringBuffer s=new StringBuffer();
        s.append("local:").append(socket.getInetAddress()).append(":").append(socket.getPort()).append(" ");
        if (remoteIp !=null) {
            s.append("Remote").append(remoteIp.toString()).append(":").append(remotePort);
        }
        return s.toString();
    }
}
