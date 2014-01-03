package org.it4y.jni;

import java.nio.ByteBuffer;

/**
 * Created by luc on 12/31/13.
 */
public class libnetlink {

    public static final class linux {
        //from include<linux/netlink.h>
        public static final class netlink {

            public static final int NLMSGHDR_SIZE=16;

            public static final int NETLINK_ROUTE = 0;
            ;	/* Routing/device hook				*/
            public static final int NETLINK_UNUSED = 1;
            ;	/* Unused number				*/
            public static final int NETLINK_USERSOCK = 2;
            ;	/* Reserved for user mode socket protocols 	*/
            public static final int NETLINK_FIREWALL = 3;
            ;	/* Unused number, formerly ip_queue		*/
            public static final int NETLINK_SOCK_DIAG = 4;
            ;	/* socket monitoring				*/
            public static final int NETLINK_NFLOG = 5;
            ;	/* netfilter/iptables ULOG */
            public static final int NETLINK_XFRM = 6;
            ;	/* ipsec */
            public static final int NETLINK_SELINUX = 7;
            ;	/* SELinux event notifications */
            public static final int NETLINK_ISCSI = 8;
            ;	/* Open-iSCSI */
            public static final int NETLINK_AUDIT = 9;
            ;	/* auditing */
            public static final int NETLINK_FIB_LOOKUP = 10;
            public static final int NETLINK_CONNECTOR = 11;
            public static final int NETLINK_NETFILTER = 12;
            ;	/* netfilter subsystem */
            public static final int NETLINK_IP6_FW = 13;
            public static final int NETLINK_DNRTMSG = 14;
            ;	/* DECnet routing messages */
            public static final int NETLINK_KOBJECT_UEVENT = 15;
            ;	/* Kernel messages to userspace */
            public static final int NETLINK_GENERIC = 16;
            /* leave room for NETLINK_DM (DM Events) */
            public static final int NETLINK_SCSITRANSPORT = 18;
            ;	/* SCSI Transports */
            public static final int NETLINK_ECRYPTFS = 19;
            public static final int NETLINK_RDMA = 20;
            public static final int NETLINK_CRYPTO = 21;
            ;	/* Crypto layer */

            public static final int NETLINK_INET_DIAG = NETLINK_SOCK_DIAG;

            /* Flags values */

            public static final int NLM_F_REQUEST = 1;	/* It is request message. 	*/
            public static final int NLM_F_MULTI = 2;	/* Multipart message, terminated by NLMSG_DONE */
            public static final int NLM_F_ACK = 4;	/* Reply with ack, with zero or error code */
            public static final int NLM_F_ECHO = 8;	/* Echo this request 		*/
            public static final int NLM_F_DUMP_INTR = 16;	/* Dump was inconsistent due to sequence change */

            /* Modifiers to GET request */
            public static final int NLM_F_ROOT = 0x100;	/* specify tree	root	*/
            public static final int NLM_F_MATCH = 0x200;	/* return all matching	*/
            public static final int NLM_F_ATOMIC = 0x400;	/* atomic GET		*/
            public static final int NLM_F_DUMP = (NLM_F_ROOT | NLM_F_MATCH);

            /* Modifiers to NEW request */
            public static final int NLM_F_REPLACE = 0x100;	/* Override existing		*/
            public static final int NLM_F_EXCL = 0x200;	/* Do not touch, if it exists	*/
            public static final int NLM_F_CREATE = 0x400;	/* Create, if it does not exist	*/
            public static final int NLM_F_APPEND = 0x800;	/* Add to end of list		*/

            public static final int NLMSG_NOOP = 0x1;	/* Nothing.		*/
            public static final int NLMSG_ERROR = 0x2;	/* Error		*/
            public static final int NLMSG_DONE = 0x3;	/* End of a dump	*/
            public static final int NLMSG_OVERRUN = 0x4;	/* Data lost		*/

            public static final int NLMSG_MIN_TYPE = 0x10;	/* < 0x10: reserved control messages */
        }

        //from include<linux/rtnetlink.h>
        public static final class rtnetlink {
            public static final int RTNL_FAMILY_IPMR = 128;
            public static final int RTNL_FAMILY_IP6MR = 129;
            public static final int RTNL_FAMILY_MAX = 129;

            public static final int RTMGRP_LINK = 1;
            public static final int RTMGRP_NOTIFY = 2;
            public static final int RTMGRP_NEIGH = 4;
            public static final int RTMGRP_TC = 8;

