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

import org.it4y.util.IndexNameMap;

import java.util.Collections;
import java.util.Map;

//from include<linux/rtnetlink.h>
public final class rtnetlink {
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
        public static final int RTNLGRP_NONE = 0;
        public static final int RTNLGRP_LINK = 1;
        public static final int RTNLGRP_NOTIFY = 2;
        public static final int RTNLGRP_NEIGH = 3;
        public static final int RTNLGRP_TC = 4;
        public static final int RTNLGRP_IPV4_IFADDR = 5;
        public static final int RTNLGRP_IPV4_MROUTE = 6;
        public static final int RTNLGRP_IPV4_ROUTE = 7;
        public static final int RTNLGRP_IPV4_RULE = 8;
        public static final int RTNLGRP_IPV6_IFADDR = 9;
        public static final int RTNLGRP_IPV6_MROUTE = 10;
        public static final int RTNLGRP_IPV6_ROUTE = 11;
        public static final int RTNLGRP_IPV6_IFINFO = 12;
        public static final int RTNLGRP_DECnet_IFADDR = 13;
        public static final int RTNLGRP_NOP2 = 14;
        public static final int RTNLGRP_DECnet_ROUTE = 15;
        public static final int RTNLGRP_DECnet_RULE = 16;
        public static final int RTNLGRP_NOP4 = 17;
        public static final int RTNLGRP_IPV6_PREFIX = 18;
        public static final int RTNLGRP_IPV6_RULE = 19;
        public static final int RTNLGRP_ND_USEROPT = 20;
        public static final int RTNLGRP_PHONET_IFADDR = 21;
        public static final int RTNLGRP_PHONET_ROUTE = 22;
        public static final int RTNLGRP_DCB = 23;
        public static final int RTNLGRP_IPV4_NETCONF = 24;
        public static final int RTNLGRP_IPV6_NETCONF = 25;
        public static final int RTNLGRP_MDB = 26;


        /* RTM_METRICS ---types of RTAX_* */
        public static final int RTAX_UNSPEC = 0;
        public static final int RTAX_LOCK = 1;
        public static final int RTAX_MTU = 2;
        public static final int RTAX_WINDOW = 3;
        public static final int RTAX_RTT = 4;
        public static final int RTAX_RTTVAR = 5;
        public static final int RTAX_SSTHRESH = 6;
        public static final int RTAX_CWND = 7;
        public static final int RTAX_ADVMSS = 8;
        public static final int RTAX_REORDERING = 9;
        public static final int RTAX_HOPLIMIT = 10;
        public static final int RTAX_INITCWND = 11;
        public static final int RTAX_FEATURES = 12;
        public static final int RTAX_RTO_MIN = 13;
        public static final int RTAX_INITRWND = 14;
        public static final int RTAX_QUICKACK = 15;
        /* RT routing scopes */
        public static final int RT_SCOPE_UNIVERSE = 0;
        /* User defined values  */
        public static final int RT_SCOPE_SITE = 200;
        public static final int RT_SCOPE_LINK = 253;
        public static final int RT_SCOPE_HOST = 254;
        public static final int RT_SCOPE_NOWHERE = 255;
        /* routing protocols */
        public static final byte RTPROT_UNSPEC = 0;
        public static final byte RTPROT_REDIRECT = 1;      /* Route installed by ICMP redirects */
        /* not used by current IPv4 */
        public static final byte RTPROT_KERNEL = 2;        /* Route installed by kernel            */
        public static final byte RTPROT_BOOT = 3;          /* Route installed during boot          */
        public static final byte RTPROT_STATIC = 4;        /* Route installed by administrator     */
        public static final byte RTPROT_GATED = 8;         /* Apparently, GateD */
        public static final byte RTPROT_RA = 9;            /* RDISC/ND router advertisements */
        public static final byte RTPROT_MRT = 10;          /* Merit MRT */
        public static final byte RTPROT_ZEBRA = 11;        /* Zebra */
        public static final byte RTPROT_BIRD = 12;         /* BIRD */
        public static final byte RTPROT_DNROUTED = 13;     /* DECnet routing daemon */
        public static final byte RTPROT_XORP = 14;         /* XORP */
        public static final byte RTPROT_NTK = 15;          /* Netsukuku */
        public static final byte RTPROT_DHCP = 16;         /* DHCP client */
        public static final byte RTPROT_MROUTED = 17;      /* Multicast daemon */

