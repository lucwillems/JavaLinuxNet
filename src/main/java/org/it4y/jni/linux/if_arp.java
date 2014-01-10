/*
 * This file is derived from Linux kernel headers
 * All copyrights preserved to original kernel author.
 *
 * more info see https://www.kernel.org/pub/linux/kernel/COPYING
 */

package org.it4y.jni.linux;

import org.it4y.util.IndexNameMap;

import java.util.Collections;
import java.util.Map;

//from include <linux/if_arp.h>
public final class if_arp {
    public static final Map<Integer, String> ARPHDR_NAMES =
            Collections.unmodifiableMap(new IndexNameMap<Integer, String>() {{
                put(ARPHRD_NETROM, "netrom");
                put(ARPHRD_ETHER, "ether");
                put(ARPHRD_EETHER, "eether");
                put(ARPHRD_AX25, "ax25");
                put(ARPHRD_PRONET, "pronet");
                put(ARPHRD_CHAOS, "chaos");
                put(ARPHRD_IEEE802, "ieee802");
                put(ARPHRD_ARCNET, "arcnet");
                put(ARPHRD_APPLETLK, "appletalk");
                put(ARPHRD_DLCI, "dlci");
                put(ARPHRD_ATM, "atm");
                put(ARPHRD_METRICOM, "metricom");
                put(ARPHRD_IEEE1394, "ieee1394");
                put(ARPHRD_EUI64, "EUI64");
                put(ARPHRD_INFINIBAND, "infiniband");
                put(ARPHRD_SLIP, "slip");
                put(ARPHRD_CSLIP, "cslip");
                put(ARPHRD_SLIP6, "slip6");
                put(ARPHRD_CSLIP6, "cslip6");
                put(ARPHRD_RSRVD, "rsrvd");
                put(ARPHRD_ADAPT, "adapt");
                put(ARPHRD_ROSE, "rose");
                put(ARPHRD_X25, "x25");
                put(ARPHRD_HWX25, "hwx25");
                put(ARPHRD_CAN, "can");
                put(ARPHRD_PPP, "ppp");
                put(ARPHRD_CISCO, "cisco");
                put(ARPHRD_LAPB, "lapb");
                put(ARPHRD_DDCMP, "ddcmp");
                put(ARPHRD_RAWHDLC, "rawhdlc");
                put(ARPHRD_TUNNEL, "ipip");
                put(ARPHRD_TUNNEL6, "ipip6");
                put(ARPHRD_FRAD, "frad");
                put(ARPHRD_SKIP, "skip");
                put(ARPHRD_LOOPBACK, "loopback");
                put(ARPHRD_LOCALTLK, "localtalk");
                put(ARPHRD_FDDI, "fddi");
                put(ARPHRD_BIF, "bif");
                put(ARPHRD_SIT, "sit");
                put(ARPHRD_IPDDP, "ipddp");
                put(ARPHRD_IPGRE, "gre");
                put(ARPHRD_PIMREG, "pimreg");
                put(ARPHRD_HIPPI, "hippi");
                put(ARPHRD_ASH, "nexus_ash");
                put(ARPHRD_ECONET, "econet");
                put(ARPHRD_IRDA, "irda");
                put(ARPHRD_VOID, "void");
                put(ARPHRD_NONE, "none");
            }});

