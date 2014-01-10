/*
 * This file is derived from GNU libc headers
 * All copyrights preserved to original kernel author.
 *
 * more info see http://www.gnu.org/software/libc/
 */
package org.it4y.jni;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

public final class libc {

    /* i'm on littleendian ? */
    static final boolean isLittleEndian=ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN);

    /*
     * this class wraps libc in_addr (ipv4 address) from C lib
     *
     */

    public static int ntol(int address) {
        int[] ab=new int[4];
        if (isLittleEndian) {
            ab[0] = ((address >> 24) & 0x00ff);
            ab[1] = ((address >> 16) & 0x00ff);
            ab[2] = ((address >> 8) & 0x00ff);
            ab[3] = (address & 0x00ff);
            return (ab[3]<<24)+(ab[2]<<16)+(ab[1]<<8)+(ab[0]);
        } else {
           return address;
        }
    }

    public static InetAddress toInetAddress(int address) {
        InetAddress x = null;
        byte[] ab = new byte[4];
        ab[0] = (byte) ((address >> 24) & 0x00ff);
        ab[1] = (byte) ((address >> 16) & 0x00ff);
        ab[2] = (byte) ((address >> 8) & 0x00ff);
        ab[3] = (byte) (address & 0x00ff);
        //Why throw this will it is not going to happen ?
        try {
            x = Inet4Address.getByAddress(ab);
        } catch (UnknownHostException ignore) {
        }
        return x;
    }

    public static class in_addr {
        public int address = 0;
        private InetAddress cached_address = null;

        public in_addr() {
        }

        public in_addr(int address) {
            this.address = address;
        }

        public InetAddress toInetAddress() {
            if (cached_address == null) {
                synchronized (this) {
                    if (cached_address == null) {
                        /* ok java uses shitty constructions so we need to hack here */
                        /* Inet4Address.getByAddress() will do reverse of this. */
                        /* a private constructor(int address,int port) exist but it is private */
                        cached_address = libc.toInetAddress(address);
                    }
                }
            }
            return cached_address;
        }

        public String toString() {
            return String.format("0x%08x",(address & 0xffffffff));
        }
    }

    /*
     * this class wraps libc sockaddr_in structure from C lib
     *
     */
    public static class sockaddr_in {
        public int family;
        public int port;
        public int address;

        //cache this result as it is expensive
        private InetAddress cached_address = null;

        //Used by jni code
        private sockaddr_in() {}

        public sockaddr_in(int address, int port, int family) {
            this.address = address;
            this.port = port;
            this.family = family;
        }

        public InetAddress toInetAddress() {
            if (cached_address == null) {
                synchronized (this) {
                    if (cached_address == null) {
                        /* ok java uses shitty constructions so we need to hack here */
                        /* Inet4Address.getByAddress() will do reverse of this. */
                        /* a private constructor(int address,int port) exist but it is private */
                        cached_address = libc.toInetAddress(address);
                    }
                }
            }
            return cached_address;
        }

        public InetSocketAddress toInetSocketAddress() {
            return new InetSocketAddress(toInetAddress(), port);
        }

        public String toString() {
            return String.format("0x%08x:%d",(address & 0xffffffff),(port &0xffff));
        }


    }

    /* based on kernel 3.12 */
    public static class tcp_info {
        public static final int struct_size_latest = 104; //todo check this in 2.6 kernels

        public byte tcpi_state;        //u8
        public byte tcpi_ca_state;     //u8
        public byte tcpi_retransmits;  //u8
        public byte tcpi_probes;       //u8
        public byte tcpi_backoff;      //u8
        public byte tcpi_options;      //u8
        public byte tcpi_snd_wscale;   //u8
        public byte tcpi_rcv_wscale;   //u8

        public int tcpi_rto;          //u32
        public int tcpi_ato;          //u32
        public int tcpi_snd_mss;      //u32
        public int tcpi_rcv_mss;      //u32
        public int tcpi_unacked;      //u32
        public int tcpi_sacked;       //u32
        public int tcpi_lost;         //u32
        public int tcpi_retrans;      //u32
        public int tcpi_fackets;      //u32

        /* Times. */
        public int tcpi_last_data_sent;     //u32
        public int tcpi_last_ack_sent;      //u32
        public int tcpi_last_data_recv;     //u32
        public int tcpi_last_ack_recv;      //u32

        /* Metrics. */
        public int tcpi_pmtu;             //u32
        public int tcpi_rcv_ssthresh;     //u32
        public int tcpi_rtt;              //u32
        public int tcpi_rttvar;           //u32
        public int tcpi_snd_ssthresh;     //u32
        public int tcpi_snd_cwnd;         //u32
        public int tcpi_advmss;           //u32
        public int tcpi_reordering;       //u32
        public int tcpi_rcv_rtt;          //u32
        public int tcpi_rcv_space;         //u32

        /* counters */
        int tcpi_total_retrans;           //u32

        public tcp_info() {
        }

        /* this method is called by jni code to set data */
        /*            Signature: (BBBBBBBBIIIIIIIIIIIIIIIIIIIIIIII)V */

        private void setdata(
                byte tcpi_state,
                byte tcpi_ca_state,
                byte tcpi_retransmits,
                byte tcpi_probes,
                byte tcpi_backoff,
                byte tcpi_options,
                byte tcpi_snd_wscale,
                byte tcpi_rcv_wscale,
                int tcpi_rto,
                int tcpi_ato,
                int tcpi_snd_mss,
                int tcpi_rcv_mss,
                int tcpi_unacked,
                int tcpi_sacked,
                int tcpi_lost,
                int tcpi_retrans,
                int tcpi_fackets,
                int tcpi_last_data_sent,
                int tcpi_last_ack_sent,
                int tcpi_last_data_recv,
                int tcpi_last_ack_recv,
                int tcpi_pmtu,
                int tcpi_rcv_ssthresh,
                int tcpi_rtt,
                int tcpi_rttvar,
                int tcpi_snd_ssthresh,
                int tcpi_snd_cwnd,
                int tcpi_advmss,
                int tcpi_reordering,
                int tcpi_rcv_rtt,
                int tcpi_rcv_space,
                int tcpi_total_retrans
        ) {
            //System.out.print("setData");
            this.tcpi_state = tcpi_state;
            this.tcpi_ca_state = tcpi_ca_state;
            this.tcpi_retransmits = tcpi_retransmits;
            this.tcpi_probes = tcpi_probes;
            this.tcpi_backoff = tcpi_backoff;
            this.tcpi_options = tcpi_options;
            this.tcpi_snd_wscale = tcpi_snd_wscale;
            this.tcpi_rcv_wscale = tcpi_rcv_wscale;
            this.tcpi_rto = tcpi_rto;         //usec
            this.tcpi_ato = tcpi_ato;         //usec ack timeout
            this.tcpi_snd_mss = tcpi_snd_mss;
            this.tcpi_rcv_mss = tcpi_rcv_mss;
            this.tcpi_unacked = tcpi_unacked;
            this.tcpi_sacked = tcpi_sacked;
            this.tcpi_lost = tcpi_lost;
            this.tcpi_retrans = tcpi_retrans;
            this.tcpi_fackets = tcpi_fackets;
            this.tcpi_last_data_sent = tcpi_last_data_sent;   //msec since start
            this.tcpi_last_ack_sent = tcpi_last_ack_sent;     //
            this.tcpi_last_data_recv = tcpi_last_data_recv;   //msec
            this.tcpi_last_ack_recv = tcpi_last_ack_recv;     //msec
            this.tcpi_pmtu = tcpi_pmtu;
            this.tcpi_rcv_ssthresh = tcpi_rcv_ssthresh;
            this.tcpi_rtt = tcpi_rtt;    //usec
            this.tcpi_rttvar = tcpi_rttvar;    //usec
            this.tcpi_snd_ssthresh = tcpi_snd_ssthresh;
            this.tcpi_snd_cwnd = tcpi_snd_cwnd;
            this.tcpi_advmss = tcpi_advmss;
            this.tcpi_reordering = tcpi_reordering;
            this.tcpi_rcv_rtt = tcpi_rcv_rtt;       //usec
            this.tcpi_rcv_space = tcpi_rcv_space;
            this.tcpi_total_retrans = tcpi_total_retrans;  //total retransmits

        }

        public String toString() {
            StringBuffer s = new StringBuffer();
            s.append("state:").append(tcpi_state).append(" ");
            s.append("ca_state:").append(tcpi_ca_state).append("\n ");
            s.append("retransmits:").append(tcpi_retransmits).append(" ");
            s.append("probes:").append(tcpi_probes).append(" ");
            s.append("backoff:").append(tcpi_backoff).append(" ");
            s.append("options:").append(tcpi_options).append("\n ");
            s.append("snd_wscale:").append(tcpi_snd_wscale).append(" ");
            s.append("rcv_wscale:").append(tcpi_rcv_wscale).append(" ");
            s.append("rto:").append(tcpi_rto).append(" ");
            s.append("ato:").append(tcpi_ato).append(" ");
            s.append("snd_mss:").append(tcpi_snd_mss).append(" ");
            s.append("rcv_mss:").append(tcpi_rcv_mss).append("\n ");
            s.append("unacked:").append(tcpi_unacked).append(" ");
            s.append("sacked:").append(tcpi_sacked).append(" ");
            s.append("lost:").append(tcpi_lost).append(" ");
            s.append("retrans:").append(tcpi_retrans).append(" ");
            s.append("fackets:").append(tcpi_fackets).append("\n ");
            s.append("last_data_sent:").append(tcpi_last_data_sent).append(" ");
            s.append("last_ack_sent:").append(tcpi_last_ack_sent).append("\n ");
            s.append("last_data_recv:").append(tcpi_last_data_recv).append(" ");
            s.append("last_ack_recv:").append(tcpi_last_ack_recv).append("\n ");
            s.append("pmtu:").append(tcpi_pmtu).append(" ");
            s.append("rcv_ssthresh:").append(tcpi_rcv_ssthresh).append(" ");
            s.append("rtt:").append(tcpi_rtt).append(" ");
            s.append("rttvar:").append(tcpi_rttvar).append(" ");
            s.append("snd_ssthresh:").append(tcpi_snd_ssthresh).append(" ");
            s.append("snd_cwnd:").append(tcpi_snd_cwnd).append(" ");
            s.append("advmss:").append(tcpi_advmss).append("\n ");
            s.append("reordering:").append(tcpi_reordering).append(" ");
            s.append("rcv_rtt:").append(tcpi_rcv_rtt).append(" ");
            s.append("rcv_space:").append(tcpi_rcv_space).append("\n ");
            s.append("total_retrans:").append(tcpi_total_retrans);
            return s.toString();
        }
    }

    /*
     * Throw errno results with exceptions
     */
    public static class ErrnoException extends Exception {

        private int errno;

        public ErrnoException(int errno) {
            super();
            this.errno = errno;
        }

        public ErrnoException(String message, int errno) {
            super(message);
            this.errno = errno;
        }

        public int getErrno() {
            return this.errno;
        }

        public String toString() {
            return this.getMessage() + " errno: " + Integer.toString(errno);
        }
    }

}
