package org.it4y.jni;

/**
 * Created by luc on 12/31/13.
 */
public class libnetlink {

    public static final int RTMGRP_LINK=1;
    public static final int RTMGRP_NOTIFY=2;
    public static final int RTMGRP_NEIGH=4;
    public static final int RTMGRP_TC=8;
    public static final int RTMGRP_IPV4_IFADDR=0x10;
    public static final int RTMGRP_IPV4_MROUTE=0x20;
    public static final int RTMGRP_IPV4_ROUTE=0x40;
    public static final int RTMGRP_IPV4_RULE=0x80;
    public static final int RTMGRP_IPV6_IFADDR=0x100;
    public static final int RTMGRP_IPV6_MROUTE=0x200;
    public static final int RTMGRP_IPV6_ROUTE=0x400;
    public static final int RTMGRP_IPV6_IFINFO=0x800;
    public static final int RTMGRP_DECnet_IFADDR=0x1000;
    public static final int RTMGRP_DECnet_ROUTE=0x4000;
    public static final int RTMGRP_IPV6_PREFIX=0x20000;

    public static class rtnl_handle {
        public static final int SIZE=36;
        public byte[] handle=new byte[SIZE];

        public rtnl_handle() {
        }
    }
}
