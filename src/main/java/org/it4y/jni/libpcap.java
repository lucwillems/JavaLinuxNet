package org.it4y.jni;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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

    //Class to hold a BPF instruction set in byte buffer
    public static class bpfPprogram {
        private ByteBuffer buffer=null;

        public bpfPprogram() {}

        public ByteBuffer init(int nrInstructions) {
            buffer=ByteBuffer.allocateDirect(nrInstructions*8); //8bytes per instructions
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            return buffer;
        }

        public void clear() {
            buffer.clear();
        }

        public boolean isValid() {
            return  buffer !=null & buffer.capacity()>0;
        }

        public int getSize() {
            return buffer.capacity();
        }
        public ByteBuffer getBuffer() { return buffer;}

        public int getInstructionCnt() {
            return getSize()/8;
        }

    }

    //This method should be called first before using the library
    //it's used to initialize internal jni structure to speedup jni lookups
    private static native int initlib();
    public static native int pcap_datalink_name_to_val(String datalink);
    public static native int pcap_compile_nopcap (int snaplen, int linktype,bpfPprogram program, String filter, boolean optimize, int mask);
    public static native int pcap_offline_filter(ByteBuffer bpf,int snapLen,int pktLen, ByteBuffer pkt);

}
