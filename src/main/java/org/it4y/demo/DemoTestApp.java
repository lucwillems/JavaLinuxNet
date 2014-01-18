package org.it4y.demo;

import org.it4y.net.link.LinkManager;
import org.it4y.net.link.LinkNotification;
import org.it4y.net.link.NetworkInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by luc on 12/25/13.
 * run this to have the tun device ready (as root):
 * ip tuntap add dev luc mode tun user luc group users
 * ip addr add 127.0.0.2/32 dev luc
 * ip link set dev luc up
 * ip route add 8.8.4.4/32 via luc
 * and now ping to 8.8.4.4
 */
public class DemoTestApp {
    private static Logger log= LoggerFactory.getLogger("Demo");

    public static void main(String[] args) throws Exception {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");
        TProxyListener tproxy = new TProxyListener();
        tproxy.start();
        log.info("tproxy server running");
        Thread.sleep(100);
        TunTapInterfaceListener tundev = new TunTapInterfaceListener("test", 1500);
        tundev.start(); //this will bring interface luc UP
        log.info("tun interface listener running");
        LinkManager lnkMng=new LinkManager();
        lnkMng.registerListener(LinkNotification.EventAction.None,LinkNotification.EventType.All, new LinkNotification() {
            @Override
            public void onEvent(EventAction action, EventType type, NetworkInterface network) {
                log.info("onEvent : "+action+" "+type+" "+network);
            }

            @Override
            public void onStateChanged(NetworkInterface network) {
                log.info("onStateChanged : "+network+" active:"+network.isActive());
            }
        });
        lnkMng.start();
        Thread.sleep(500);
        log.info("ready to rock and roll");
        while (true) {
            Thread.sleep(1000);
            tundev.dumpSpeed();
        }
    }
}
