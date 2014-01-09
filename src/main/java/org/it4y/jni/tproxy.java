package org.it4y.jni;

/**
 * Created by luc on 12/28/13.
 */
public class tproxy {
    //Load our native JNI lib
    static {
        JNILoader.loadLibrary("libjnitproxy.so");
    }

    public int remoteIp;
    public int remotePort;

    public tproxy() {
    }

    public native int setIPTransparant(int fd);

    public native int getOriginalDestination(int fd);

}
