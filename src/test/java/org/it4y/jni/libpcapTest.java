package org.it4y.jni;

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
        Assert.assertEquals(pcap.DLT_IPV4, libpcap.pcap_datalink_name_to_val("IPv4"));
        Assert.assertEquals(pcap.DLT_RAW, libpcap.pcap_datalink_name_to_val("RAW"));
        Assert.assertEquals(pcap.DLT_IPV6, libpcap.pcap_datalink_name_to_val("IPv6"));
    }

    @Test
    public void testPcap_compile() throws Exception {
        int linkType = pcap.DLT_IPV4;
        String filter = "host 8.8.8.8";
        libpcap.bpfPprogram program = new libpcap.bpfPprogram();
        //compile
        int result = libpcap.pcap_compile_nopcap(65536, linkType, program, filter, true, 0);
        Assert.assertEquals(0, result);
        //this is based on bpf_compile result
        Assert.assertNotNull(program.getBuffer());
        Assert.assertTrue(program.isValid());
        System.out.println(bytesToHex(program.getBuffer(), 7 * 8));

        //First instruction
        Assert.assertEquals((short) 0, program.getBuffer().getShort());//inst
        Assert.assertEquals((byte) 0, program.getBuffer().get());//jt
        Assert.assertEquals((byte) 0, program.getBuffer().get());//jf
        Assert.assertEquals(0, program.getBuffer().getInt());//k
        //second instruction
        Assert.assertEquals((short) 0x20, program.getBuffer().getShort());//inst
        Assert.assertEquals((byte) 0x0, program.getBuffer().get());//jt
        Assert.assertEquals((byte) 0x0, program.getBuffer().get());//jf
        Assert.assertEquals(0x0c, program.getBuffer().getInt());//k
        //third instruction
        Assert.assertEquals((short) 0x15, program.getBuffer().getShort());//inst
        Assert.assertEquals((byte) 0x02, program.getBuffer().get());//jt
        Assert.assertEquals((byte) 0x00, program.getBuffer().get());//jf
        Assert.assertEquals(0x08080808, program.getBuffer().getInt());//k
        //Fourth instruction
        Assert.assertEquals((short) 0x20, program.getBuffer().getShort());//inst
        Assert.assertEquals((byte) 0x0, program.getBuffer().get());//jt
        Assert.assertEquals((byte) 0x0, program.getBuffer().get());//jf
        Assert.assertEquals(0x10, program.getBuffer().getInt());//k
    }

    @Test
    public void testPcap_compileBadFilter() throws Exception {
        int linkType = pcap.DLT_IPV4;
        String filter = "hostandshit 8.8.8.8";
        libpcap.bpfPprogram program = new libpcap.bpfPprogram();
        //compile
        Assert.assertEquals(-1,libpcap.pcap_compile(linkType, program, filter));
    }

    @Test
    public void testPcap_validate_filter() throws Exception {
        int linkType = pcap.DLT_IPV4;
        String filter = "host 8.8.8.8";
        libpcap.bpfPprogram program = new libpcap.bpfPprogram();
        //compile
        Assert.assertEquals(0,libpcap.pcap_compile(linkType, program, filter));
        //validate
        Assert.assertEquals(true, libpcap.bpf_validate(program));
        //validate
        Assert.assertEquals(true,program.isValid());
    }

    @Test
    public void testPcap_bpf_filter() throws Exception {
        int linkType = pcap.DLT_IPV4;
        ByteBuffer zerroPkt = ByteBuffer.allocateDirect(1500);
        zerroPkt.clear();
        String filter = "host 8.8.8.8";
        libpcap.bpfPprogram program = new libpcap.bpfPprogram();
        //compile
        int result = libpcap.pcap_compile_nopcap(65536, linkType, program, filter, true, 0);
        //this is based on bpf_compile result
        Assert.assertNotNull(program.getBuffer());
        Assert.assertTrue(program.isValid());
        System.out.println(program.getInstructionCnt());
        System.out.println(bytesToHex(program.getBuffer(), program.getInstructionCnt() * 8));

        //First instruction
        Assert.assertEquals((short) 0, program.getBuffer().getShort());//inst
        Assert.assertEquals((byte) 0, program.getBuffer().get());//jt
        Assert.assertEquals((byte) 0, program.getBuffer().get());//jf
        Assert.assertEquals(0, program.getBuffer().getInt());//k
        //second instruction
        Assert.assertEquals((short) 0x20, program.getBuffer().getShort());//inst
        Assert.assertEquals((byte) 0x0, program.getBuffer().get());//jt
        Assert.assertEquals((byte) 0x0, program.getBuffer().get());//jf
        Assert.assertEquals(0x0c, program.getBuffer().getInt());//k
        Assert.assertEquals(0, result);

        //must be false
        Assert.assertEquals(0,libpcap.bpf_filter(program.getBuffer(), 1500, 1500, zerroPkt));

        ByteBuffer pingPkt1=getPingPacket(100,0x08080808,0x08080808);
        ByteBuffer pingPkt2=getPingPacket(100,0x01020304,0x08080808);
        ByteBuffer pingPkt3=getPingPacket(100,0x08080808,0x01020304);
        ByteBuffer pingPkt4=getPingPacket(100,0x01020304,0x05060708);
        Assert.assertEquals(65536,libpcap.bpf_filter(program.getBuffer(), 1500, 1500, pingPkt1));//true
        Assert.assertEquals(65536,libpcap.bpf_filter(program.getBuffer(), 1500, 1500, pingPkt2));//true
        Assert.assertEquals(65536,libpcap.bpf_filter(program.getBuffer(), 1500, 1500, pingPkt3));//true
        Assert.assertEquals(0,libpcap.bpf_filter(program.getBuffer(), 1500, 1500, pingPkt4));//false

    }

    @Test
    public void testPcap_offline_filterNullProgram() throws Exception {
        ByteBuffer pingPkt1=getPingPacket(100,0x08080808,0x08080808);
        try {
            libpcap.bpf_filter(null, pingPkt1);
        } catch (Throwable t) {
            Assert.assertEquals(t.getClass(),AssertionError.class);//failure
        }

    }

    @Test
    public void testPcap_offline_filterNullPacket() throws Exception {
        int linkType = pcap.DLT_IPV4;
        String filter = "host 8.8.8.8";
        libpcap.bpfPprogram program = new libpcap.bpfPprogram();
        //compile
        final int result = libpcap.pcap_compile_nopcap(65536, linkType, program, filter, true, 0);
        try {
            libpcap.bpf_filter(program.getBuffer(), null);//failure
        } catch (final Throwable t) {
            Assert.assertEquals(t.getClass(),AssertionError.class);
        }
    }

    @Test
    public void testPcap_offline_Performance() throws Exception {
        int linkType = pcap.DLT_IPV4;
        String filter = "host 8.8.8.8";
        libpcap.bpfPprogram program = new libpcap.bpfPprogram();
        //compile
        int result = libpcap.pcap_compile_nopcap(65536, linkType, program, filter, true, 0);
        ByteBuffer pingPkt1=getPingPacket(100,0x08080808,0x08080808);
        long start=System.currentTimeMillis();
        long total=100000000;
        for(int x=0;x<total;x++){
            libpcap.bpf_filter(program.getBuffer(), 1500, 1500,pingPkt1);//ok
        }
        long stop=System.currentTimeMillis();
        System.out.println("time: "+(stop-start)+"msec");
        System.out.println("time: "+String.format("%1.10f",((double)(stop-start))/total)+" msec per test");
    }

    public static byte[] toBytes(int i)
    {
        byte[] result = new byte[4];
        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i /*>> 0*/);

        return result;
    }
    public static ByteBuffer getIPPacket(int len, byte protocol, int srcIp, int dstIp) {
        //create UDP packet
        ByteBuffer ipPacket = ByteBuffer.allocateDirect(len);
        ipPacket.put((byte) 0x45);  //ipv4 , size header
        ipPacket.put((byte) 0x00);  //tos
        ipPacket.putShort((short) (len & 0xffff));  //len=64
        ipPacket.putShort((short) 0x00);  //identification=0
        ipPacket.putShort((short) 0x00);  //flags/fragment
        ipPacket.put((byte) 0x40);  //TTL=64
        ipPacket.put((byte) protocol);  //protocol
        ipPacket.putShort((short) 0x00);  //checksum
        return ipPacket;
    }

    public static ByteBuffer getUDPPacket(int len, int srcIp, int dstIp, int srcPort, int dstPort) {
        //create UDP packet
        ByteBuffer udpPacket = ByteBuffer.allocateDirect(71);
        //based on wireshark dump of dns request
        //ip header
        udpPacket.put((byte) 0x45);
        udpPacket.put((byte) 0x00);
        udpPacket.put((byte) 0x00);
        udpPacket.put((byte) 0x47);
        udpPacket.put((byte) 0x0a);
        udpPacket.put((byte) 0x5b);
        udpPacket.put((byte) 0x00);
        udpPacket.put((byte) 0x00);
        udpPacket.put((byte) 0x40);
        udpPacket.put((byte) 0x11);
        udpPacket.put((byte) 0x9f);
        udpPacket.put((byte) 0x03);
        udpPacket.put((byte) 0xc0);
        udpPacket.put((byte) 0xa8);
        udpPacket.put((byte) 0x00);
        udpPacket.put((byte) 0x90);
        udpPacket.put((byte) 0x08);
        udpPacket.put((byte) 0x08);
        udpPacket.put((byte) 0x08);
        udpPacket.put((byte) 0x08);

        //udp header
        udpPacket.put((byte) 0xa0);
        udpPacket.put((byte) 0x00);
        udpPacket.put((byte) 0x00);
        udpPacket.put((byte) 0x35);
        udpPacket.put((byte) 0x00);
        udpPacket.put((byte) 0x33);
        udpPacket.put((byte) 0x49);
        udpPacket.put((byte) 0xa7);

        //DNS query
        udpPacket.position(0); //make sure position=0;
        return udpPacket;
    }

    public static ByteBuffer getPingPacket(int len, int srcIp, int dstIp) {
        //create UDP packet
        ByteBuffer pingPacket = ByteBuffer.allocateDirect(71);
        //based on wireshark dump of dns request
        //ip header
        pingPacket.put((byte) 0x45);
        pingPacket.put((byte) 0x00);
        pingPacket.put((byte) 0x00);
        pingPacket.put((byte) 0x47);
        pingPacket.put((byte) 0x0a);
        pingPacket.put((byte) 0x5b);
        pingPacket.put((byte) 0x00);
        pingPacket.put((byte) 0x00);
        pingPacket.put((byte) 0x40);
        pingPacket.put((byte) 0x01); //icmp
        pingPacket.put((byte) 0x9f);
        pingPacket.put((byte) 0x03);
        byte[] src=toBytes(srcIp);
        pingPacket.put((byte) src[0]);//src[0]
        pingPacket.put((byte) src[1]);//src[1]
        pingPacket.put((byte) src[2]);//src[2]
        pingPacket.put((byte) src[3]);//src[3]
        byte[] dst=toBytes(dstIp);
        pingPacket.put((byte) dst[0]);//dst[0]
        pingPacket.put((byte) dst[1]);//dst[1]
        pingPacket.put((byte) dst[2]);//dst[2]
        pingPacket.put((byte) dst[3]);//dst[3]

        //ICMP header
        pingPacket.put((byte) 0x00); //type
        pingPacket.put((byte) 0x00); //code
        pingPacket.put((byte) 0x00); //checksum 1
        pingPacket.put((byte) 0x35); //checksum 1
        pingPacket.put((byte) 0x00); //id
        pingPacket.put((byte) 0x01); //id
        pingPacket.put((byte) 0x49); //seq nummer
        pingPacket.put((byte) 0xa7); //seq nummer

        pingPacket.position(0); //make sure position=0;
        return pingPacket;
    }

    public static ByteBuffer getTCPPacket(int len, int srcIp, int dstIp, int srcPort, int dstPort) {
        //create UDP packet
        ByteBuffer tcpPacket = ByteBuffer.allocateDirect(71);
        //based on wireshark dump of dns request
        //ip header
        tcpPacket.put((byte) 0x45);
        tcpPacket.put((byte) 0x00);
        tcpPacket.put((byte) 0x00);
        tcpPacket.put((byte) 0x47);
        tcpPacket.put((byte) 0x0a);
        tcpPacket.put((byte) 0x5b);
        tcpPacket.put((byte) 0x00);
        tcpPacket.put((byte) 0x00);
        tcpPacket.put((byte) 0x40);
        tcpPacket.put((byte) 0x06); //TCP
        tcpPacket.put((byte) 0x9f);
        tcpPacket.put((byte) 0x03);
        tcpPacket.put((byte) 0xc0);
        tcpPacket.put((byte) 0xa8);
        tcpPacket.put((byte) 0x00);
        tcpPacket.put((byte) 0x90);
        tcpPacket.put((byte) 0x08);
        tcpPacket.put((byte) 0x08);
        tcpPacket.put((byte) 0x08);
        tcpPacket.put((byte) 0x08);

        //TCP header
        tcpPacket.put((byte) 0xa0);//src port 1
        tcpPacket.put((byte) 0x00);//src port 2
        tcpPacket.put((byte) 0x00);//dst port 1
        tcpPacket.put((byte) 0x35);//dst port 2

        tcpPacket.position(0);
        return tcpPacket;
    }

}