    /* ARP protocol HARDWARE identifiers. */
    public static final int ARPHRD_NETROM = 0;		/* from KA9Q: NET/ROM pseudo	*/
    public static final int ARPHRD_ETHER = 1;		    /* Ethernet 10Mbps		*/
    public static final int ARPHRD_EETHER = 2;		/* Experimental Ethernet	*/
    public static final int ARPHRD_AX25 = 3;		    /* AX.25 Level 2		*/
    public static final int ARPHRD_PRONET = 4;	    /* PROnet token ring		*/
    public static final int ARPHRD_CHAOS = 5;		    /* Chaosnet			*/
    public static final int ARPHRD_IEEE802 = 6;		/* IEEE 802.2 Ethernet/TR/TB	*/
    public static final int ARPHRD_ARCNET = 7;	    /* ARCnet			*/
    public static final int ARPHRD_APPLETLK = 8;   	/* APPLEtalk			*/
    public static final int ARPHRD_DLCI = 15;		    /* Frame Relay DLCI		*/
    public static final int ARPHRD_ATM = 19;		    /* ATM 				*/
    public static final int ARPHRD_METRICOM = 23;		/* Metricom STRIP (new IANA id)	*/
    public static final int ARPHRD_IEEE1394 = 24;		/* IEEE 1394 IPv4 - RFC 2734	*/
    public static final int ARPHRD_EUI64 = 27;	    /* EUI-64                       */
    public static final int ARPHRD_INFINIBAND = 32;	/* InfiniBand			*/
    /* Dummy types for non ARP hardware */
    public static final int ARPHRD_SLIP = 256;
    public static final int ARPHRD_CSLIP = 257;
    public static final int ARPHRD_SLIP6 = 258;
    public static final int ARPHRD_CSLIP6 = 259;
    public static final int ARPHRD_RSRVD = 260;		/* Notional KISS type 		*/
    public static final int ARPHRD_ADAPT = 264;
    public static final int ARPHRD_ROSE = 270;
    public static final int ARPHRD_X25 = 271;		/* CCITT X.25			*/
    public static final int ARPHRD_HWX25 = 272;		/* Boards with X.25 in firmware	*/
    public static final int ARPHRD_CAN = 280;		/* Controller Area Network      */
    public static final int ARPHRD_PPP = 512;
    public static final int ARPHRD_CISCO = 513;		/* Cisco HDLC	 		*/
    public static final int ARPHRD_HDLC = ARPHRD_CISCO;
    public static final int ARPHRD_LAPB = 516;	/* LAPB				*/
    public static final int ARPHRD_DDCMP = 517;		/* Digital's DDCMP protocol     */
    public static final int ARPHRD_RAWHDLC = 518;		/* Raw HDLC			*/
    public static final int ARPHRD_TUNNEL = 768;		/* IPIP tunnel			*/
    public static final int ARPHRD_TUNNEL6 = 769;		/* IP6IP6 tunnel       		*/
    public static final int ARPHRD_FRAD = 770;             /* Frame Relay Access Device    */
    public static final int ARPHRD_SKIP = 771;		/* SKIP vif			*/
    public static final int ARPHRD_LOOPBACK = 772;		/* Loopback device		*/
    public static final int ARPHRD_LOCALTLK = 773;		/* Localtalk device		*/
    public static final int ARPHRD_FDDI = 774;		/* Fiber Distributed Data Interface */
    public static final int ARPHRD_BIF = 775;             /* AP1000 BIF                   */
    public static final int ARPHRD_SIT = 776;		/* sit0 device - IPv6-in-IPv4	*/
    public static final int ARPHRD_IPDDP = 777;		/* IP over DDP tunneller	*/
    public static final int ARPHRD_IPGRE = 778;		/* GRE over IP			*/
    public static final int ARPHRD_PIMREG = 779;		/* PIMSM register interface	*/
    public static final int ARPHRD_HIPPI = 780;		/* High Performance Parallel Interface */
    public static final int ARPHRD_ASH = 781;		/* Nexus 64Mbps Ash		*/
    public static final int ARPHRD_ECONET = 782;		/* Acorn Econet			*/
    public static final int ARPHRD_IRDA = 783;		/* Linux-IrDA			*/
    /* ARP works differently on different FC media .. so  */
    public static final int ARPHRD_FCPP = 784;		/* Point to point fibrechannel	*/
    public static final int ARPHRD_FCAL = 785;		/* Fibrechannel arbitrated loop */
    public static final int ARPHRD_FCPL = 786;		/* Fibrechannel public loop	*/
    public static final int ARPHRD_FCFABRIC = 787;		/* Fibrechannel fabric		*/
    /* 787->799 reserved for fibrechannel media types */
    public static final int ARPHRD_IEEE802_TR = 800;		/* Magic type ident for TR	*/
    public static final int ARPHRD_IEEE80211 = 801;		/* IEEE 802.11			*/
    public static final int ARPHRD_IEEE80211_PRISM = 802;	/* IEEE 802.11 + Prism2 header  */
    public static final int ARPHRD_IEEE80211_RADIOTAP = 803;	/* IEEE 802.11 + radiotap header */
    public static final int ARPHRD_IEEE802154 = 804;
    public static final int ARPHRD_IEEE802154_MONITOR = 805;	/* IEEE 802.15.4 network monitor */
    public static final int ARPHRD_PHONET = 820;		/* PhoNet media type		*/
    public static final int ARPHRD_PHONET_PIPE = 821;		/* PhoNet pipe header		*/
    public static final int ARPHRD_CAIF = 822;		/* CAIF media type		*/
    public static final int ARPHRD_IP6GRE = 823;		/* GRE over IPv6		*/
    public static final int ARPHRD_NETLINK = 824;		/* Netlink header		*/
    public static final int ARPHRD_VOID = 0xFFFF;	/* Void type, nothing is known */
    public static final int ARPHRD_NONE = 0xFFFE;	/* zero header length */

}
