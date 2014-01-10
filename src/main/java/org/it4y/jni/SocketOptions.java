package org.it4y.jni;

/**
 * Created by luc on 12/28/13.
 */
/*
 * This a list of Linux SOCKET options not supported by JAVA
 */
public class SocketOptions {

    public static final int SOL_IP = 0;
    public static final int SOL_TCP = 6;
    public static final int SOL_UDP = 17;


    public static final int IP_TRANSPARENT = 19;

    /* TCP socket options based on 3.12 kernel uapi/linux/tcp.h list*/
    public static final int TCP_NODELAY = 1;       /* Turn off Nagle's algorithm. */
    public static final int TCP_MAXSEG = 2;        /* Limit MSS */
    public static final int TCP_CORK = 3;          /* Never send partially complete segments */
    public static final int TCP_KEEPIDLE = 4;      /* Start keeplives after this period */
    public static final int TCI_KEEPINTVL = 5;     /* Interval between keepalives */
    public static final int TCP_KEEPCNT = 6;       /* Number of keepalives before death */
    public static final int TCP_SYNCNT = 7;        /* Number of SYN retransmits */
    public static final int TCP_LINGER2 = 8;       /* Life time of orphaned FIN-WAIT-2 state */
    public static final int TCP_DEFER_ACCEPT = 9;  /* Wake up listener only when data arrive */
    public static final int TCP_WINDOW_CLAMP = 10; /* Bound advertised window */
    public static final int TCP_INFO = 11;         /* Information about this connection. */
    public static final int TCP_QUICKACK = 12;     /* Block/reenable quick acks */
    public static final int TCP_CONGESTION = 13;   /* Congestion control algorithm */
    public static final int TCP_MD5SIG = 14;       /* TCP MD5 Signature (RFC2385) */
    public static final int TCP_THIN_LINEAR_TIMEOUTS = 16;      /* Use linear timeouts for thin streams*/
    public static final int TCP_THIN_DUPACK = 17;      /* Fast retrans. after 1 dupack */
    public static final int TCP_USER_TIMEOUT = 18;     /* How long for loss retry before timeout */
    public static final int TCP_REPAIR = 19;           /* TCP sock is under repair right now */
    public static final int TCP_REPAIR_QUEUE = 20;
    public static final int TCP_QUEUE_SEQ = 21;
    public static final int TCP_REPAIR_OPTIONS = 22;
    public static final int TCP_FASTOPEN = 23;         /* Enable FastOpen on listeners */
    public static final int TCP_TIMESTAMP = 24;
    public static final int TCP_NOTSENT_LOWAT = 25;    /* limit number of unsent bytes in write queue */

    /* TCP socket options based on 3.12 kernel uapi/linux/udp.h list*/
    public static final int UDP_CORK = 1;       /* Never send partially complete segments */
    public static final int UDP_ENCAP = 100;    /* Set the socket to accept encapsulated packets */

}
