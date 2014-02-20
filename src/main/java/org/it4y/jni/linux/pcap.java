package org.it4y.jni.linux;

/**
 * Created by luc on 2/18/14.
 */
public class pcap {

    public final static int DLT_NULL = 0;	/* BSD loopback encapsulation */
    public final static int DLT_EN10MB = 1;	/* Ethernet (10Mb) */
    public final static int DLT_EN3MB = 2;	/* Experimental Ethernet (3Mb) */
    public final static int DLT_AX25 = 3;	/* Amateur Radio AX.25 */
    public final static int DLT_PRONET = 4;	/* Proteon ProNET Token Ring */
    public final static int DLT_CHAOS = 5;	/* Chaos */
    public final static int DLT_IEEE802 = 6;	/* 802.5 Token Ring */
    public final static int DLT_ARCNET = 7;	/* ARCNET, with BSD-style header */
    public final static int DLT_SLIP = 8;	/* Serial Line IP */
    public final static int DLT_PPP = 9;	    /* Point-to-point Protocol */
    public final static int DLT_FDDI = 10;	/* FDDI */
    public final static int DLT_RAW = 12;	/* raw IP */
    public final static int DLT_IPV4 = 228;  /* Raw IPv4; the packet begins with an IPv4 header. */
    public final static int DLT_IPV6 = 229;	/* DLT_IPV6 Raw IPv6; the packet begins with an IPv6 header. */

    /*
     * Error codes for the pcap API.
     * These will all be negative, so you can check for the success or
     * failure of a call that returns these codes by checking for a
     * negative value.
     */
    public final static int PCAP_ERROR = -1;	/* generic error code */
    public final static int PCAP_ERROR_BREAK = -2;	/* loop terminated by pcap_breakloop */
    public final static int PCAP_ERROR_NOT_ACTIVATED = -3;	/* the capture needs to be activated */
    public final static int PCAP_ERROR_ACTIVATED = -4;	/* the operation can't be performed on already activated captures */
    public final static int PCAP_ERROR_NO_SUCH_DEVICE = -5;	/* no such device exists */
    public final static int PCAP_ERROR_RFMON_NOTSUP = -6;	/* this device doesn't support rfmon (monitor) mode */
    public final static int PCAP_ERROR_NOT_RFMON = -7;	/* operation supported only in monitor mode */
    public final static int PCAP_ERROR_PERM_DENIED = -8;	/* no permission to open the device */
    public final static int PCAP_ERROR_IFACE_NOT_UP = -9;	/* interface isn't up */
    public final static int PCAP_ERROR_CANTSET_TSTAMP_TYPE = -10;	/* this device doesn't support setting the time stamp type */
    public final static int PCAP_ERROR_PROMISC_PERM_DENIED = -11;	/* you don't have permission to capture in promiscuous mode */
    public final static int PCAP_ERROR_TSTAMP_PRECISION_NOTSUP = -12;  /* the requested time stamp precision is not supported */

    /*
     * Warning codes for the pcap API.
     * These will all be positive and non-zero, so they won't look like
     * errors.
     */
    public final static int PCAP_WARNING = 1;	/* generic warning code */
    public final static int PCAP_WARNING_PROMISC_NOTSUP = 2;	/* this device doesn't support promiscuous mode */
    public final static int PCAP_WARNING_TSTAMP_TYPE_NOTSUP = 3;	/* the requested time stamp type is not supported */

    /*
     * Value to pass to pcap_compile() as the netmask if you don't know what
     * the netmask is.
     */
    public final static int PCAP_NETMASK_UNKNOWN = 0xffffffff;

}
