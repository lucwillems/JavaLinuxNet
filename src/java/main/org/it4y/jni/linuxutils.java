package org.it4y.jni;

import org.it4y.net.SocketUtils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by luc on 12/28/13.
 */
public class linuxutils {
    //Load our native JNI lib
    static {
        JNILoader.loadLibrary("liblinuxutils.so");
    }

    public static native void setbooleanSockOption(int fd, int level, int option, boolean booleanValue) throws libc.ErrnoException;
    public static void setbooleanSockOption(Socket s, int level, int option, boolean booleanValue) throws libc.ErrnoException {
        int fd= SocketUtils.getFd(s);
        setbooleanSockOption(fd,level,option,booleanValue);
    }
    public static void setbooleanSockOption(ServerSocket s, int level, int option, boolean booleanValue) throws libc.ErrnoException {
        int fd= SocketUtils.getFd(s);
        setbooleanSockOption(fd,level,option,booleanValue);
    }
    public static void setbooleanSockOption(ServerSocketChannel sc, int level, int option, boolean booleanValue) throws libc.ErrnoException {
        int fd= SocketUtils.getFd(sc.socket());
        setbooleanSockOption(fd,level,option,booleanValue);
    }
    public static void setbooleanSockOption(SocketChannel sc, int level, int option, boolean booleanValue) throws libc.ErrnoException {
        int fd= SocketUtils.getFd(sc.socket());
        setbooleanSockOption(fd,level,option,booleanValue);
    }

    public static native void setuint16SockOption(int fd, int level, int option, int u16value) throws libc.ErrnoException;
    public static native void setuint32SockOption(int fd, int level, int option, int u32value) throws libc.ErrnoException;
    public static native void setstringSockOption(int fd, int level, int option, String s) throws libc.ErrnoException;

    public static native boolean getbooleanSockOption(int fd, int level, int option) throws libc.ErrnoException;
    public static native int getuint16SockOption(int fd, int level, int option) throws libc.ErrnoException;
    public static native int getuint32SockOption(int fd, int level, int option)  throws libc.ErrnoException;
    public static native String getstringSockOption(int fd, int level, int option)  throws libc.ErrnoException;

    public static native libc.sockaddr_in getsockname(int fd) throws libc.ErrnoException;
    public static libc.sockaddr_in getsockname(Socket s) throws libc.ErrnoException {
        int fd= SocketUtils.getFd(s);
        return getsockname(fd);
    }
    public static libc.sockaddr_in getsockname(ServerSocket s) throws libc.ErrnoException {
        int fd= SocketUtils.getFd(s);
        return getsockname(fd);
    }
    public static libc.sockaddr_in getsockname(ServerSocketChannel sc) throws libc.ErrnoException {
        int fd= SocketUtils.getFd(sc.socket());
        return getsockname(fd);
    }
    public static libc.sockaddr_in getsockname(SocketChannel sc) throws libc.ErrnoException {
        int fd= SocketUtils.getFd(sc.socket());
        return getsockname(fd);
    }


    public static native InetSocketAddress getLocalHost();
}
