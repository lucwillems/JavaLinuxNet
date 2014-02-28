package org.it4y.jni;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by luc on 2/17/14.
 */
public class libpcap {

    //some errorcodes from our jni methods
    public static final int JNI_OK = 0;
    public static final int JNI_ERROR = -1;
    public static final int JNI_ERR_FIND_CLASS_FAILED = -2;
    public static final int JNI_ERR_GET_METHOD_FAILED = -3;
    public static final int JNI_ERR_CALL_METHOD_FAILED = -4;
    public static final int JNI_ERR_BUFFER_TO_SMALL = -5;
    public static final int JNI_ERR_EXCEPTION = -6;


    //Load our native JNI lib
    static {
        //THIS requires libnl3 !!!!
        JNILoader.loadLibrary("libjnipcap.so");
        int initResult = initlib();
        if (initResult != JNI_OK) {
            throw new RuntimeException("Failed to initialize libpcap jni interface : " + initResult);
        }
        ;
    }

    //Class to hold a BPF instruction set in byte buffer
    public static class bpfPprogram {
        private ByteBuffer buffer = null;

        public bpfPprogram() {
        }

        public ByteBuffer init(final int nrInstructions) {
            buffer = ByteBuffer.allocateDirect(nrInstructions * 8); //8bytes per instructions
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            return buffer;
        }

        public void clear() {
            buffer.clear();
        }

        public boolean isValid() {
            return buffer != null & buffer.capacity() > 0 & libpcap.bpf_validate(this);
        }

        public int getSize() {
            return buffer.capacity();
        }

        public ByteBuffer getBuffer() {
            return buffer;
        }

        public int getInstructionCnt() {
            return getSize() / 8;
        }

    }

    //This method should be called first before using the library
    //it's used to initialize internal jni structure to speedup jni lookups
    private static native int initlib();

    /**
     * convert link name to internal libpcap id
     * @param datalink
     * @return
     */
    public static native int pcap_datalink_name_to_val(final String datalink);

    /**
     * native method to compile the filter, don't use this.
     * @param snaplen
     * @param linktype
     * @param program
     * @param filter
     * @param optimize
     * @param mask
     * @return
     */
    public static native int pcap_compile_nopcap(final int snaplen, final int linktype, final bpfPprogram program, final String filter, final boolean optimize, final int mask);

    /**
     * Compile given filter expression info a BPF program class
     * @param linktype
     * @param program
     * @param filter
     * @return
     *
     * we assert(program !=null) else jvm can crash !
     */
    public static int pcap_compile(final int linktype, final bpfPprogram program, final String filter) {
        assert(program !=null);
        return pcap_compile_nopcap(65535, linktype, program, filter, true, 0);
    }

    /**
     * native libpcap bpf_filter, don't call this directly
     * @param bpf
     * @param snapLen
     * @param pktLen
     * @param pkt
     * @return
     */
    public static native int bpf_filter(final ByteBuffer bpf, final int snapLen, final int pktLen, final ByteBuffer pkt);

    /**
     * run Berkeley Packet filter on given packet
     * use this method. bpf and pkt can not be NULL !!
     * @param bpf
     * @param pkt
     * @return true for match
     *
     *
     * we assert(bpf !=null) & assert(pkt != null) else jvm can crash !
     */
    public static boolean bpf_filter(final ByteBuffer bpf, final ByteBuffer pkt) {
        assert(bpf != null);
        assert(pkt != null);
        return bpf_filter(bpf, bpf.capacity(), pkt.capacity(), pkt) > 0;
    }

    /**
     * native verify bpf program , don't use this
     * @param bpf
     * @param len
     * @return
     */
    public static native int bpf_validate(ByteBuffer bpf, int len);

    /**
     * Verify bpf program for valid instruction and result
     * @param program
     * @return
     *
     * we assert(program !=null) else jvm can crash !
     */
    public static boolean bpf_validate(final bpfPprogram program) {
        assert(program !=null);
        return bpf_validate(program.getBuffer(), program.getInstructionCnt()) > 0;
    }
}
