/*
 * This file is derived from Linux kernel headers
 * All copyrights preserved to original kernel author.
 *
 * more info see https://www.kernel.org/pub/linux/kernel/COPYING
 */

package org.it4y.jni.linux;

//based on include <linux/netlink.h>
public final class netlink {

        public static final int NLMSGHDR_SIZE = 16;
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