            public static final int RTMGRP_IPV4_IFADDR = 0x10;
            public static final int RTMGRP_IPV4_MROUTE = 0x20;
            public static final int RTMGRP_IPV4_ROUTE = 0x40;
            public static final int RTMGRP_IPV4_RULE = 0x80;
            public static final int RTMGRP_IPV6_IFADDR = 0x100;
            public static final int RTMGRP_IPV6_MROUTE = 0x200;
            public static final int RTMGRP_IPV6_ROUTE = 0x400;
            public static final int RTMGRP_IPV6_IFINFO = 0x800;
            public static final int RTMGRP_DECnet_IFADDR = 0x1000;
            public static final int RTMGRP_DECnet_ROUTE = 0x4000;
            public static final int RTMGRP_IPV6_PREFIX = 0x20000;


            /* Types of messages */
            public static final int RTM_BASE = 16;
            public static final int RTM_NEWLINK = 16;
            public static final int RTM_DELLINK = 17;
            public static final int RTM_GETLINK = 18;
            public static final int RTM_SETLINK = 19;
            public static final int RTM_NEWADDR = 20;
            public static final int RTM_DELADDR = 21;
            public static final int RTM_GETADDR = 22;
            public static final int RTM_NEWROUTE = 24;
            public static final int RTM_DELROUTE = 25;
            public static final int RTM_GETROUTE = 26;
            public static final int RTM_NEWNEIGH = 28;
            public static final int RTM_DELNEIGH = 29;
            public static final int RTM_GETNEIGH = 30;
            public static final int RTM_NEWRULE = 32;
            public static final int RTM_DELRULE = 33;
            public static final int RTM_GETRULE = 34;
            public static final int RTM_NEWQDISC = 36;
            public static final int RTM_DELQDISC = 37;
            public static final int RTM_GETQDISC = 38;
            public static final int RTM_NEWTCLASS = 40;
            public static final int RTM_DELTCLASS = 41;
            public static final int RTM_GETTCLASS = 42;
            public static final int RTM_NEWTFILTER = 44;
            public static final int RTM_DELTFILTER = 45;
            public static final int RTM_GETTFILTER = 46;
            public static final int RTM_NEWACTION = 48;
            public static final int RTM_DELACTION = 49;
            public static final int RTM_GETACTION = 50;
            public static final int RTM_NEWPREFIX = 52;
            public static final int RTM_GETMULTICAST = 58;
            public static final int RTM_GETANYCAST = 62;
            public static final int RTM_NEWNEIGHTBL = 64;
            public static final int RTM_GETNEIGHTBL = 66;
            public static final int RTM_SETNEIGHTBL = 67;
            public static final int RTM_NEWNDUSEROPT = 68;
            public static final int RTM_NEWADDRLABEL = 72;
            public static final int RTM_DELADDRLABEL = 73;
            public static final int RTM_GETADDRLABEL = 74;
            public static final int RTM_GETDCB = 78;
            public static final int RTM_SETDCB = 79;
            public static final int RTM_NEWNETCONF = 80;
            public static final int RTM_GETNETCONF = 82;
            public static final int RTM_NEWMDB = 84;
            public static final int RTM_DELMDB = 85;
            public static final int RTM_GETMDB = 86;

            /* RTnetlink multicast groups */
            public static final int RTNLGRP_NONE=0;
            public static final int RTNLGRP_LINK=1;
            public static final int RTNLGRP_NOTIFY=2;
            public static final int RTNLGRP_NEIGH=3;
            public static final int RTNLGRP_TC=4;
            public static final int RTNLGRP_IPV4_IFADDR=5;
            public static final int RTNLGRP_IPV4_MROUTE=6;
            public static final int RTNLGRP_IPV4_ROUTE=7;
            public static final int RTNLGRP_IPV4_RULE=8;
            public static final int RTNLGRP_IPV6_IFADDR=9;
            public static final int RTNLGRP_IPV6_MROUTE=10;
            public static final int RTNLGRP_IPV6_ROUTE=11;
            public static final int RTNLGRP_IPV6_IFINFO=12;
            public static final int RTNLGRP_DECnet_IFADDR=13;
            public static final int RTNLGRP_NOP2=14;
            public static final int RTNLGRP_DECnet_ROUTE=15;
            public static final int RTNLGRP_DECnet_RULE=16;
            public static final int RTNLGRP_NOP4=17;
            public static final int RTNLGRP_IPV6_PREFIX=18;
            public static final int RTNLGRP_IPV6_RULE=19;
            public static final int RTNLGRP_ND_USEROPT=20;
            public static final int RTNLGRP_PHONET_IFADDR=21;
            public static final int RTNLGRP_PHONET_ROUTE=22;
            public static final int RTNLGRP_DCB=23;
            public static final int RTNLGRP_IPV4_NETCONF=24;
            public static final int RTNLGRP_IPV6_NETCONF=25;
            public static final int RTNLGRP_MDB=26;


            /* RTM_METRICS ---types of RTAX_* */

