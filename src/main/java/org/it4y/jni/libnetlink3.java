
/*
 * This file is derived from Linux kernel headers
 * All copyrights preserved to original kernel author.
 *
 * more info see https://www.kernel.org/pub/linux/kernel/COPYING
 */

package org.it4y.jni;

import java.nio.ByteBuffer;

public class libnetlink3 {

    //Load our native JNI lib
    static {
        //THIS requires libnl3 !!!!
        JNILoader.loadLibrary("libjninetlink3.so");
    }


    //return code to send from accept interface
    public static int rtl_accept_CONTINUE = 0;
    public static int rtl_accept_STOP = -1;
    public static int rtl_accept_FAILED_NOMESSAGEBUFFER = -2;
    public static int rtl_accept_FAILED_NOLISTENER = -3;
    public static int rtl_accept_FAILED_BUFFERTOSMALL = -4;
    public static int rtl_accept_FAILED_JVMFAILURE = -5;

    //interface to accept messages from rtnl_listen()
    public interface rtnl_accept {
        public int accept(ByteBuffer message);
    }

    //From include
    public static class utils {
        public static int nl_mgrp(int group) {
            return group > 0 ? (1 << (group - 1)) : 0;
        }
    }

    //from include <libnetlink.h>
    public static class rtnl_handle {
        public static final int SIZE = 36;
        public byte[] handle = new byte[SIZE];

        public rtnl_handle() {
        }

        public ByteBuffer toByteBuffer() {
            return ByteBuffer.wrap(handle);
        }

        private void clear() {
            for(int i=0;i<handle.length;i++) {
                handle[i]=0;
            }
        }

    }

    //libnet routing stuff
    /*
     * Open  a rtnetlink socket and save the state into the rth handle.
     * This handle is passed to all subsequent calls.  subscriptions is
     * a  bitmap of the rtnetlink multicast groups the socket will be a
     * member of.
     */
    private static native int rtnl_open(byte[] handle, int subscriptions);

    public static int rtnl_open(rtnl_handle handler, int subscriptions) {
        return rtnl_open(handler.handle, subscriptions);
    }

    /*
     * Open  a rtnetlink socket and save the state into the rth handle.
     * This handle is passed to all subsequent calls.  subscriptions is
     * a  bitmap of the rtnetlink multicast groups the socket will be a
     * member of.
     */
    private static native int rtnl_open_byproto(byte[] handle, int subscriptions, int protocol);

    public static int rtnl_open_byproto(rtnl_handle handler, int subscriptions, int protocol) {
        return rtnl_open_byproto(handler.handle, subscriptions, protocol);
    }

    /*
     * close given rtnl handle
     */
    private static native void rtnl_close(byte[] handle);

    public static void rtnl_close(rtnl_handle handler) {
        rtnl_close(handler.handle);
        handler.clear();
    }


    public static native int rtnl_wilddump_request(byte[] handle, int family, int type);

    public static int rtnl_wilddump_request(rtnl_handle handle, int family, int type) {
        return rtnl_wilddump_request(handle.handle, family, type);
    }

    public static native int rtnl_send(byte[] handle, ByteBuffer buf, int len);

    public static native int rtnl_dump_request(byte[] handle, int type, ByteBuffer req, int len);

    private static native int rtnl_listen(byte[] handle, ByteBuffer buf, rtnl_accept listener);

    public static int rtnl_listen(rtnl_handle handle, ByteBuffer buf, rtnl_accept listener) {
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
