package org.it4y.jni.linux;

/**
 * Created by luc on 1/4/14.
 */
//from include<linux/if_link.h>
public final class if_link {
    public static final String[] RTA_NAMES = {
            "unspec",
            "address",
            "broadcast",
            "ifname",
            "mtu",
            "link",
            "qdisc",
            "stats",
            "cost",
            "priority",
            "master",
            "wireless",
            "protinfo",
            "txqlen",
            "map",
            "weight",
            "operstate",
            "linkmode",
            "linkinfo",
            "net_ns_pid",
            "ifalias",
            "num_vf",
            "vfinfo_list",
            "stats64",
            "vf_ports",
            "port_self",
            "af_spec",
            "group",
            "net_ns_fd",
            "ext_mask",
            "promiscuity",
            "num_tx_queues",
            "num_rx_queues",
            "carrier"
    };
    public static final int IFLA_UNSPEC = 0;
    public static final int IFLA_ADDRESS = 1;
    public static final int IFLA_BROADCAST = 2;
    public static final int IFLA_IFNAME = 3;
    public static final int IFLA_MTU = 4;
    public static final int IFLA_LINK = 5;
    public static final int IFLA_QDISC = 6;
    public static final int IFLA_STATS = 7;
    public static final int IFLA_COST = 8;
    public static final int IFLA_PRIORITY = 9;
    public static final int IFLA_MASTER = 10;
    public static final int IFLA_WIRELESS = 11;		/* Wireless Extension event - see wireless.h */
    public static final int IFLA_PROTINFO = 12;		/* Protocol specific information for a link */
    public static final int IFLA_TXQLEN = 13;
    public static final int IFLA_MAP = 14;
    public static final int IFLA_WEIGHT = 15;
    public static final int IFLA_OPERSTATE = 16;
    public static final int IFLA_LINKMODE = 17;
    public static final int IFLA_LINKINFO = 18;
    public static final int IFLA_NET_NS_PID = 19;
    public static final int IFLA_IFALIAS = 20;
    public static final int IFLA_NUM_VF = 21;		/* Number of VFs if device is SR-IOV PF */
    public static final int IFLA_VFINFO_LIST = 22;
    public static final int IFLA_STATS64 = 23;
    public static final int IFLA_VF_PORTS = 24;
    public static final int IFLA_PORT_SELF = 25;
    public static final int IFLA_AF_SPEC = 26;
    public static final int IFLA_GROUP = 27;		/* Group the device belongs to */
    public static final int IFLA_NET_NS_FD = 28;
    public static final int IFLA_EXT_MASK = 29;		/* Extended info mask, VFs, etc */
    public static final int IFLA_PROMISCUITY = 30;	/* Promiscuity count: > 0 means acts PROMISC */
    public static final int IFLA_NUM_TX_QUEUES = 31;
    public static final int IFLA_NUM_RX_QUEUES = 32;
    public static final int IFLA_CARRIER = 33;
    public static final int __IFLA_MAX = IFLA_CARRIER;
}
