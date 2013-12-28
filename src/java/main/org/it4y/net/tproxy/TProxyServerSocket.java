package org.it4y.net.tproxy;

import org.it4y.net.SocketUtils;
import org.it4y.net.tproxy.TProxyClientSocket;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.*;


/**
 * Created by luc on 12/27/13.
 * You need todo this on linux to run it as normal user
 * setcap "cap_net_raw=+eip cap_net_admin=+eip" /usr/lib/jvm/java-1.7.0/bin/java
 * replace java executable path with your java and use that java
 * Note: after upgrade/changing this file you need to repeat this
 */
public class TProxyServerSocket extends ServerSocket {
    /**
     * Load a libary (*.so or *.dll).
     * @param libs the libary names
     * @throws java.io.IOException
     */
    static void loadLib(String... libs) throws Throwable {
        Throwable e=null;
        for(String lib : libs) {
            try {
                System.load(new File(lib).getCanonicalPath());
                break;
            } catch (Throwable eio) {e = eio;}
        }
        if (e!=null) throw e;
    }

    //Load our native JNI lib
    static {
        try {
            loadLib("clib/libtproxy.so");
        } catch (Throwable e) {
            System.out.println("Could not load libTunTapLinux.so" + e.getMessage());
        }
    }

    /*
     * This fields are manipulated by native c code so don't change it !!!
     */
    private int remoteIp;
    private int remotePort;

    public TProxyServerSocket() throws IOException {
        super();
    }

    public SocketImpl getImplementation() {
        return SocketUtils.getImplementation(this);
    }

    public FileDescriptor getFileDescriptor() {
        return SocketUtils.getFileDescriptor(this);
    }

    public int getFd() {
        return SocketUtils.getFd(this);
    }

    private native int setIPTransparant(int fd);
    private native int getOriginalDestination(int fd);

    public void initTProxy() throws SocketException,IOException,UnknownHostException {
        SocketImpl impl=getImplementation();
        int fd=getFd();
        System.out.println("TPROXY server socket fd: "+fd);
        int res=setIPTransparant(fd);
        if (res !=0 ) {
            System.out.println("oeps... IPtrans failed:"+res);
        }
        setReuseAddress(true);
        //bind to localhost interface
        InetSocketAddress local=new InetSocketAddress(InetAddress.getByName("127.0.0.1"),1800);
        bind(local,500);

    }

    /*
     * This method is not thread save, so only 1 thread please !!!
     */
    public TProxyClientSocket accepProxy() throws IOException {
        Socket c=accept();
        //get original destination address
        if (getOriginalDestination(SocketUtils.getFd(c)) !=0) {
            throw new RuntimeException("could not get original dst ip");
        }
        return new TProxyClientSocket(c,remoteIp,remotePort);
    }

}
