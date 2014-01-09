package org.it4y.jni.linux;

import org.it4y.util.IndexNameMap;

import java.util.Collections;
import java.util.Map;

/**
 * Created by luc on 1/4/14.
 */
//from include<linux/if_link.h>
public final class if_link {
    public static final Map<Integer, String> IFLA_NAMES =
            Collections.unmodifiableMap(new IndexNameMap<Integer, String>() {
                {
                    put(IFLA_UNSPEC,"unspec");
                    put(IFLA_ADDRESS, "address");
                    put(IFLA_BROADCAST,"broadcast");
                    put(IFLA_IFNAME,"ifname");
                    put(IFLA_MTU,"mtu");
                    put(IFLA_LINK,"link");
                    put(IFLA_QDISC,"qdisc");
                    put(IFLA_STATS,"stats");
                    put(IFLA_COST,"cost");
                    put(IFLA_PRIORITY,"priority");
                    put(IFLA_MASTER,"master");
                    put(IFLA_WIRELESS,"wireless");
                    put(IFLA_PROTINFO,"protinfo");
                    put(IFLA_TXQLEN,"txqlen");
                    put(IFLA_MAP,"map");
                    put(IFLA_WEIGHT,"weight");
                    put(IFLA_OPERSTATE,"operstate");
                    put(IFLA_LINKMODE,"linkmode");
                    put(IFLA_LINKINFO,"linkinfo");
                    put(IFLA_NET_NS_PID,"net_ns_pid");
                    put(IFLA_IFALIAS,"ifalias");
                    put(IFLA_NUM_VF,"num_vf");
                    put(IFLA_VFINFO_LIST,"vfinfo_list");
                    put(IFLA_STATS64,"stats64");
                    put(IFLA_VF_PORTS,"vf_ports");
                    put(IFLA_PORT_SELF,"port_self");
                    put(IFLA_AF_SPEC,"af_spec");
                    put(IFLA_GROUP,"group");
                    put(IFLA_NET_NS_FD,"net_ns_fd");
                    put(IFLA_EXT_MASK,"ext_mask");
                    put(IFLA_PROMISCUITY,"promiscuity");
                    put(IFLA_NUM_TX_QUEUES,"num_tx_queues");
                    put(IFLA_NUM_RX_QUEUES,"num_rx_queues");
                    put(IFLA_CARRIER,"carrier");
                }});

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
