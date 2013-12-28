package org.it4y.jni;

/**
 * Created by luc on 12/28/13.
 */
public class linuxutils {
    //Load our native JNI lib
    static {
        JNILoader.loadLibrary("liblinuxutils.so");
    }

    public native int setbooleanSockOption(int fd, int level, int option, byte booleanValue);
    public native int setuint16SockOption(int fd, int level, int option, int u16value);
    public native int setuint32SockOption(int fd, int level, int option, int u32value);
    public native int setstringSockOption(int fd, int level, int option, String s);

    public native byte getbooleanSockOption(int fd, int level, int option);
    public native int getuint16SockOption(int fd, int level, int option);
    public native int getuint32SockOption(int fd, int level, int option);
    public native String getstringSockOption(int fd, int level, int option);

}
