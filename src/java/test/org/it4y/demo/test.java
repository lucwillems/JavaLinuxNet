package org.it4y.demo;

import org.it4y.jni.libnetlink;
import org.it4y.jni.linuxutils;
import org.it4y.util.Hexdump;

import java.lang.Exception;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by luc on 12/25/13.
 *  run this to have the tun device ready (as root):
 *    ip tuntap add dev luc mode tun user luc group users
 *    ip addr add 127.0.0.2/32 dev luc
 *    ip link set dev luc up
 *    ip route add 8.8.4.4/32 via luc
 *    and now ping to 8.8.4.4
 *
 */
public class test {

    public static void main(String [] args) throws Exception {
        TunTapInterfaceListener tundev=new TunTapInterfaceListener("luc",1500);
        tundev.start();
        System.out.println("tun interface listener running");
        TProxyListener tproxy=new TProxyListener();
        tproxy.start();
        Thread.sleep(100);
        System.out.println("tproxy server running");
        //InetAddress x=InetAddress.getByName("localhost");
        //System.out.println(x);
        //InetSocketAddress y=linuxutils.getLocalHost();
        //System.out.println(y);
        System.out.println(linuxutils.getsockname(tproxy.getFd()).toInetSocketAddress());
        libnetlink.rtnl_handle handle=new libnetlink.rtnl_handle();
        linuxutils.rtnl_open(handle.handle,0);
        System.out.println(Hexdump.bytesToHex(handle.handle,4));

        while(true) {
            Thread.sleep(1000);
            //tundev.dumpSpeed();
            //tproxy.dumpTCPInfo();
        }
    }
}
