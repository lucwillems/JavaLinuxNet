package org.it4y.net.protocols.IP.TCP;

import org.it4y.net.protocols.IP.ICMP.ICMPPacket;
import org.it4y.net.protocols.IP.IPFactory;
import org.it4y.net.protocols.IP.IpPacket;
import org.it4y.net.protocols.IP.UDP.UDPPacket;
import org.it4y.util.Counter;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by luc on 3/23/14.
 */
public class TCPPacketTest {
    Logger logger = LoggerFactory.getLogger(TCPPacket.class);
    /* Frame (74 bytes)
     * wget http://8.8.8.8 from 192.168.0.144
     * */
    static final byte[] tcp_sync = {
            (byte)0x45, (byte)0x00, /* 8/....E. */
            (byte)0x00, (byte)0x3c, (byte)0xd9, (byte)0xa5, (byte)0x40, (byte)0x00, (byte)0x40, (byte)0x06, /* .<..@.@. */
            (byte)0x8f, (byte)0xce, (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x90, (byte)0x08, (byte)0x08, /* ........ */
            (byte)0x08, (byte)0x08, (byte)0xdd, (byte)0x60, (byte)0x00, (byte)0x50, (byte)0xf5, (byte)0x73, /* ...`.P.s */
            (byte)0xd7, (byte)0xd3, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xa0, (byte)0x02, /* ........ */
            (byte)0x72, (byte)0x10, (byte)0x4a, (byte)0x39, (byte)0x00, (byte)0x00, (byte)0x02, (byte)0x04, /* r.J9.... */
            (byte)0x05, (byte)0xb4, (byte)0x04, (byte)0x02, (byte)0x08, (byte)0x0a, (byte)0x00, (byte)0xfd, /* ........ */
            (byte)0x0e, (byte)0x76, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x03, /* .v...... */
            (byte)0x03, (byte)0x0a                                      /* .. */
    };

    /**
     * Following is based on https://ntvcapi.21net.com/ntv-soa/status/alive request
     * the capture is stored under test/pcap
     */
    static final byte[] https1 = {
            (byte)0x45, (byte)0x00, /* 8/....E. */
            (byte)0x00, (byte)0x3c, (byte)0x75, (byte)0x1d, (byte)0x40, (byte)0x00, (byte)0x40, (byte)0x06, /* .<u.@.@. */
            (byte)0x2e, (byte)0x80, (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x90, (byte)0x25, (byte)0x94, /* ......%. */
            (byte)0xb0, (byte)0x52, (byte)0xd5, (byte)0x38, (byte)0x01, (byte)0xbb, (byte)0x07, (byte)0xa3, /* .R.8.... */
            (byte)0x2f, (byte)0x9f, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xa0, (byte)0x02, /* /....... */
            (byte)0x72, (byte)0x10, (byte)0xd4, (byte)0xbb, (byte)0x00, (byte)0x00, (byte)0x02, (byte)0x04, /* r....... */
            (byte)0x05, (byte)0xb4, (byte)0x04, (byte)0x02, (byte)0x08, (byte)0x0a, (byte)0x01, (byte)0x5a, /* .......Z */
            (byte)0x5a, (byte)0x82, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x03, /* Z....... */
            (byte)0x03, (byte)0x0a                                      /* .. */
    };

    static final byte[] https2 = {
            (byte)0x45, (byte)0x00, /* Kn;...E. */
            (byte)0x00, (byte)0x3c, (byte)0x00, (byte)0x00, (byte)0x40, (byte)0x00, (byte)0x35, (byte)0x06, /* .<..@.5. */
            (byte)0xae, (byte)0x9d, (byte)0x25, (byte)0x94, (byte)0xb0, (byte)0x52, (byte)0xc0, (byte)0xa8, /* ..%..R.. */
            (byte)0x00, (byte)0x90, (byte)0x01, (byte)0xbb, (byte)0xd5, (byte)0x38, (byte)0x50, (byte)0xff, /* .....8P. */
            (byte)0x9a, (byte)0x66, (byte)0x07, (byte)0xa3, (byte)0x2f, (byte)0xa0, (byte)0xa0, (byte)0x12, /* .f../... */
            (byte)0xff, (byte)0xff, (byte)0xfc, (byte)0x0e, (byte)0x00, (byte)0x00, (byte)0x02, (byte)0x04, /* ........ */
            (byte)0x05, (byte)0xb4, (byte)0x04, (byte)0x02, (byte)0x08, (byte)0x0a, (byte)0x49, (byte)0xcb, /* ......I. */
            (byte)0x15, (byte)0x7a, (byte)0x01, (byte)0x5a, (byte)0x5a, (byte)0x82, (byte)0x01, (byte)0x03, /* .z.ZZ... */
            (byte)0x03, (byte)0x0b                                      /* .. */
    };

    static final byte[] https3 = {
            (byte)0x45, (byte)0x00, /* 8/....E. */
            (byte)0x00, (byte)0x34, (byte)0x75, (byte)0x1e, (byte)0x40, (byte)0x00, (byte)0x40, (byte)0x06, /* .4u.@.@. */
            (byte)0x2e, (byte)0x87, (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x90, (byte)0x25, (byte)0x94, /* ......%. */
            (byte)0xb0, (byte)0x52, (byte)0xd5, (byte)0x38, (byte)0x01, (byte)0xbb, (byte)0x07, (byte)0xa3, /* .R.8.... */
            (byte)0x2f, (byte)0xa0, (byte)0x50, (byte)0xff, (byte)0x9a, (byte)0x67, (byte)0x80, (byte)0x10, /* /.P..g.. */
            (byte)0x00, (byte)0x1d, (byte)0x2a, (byte)0xb1, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x01, /* ..*..... */
            (byte)0x08, (byte)0x0a, (byte)0x01, (byte)0x5a, (byte)0x5a, (byte)0x93, (byte)0x49, (byte)0xcb, /* ...ZZ.I. */
            (byte)0x15, (byte)0x7a                                      /* .z */
    };

    static final byte[] https4 = {
            (byte)0x45, (byte)0x00, /* 8/....E. */
            (byte)0x02, (byte)0x39, (byte)0x75, (byte)0x1f, (byte)0x40, (byte)0x00, (byte)0x40, (byte)0x06, /* .9u.@.@. */
            (byte)0x2c, (byte)0x81, (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x90, (byte)0x25, (byte)0x94, /* ,.....%. */
            (byte)0xb0, (byte)0x52, (byte)0xd5, (byte)0x38, (byte)0x01, (byte)0xbb, (byte)0x07, (byte)0xa3, /* .R.8.... */
            (byte)0x2f, (byte)0xa0, (byte)0x50, (byte)0xff, (byte)0x9a, (byte)0x67, (byte)0x80, (byte)0x18, /* /.P..g.. */
            (byte)0x00, (byte)0x1d, (byte)0x0f, (byte)0xdd, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x01, /* ........ */
            (byte)0x08, (byte)0x0a, (byte)0x01, (byte)0x5a, (byte)0x5a, (byte)0x93, (byte)0x49, (byte)0xcb, /* ...ZZ.I. */
            (byte)0x15, (byte)0x7a, (byte)0x16, (byte)0x03, (byte)0x01, (byte)0x02, (byte)0x00, (byte)0x01, /* .z...... */
            (byte)0x00, (byte)0x01, (byte)0xfc, (byte)0x03, (byte)0x03, (byte)0x53, (byte)0x2e, (byte)0xc7, /* .....S.. */
            (byte)0x6e, (byte)0x62, (byte)0xdf, (byte)0x5c, (byte)0xca, (byte)0xe3, (byte)0xff, (byte)0xa9, /* nb.\.... */
            (byte)0xf9, (byte)0x62, (byte)0x91, (byte)0x2c, (byte)0xae, (byte)0xb4, (byte)0xe1, (byte)0x6a, /* .b.,...j */
            (byte)0xa5, (byte)0xe0, (byte)0xc1, (byte)0x84, (byte)0x83, (byte)0x77, (byte)0x68, (byte)0x06, /* .....wh. */
            (byte)0xce, (byte)0xd3, (byte)0x60, (byte)0x79, (byte)0x94, (byte)0x20, (byte)0x2e, (byte)0x04, /* ..`y. .. */
            (byte)0xe9, (byte)0x8c, (byte)0xfc, (byte)0x9b, (byte)0x18, (byte)0x04, (byte)0xe0, (byte)0x7f, /* ........ */
            (byte)0x36, (byte)0x85, (byte)0x49, (byte)0x29, (byte)0x11, (byte)0x26, (byte)0x22, (byte)0x8d, /* 6.I).&". */
            (byte)0x89, (byte)0x6c, (byte)0xd0, (byte)0xbe, (byte)0x65, (byte)0x49, (byte)0x34, (byte)0xb8, /* .l..eI4. */
            (byte)0xbd, (byte)0x4c, (byte)0x45, (byte)0xf9, (byte)0x0b, (byte)0xe5, (byte)0x00, (byte)0x24, /* .LE....$ */
            (byte)0xc0, (byte)0x2b, (byte)0xc0, (byte)0x2f, (byte)0x00, (byte)0x9e, (byte)0x00, (byte)0x9c, /* .+./.... */
            (byte)0xc0, (byte)0x0a, (byte)0xc0, (byte)0x14, (byte)0x00, (byte)0x39, (byte)0x00, (byte)0x35, /* .....9.5 */
            (byte)0xc0, (byte)0x07, (byte)0xc0, (byte)0x09, (byte)0xc0, (byte)0x11, (byte)0xc0, (byte)0x13, /* ........ */
            (byte)0x00, (byte)0x33, (byte)0x00, (byte)0x32, (byte)0x00, (byte)0x05, (byte)0x00, (byte)0x04, /* .3.2.... */
            (byte)0x00, (byte)0x2f, (byte)0x00, (byte)0x0a, (byte)0x01, (byte)0x00, (byte)0x01, (byte)0x8f, /* ./...... */
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x16, (byte)0x00, (byte)0x14, (byte)0x00, (byte)0x00, /* ........ */
            (byte)0x11, (byte)0x6e, (byte)0x74, (byte)0x76, (byte)0x63, (byte)0x61, (byte)0x70, (byte)0x69, /* .ntvcapi */
            (byte)0x2e, (byte)0x32, (byte)0x31, (byte)0x6e, (byte)0x65, (byte)0x74, (byte)0x2e, (byte)0x63, /* .21net.c */
            (byte)0x6f, (byte)0x6d, (byte)0xff, (byte)0x01, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, /* om...... */
            (byte)0x0a, (byte)0x00, (byte)0x08, (byte)0x00, (byte)0x06, (byte)0x00, (byte)0x17, (byte)0x00, /* ........ */
            (byte)0x18, (byte)0x00, (byte)0x19, (byte)0x00, (byte)0x0b, (byte)0x00, (byte)0x02, (byte)0x01, /* ........ */
            (byte)0x00, (byte)0x00, (byte)0x23, (byte)0x00, (byte)0xd0, (byte)0x81, (byte)0x5e, (byte)0x7a, /* ..#...^z */
            (byte)0x7b, (byte)0x4a, (byte)0xd4, (byte)0xdd, (byte)0x4c, (byte)0xe9, (byte)0xcd, (byte)0x41, /* {J..L..A */
            (byte)0x30, (byte)0xbe, (byte)0x0c, (byte)0x85, (byte)0x22, (byte)0x3a, (byte)0xc9, (byte)0xa8, /* 0...":.. */
            (byte)0xb0, (byte)0x2b, (byte)0x25, (byte)0x82, (byte)0xfb, (byte)0x68, (byte)0xfa, (byte)0xbb, /* .+%..h.. */
            (byte)0xfd, (byte)0x6a, (byte)0x69, (byte)0xcb, (byte)0x24, (byte)0xc5, (byte)0x8d, (byte)0x4d, /* .ji.$..M */
            (byte)0x59, (byte)0xa6, (byte)0xfb, (byte)0x7a, (byte)0xf2, (byte)0x4b, (byte)0x95, (byte)0x02, /* Y..z.K.. */
            (byte)0xca, (byte)0xb7, (byte)0xd7, (byte)0x2b, (byte)0x4b, (byte)0x5e, (byte)0x01, (byte)0xf3, /* ...+K^.. */
            (byte)0xd5, (byte)0x74, (byte)0xce, (byte)0x8b, (byte)0x2f, (byte)0x93, (byte)0x9c, (byte)0xb9, /* .t../... */
            (byte)0x85, (byte)0xe6, (byte)0xa5, (byte)0x0d, (byte)0x51, (byte)0x56, (byte)0x73, (byte)0x79, /* ....QVsy */
            (byte)0x5f, (byte)0x60, (byte)0xe1, (byte)0xf7, (byte)0x6c, (byte)0x94, (byte)0x81, (byte)0x85, /* _`..l... */
            (byte)0xa9, (byte)0x86, (byte)0x13, (byte)0x23, (byte)0xaa, (byte)0x55, (byte)0x8b, (byte)0x79, /* ...#.U.y */
            (byte)0x45, (byte)0x0d, (byte)0x78, (byte)0xcb, (byte)0x48, (byte)0xc2, (byte)0xc8, (byte)0xd2, /* E.x.H... */
            (byte)0xe6, (byte)0xdb, (byte)0xbd, (byte)0x17, (byte)0x6c, (byte)0xb4, (byte)0x9c, (byte)0xb8, /* ....l... */
            (byte)0x20, (byte)0x75, (byte)0x8b, (byte)0xe4, (byte)0xdd, (byte)0x05, (byte)0xd0, (byte)0xc3, /*  u...... */
            (byte)0x91, (byte)0x73, (byte)0x3d, (byte)0x39, (byte)0xba, (byte)0xff, (byte)0xb2, (byte)0x7c, /* .s=9...| */
            (byte)0xd7, (byte)0xe7, (byte)0x47, (byte)0xc2, (byte)0x63, (byte)0xbf, (byte)0x50, (byte)0x8b, /* ..G.c.P. */
            (byte)0x3d, (byte)0xb4, (byte)0x57, (byte)0x11, (byte)0x5b, (byte)0x4a, (byte)0xeb, (byte)0x21, /* =.W.[J.! */
            (byte)0xd4, (byte)0x6e, (byte)0x7e, (byte)0x76, (byte)0xda, (byte)0xe0, (byte)0xe1, (byte)0xe9, /* .n~v.... */
            (byte)0xeb, (byte)0xaf, (byte)0x72, (byte)0xa2, (byte)0x0b, (byte)0x9e, (byte)0x0a, (byte)0x42, /* ..r....B */
            (byte)0xfa, (byte)0x9c, (byte)0x47, (byte)0x4b, (byte)0xdb, (byte)0x1d, (byte)0x5e, (byte)0x79, /* ..GK..^y */
            (byte)0x0b, (byte)0x87, (byte)0xa0, (byte)0x91, (byte)0x2d, (byte)0x3e, (byte)0x08, (byte)0xa5, /* ....->.. */
            (byte)0xde, (byte)0x25, (byte)0x16, (byte)0x29, (byte)0x85, (byte)0xb4, (byte)0x4a, (byte)0xda, /* .%.)..J. */
            (byte)0x68, (byte)0x70, (byte)0x7b, (byte)0xfe, (byte)0xe4, (byte)0x8d, (byte)0xb9, (byte)0xca, /* hp{..... */
            (byte)0x10, (byte)0x0e, (byte)0x9a, (byte)0xb3, (byte)0x8d, (byte)0x72, (byte)0x8c, (byte)0x38, /* .....r.8 */
            (byte)0x6a, (byte)0xe1, (byte)0x12, (byte)0xe7, (byte)0x9d, (byte)0xff, (byte)0x23, (byte)0xe2, /* j.....#. */
            (byte)0x20, (byte)0x3a, (byte)0x52, (byte)0xa6, (byte)0x91, (byte)0xb1, (byte)0x2a, (byte)0x52, /*  :R...*R */
            (byte)0xf8, (byte)0x96, (byte)0xe2, (byte)0xda, (byte)0x5d, (byte)0x33, (byte)0x74, (byte)0x00, /* ....]3t. */
            (byte)0x00, (byte)0x00, (byte)0x10, (byte)0x00, (byte)0x1b, (byte)0x00, (byte)0x19, (byte)0x06, /* ........ */
            (byte)0x73, (byte)0x70, (byte)0x64, (byte)0x79, (byte)0x2f, (byte)0x33, (byte)0x08, (byte)0x73, /* spdy/3.s */
            (byte)0x70, (byte)0x64, (byte)0x79, (byte)0x2f, (byte)0x33, (byte)0x2e, (byte)0x31, (byte)0x08, /* pdy/3.1. */
            (byte)0x68, (byte)0x74, (byte)0x74, (byte)0x70, (byte)0x2f, (byte)0x31, (byte)0x2e, (byte)0x31, /* http/1.1 */
            (byte)0x00, (byte)0x05, (byte)0x00, (byte)0x05, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, /* ........ */
            (byte)0x00, (byte)0x00, (byte)0x0d, (byte)0x00, (byte)0x12, (byte)0x00, (byte)0x10, (byte)0x04, /* ........ */
            (byte)0x01, (byte)0x05, (byte)0x01, (byte)0x02, (byte)0x01, (byte)0x04, (byte)0x03, (byte)0x05, /* ........ */
            (byte)0x03, (byte)0x02, (byte)0x03, (byte)0x04, (byte)0x02, (byte)0x02, (byte)0x02, (byte)0x00, /* ........ */
            (byte)0x12, (byte)0x00, (byte)0x00, (byte)0x8b, (byte)0x47, (byte)0x00, (byte)0x40, (byte)0x00, /* ....G.@. */
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, /* ........ */
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, /* ........ */
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, /* ........ */
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, /* ........ */
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, /* ........ */
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, /* ........ */
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, /* ........ */
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00        /* ....... */
    };

    static final byte[] https5 = {
            (byte)0x45, (byte)0x00, /* Kn;...E. */
            (byte)0x00, (byte)0x34, (byte)0x8f, (byte)0x18, (byte)0x40, (byte)0x00, (byte)0x35, (byte)0x06, /* .4..@.5. */
            (byte)0x1f, (byte)0x8d, (byte)0x25, (byte)0x94, (byte)0xb0, (byte)0x52, (byte)0xc0, (byte)0xa8, /* ..%..R.. */
            (byte)0x00, (byte)0x90, (byte)0x01, (byte)0xbb, (byte)0xd5, (byte)0x38, (byte)0x50, (byte)0xff, /* .....8P. */
            (byte)0x9a, (byte)0x67, (byte)0x07, (byte)0xa3, (byte)0x31, (byte)0xa5, (byte)0x80, (byte)0x10, /* .g..1... */
            (byte)0x04, (byte)0xe2, (byte)0x23, (byte)0xe2, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x01, /* ..#..... */
            (byte)0x08, (byte)0x0a, (byte)0x49, (byte)0xcb, (byte)0x15, (byte)0x7f, (byte)0x01, (byte)0x5a, /* ..I....Z */
            (byte)0x5a, (byte)0x93                                      /* Z. */
    };

    static final byte[] https6 = {
            (byte)0x45, (byte)0x00, /* Kn;...E. */
            (byte)0x00, (byte)0xbd, (byte)0x8f, (byte)0x19, (byte)0x40, (byte)0x00, (byte)0x35, (byte)0x06, /* ....@.5. */
            (byte)0x1f, (byte)0x03, (byte)0x25, (byte)0x94, (byte)0xb0, (byte)0x52, (byte)0xc0, (byte)0xa8, /* ..%..R.. */
            (byte)0x00, (byte)0x90, (byte)0x01, (byte)0xbb, (byte)0xd5, (byte)0x38, (byte)0x50, (byte)0xff, /* .....8P. */
            (byte)0x9a, (byte)0x67, (byte)0x07, (byte)0xa3, (byte)0x31, (byte)0xa5, (byte)0x80, (byte)0x18, /* .g..1... */
            (byte)0x04, (byte)0xe2, (byte)0xda, (byte)0xf4, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x01, /* ........ */
            (byte)0x08, (byte)0x0a, (byte)0x49, (byte)0xcb, (byte)0x15, (byte)0x7f, (byte)0x01, (byte)0x5a, /* ..I....Z */
            (byte)0x5a, (byte)0x93, (byte)0x16, (byte)0x03, (byte)0x03, (byte)0x00, (byte)0x51, (byte)0x02, /* Z.....Q. */
            (byte)0x00, (byte)0x00, (byte)0x4d, (byte)0x03, (byte)0x03, (byte)0x53, (byte)0x2e, (byte)0xc7, /* ..M..S.. */
            (byte)0x6e, (byte)0x8a, (byte)0x22, (byte)0x70, (byte)0x5c, (byte)0xf1, (byte)0x8b, (byte)0xdf, /* n."p\... */
            (byte)0x17, (byte)0x94, (byte)0xd9, (byte)0xe5, (byte)0xb4, (byte)0x15, (byte)0xb6, (byte)0x86, /* ........ */
            (byte)0xf1, (byte)0xc3, (byte)0xd6, (byte)0x44, (byte)0x78, (byte)0x9e, (byte)0xb3, (byte)0x00, /* ...Dx... */
            (byte)0x87, (byte)0x62, (byte)0x3c, (byte)0xd1, (byte)0x5d, (byte)0x20, (byte)0x2e, (byte)0x04, /* .b<.] .. */
            (byte)0xe9, (byte)0x8c, (byte)0xfc, (byte)0x9b, (byte)0x18, (byte)0x04, (byte)0xe0, (byte)0x7f, /* ........ */
            (byte)0x36, (byte)0x85, (byte)0x49, (byte)0x29, (byte)0x11, (byte)0x26, (byte)0x22, (byte)0x8d, /* 6.I).&". */
            (byte)0x89, (byte)0x6c, (byte)0xd0, (byte)0xbe, (byte)0x65, (byte)0x49, (byte)0x34, (byte)0xb8, /* .l..eI4. */
            (byte)0xbd, (byte)0x4c, (byte)0x45, (byte)0xf9, (byte)0x0b, (byte)0xe5, (byte)0x00, (byte)0x9e, /* .LE..... */
            (byte)0x00, (byte)0x00, (byte)0x05, (byte)0xff, (byte)0x01, (byte)0x00, (byte)0x01, (byte)0x00, /* ........ */
            (byte)0x14, (byte)0x03, (byte)0x03, (byte)0x00, (byte)0x01, (byte)0x01, (byte)0x16, (byte)0x03, /* ........ */
            (byte)0x03, (byte)0x00, (byte)0x28, (byte)0xa2, (byte)0xe4, (byte)0xf8, (byte)0x49, (byte)0x41, /* ..(...IA */
            (byte)0x03, (byte)0x87, (byte)0x44, (byte)0xec, (byte)0x17, (byte)0xa8, (byte)0x93, (byte)0xd4, /* ..D..... */
            (byte)0x0b, (byte)0xf8, (byte)0xc9, (byte)0xf9, (byte)0x25, (byte)0x22, (byte)0xd3, (byte)0x86, /* ....%".. */
            (byte)0xbb, (byte)0xe1, (byte)0xf5, (byte)0x5e, (byte)0x7b, (byte)0x38, (byte)0xc6, (byte)0x83, /* ...^{8.. */
            (byte)0x02, (byte)0x3c, (byte)0x69, (byte)0xab, (byte)0x8a, (byte)0x77, (byte)0x3b, (byte)0xf0, /* .<i..w;. */
            (byte)0xa3, (byte)0x98, (byte)0x95                                /* ... */
    };

    static final byte[] https7 = {
            (byte)0x45, (byte)0x00, /* 8/....E. */
            (byte)0x00, (byte)0x34, (byte)0x75, (byte)0x20, (byte)0x40, (byte)0x00, (byte)0x40, (byte)0x06, /* .4u @.@. */
            (byte)0x2e, (byte)0x85, (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x90, (byte)0x25, (byte)0x94, /* ......%. */
            (byte)0xb0, (byte)0x52, (byte)0xd5, (byte)0x38, (byte)0x01, (byte)0xbb, (byte)0x07, (byte)0xa3, /* .R.8.... */
            (byte)0x31, (byte)0xa5, (byte)0x50, (byte)0xff, (byte)0x9a, (byte)0xf0, (byte)0x80, (byte)0x10, /* 1.P..... */
            (byte)0x00, (byte)0x1e, (byte)0x28, (byte)0x0d, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x01, /* ..(..... */
            (byte)0x08, (byte)0x0a, (byte)0x01, (byte)0x5a, (byte)0x5a, (byte)0xa3, (byte)0x49, (byte)0xcb, /* ...ZZ.I. */
            (byte)0x15, (byte)0x7f                                      /* .. */
    };

    static final byte[] https8 = {
            (byte)0x45, (byte)0x00, /* 8/....E. */
            (byte)0x00, (byte)0x67, (byte)0x75, (byte)0x21, (byte)0x40, (byte)0x00, (byte)0x40, (byte)0x06, /* .gu!@.@. */
            (byte)0x2e, (byte)0x51, (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x90, (byte)0x25, (byte)0x94, /* .Q....%. */
            (byte)0xb0, (byte)0x52, (byte)0xd5, (byte)0x38, (byte)0x01, (byte)0xbb, (byte)0x07, (byte)0xa3, /* .R.8.... */
            (byte)0x31, (byte)0xa5, (byte)0x50, (byte)0xff, (byte)0x9a, (byte)0xf0, (byte)0x80, (byte)0x18, /* 1.P..... */
            (byte)0x00, (byte)0x1e, (byte)0x4f, (byte)0x71, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x01, /* ..Oq.... */
            (byte)0x08, (byte)0x0a, (byte)0x01, (byte)0x5a, (byte)0x5a, (byte)0xa4, (byte)0x49, (byte)0xcb, /* ...ZZ.I. */
            (byte)0x15, (byte)0x7f, (byte)0x14, (byte)0x03, (byte)0x03, (byte)0x00, (byte)0x01, (byte)0x01, /* ........ */
            (byte)0x16, (byte)0x03, (byte)0x03, (byte)0x00, (byte)0x28, (byte)0x00, (byte)0x00, (byte)0x00, /* ....(... */
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x9c, (byte)0xc7, (byte)0xcc, /* ........ */
            (byte)0x68, (byte)0xc8, (byte)0xcd, (byte)0xda, (byte)0xdb, (byte)0xf5, (byte)0x1d, (byte)0xcf, /* h....... */
            (byte)0x46, (byte)0x16, (byte)0xd0, (byte)0xb9, (byte)0x15, (byte)0x17, (byte)0x17, (byte)0x17, /* F....... */
            (byte)0x3e, (byte)0xd4, (byte)0xeb, (byte)0x24, (byte)0xe2, (byte)0x9f, (byte)0xcd, (byte)0xfb, /* >..$.... */
            (byte)0x62, (byte)0xaa, (byte)0x60, (byte)0x49, (byte)0xa6                    /* b.`I. */
    };

    static final byte[] https9 = {
            (byte)0x45, (byte)0x00, /* Kn;...E. */
            (byte)0x00, (byte)0x34, (byte)0x8f, (byte)0x1a, (byte)0x40, (byte)0x00, (byte)0x35, (byte)0x06, /* .4..@.5. */
            (byte)0x1f, (byte)0x8b, (byte)0x25, (byte)0x94, (byte)0xb0, (byte)0x52, (byte)0xc0, (byte)0xa8, /* ..%..R.. */
            (byte)0x00, (byte)0x90, (byte)0x01, (byte)0xbb, (byte)0xd5, (byte)0x38, (byte)0x50, (byte)0xff, /* .....8P. */
            (byte)0x9a, (byte)0xf0, (byte)0x07, (byte)0xa3, (byte)0x31, (byte)0xd8, (byte)0x80, (byte)0x10, /* ....1... */
            (byte)0x04, (byte)0xe2, (byte)0x23, (byte)0x07, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x01, /* ..#..... */
            (byte)0x08, (byte)0x0a, (byte)0x49, (byte)0xcb, (byte)0x15, (byte)0x8d, (byte)0x01, (byte)0x5a, /* ..I....Z */
            (byte)0x5a, (byte)0xa4                                      /* Z. */
    };

    static final byte[] https10 = {
            (byte)0x45, (byte)0x00, /* 8/....E. */
            (byte)0x00, (byte)0x34, (byte)0x75, (byte)0x22, (byte)0x40, (byte)0x00, (byte)0x40, (byte)0x06, /* .4u"@.@. */
            (byte)0x2e, (byte)0x83, (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x90, (byte)0x25, (byte)0x94, /* ......%. */
            (byte)0xb0, (byte)0x52, (byte)0xd5, (byte)0x38, (byte)0x01, (byte)0xbb, (byte)0x07, (byte)0xa3, /* .R.8.... */
            (byte)0x31, (byte)0xd8, (byte)0x50, (byte)0xff, (byte)0x9a, (byte)0xf0, (byte)0x80, (byte)0x11, /* 1.P..... */
            (byte)0x00, (byte)0x1e, (byte)0x03, (byte)0x33, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x01, /* ...3.... */
            (byte)0x08, (byte)0x0a, (byte)0x01, (byte)0x5a, (byte)0x7f, (byte)0x3b, (byte)0x49, (byte)0xcb, /* ...Z.;I. */
            (byte)0x15, (byte)0x8d                                      /* .. */
    };

    static final byte[] https11 = {
            (byte)0x45, (byte)0x00, /* Kn;...E. */
            (byte)0x00, (byte)0x53, (byte)0x8f, (byte)0x1b, (byte)0x40, (byte)0x00, (byte)0x35, (byte)0x06, /* .S..@.5. */
            (byte)0x1f, (byte)0x6b, (byte)0x25, (byte)0x94, (byte)0xb0, (byte)0x52, (byte)0xc0, (byte)0xa8, /* .k%..R.. */
            (byte)0x00, (byte)0x90, (byte)0x01, (byte)0xbb, (byte)0xd5, (byte)0x38, (byte)0x50, (byte)0xff, /* .....8P. */
            (byte)0x9a, (byte)0xf0, (byte)0x07, (byte)0xa3, (byte)0x31, (byte)0xd9, (byte)0x80, (byte)0x18, /* ....1... */
            (byte)0x04, (byte)0xe2, (byte)0x3b, (byte)0x13, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x01, /* ..;..... */
            (byte)0x08, (byte)0x0a, (byte)0x49, (byte)0xcb, (byte)0x1e, (byte)0xa9, (byte)0x01, (byte)0x5a, /* ..I....Z */
            (byte)0x7f, (byte)0x3b, (byte)0x15, (byte)0x03, (byte)0x03, (byte)0x00, (byte)0x1a, (byte)0xa2, /* .;...... */
            (byte)0xe4, (byte)0xf8, (byte)0x49, (byte)0x41, (byte)0x03, (byte)0x87, (byte)0x45, (byte)0xf4, /* ..IA..E. */
            (byte)0xe7, (byte)0x21, (byte)0x7f, (byte)0x39, (byte)0x20, (byte)0x2a, (byte)0x67, (byte)0x1f, /* .!.9 *g. */
            (byte)0xc4, (byte)0x4b, (byte)0x48, (byte)0x17, (byte)0x6f, (byte)0x55, (byte)0xc9, (byte)0x5f, /* .KH.oU._ */
            (byte)0xdd                                            /* . */
    };

    static final byte[] https12 = {
            (byte)0x45, (byte)0x00, /* 8/....E. */
            (byte)0x00, (byte)0x28, (byte)0x77, (byte)0xa3, (byte)0x40, (byte)0x00, (byte)0x40, (byte)0x06, /* .(w.@.@. */
            (byte)0x2c, (byte)0x0e, (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x90, (byte)0x25, (byte)0x94, /* ,.....%. */
            (byte)0xb0, (byte)0x52, (byte)0xd5, (byte)0x38, (byte)0x01, (byte)0xbb, (byte)0x07, (byte)0xa3, /* .R.8.... */
            (byte)0x31, (byte)0xd9, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x50, (byte)0x04, /* 1.....P. */
            (byte)0x00, (byte)0x00, (byte)0x08, (byte)0x52, (byte)0x00, (byte)0x00              /* ...R.. */
    };

    static final byte[] https13 = {
            (byte)0x45, (byte)0x00, /* Kn;...E. */
            (byte)0x00, (byte)0x34, (byte)0x8f, (byte)0x1c, (byte)0x40, (byte)0x00, (byte)0x35, (byte)0x06, /* .4..@.5. */
            (byte)0x1f, (byte)0x89, (byte)0x25, (byte)0x94, (byte)0xb0, (byte)0x52, (byte)0xc0, (byte)0xa8, /* ..%..R.. */
            (byte)0x00, (byte)0x90, (byte)0x01, (byte)0xbb, (byte)0xd5, (byte)0x38, (byte)0x50, (byte)0xff, /* .....8P. */
            (byte)0x9b, (byte)0x0f, (byte)0x07, (byte)0xa3, (byte)0x31, (byte)0xd9, (byte)0x80, (byte)0x11, /* ....1... */
            (byte)0x04, (byte)0xe2, (byte)0xf5, (byte)0x32, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x01, /* ...2.... */
            (byte)0x08, (byte)0x0a, (byte)0x49, (byte)0xcb, (byte)0x1e, (byte)0xa9, (byte)0x01, (byte)0x5a, /* ..I....Z */
            (byte)0x7f, (byte)0x3b                                      /* .; */
    };

    static final byte[] https14 = {
            (byte)0x45, (byte)0x00, /* 8/....E. */
            (byte)0x00, (byte)0x28, (byte)0x77, (byte)0xa4, (byte)0x40, (byte)0x00, (byte)0x40, (byte)0x06, /* .(w.@.@. */
            (byte)0x2c, (byte)0x0d, (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x90, (byte)0x25, (byte)0x94, /* ,.....%. */
            (byte)0xb0, (byte)0x52, (byte)0xd5, (byte)0x38, (byte)0x01, (byte)0xbb, (byte)0x07, (byte)0xa3, /* .R.8.... */
            (byte)0x31, (byte)0xd9, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x50, (byte)0x04, /* 1.....P. */
            (byte)0x00, (byte)0x00, (byte)0x08, (byte)0x52, (byte)0x00, (byte)0x00              /* ...R.. */
    };

    /*
     * This is a full HTTPS stream
     */
    static final byte[][] https_stream={https1,https2,https3,https4,https5,
                                        https6,https7,https8,https9,https10,
                                        https11,https12,https13,https14};

    @Test
    public void testTCPPacket() {
        ByteBuffer rawData=ByteBuffer.allocate(tcp_sync.length);
        rawData.put(tcp_sync);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, rawData.limit());
        Assert.assertNotNull(packet);
        Assert.assertTrue(packet instanceof TCPPacket);
        Assert.assertEquals(0, ((TCPPacket) packet).getAckNumber());
        Assert.assertEquals(0xf573d7d3, ((TCPPacket) packet).getSequenceNumber());
        Assert.assertEquals(0, ((TCPPacket) packet).getUrgentPointer());
        Assert.assertEquals(29200, ((TCPPacket) packet).getWindowSize());
        Assert.assertEquals((short) 0xdd60, ((TCPPacket) packet).getSourcePort());
        Assert.assertEquals(80, ((TCPPacket) packet).getDestinationPort());
        Assert.assertNotNull(((TCPPacket) packet).getOption(0));
        Assert.assertTrue(((TCPPacket) packet).isSYN());
        Assert.assertFalse(((TCPPacket) packet).isACK());
        Assert.assertFalse(((TCPPacket) packet).isCWR());
        Assert.assertFalse(((TCPPacket) packet).isECE());
        Assert.assertFalse(((TCPPacket)packet).isFIN());
        Assert.assertFalse(((TCPPacket)packet).isNS());
        Assert.assertFalse(((TCPPacket)packet).isPSH());
        Assert.assertFalse(((TCPPacket)packet).isRST());
        Assert.assertFalse(((TCPPacket)packet).isURG());
        Assert.assertEquals(0,((TCPPacket)packet).getPayLoadSize());
        Assert.assertEquals(40,((TCPPacket)packet).getHeaderSize());
        Assert.assertEquals(20,((TCPPacket)packet).getIpHeaderSize());

        ((TCPPacket)packet).swapSourceDestinationPort();
        Assert.assertEquals((short) 0xdd60, ((TCPPacket) packet).getDestinationPort());
        Assert.assertEquals(80, ((TCPPacket) packet).getSourcePort());
        Assert.assertTrue(((TCPPacket)packet).hasOptions());
        Assert.assertEquals(19001,((TCPPacket)packet).getTCPChecksum());
        Assert.assertNotNull(((TCPPacket)packet).toString());
        Assert.assertEquals(0xd8388ee8,packet.getFlowHash());
        Assert.assertEquals(0x35a4919f,packet.getDstRoutingHash());
    }

    @Test
    public void testTCPDataPacket() {
        ByteBuffer rawData = ByteBuffer.allocate(https4.length);
        rawData.put(https4);
        rawData.flip();
        TCPPacket packet = (TCPPacket)IPFactory.processRawPacket(rawData, rawData.limit());
        Assert.assertNotNull(packet);

        //Test payload
        ByteBuffer payload=packet.getPayLoad();
        Assert.assertEquals(32,packet.getHeaderSize());
        Assert.assertEquals(517,packet.getPayLoadSize());

        //check ICMP header
        ByteBuffer header=packet.getHeader();
        Assert.assertEquals(packet.getHeaderSize(),header.limit());
        Assert.assertEquals(packet.getRawPacket().getInt(packet.getIpHeaderSize()),header.getInt(0));

        //Check IP Header
        ByteBuffer Ipheader=(packet.getIpHeader());
        Assert.assertEquals(packet.getIpHeaderSize(),Ipheader.limit());
        Assert.assertEquals(packet.getRawPacket().getInt(0),Ipheader.getInt(0));


    }


        @Test
    public void testTCPOptionMSS() {
        ByteBuffer rawData=ByteBuffer.allocate(tcp_sync.length);
        rawData.put(tcp_sync);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, rawData.limit());
        Assert.assertNotNull(packet);
        Assert.assertTrue(packet instanceof TCPPacket);
        TCPOption o=((TCPPacket) packet).getOptionByType(TCPOption.MSS);
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TCPoptionMSS);
        Assert.assertNotNull(o.toString());
        Assert.assertEquals(1460, ((TCPoptionMSS) o).getMss());
        Assert.assertEquals(TCPoptionMSS.name,o.getName());
        Assert.assertEquals(TCPoptionMSS.length,o.getLength());
    }

    @Test
    public void testTCPOptionWindowScale() {
        ByteBuffer rawData=ByteBuffer.allocate(tcp_sync.length);
        rawData.put(tcp_sync);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, rawData.limit());
        Assert.assertNotNull(packet);
        Assert.assertTrue(packet instanceof TCPPacket);
        TCPOption o=((TCPPacket) packet).getOptionByType(TCPOption.WSCALE);
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TCPoptionWindowScale);
        Assert.assertNotNull(o.toString());
        Assert.assertEquals(10, ((TCPoptionWindowScale) o).getScale());
        Assert.assertEquals(TCPoptionWindowScale.name,o.getName());
        Assert.assertEquals(TCPoptionWindowScale.length,o.getLength());
    }

    @Test
    public void testTCPOptionSACK() {
        ByteBuffer rawData=ByteBuffer.allocate(tcp_sync.length);
        rawData.put(tcp_sync);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, rawData.limit());
        Assert.assertNotNull(packet);
        Assert.assertTrue(packet instanceof TCPPacket);
        TCPOption o=((TCPPacket) packet).getOptionByType(TCPOption.SACK);
        Assert.assertNull(o);
        //some dummy testing
        o=new TCPoptionSACK();
        Assert.assertEquals(TCPoptionSACK.name,o.getName());
        Assert.assertEquals(TCPoptionSACK.length, o.getLength());
        Assert.assertNotNull(o.toString());
    }

    @Test
    public void testTCPOptionTimeStamp() {
        ByteBuffer rawData=ByteBuffer.allocate(tcp_sync.length);
        rawData.put(tcp_sync);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, rawData.limit());
        Assert.assertNotNull(packet);
        Assert.assertTrue(packet instanceof TCPPacket);
        TCPOption o=((TCPPacket) packet).getOptionByType(TCPOption.TIMESTAMP);
        Assert.assertNotNull(o);
        Assert.assertNotNull(o.toString());
        Assert.assertEquals(0, ((TCPoptionTimeStamp) o).getTsecr());
        Assert.assertEquals(0x00fd0e76, ((TCPoptionTimeStamp) o).getTsval());
        Assert.assertEquals(TCPoptionTimeStamp.name,o.getName());
        Assert.assertEquals(TCPoptionTimeStamp.length,o.getLength());
    }

    @Test
    public void testTCPOptionNOP() {
        ByteBuffer rawData=ByteBuffer.allocate(tcp_sync.length);
        rawData.put(tcp_sync);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, rawData.limit());
        Assert.assertNotNull(packet);
        Assert.assertTrue(packet instanceof TCPPacket);
        TCPOption o=((TCPPacket) packet).getOptionByType(TCPOption.NOP);
        Assert.assertNotNull(o);
        Assert.assertNotNull(o.toString());
        Assert.assertEquals(TCPoptionNOP.name,o.getName());
        Assert.assertEquals(TCPoptionNOP.length,o.getLength());

    }

    @Test
    public void testTCPOptionEnd() {
        ByteBuffer rawData=ByteBuffer.allocate(tcp_sync.length);
        rawData.put(tcp_sync);
        rawData.flip();
        IpPacket packet = IPFactory.processRawPacket(rawData, rawData.limit());
        Assert.assertNotNull(packet);
        Assert.assertTrue(packet instanceof TCPPacket);
        TCPOption o=((TCPPacket) packet).getOptionByType(TCPOption.END);
        Assert.assertNull(o);
        //some dummy testing
        o=new TCPoptionEnd();
        Assert.assertEquals(TCPoptionEnd.name,o.getName());
        Assert.assertEquals(TCPoptionEnd.length,o.getLength());
        Assert.assertNotNull(o.toString());
    }

    @Test
    public void testTCPStream() {
        logger.info("Test TCP stream...");
        //get first sync packet and flow hash
        ByteBuffer syncData=ByteBuffer.allocate(https_stream[0].length);
        syncData.put(https_stream[0]);
        syncData.flip();
        TCPPacket tcpSync= (TCPPacket) IPFactory.processRawPacket(syncData,syncData.limit());
        Assert.assertTrue(tcpSync.isSYN());
        Assert.assertFalse(tcpSync.isACK());

        //Test payload
        ByteBuffer payload=tcpSync.getPayLoad();
        Assert.assertEquals(40,tcpSync.getHeaderSize());
        Assert.assertEquals(0,tcpSync.getPayLoadSize());

        //check ICMP header
        ByteBuffer header=tcpSync.getHeader();
        Assert.assertEquals(tcpSync.getHeaderSize(),header.limit());
        Assert.assertEquals(tcpSync.getRawPacket().getInt(tcpSync.getIpHeaderSize()),header.getInt(0));

        //Check IP Header
        ByteBuffer Ipheader=(tcpSync.getIpHeader());
        Assert.assertEquals(tcpSync.getIpHeaderSize(),Ipheader.limit());
        Assert.assertEquals(tcpSync.getRawPacket().getInt(0),Ipheader.getInt(0));

        //Flow hash must be the same for all packets
        int flowHash1=tcpSync.getFlowHash();
        int reverseflowHash1=tcpSync.getReverseFlowHash();
        int routeHash1=tcpSync.getDstRoutingHash();
        int source1=tcpSync.getSourceAddress();
        logger.info("flow hash: {}",Integer.toHexString(flowHash1));
        logger.info("reverse flow hash: {}",Integer.toHexString(reverseflowHash1));
        logger.info("Route hash: {}",Integer.toHexString(routeHash1));
        //some typical TCP events
        Counter syncSeen=new Counter();
        Counter ackSeen=new Counter();
        Counter finSeen=new Counter();
        Counter resetSeen=new Counter();
        for (int pkt=0;pkt<13;pkt++) {
            byte[] data=https_stream[pkt];
            ByteBuffer rawData=ByteBuffer.allocate(data.length);
            rawData.put(data);
            rawData.flip();
            TCPPacket packet = (TCPPacket) IPFactory.processRawPacket(rawData, rawData.limit());
            Assert.assertNotNull(packet);
            Assert.assertNotNull(packet.toString());
            logger.info(packet.toString());
            if (packet.getSourceAddress()==source1) {
                Assert.assertEquals(flowHash1,packet.getFlowHash());
                Assert.assertEquals(routeHash1,packet.getDstRoutingHash());
            } else {
                //reverse packet , so we check reverse flow hash
                Assert.assertEquals(reverseflowHash1,packet.getFlowHash());
            }
            if (packet.isSYN())
                syncSeen.inc();
            if (packet.isACK())
                ackSeen.inc();
            if (packet.isRST())
                resetSeen.inc();
            if (packet.isFIN())
                finSeen.inc();
        }
        Assert.assertEquals(2,syncSeen.getCount());
        Assert.assertEquals(11,ackSeen.getCount());
        Assert.assertEquals(1,resetSeen.getCount());
        Assert.assertEquals(2,finSeen.getCount());
    }

    }
