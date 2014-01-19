/*
 * This file is derived from Linux kernel headers
 * All copyrights preserved to original kernel author.
 *
 * more info see https://www.kernel.org/pub/linux/kernel/COPYING
 */

package org.it4y.jni;

public class tproxy {
    //Load our native JNI lib
    static {
        JNILoader.loadLibrary("libjnitproxy.so");
    }

    public int remoteIp;
    public int remotePort;

    public native int setIPTransparant(int fd);

    public native int getOriginalDestination(int fd);

}
