package org.it4y.jni;

import org.it4y.jni.linux.netlink;
import org.it4y.jni.linux.pcap;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Created by luc on 2/17/14.
 */
public class libpcapTest {

     final protected static char[] hexArray = "0123456789ABCDEF ".toCharArray();

     public static String bytesToHex(final ByteBuffer bytes, final int maxSize) {
        final char[] hexChars = new char[Math.min(bytes.capacity(), maxSize) * 3];
        int v;
        for (int j = 0; j < Math.min(maxSize, bytes.capacity()); j++) {
            v = bytes.get(j) & 0xFF;
            hexChars[j * 3] = hexArray[v >>> 4];
            hexChars[j * 3 + 1] = hexArray[v & 0x0F];
            hexChars[j * 3 + 2] = hexArray[0x10];
        }
        return new String(hexChars);
    }

    @Test
    public void testPcap_datalink_name_to_val() throws Exception {
        Assert.assertEquals(pcap.DLT_IPV4,libpcap.pcap_datalink_name_to_val("IPv4"));
        Assert.assertEquals(pcap.DLT_RAW,libpcap.pcap_datalink_name_to_val("RAW"));
        Assert.assertEquals(pcap.DLT_IPV6,libpcap.pcap_datalink_name_to_val("IPv6"));
    }

    @Test
    public void testPcap_compile_nopcap() throws Exception {
        int linkType=pcap.DLT_IPV4;
        String filter="host 8.8.8.8";
        libpcap.bpfPprogram program = new libpcap.bpfPprogram();
        //compile
        int result=libpcap.pcap_compile_nopcap(65536,linkType,program,filter,true,0);
        Assert.assertEquals(0,result);
        //this is based on bpf_compile result
        Assert.assertNotNull(program.getBuffer());
        Assert.assertTrue(program.isValid());
        System.out.println(bytesToHex(program.getBuffer(),7*8));

        //First instruction
        Assert.assertEquals((short)0,program.getBuffer().getShort());//inst
        Assert.assertEquals((byte)0,program.getBuffer().get());//jt
        Assert.assertEquals((byte)0,program.getBuffer().get());//jf
        Assert.assertEquals(0,program.getBuffer().getInt());//k
        //second instruction
        Assert.assertEquals((short)0x20,program.getBuffer().getShort());//inst
        Assert.assertEquals((byte)0x0,program.getBuffer().get());//jt
        Assert.assertEquals((byte)0x0,program.getBuffer().get());//jf
        Assert.assertEquals(0x0c,program.getBuffer().getInt());//k
        //third instruction
        Assert.assertEquals((short)0x15,program.getBuffer().getShort());//inst
        Assert.assertEquals((byte)0x02,program.getBuffer().get());//jt
        Assert.assertEquals((byte)0x00,program.getBuffer().get());//jf
        Assert.assertEquals(0x08080808,program.getBuffer().getInt());//k
        //Fourth instruction
        Assert.assertEquals((short)0x20,program.getBuffer().getShort());//inst
        Assert.assertEquals((byte)0x0,program.getBuffer().get());//jt
        Assert.assertEquals((byte)0x0,program.getBuffer().get());//jf
        Assert.assertEquals(0x10,program.getBuffer().getInt());//k
    }

    @Test
    public void testPcap_offline_filter() throws Exception {

    }
}
