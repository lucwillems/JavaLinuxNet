package org.it4y.jni;

import java.nio.ByteBuffer;

/**
 * Created by luc on 2/17/14.
 */
public class libpcap {

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
        JNILoader.loadLibrary("libjnipcap.so");
        int initResult=initlib();
        if (initResult != JNI_OK) {
            throw new RuntimeException("Failed to initialize libpcap jni interface : "+initResult);
        };
    }

    //This method should be called first before using the library
    //it's used to initialize internal jni structure to speedup jni lookups
    private static native int initlib();

    //interface to accept messages from rtnl_listen()
    public interface pcap_instruction {
        public int bpf_instruction(int code,byte jt , byte jf , int k);
    }


    public static native int pcap_datalink_name_to_val(String datalink);
    public static native int pcap_compile_nopcap (int snaplen, int linktype, pcap_instruction callback , String filter, boolean optimize, int mask);
    public static native int pcap_offline_filter(byte[] bpf,int snapLen,int pktLen, ByteBuffer pkt);

}
