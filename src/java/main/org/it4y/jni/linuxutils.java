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

    /* note: int=4 bytes !!! */
    public static native void setintSockOption(int fd, int level, int option, int int32value) throws libc.ErrnoException;
    public static void setintSockOption(Socket s, int level, int option, int intvalue) throws libc.ErrnoException {
        int fd= SocketUtils.getFd(s);
        setintSockOption(fd,level,option,intvalue);
    }
    public static void setintSockOption(ServerSocket s, int level, int option, int intvalue) throws libc.ErrnoException {
        int fd= SocketUtils.getFd(s);
        setintSockOption(fd,level,option,intvalue);
    }
    public static void setintSockOption(SocketChannel sc, int level, int option, int intvalue) throws libc.ErrnoException {
        int fd= SocketUtils.getFd(sc.socket());
        setintSockOption(fd,level,option,intvalue);
    }
    public static void setintSockOption(ServerSocketChannel sc, int level, int option, int intvalue) throws libc.ErrnoException {
        int fd= SocketUtils.getFd(sc.socket());
        setintSockOption(fd,level,option,intvalue);
    }

    public static native void setstringSockOption(int fd, int level, int option, String s) throws libc.ErrnoException;
    public static void setstringSockOption(Socket s, int level, int option, String x) throws libc.ErrnoException {
        int fd=SocketUtils.getFd(s);
        setstringSockOption(fd,level,option,x);
    }
    public static void setstringSockOption(ServerSocket s, int level, int option, String x) throws libc.ErrnoException {
        int fd=SocketUtils.getFd(s);
        setstringSockOption(fd,level,option,x);
    }
    public static void setstringSockOption(SocketChannel sc, int level, int option, String x) throws libc.ErrnoException {
        int fd=SocketUtils.getFd(sc.socket());
        setstringSockOption(fd,level,option,x);
    }
    public static void setstringSockOption(ServerSocketChannel sc, int level, int option, String x) throws libc.ErrnoException {
        int fd=SocketUtils.getFd(sc.socket());
        setstringSockOption(fd,level,option,x);
    }


    public static native boolean getbooleanSockOption(int fd, int level, int option) throws libc.ErrnoException;
    public static native int getintSockOption(int fd, int level, int option) throws libc.ErrnoException;
    public static int getintSockOption(Socket s,int level,int option) throws libc.ErrnoException {
        int fd= SocketUtils.getFd(s);
        return getintSockOption(fd,level,option);
    }
    public static int getintSockOption(ServerSocket s,int level,int option) throws libc.ErrnoException {
        int fd= SocketUtils.getFd(s);
        return getintSockOption(fd,level,option);
    }
    public static int getintSockOption(SocketChannel sc,int level,int option) throws libc.ErrnoException {
        int fd= SocketUtils.getFd(sc.socket());
        return getintSockOption(fd,level,option);
    }
    public static int getintSockOption(ServerSocketChannel sc,int level,int option) throws libc.ErrnoException {
        int fd=SocketUtils.getFd(sc.socket());
        return getintSockOption(fd,level,option);
    }

    public static native String getstringSockOption(int fd, int level, int option)  throws libc.ErrnoException;
    public static String getstringSockOption(Socket s,int level,int option) throws libc.ErrnoException {
        int fd= SocketUtils.getFd(s);
        return getstringSockOption(fd,level,option);
    }
    public static String getstringSockOption(ServerSocket s,int level,int option) throws libc.ErrnoException {
        int fd= SocketUtils.getFd(s);
        return getstringSockOption(fd,level,option);
    }
    public static String getstringSockOption(SocketChannel sc,int level,int option) throws libc.ErrnoException {
        int fd= SocketUtils.getFd(sc.socket());
        return getstringSockOption(fd,level,option);
    }
    public static String getstringSockOption(ServerSocketChannel sc,int level,int option) throws libc.ErrnoException {
        int fd=SocketUtils.getFd(sc.socket());
        return getstringSockOption(fd,level,option);
    }

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
