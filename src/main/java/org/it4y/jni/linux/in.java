/*
 * Copyright 2014 Luc Willems (T.M.M.)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.it4y.jni.linux;

//from include<linux/in.h>

public final class in {
    /* Standard well-defined IP protocols.  */
    public static final int IPPROTO_IP = 0;		/* Dummy protocol for TCP		*/
    public static final int IPPROTO_ICMP = 1;		/* Internet Control Message Protocol	*/
    public static final int IPPROTO_IGMP = 2;		/* Internet Group Management Protocol	*/
    public static final int IPPROTO_IPIP = 4;		/* IPIP tunnels (older KA9Q tunnels use 94) */
    public static final int IPPROTO_TCP = 6;		/* Transmission Control Protocol	*/
    public static final int IPPROTO_EGP = 8;		/* Exterior Gateway Protocol		*/
    public static final int IPPROTO_PUP = 12;		/* PUP protocol				*/
    public static final int IPPROTO_UDP = 17;		/* User Datagram Protocol		*/
    public static final int IPPROTO_IDP = 22;		/* XNS IDP protocol			*/
    public static final int IPPROTO_DCCP = 33;		/* Datagram Congestion Control Protocol */
    public static final int IPPROTO_RSVP = 46;		/* RSVP protocol			*/
    public static final int IPPROTO_GRE = 47;		/* Cisco GRE tunnels (rfc 1701,1702)	*/

    public static final int IPPROTO_IPV6 = 41;		/* IPv6-in-IPv4 tunnelling		*/

    public static final int IPPROTO_ESP = 50;            /* Encapsulation Security Payload protocol */
    public static final int IPPROTO_AH = 51;             /* Authentication Header protocol       */
    public static final int IPPROTO_BEETPH = 94;	       /* IP option pseudo header for BEET */
    public static final int IPPROTO_PIM = 103;		/* Protocol Independent Multicast	*/

    public static final int IPPROTO_COMP = 108;                /* Compression Header protocol */
    public static final int IPPROTO_SCTP = 132;		/* Stream Control Transport Protocol	*/
    public static final int IPPROTO_UDPLITE = 136;	/* UDP-Lite (RFC 3828)			*/

    public static final int IPPROTO_RAW = 255;		/* Raw IP packets			*/

    public static final int IP_TOS = 1;
    public static final int IP_TTL = 2;
    public static final int IP_HDRINCL = 3;
    public static final int IP_OPTIONS = 4;
    public static final int IP_ROUTER_ALERT = 5;
    public static final int IP_RECVOPTS = 6;
    public static final int IP_RETOPTS = 7;
    public static final int IP_PKTINFO = 8;
    public static final int IP_PKTOPTIONS = 9;
    public static final int IP_MTU_DISCOVER = 10;
    public static final int IP_RECVERR = 11;
    public static final int IP_RECVTTL = 12;
    public static final int IP_RECVTOS = 13;
    public static final int IP_MTU = 14;
    public static final int IP_FREEBIND = 15;
    public static final int IP_IPSEC_POLICY = 16;
    public static final int IP_XFRM_POLICY = 17;
    public static final int IP_PASSSEC = 18;
    public static final int IP_TRANSPARENT = 19;

    /* BSD compatibility */
    public static final int IP_RECVRETOPTS = IP_RETOPTS;

    /* TProxy original addresses */
    public static final int IP_ORIGDSTADDR = 20;
    public static final int IP_RECVORIGDSTADDR = IP_ORIGDSTADDR;

    public static final int IP_MINTTL = 21;
    public static final int IP_NODEFRAG = 22;

    /* IP_MTU_DISCOVER values */
    public static final int IP_PMTUDISC_DONT = 0;	/* Never send DF frames */
    public static final int IP_PMTUDISC_WANT = 1;	/* Use per route hints	*/
    public static final int IP_PMTUDISC_DO = 2;	/* Always DF		*/
    public static final int IP_PMTUDISC_PROBE = 3;       /* Ignore dst pmtu      */

    public static final int IP_MULTICAST_IF = 32;
    public static final int IP_MULTICAST_TTL = 33;
    public static final int IP_MULTICAST_LOOP = 34;
    public static final int IP_ADD_MEMBERSHIP = 35;
    public static final int IP_DROP_MEMBERSHIP = 36;
    public static final int IP_UNBLOCK_SOURCE = 37;
    public static final int IP_BLOCK_SOURCE = 38;
    public static final int IP_ADD_SOURCE_MEMBERSHIP = 39;
    public static final int IP_DROP_SOURCE_MEMBERSHIP = 40;
    public static final int IP_MSFILTER = 41;
    public static final int MCAST_JOIN_GROUP = 42;
    public static final int MCAST_BLOCK_SOURCE = 43;
    public static final int MCAST_UNBLOCK_SOURCE = 44;
    public static final int MCAST_LEAVE_GROUP = 45;
    public static final int MCAST_JOIN_SOURCE_GROUP = 46;
    public static final int MCAST_LEAVE_SOURCE_GROUP = 47;
    public static final int MCAST_MSFILTER = 48;
    public static final int IP_MULTICAST_ALL = 49;
    public static final int IP_UNICAST_IF = 50;

    public static final int MCAST_EXCLUDE = 0;
    public static final int MCAST_INCLUDE = 1;

    /* These need to appear somewhere around here */
    public static final int IP_DEFAULT_MULTICAST_TTL = 1;
    public static final int IP_DEFAULT_MULTICAST_LOOP = 1;

}
