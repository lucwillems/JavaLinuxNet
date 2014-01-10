/*
 * This file is derived from Linux kernel headers
 * All copyrights preserved to original kernel author.
 *
 * more info see https://www.kernel.org/pub/linux/kernel/COPYING
 */

package org.it4y.jni;

import java.nio.ByteBuffer;

/**
 * Created by luc on 12/28/13.
 * this class encapsulate the native methods for tuntap access
 * DO NOT REFACTORY this class our you are scruwed
 */

public class tuntap {
    /* Load libtuntap.so */
    static {
        JNILoader.loadLibrary("libjnituntap.so");
    }

    protected int fd = -1;
    protected String device = null;

    protected native int openTunDevice(String device);

    protected native int openTun();

    public native void close();

    public native void writeByteBuffer(ByteBuffer buffer, int len);

    public native int readByteBuffer(ByteBuffer buffer);
}
