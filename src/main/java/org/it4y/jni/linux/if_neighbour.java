package org.it4y.jni.linux;

/**
 * Created by luc on 1/4/14.
 */
//from include<linux/if_neighbour.h>
public final class if_neighbour {
        public static final String[] RTA_NAMES = {
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
        public static final int NDA_UNSPEC = 0;
        public static final int NDA_DST = 1;
        public static final int NDA_LLADDR = 2;
        public static final int NDA_CACHEINFO = 3;
        public static final int NDA_PROBES = 4;
        public static final int NDA_VLAN = 5;
        public static final int NDA_PORT = 6;
        public static final int NDA_VNI = 7;
        public static final int NDA_IFINDEX = 8;
        public static final int __NDA_MAX = NDA_IFINDEX;
}