            public static final int RTAX_UNSPEC=0;
            public static final int RTAX_LOCK=1;
            public static final int RTAX_MTU=2;
            public static final int RTAX_WINDOW=3;
            public static final int RTAX_RTT=4;
            public static final int RTAX_RTTVAR=5;
            public static final int RTAX_SSTHRESH=6;
            public static final int RTAX_CWND=7;
            public static final int RTAX_ADVMSS=8;
            public static final int RTAX_REORDERING=9;
            public static final int RTAX_HOPLIMIT=10;
            public static final int RTAX_INITCWND=11;
            public static final int RTAX_FEATURES=12;
            public static final int RTAX_RTO_MIN=13;
            public static final int RTAX_INITRWND=14;
            public static final int RTAX_QUICKACK=15;

            /* RT routing scopes */
            public static final int RT_SCOPE_UNIVERSE=0;
            /* User defined values  */
            public static final int RT_SCOPE_SITE=200;
            public static final int RT_SCOPE_LINK=253;
            public static final int RT_SCOPE_HOST=254;
            public static final int RT_SCOPE_NOWHERE=255;

            /* routing protocols */
            public static final byte RTPROT_UNSPEC=0;
            public static final byte RTPROT_REDIRECT=1;      /* Route installed by ICMP redirects */
                                                             /* not used by current IPv4 */
            public static final byte RTPROT_KERNEL=2;        /* Route installed by kernel            */
            public static final byte RTPROT_BOOT=3;          /* Route installed during boot          */
            public static final byte RTPROT_STATIC=4;        /* Route installed by administrator     */
            public static final byte RTPROT_GATED=8;         /* Apparently, GateD */
            public static final byte RTPROT_RA=9;            /* RDISC/ND router advertisements */
            public static final byte RTPROT_MRT=10;          /* Merit MRT */
            public static final byte RTPROT_ZEBRA=11;        /* Zebra */
            public static final byte RTPROT_BIRD=12;         /* BIRD */
            public static final byte RTPROT_DNROUTED=13;     /* DECnet routing daemon */
            public static final byte RTPROT_XORP=14;         /* XORP */
            public static final byte RTPROT_NTK=15;          /* Netsukuku */
            public static final byte RTPROT_DHCP=16;         /* DHCP client */
            public static final byte RTPROT_MROUTED=17;      /* Multicast daemon */

            /* Routing message attributes */
            public static final String[] RTA_NAMES ={
                "unspec",
                "dst",
                "src",
                "iif",
                "oif",
                "gateway",
                "priority",
                "prefsrc",
                "metrics",
                "multipath",
                "protoinfo",
                "flow",
                "cacheinfo",
                "session",
                "mp_algo",
                "table",
                "mark",
                "mfc_stats"
            };
            public static final int RTA_UNSPEC=0;
            public static final int RTA_DST=1;
            public static final int RTA_SRC=2;
            public static final int RTA_IIF=3;
            public static final int RTA_OIF=4;
            public static final int RTA_GATEWA=5;
            public static final int RTA_PRIORITY=6;
            public static final int RTA_PREFSRC=7;
            public static final int RTA_METRICS=8;
            public static final int RTA_MULTIPATH=9;
            public static final int RTA_PROTOINFO=10; /* no longer used */
            public static final int RTA_FLOW=11;
            public static final int RTA_CACHEINFO=12;
            public static final int RTA_SESSION=13; /* no longer used */
            public static final int RTA_MP_ALGO=14; /* no longer used */
            public static final int RTA_TABLE=15;
            public static final int RTA_MARK=16;
            public static final int RTA_MFC_STATS=17;
            public static final int __RTA_MAX=RTA_MFC_STATS;

            /* rtm_type */
            public static final String[] RTTYPE_NAMES ={
                    "unspec",
                    "unicast",
                    "local",
                    "broadcast",
                    "anycast",
                    "multicast",
                    "blackhole",
                    "unreachable",
                    "prohibited",
                    "throw",
                    "nat",
                    "xresolve"
            };
            public static final int RTN_UNSPEC=0;
            public static final int RTN_UNICAST=1;          /* Gateway or direct route      */
            public static final int RTN_LOCAL=2;            /* Accept locally               */
            public static final int RTN_BROADCAST=3;        /* Accept locally as broadcast, */
                                                            /* send as broadcast */
            public static final int RTN_ANYCAST=4;          /* Accept locally as broadcast,
                                                             /* but send as unicast */
            public static final int RTN_MULTICAST=5;       /* Multicast route              */
            public static final int RTN_BLACKHOLE=6;        /* Drop                         */
            public static final int RTN_UNREACHABLE=7;      /* Destination is unreachable   */
            public static final int RTN_PROHIBIT=8;         /* Administratively prohibited  */
            public static final int RTN_THROW=9;            /* Not in this table            */
            public static final int RTN_NAT=10;             /* Translate this address       */
            public static final int RTN_XRESOLVE=11;        /* Use external resolver        */
            public static final int __RTN_MAX=RTN_XRESOLVE;
        }

