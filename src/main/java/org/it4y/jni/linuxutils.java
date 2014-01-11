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
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class linuxutils {
    //Load our native JNI lib
    static {
        //THIS requires libnl3 !!!!
        JNILoader.loadLibrary("libjnilinuxutils.so");
    }

    public static native void setbooleanSockOption(int fd, int level, int option, boolean booleanValue) throws libc.ErrnoException;

    public static void setbooleanSockOption(Socket s, int level, int option, boolean booleanValue) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(s);
        setbooleanSockOption(fd, level, option, booleanValue);
    }

    public static void setbooleanSockOption(ServerSocket s, int level, int option, boolean booleanValue) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(s);
        setbooleanSockOption(fd, level, option, booleanValue);
    }

    public static void setbooleanSockOption(ServerSocketChannel sc, int level, int option, boolean booleanValue) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(sc.socket());
        setbooleanSockOption(fd, level, option, booleanValue);
    }

    public static void setbooleanSockOption(SocketChannel sc, int level, int option, boolean booleanValue) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(sc.socket());
        setbooleanSockOption(fd, level, option, booleanValue);
    }

    /* note: int=4 bytes !!! */
    public static native void setintSockOption(int fd, int level, int option, int int32value) throws libc.ErrnoException;

    public static void setintSockOption(Socket s, int level, int option, int intvalue) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(s);
        setintSockOption(fd, level, option, intvalue);
    }

    public static void setintSockOption(ServerSocket s, int level, int option, int intvalue) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(s);
        setintSockOption(fd, level, option, intvalue);
    }

    public static void setintSockOption(SocketChannel sc, int level, int option, int intvalue) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(sc.socket());
        setintSockOption(fd, level, option, intvalue);
    }

    public static void setintSockOption(ServerSocketChannel sc, int level, int option, int intvalue) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(sc.socket());
        setintSockOption(fd, level, option, intvalue);
    }

    public static native void setstringSockOption(int fd, int level, int option, String s) throws libc.ErrnoException;

    public static void setstringSockOption(Socket s, int level, int option, String x) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(s);
        setstringSockOption(fd, level, option, x);
    }

    public static void setstringSockOption(ServerSocket s, int level, int option, String x) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(s);
        setstringSockOption(fd, level, option, x);
    }

    public static void setstringSockOption(SocketChannel sc, int level, int option, String x) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(sc.socket());
        setstringSockOption(fd, level, option, x);
    }

    public static void setstringSockOption(ServerSocketChannel sc, int level, int option, String x) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(sc.socket());
        setstringSockOption(fd, level, option, x);
    }


    public static native boolean getbooleanSockOption(int fd, int level, int option) throws libc.ErrnoException;

    public static native int getintSockOption(int fd, int level, int option) throws libc.ErrnoException;

    public static int getintSockOption(Socket s, int level, int option) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(s);
        return getintSockOption(fd, level, option);
    }

    public static int getintSockOption(ServerSocket s, int level, int option) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(s);
        return getintSockOption(fd, level, option);
    }

    public static int getintSockOption(SocketChannel sc, int level, int option) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(sc.socket());
        return getintSockOption(fd, level, option);
    }

    public static int getintSockOption(ServerSocketChannel sc, int level, int option) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(sc.socket());
        return getintSockOption(fd, level, option);
    }

    public static native String getstringSockOption(int fd, int level, int option) throws libc.ErrnoException;

    public static String getstringSockOption(Socket s, int level, int option) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(s);
        return getstringSockOption(fd, level, option);
    }

    public static String getstringSockOption(ServerSocket s, int level, int option) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(s);
        return getstringSockOption(fd, level, option);
    }

    public static String getstringSockOption(SocketChannel sc, int level, int option) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(sc.socket());
        return getstringSockOption(fd, level, option);
    }

    public static String getstringSockOption(ServerSocketChannel sc, int level, int option) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(sc.socket());
        return getstringSockOption(fd, level, option);
    }

    public static native libc.sockaddr_in getsockname(int fd) throws libc.ErrnoException;

    public static libc.sockaddr_in getsockname(Socket s) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(s);
        return getsockname(fd);
    }

    public static libc.sockaddr_in getsockname(ServerSocket s) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(s);
        return getsockname(fd);
    }

    public static libc.sockaddr_in getsockname(ServerSocketChannel sc) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(sc.socket());
        return getsockname(fd);
    }

    public static libc.sockaddr_in getsockname(SocketChannel sc) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(sc.socket());
        return getsockname(fd);
    }


    public static native int gettcpinfo(int fd, libc.tcp_info info) throws libc.ErrnoException;

    public static int gettcpinfo(Socket s, libc.tcp_info info) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(s);
        return gettcpinfo(fd, info);
    }

    public static int gettcpinfo(ServerSocket s, libc.tcp_info info) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(s);
        return gettcpinfo(fd, info);
    }

    public static int gettcpinfo(ServerSocketChannel sc, libc.tcp_info info) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(sc.socket());
        return gettcpinfo(fd, info);
    }

    public static int gettcpinfo(SocketChannel sc, libc.tcp_info info) throws libc.ErrnoException {
        int fd = SocketUtils.getFd(sc.socket());
        return gettcpinfo(fd, info);
    }

    public static native InetSocketAddress getLocalHost();

    //libnet routing stuff
    /*
     * Open  a rtnetlink socket and save the state into the rth handle.
     * This handle is passed to all subsequent calls.  subscriptions is
     * a  bitmap of the rtnetlink multicast groups the socket will be a
     * member of.
     */
    private static native int rtnl_open(byte[] handle, int subscriptions);

    public static int rtnl_open(libnetlink3.rtnl_handle handler, int subscriptions) {
        return rtnl_open(handler.handle, subscriptions);
    }

    /*
     * Open  a rtnetlink socket and save the state into the rth handle.
     * This handle is passed to all subsequent calls.  subscriptions is
     * a  bitmap of the rtnetlink multicast groups the socket will be a
     * member of.
     */
    private static native int rtnl_open_byproto(byte[] handle, int subscriptions, int protocol);

    public static int rtnl_open_byproto(libnetlink3.rtnl_handle handler, int subscriptions, int protocol) {
        return rtnl_open_byproto(handler.handle, subscriptions, protocol);
    }

    /*
     * close given rtnl handle
     */
    private static native void rtnl_close(byte[] handle);

    public static void rtnl_close(libnetlink3.rtnl_handle handler) {
        rtnl_close(handler.handle);
    }


    public static native int rtnl_wilddump_request(byte[] handle, int family, int type);

    public static int rtnl_wilddump_request(libnetlink3.rtnl_handle handle, int family, int type) {
        return rtnl_wilddump_request(handle.handle, family, type);
    }

    public static native int rtnl_send(byte[] handle, ByteBuffer buf, int len);

    public static native int rtnl_dump_request(byte[] handle, int type, ByteBuffer req, int len);

    private static native int rtnl_listen(byte[] handle, ByteBuffer buf, libnetlink3.rtnl_accept listener);

    public static int rtnl_listen(libnetlink3.rtnl_handle handle, ByteBuffer buf, libnetlink3.rtnl_accept listener) {
        return rtnl_listen(handle.handle, buf, listener);
    }

/*
    public static static int rtnl_dump_filter(struct rtnl_handle *rth,
                      int (*filter)(struct sockaddr_nl *, struct nlmsghdr *n, void *),
                      void *arg1,
                      int (*junk)(struct sockaddr_nl *,struct nlmsghdr *n, void *),
                      void *arg2)

       int rtnl_talk(struct rtnl_handle *rtnl, struct nlmsghdr *n, pid_t peer,
                  unsigned groups, struct nlmsghdr *answer,
                  int (*junk)(struct sockaddr_nl *,struct nlmsghdr *n, void *),
                  void *jarg)

       int rtnl_listen(struct rtnl_handle *rtnl,
                  int (*handler)(struct sockaddr_nl *,struct nlmsghdr *n, void *),
                  void *jarg)
*/


}
