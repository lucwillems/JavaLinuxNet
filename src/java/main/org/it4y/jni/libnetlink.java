package org.it4y.jni;

import java.nio.ByteBuffer;

/**
 * Created by luc on 12/31/13.
 */
public class libnetlink {

    public static final class linux {
        //from include<linux/netlink.h>
        public static final class netlink {

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