        /* Routing message attributes */
        public static final Map<Integer, String> RTA_NAMES =
           Collections.unmodifiableMap(new IndexNameMap<Integer, String>() {
               {
                        put(RTA_UNSPEC, "unspec");
                        put(RTA_DST,"dst");
                        put(RTA_SRC,"src");
                        put(RTA_IIF,"iif");
                        put(RTA_OIF,"oif");
                        put(RTA_GATEWA,"gateway");
                        put(RTA_PRIORITY,"priority");
                        put(RTA_PREFSRC,"prefsrc");
                        put(RTA_METRICS,"metrics");
                        put(RTA_MULTIPATH,"multipath");
                        put(RTA_PROTOINFO,"protoinfo");
                        put(RTA_FLOW,"flow");
                        put(RTA_CACHEINFO,"cacheinfo");
                        put(RTA_SESSION,"session");
                        put(RTA_MP_ALGO,"mp_algo");
                        put(RTA_TABLE,"table");
                        put(RTA_MARK,"mark");
                        put(RTA_MFC_STATS,"mfc_stats");
                    }});

                    public static final int RTA_UNSPEC = 0;
                    public static final int RTA_DST = 1;
                    public static final int RTA_SRC = 2;
                    public static final int RTA_IIF = 3;
                    public static final int RTA_OIF = 4;
                    public static final int RTA_GATEWA = 5;
                    public static final int RTA_PRIORITY = 6;
                    public static final int RTA_PREFSRC = 7;
                    public static final int RTA_METRICS = 8;
                    public static final int RTA_MULTIPATH = 9;
                    public static final int RTA_PROTOINFO = 10; /* no longer used */
                    public static final int RTA_FLOW = 11;
                    public static final int RTA_CACHEINFO = 12;
                    public static final int RTA_SESSION = 13; /* no longer used */
                    public static final int RTA_MP_ALGO = 14; /* no longer used */
                    public static final int RTA_TABLE = 15;
                    public static final int RTA_MARK = 16;
                    public static final int RTA_MFC_STATS = 17;
                    public static final int __RTA_MAX = RTA_MFC_STATS;
        /* rtm_type */

                    public static final Map<Integer, String> RTN_NAMES =
                            Collections.unmodifiableMap(new IndexNameMap<Integer, String>() {
                                {
                                put(RTN_UNSPEC,"unspec");
                                put(RTN_UNICAST,"unicast");
                                put(RTN_LOCAL,"local");
                                put(RTN_BROADCAST,"broadcast");
                                put(RTN_ANYCAST,"anycast");
                                put(RTN_MULTICAST,"multicast");
                                put(RTN_BLACKHOLE,"blackhole");
                                put(RTN_UNREACHABLE,"unreachable");
                                put(RTN_PROHIBIT,"prohibited");
                                put(RTN_THROW,"throw");
                                put(RTN_NAT,"nat");
                                put(RTN_XRESOLVE,"xresolve");
                            }});

                    public static final int RTN_UNSPEC = 0;
                    public static final int RTN_UNICAST = 1;          /* Gateway or direct route      */
                    public static final int RTN_LOCAL = 2;            /* Accept locally               */
                    public static final int RTN_BROADCAST = 3;        /* Accept locally as broadcast, */
        /* send as broadcast */
                    public static final int RTN_ANYCAST = 4;          /* Accept locally as broadcast,
                                                             /* but send as unicast */
                    public static final int RTN_MULTICAST = 5;       /* Multicast route              */
                    public static final int RTN_BLACKHOLE = 6;        /* Drop                         */
                    public static final int RTN_UNREACHABLE = 7;      /* Destination is unreachable   */
                    public static final int RTN_PROHIBIT = 8;         /* Administratively prohibited  */
                    public static final int RTN_THROW = 9;            /* Not in this table            */
                    public static final int RTN_NAT = 10;             /* Translate this address       */
                    public static final int RTN_XRESOLVE = 11;        /* Use external resolver        */
                    public static final int __RTN_MAX = RTN_XRESOLVE;

}