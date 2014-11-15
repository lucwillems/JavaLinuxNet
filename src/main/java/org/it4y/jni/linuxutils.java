/*
 * Copyright 2014 Luc Willems (T.M.M.)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.it4y.jni;

import org.it4y.net.SocketUtils;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class linuxutils {
    //some errorcodes from our jni methods
    public static final int JNI_OK = 0;
    public static final int JNI_ERROR = -1;
    public static final int JNI_ERR_FIND_CLASS_FAILED = -2;
    public static final int JNI_ERR_GET_METHOD_FAILED = -3;
    public static final int JNI_ERR_CALL_METHOD_FAILED = -4;
    public static final int JNI_ERR_BUFFER_TO_SMALL = -5;
    public static final int JNI_ERR_EXCEPTION = -6;

    //Load our native JNI lib
    static {
        //THIS requires libnl3 !!!!
        JNILoader.loadLibrary("libjnilinuxutils");
        final int initResult = initlib();
        if (initResult != JNI_OK) {
            throw new RuntimeException("Failed to initialize libpcap jni interface : " + initResult);
        }
    }

    //This method should be called first before using the library
    //it's used to initialize internal jni structure to speedup jni lookups
    private static native int initlib();

    public static native void setbooleanSockOption(int fd, int level, int option, boolean booleanValue) throws libc.ErrnoException;

    public static void setbooleanSockOption(final Socket s, final int level, final int option, final boolean booleanValue) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(s);
        setbooleanSockOption(fd, level, option, booleanValue);
    }

    public static void setbooleanSockOption(final ServerSocket s, final int level, final int option, final boolean booleanValue) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(s);
        setbooleanSockOption(fd, level, option, booleanValue);
    }

    public static void setbooleanSockOption(final ServerSocketChannel sc, final int level, final int option, final boolean booleanValue) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(sc.socket());
        setbooleanSockOption(fd, level, option, booleanValue);
    }

    public static void setbooleanSockOption(final SocketChannel sc, final int level, final int option, final boolean booleanValue) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(sc.socket());
        setbooleanSockOption(fd, level, option, booleanValue);
    }

    /* note: int=4 bytes !!! */
    public static native void setintSockOption(int fd, int level, int option, int int32value) throws libc.ErrnoException;

    public static void setintSockOption(final Socket s, final int level, final int option, final int intvalue) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(s);
        setintSockOption(fd, level, option, intvalue);
    }

    public static void setintSockOption(final ServerSocket s, final int level, final int option, final int intvalue) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(s);
        setintSockOption(fd, level, option, intvalue);
    }

    public static void setintSockOption(final SocketChannel sc, final int level, final int option, final int intvalue) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(sc.socket());
        setintSockOption(fd, level, option, intvalue);
    }

    public static void setintSockOption(final ServerSocketChannel sc, final int level, final int option, final int intvalue) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(sc.socket());
        setintSockOption(fd, level, option, intvalue);
    }
    public static void setintSockOption(final DatagramChannel dc, final int level, final int option, final int intvalue) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(dc);
        setintSockOption(fd, level, option, intvalue);
    }

    public static native void setstringSockOption(int fd, int level, int option, String s) throws libc.ErrnoException;

    public static void setstringSockOption(final Socket s, final int level, final int option, final String x) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(s);
        setstringSockOption(fd, level, option, x);
    }

    public static void setstringSockOption(final ServerSocket s, final int level, final int option, final String x) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(s);
        setstringSockOption(fd, level, option, x);
    }

    public static void setstringSockOption(final SocketChannel sc, final int level, final int option, final String x) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(sc.socket());
        setstringSockOption(fd, level, option, x);
    }

    public static void setstringSockOption(final ServerSocketChannel sc, final int level, final int option, final String x) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(sc.socket());
        setstringSockOption(fd, level, option, x);
    }


    public static native boolean getbooleanSockOption(int fd, int level, int option) throws libc.ErrnoException;

    public static native int getintSockOption(int fd, int level, int option) throws libc.ErrnoException;

    public static int getintSockOption(final Socket s, final int level, final int option) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(s);
        return getintSockOption(fd, level, option);
    }

    public static int getintSockOption(final ServerSocket s, final int level, final int option) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(s);
        return getintSockOption(fd, level, option);
    }

    public static int getintSockOption(final SocketChannel sc, final int level, final int option) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(sc.socket());
        return getintSockOption(fd, level, option);
    }

    public static int getintSockOption(final ServerSocketChannel sc, final int level, final int option) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(sc.socket());
        return getintSockOption(fd, level, option);
    }

    public static int getintSockOption(final DatagramChannel dc, final int level, final int option) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(dc);
        return getintSockOption(fd, level, option);
    }

    public static native String getstringSockOption(int fd, int level, int option) throws libc.ErrnoException;

    public static String getstringSockOption(final Socket s, final int level, final int option) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(s);
        return getstringSockOption(fd, level, option);
    }

    public static String getstringSockOption(final ServerSocket s, final int level, final int option) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(s);
        return getstringSockOption(fd, level, option);
    }

    public static String getstringSockOption(final SocketChannel sc, final int level, final int option) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(sc.socket());
        return getstringSockOption(fd, level, option);
    }

    public static String getstringSockOption(final ServerSocketChannel sc, final int level, final int option) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(sc.socket());
        return getstringSockOption(fd, level, option);
    }

    private static native byte[] _getsockname(int fd) throws libc.ErrnoException;

    public static libc.sockaddr_in getsockname(final Socket s) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(s);
        final byte[] sa=_getsockname(fd);
        return new libc.sockaddr_in(sa,true);
    }

    public static libc.sockaddr_in getsockname(final ServerSocket s) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(s);
        return new libc.sockaddr_in(_getsockname(fd),true);
    }

    public static libc.sockaddr_in getsockname(final ServerSocketChannel sc) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(sc.socket());
        return new libc.sockaddr_in(_getsockname(fd),true);
    }

    public static libc.sockaddr_in getsockname(final SocketChannel sc) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(sc.socket());
        return new libc.sockaddr_in(_getsockname(fd),true);
    }


    public static native int gettcpinfo(int fd, libc.tcp_info info) throws libc.ErrnoException;

    public static int gettcpinfo(final Socket s, final libc.tcp_info info) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(s);
        return gettcpinfo(fd, info);
    }

    public static int gettcpinfo(final ServerSocket s, final libc.tcp_info info) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(s);
        return gettcpinfo(fd, info);
    }

    public static int gettcpinfo(final ServerSocketChannel sc, final libc.tcp_info info) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(sc.socket());
        return gettcpinfo(fd, info);
    }

    public static int gettcpinfo(final SocketChannel sc, final libc.tcp_info info) throws libc.ErrnoException  {
        final int fd = SocketUtils.getFd(sc.socket());
        return gettcpinfo(fd, info);
    }

    //public static native InetSocketAddress getLocalHost();

    public static native short ioctl_SIOCGIFFLAGS(String  device) throws libc.ErrnoException;
    public static native int ioctl_SIOCSIFFLAGS(String  device,short flags) throws libc.ErrnoException;

    private static native byte[] _ioctl_SIOCGIFADDR(String device) throws libc.ErrnoException;
    public static libc.sockaddr_in ioctl_SIOCGIFADDR(final String device) throws libc.ErrnoException{
        return new libc.sockaddr_in(_ioctl_SIOCGIFADDR(device),false);
    }

    private static native int _ioctl_SIOCSIFADDR(String device,byte[] ipv4) throws libc.ErrnoException;
    public static int ioctl_SIOCSIFADDR(final String device, final libc.sockaddr_in address) throws libc.ErrnoException {
        return _ioctl_SIOCSIFADDR(device,address.array(true));
    }

    private static native byte[] _ioctl_SIOCGIFNETMASK(String device) throws libc.ErrnoException;
    public static libc.sockaddr_in ioctl_SIOCGIFNETMASK(final String device) throws libc.ErrnoException {
        return new libc.sockaddr_in(_ioctl_SIOCGIFNETMASK(device));
    }

    private static native int _ioctl_SIOCSIFNETMASK(String device,byte[] ipv4) throws libc.ErrnoException;
    public static int ioctl_SIOCSIFNETMASK(final String device, final libc.sockaddr_in address) throws libc.ErrnoException {
        return _ioctl_SIOCSIFNETMASK(device,address.array(true));
    }


    public static native int ioctl_SIOCGIFMTU(String device) throws libc.ErrnoException;
    public static native int ioctl_SIOCSIFMTU(String device,int mtu) throws libc.ErrnoException;

    public static native long[] clock_gettime(int clockId);
    public static long clock_getTime(int clockId) {
        long[] timespec=clock_gettime(clockId);
        return timespec[0]*1000000000L+timespec[1];
    }

}
