package org.it4y.jni;

import org.it4y.util.IndexNameMap;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luc on 12/31/13.
 */
public class libnetlink {

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
    }
}
