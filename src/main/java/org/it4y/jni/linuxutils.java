/*
 * This file is derived from Linux kernel headers
 * All copyrights preserved to original kernel author.
 *
 * more info see https://www.kernel.org/pub/linux/kernel/COPYING
 */

package org.it4y.jni;

import org.it4y.net.SocketUtils;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class linuxutils {
    //Load our native JNI lib
    static {
        //THIS requires libnl3 !!!!
        JNILoader.loadLibrary("libjnilinuxutils.so");
    }

    public static native void setbooleanSockOption(int fd, int level, int option, boolean booleanValue) throws libc.ErrnoException;

    public static void setbooleanSockOption(final Socket s, final int level, final int option, final boolean booleanValue) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(s);
        setbooleanSockOption(fd, level, option, booleanValue);
    }

    public static void setbooleanSockOption(final ServerSocket s, final int level, final int option, final boolean booleanValue) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(s);
        setbooleanSockOption(fd, level, option, booleanValue);
    }

    public static void setbooleanSockOption(final ServerSocketChannel sc, final int level, final int option, final boolean booleanValue) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(sc.socket());
        setbooleanSockOption(fd, level, option, booleanValue);
    }

    public static void setbooleanSockOption(final SocketChannel sc, final int level, final int option, final boolean booleanValue) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(sc.socket());
        setbooleanSockOption(fd, level, option, booleanValue);
    }

    /* note: int=4 bytes !!! */
    public static native void setintSockOption(int fd, int level, int option, int int32value) throws libc.ErrnoException;

    public static void setintSockOption(final Socket s, final int level, final int option, final int intvalue) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(s);
        setintSockOption(fd, level, option, intvalue);
    }

    public static void setintSockOption(final ServerSocket s, final int level, final int option, final int intvalue) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(s);
        setintSockOption(fd, level, option, intvalue);
    }

    public static void setintSockOption(final SocketChannel sc, final int level, final int option, final int intvalue) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(sc.socket());
        setintSockOption(fd, level, option, intvalue);
    }

    public static void setintSockOption(final ServerSocketChannel sc, final int level, final int option, final int intvalue) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(sc.socket());
        setintSockOption(fd, level, option, intvalue);
    }

    public static native void setstringSockOption(int fd, int level, int option, String s) throws libc.ErrnoException;

    public static void setstringSockOption(final Socket s, final int level, final int option, final String x) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(s);
        setstringSockOption(fd, level, option, x);
    }

    public static void setstringSockOption(final ServerSocket s, final int level, final int option, final String x) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(s);
        setstringSockOption(fd, level, option, x);
    }

    public static void setstringSockOption(final SocketChannel sc, final int level, final int option, final String x) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(sc.socket());
        setstringSockOption(fd, level, option, x);
    }

    public static void setstringSockOption(final ServerSocketChannel sc, final int level, final int option, final String x) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(sc.socket());
        setstringSockOption(fd, level, option, x);
    }


    public static native boolean getbooleanSockOption(int fd, int level, int option) throws libc.ErrnoException;

    public static native int getintSockOption(int fd, int level, int option) throws libc.ErrnoException;

    public static int getintSockOption(final Socket s, final int level, final int option) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(s);
        return getintSockOption(fd, level, option);
    }

    public static int getintSockOption(final ServerSocket s, final int level, final int option) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(s);
        return getintSockOption(fd, level, option);
    }

    public static int getintSockOption(final SocketChannel sc, final int level, final int option) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(sc.socket());
        return getintSockOption(fd, level, option);
    }

    public static int getintSockOption(final ServerSocketChannel sc, final int level, final int option) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(sc.socket());
        return getintSockOption(fd, level, option);
    }

    public static native String getstringSockOption(int fd, int level, int option) throws libc.ErrnoException;

    public static String getstringSockOption(final Socket s, final int level, final int option) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(s);
        return getstringSockOption(fd, level, option);
    }

    public static String getstringSockOption(final ServerSocket s, final int level, final int option) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(s);
        return getstringSockOption(fd, level, option);
    }

    public static String getstringSockOption(final SocketChannel sc, final int level, final int option) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(sc.socket());
        return getstringSockOption(fd, level, option);
    }

    public static String getstringSockOption(final ServerSocketChannel sc, final int level, final int option) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(sc.socket());
        return getstringSockOption(fd, level, option);
    }

    public static native libc.sockaddr_in getsockname(int fd) throws libc.ErrnoException;

    public static libc.sockaddr_in getsockname(final Socket s) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(s);
        return getsockname(fd);
    }

    public static libc.sockaddr_in getsockname(final ServerSocket s) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(s);
        return getsockname(fd);
    }

    public static libc.sockaddr_in getsockname(final ServerSocketChannel sc) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(sc.socket());
        return getsockname(fd);
    }

    public static libc.sockaddr_in getsockname(final SocketChannel sc) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(sc.socket());
        return getsockname(fd);
    }


    public static native int gettcpinfo(int fd, libc.tcp_info info) throws libc.ErrnoException;

    public static int gettcpinfo(final Socket s, final libc.tcp_info info) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(s);
        return gettcpinfo(fd, info);
    }

    public static int gettcpinfo(final ServerSocket s, final libc.tcp_info info) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(s);
        return gettcpinfo(fd, info);
    }

    public static int gettcpinfo(final ServerSocketChannel sc, final libc.tcp_info info) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(sc.socket());
        return gettcpinfo(fd, info);
    }

    public static int gettcpinfo(final SocketChannel sc, final libc.tcp_info info) throws libc.ErrnoException {
        final int fd = SocketUtils.getFd(sc.socket());
        return gettcpinfo(fd, info);
    }

    public static native InetSocketAddress getLocalHost();


}