        //from include<linux/if_link.h>
        public static final class if_link {
            public static final String[] RTA_NAMES ={
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
            public static final int IFLA_UNSPEC=0;
            public static final int IFLA_ADDRESS=1;
            public static final int IFLA_BROADCAST=2;
            public static final int IFLA_IFNAME=3;
            public static final int IFLA_MTU=4;
            public static final int IFLA_LINK=5;
            public static final int IFLA_QDISC=6;
            public static final int IFLA_STATS=7;
            public static final int IFLA_COST=8;
            public static final int IFLA_PRIORITY=9;
            public static final int IFLA_MASTER=10;
            public static final int IFLA_WIRELESS=11;		/* Wireless Extension event - see wireless.h */
            public static final int IFLA_PROTINFO=12;		/* Protocol specific information for a link */
            public static final int IFLA_TXQLEN=13;
            public static final int IFLA_MAP=14;
            public static final int IFLA_WEIGHT=15;
            public static final int IFLA_OPERSTATE=16;
            public static final int IFLA_LINKMODE=17;
            public static final int IFLA_LINKINFO=18;
            public static final int IFLA_NET_NS_PID=19;
            public static final int IFLA_IFALIAS=20;
            public static final int IFLA_NUM_VF=21;		/* Number of VFs if device is SR-IOV PF */
            public static final int IFLA_VFINFO_LIST=22;
            public static final int IFLA_STATS64=23;
            public static final int IFLA_VF_PORTS=24;
            public static final int IFLA_PORT_SELF=25;
            public static final int IFLA_AF_SPEC=26;
            public static final int IFLA_GROUP=27;		/* Group the device belongs to */
            public static final int IFLA_NET_NS_FD=28;
            public static final int IFLA_EXT_MASK=29;		/* Extended info mask, VFs, etc */
            public static final int IFLA_PROMISCUITY=30;	/* Promiscuity count: > 0 means acts PROMISC */
            public static final int IFLA_NUM_TX_QUEUES=31;
            public static final int IFLA_NUM_RX_QUEUES=32;
            public static final int IFLA_CARRIER=33;
            public static final int __IFLA_MAX=IFLA_CARRIER;
        }
        //from include<linux/if_address.h>
        public static final class if_address {
            public static final String[] RTA_NAMES ={
                    "unspec",
                    "address",
                    "local",
                    "label",
                    "broadcast",
                    "anycast",
                    "cacheinfo",
                    "multicast"
            };
            public static final int IFA_UNSPEC=0;
            public static final int IFA_ADDRESS=1;
            public static final int IFA_LOCAL=2;
            public static final int IFA_LABEL=3;
            public static final int IFA_BROADCAST=4;
            public static final int IFA_ANYCAST=5;
            public static final int IFA_CACHEINFO=6;
            public static final int IFA_MULTICAST=7;
            public static final int __IFA_MAX=IFA_MULTICAST;
        }
        //from include<linux/if_neighbour.h>
        public static final class if_neighbour {
            public static final String[] RTA_NAMES ={
                    "unspec",
                    "dst",
                    "lladdr",
                    "cacheinfo",
                    "probes",
                    "vlan",
                    "port",
                    "vni",
                    "ifindex"
            };
            public static final int NDA_UNSPEC=0;
            public static final int NDA_DST=1;
            public static final int NDA_LLADDR=2;
            public static final int NDA_CACHEINFO=3;
            public static final int NDA_PROBES=4;
            public static final int NDA_VLAN=5;
            public static final int NDA_PORT=6;
            public static final int NDA_VNI=7;
            public static final int NDA_IFINDEX=8;
            public static final int __NDA_MAX=NDA_IFINDEX;
        }
    }


    //From include
    public static class utils {
        public static int nl_mgrp(int group) {
            return group >0 ? (1 << (group - 1)) : 0;
        }
    }

    //from include <libnetlink.h>
    public static class rtnl_handle {
        public static final int SIZE = 36;
        public byte[] handle = new byte[SIZE];

        public rtnl_handle() {
        }
    }

    //interface to accept messages from rtnl_listen()
    public interface rtnl_accept {
        public int accept(ByteBuffer message);
    }
    //return code to send from accept interface
    public static int rtl_accept_CONTINUE=0;
    public static int rtl_accept_STOP=-1;
    public static int rtl_accept_FAILED_NOMESSAGEBUFFER=-2;
    public static int rtl_accept_FAILED_NOLISTENER=-3;
    public static int rtl_accept_FAILED_BUFFERTOSMALL=-4;
    public static int rtl_accept_FAILED_JVMFAILURE=-5;
}
