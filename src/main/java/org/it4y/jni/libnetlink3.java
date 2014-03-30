
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

import java.nio.ByteBuffer;

public class libnetlink3 {

    //some errorcodes from our jni methods
    public static final int JNI_OK=0;
    public static final int JNI_ERROR=-1;
    public static final int JNI_ERR_FIND_CLASS_FAILED=-2;
    public static final int JNI_ERR_GET_METHOD_FAILED=-3;
    public static final int JNI_ERR_CALL_METHOD_FAILED=-4;
    public static final int JNI_ERR_BUFFER_TO_SMALL=-5;
    public static final int JNI_ERR_EXCEPTION=-6;


    //Load our native JNI lib
    static {
        //THIS requires libnl3 !!!!
        JNILoader.loadLibrary("libjninetlink3");
        final int initResult=initlib();
        if (initResult != JNI_OK) {
            throw new RuntimeException("Failed to initialize libnet3 jni interface : "+initResult);
        }
    }


    //return code to send from accept interface
    public static final int rtl_accept_CONTINUE=0;
    public static final int rtl_accept_STOP = -1;
    public static int rtl_accept_FAILED_NOMESSAGEBUFFER = -2;
    public static int rtl_accept_FAILED_NOLISTENER = -3;
    public static int rtl_accept_FAILED_BUFFERTOSMALL = -4;
    public static int rtl_accept_FAILED_JVMFAILURE = -5;

    //interface to accept messages from rtnl_listen()
    public interface rtnl_accept {
        int accept(ByteBuffer message);
    }

    //From include
    public static class utils {
        public static int nl_mgrp(final int group) {
            return group > 0 ? (1 << (group - 1)) : 0;
        }
    }

    //from include <libnetlink.h>
    //struct rtnl_handle
    //{
    //    int			fd;
    //    struct sockaddr_nl	local;
    //    struct sockaddr_nl	peer;
    //    uint32_t		seq;
    //    uint32_t		dump;
    //};

    public static class rtnl_handle {
        public static final int SIZE = 36;
        public final byte[] handle = new byte[SIZE];

        public int getFd() {
            return (handle[0]<<24)&0xff000000|(handle[1]<<16)&0xff0000|(handle[2]<<8)&0xff00|(handle[3]<<0)&0xff;
        }
        //TODO
        //to complex for now
        //public int getLocal() {
        //    return -1;
        //}
        //TODO
        //to complex for now
        //public int getPeer() {
        //    return -1;
        //}
        public int getSeq() {
            return (handle[28]<<24)&0xff000000|(handle[29]<<16)&0xff0000|(handle[30]<<8)&0xff00|(handle[31]<<0)&0xff;
        }

        public int getDump() {
            return (handle[32]<<24)&0xff000000|(handle[33]<<16)&0xff0000|(handle[34]<<8)&0xff00|(handle[35]<<0)&0xff;
        }
    }

    //This method should be called first before using the library
    //it's used to initialize internal jni structure to speedup jni lookups
    private static native int initlib();

    //libnet routing stuff
    /*
     * Open  a rtnetlink socket and save the state into the rth handle.
     * This handle is passed to all subsequent calls.  subscriptions is
     * a  bitmap of the rtnetlink multicast groups the socket will be a
     * member of.
     */
    private static native int rtnl_open(byte[] handle, int subscriptions);

    public static int rtnl_open(final rtnl_handle handler, final int subscriptions) {
        return rtnl_open(handler.handle, subscriptions);
    }

    /*
     * Open  a rtnetlink socket and save the state into the rth handle.
     * This handle is passed to all subsequent calls.  subscriptions is
     * a  bitmap of the rtnetlink multicast groups the socket will be a
     * member of.
     */
    private static native int rtnl_open_byproto(byte[] handle, int subscriptions, int protocol);

    public static int rtnl_open_byproto(final rtnl_handle handler, final int subscriptions, final int protocol) {
        return rtnl_open_byproto(handler.handle, subscriptions, protocol);
    }

    /*
     * close given rtnl handle
     */
    private static native void rtnl_close(byte[] handle);

    public static void rtnl_close(final rtnl_handle handler) {
        rtnl_close(handler.handle);
    }


    public static native int rtnl_wilddump_request(byte[] handle, int family, int type);

    public static int rtnl_wilddump_request(final rtnl_handle handle, final int family, final int type) {
        return rtnl_wilddump_request(handle.handle, family, type);
    }

    public static native int rtnl_send(byte[] handle, ByteBuffer buf, int len);

    public static native int rtnl_dump_request(byte[] handle, int type, ByteBuffer req, int len);

    private static native int rtnl_listen(byte[] handle, ByteBuffer buf, rtnl_accept listener);

    public static int rtnl_listen(final rtnl_handle handle, final ByteBuffer buf, final rtnl_accept listener) {
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
