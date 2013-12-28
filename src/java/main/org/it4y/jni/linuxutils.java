package org.it4y.jni;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by luc on 12/28/13.
 */
public class linuxutils {
    //Load our native JNI lib
    static {
        JNILoader.loadLibrary("liblinuxutils.so");
    }

    public static native int setbooleanSockOption(int fd, int level, int option, boolean booleanValue);
    public static native int setuint16SockOption(int fd, int level, int option, int u16value);
    public static native int setuint32SockOption(int fd, int level, int option, int u32value);
    public static native int setstringSockOption(int fd, int level, int option, String s);

    public static native boolean getbooleanSockOption(int fd, int level, int option);
    public static native int getuint16SockOption(int fd, int level, int option);
    public static native int getuint32SockOption(int fd, int level, int option);
    public static native String getstringSockOption(int fd, int level, int option);

    public static native int getsockname(int fd);

    public static native InetSocketAddress getLocalHost();
}
