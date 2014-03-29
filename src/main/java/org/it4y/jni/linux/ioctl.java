package org.it4y.jni.linux;

/**
 * Created by luc on 3/29/14.
 */
public class ioctl {

    public final static short IFF_UP=0x1;               /* interface is up              */
    public final static short IFF_BROADCAST=0x2;         /* broadcast address valid      */
    public final static short IFF_DEBUG=0x4;             /* turn on debugging            */
    public final static short IFF_LOOPBACK=0x8;          /* is a loopback net            */
    public final static short IFF_POINTOPOINT=0x10;      /* interface is has p-p link    */
    public final static short IFF_NOTRAILERS=0x20;       /* avoid use of trailers        */
    public final static short IFF_RUNNING=0x40;          /* interface RFC2863 OPER_UP    */
    public final static short IFF_NOARP=0x80;            /* no ARP protocol              */
    public final static short IFF_PROMISC=0x100;         /* receive all packets          */
    public final static short IFF_ALLMULTI=0x200;        /* receive all multicast packets*/
    public final static short IFF_MASTER=0x400;          /* master of a load balancer    */
    public final static short IFF_SLAVE=0x800;           /* slave of a load balancer     */
    public final static short IFF_MULTICAST=0x1000;      /* Supports multicast           */
    public final static short IFF_PORTSEL=0x2000;        /* can set media type           */
    public final static short IF_AUTOMEDIA=0x4000;       /* auto media select active     */
    public final static short IFF_DYNAMIC=(short) 0x8000;/* dialup device with changing addresses*/
}
