package org.it4y.demo;

import org.it4y.net.protocols.IP.ICMP.ICMPPacket;
import org.it4y.net.protocols.IP.*;
import org.it4y.net.protocols.IP.UDP.UDPPacket;
import org.it4y.net.tuntap.TunDevice;
import org.it4y.util.Hexdump;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by luc on 12/27/13.
 */
public class TunTapInterfaceListener extends TestRunner {
    private final Logger log= LoggerFactory.getLogger(TunTapInterfaceListener.class);
    private final ByteBuffer bbuffer;
    private final TunDevice tundev;
    private final String dev;
    private final int mtu;
    private long cnt = 0;
    private long bits = 0;
    private boolean debug = false;
    private long pktcnt = 0;

    public TunTapInterfaceListener(final String dev, final int mtu) {
        super("tuntapListener-" + dev);
        this.dev = dev;
        this.mtu = mtu;
        tundev = new TunDevice(dev);
        bbuffer = ByteBuffer.allocateDirect(mtu);
    }

    public void hexDumpIn(final ByteBuffer buffer, final int size) {
        if (debug) {
            System.out.println(System.currentTimeMillis() + " (" + size + ") >" + Hexdump.bytesToHex(buffer, size));
        }
    }

    public void hexDumpOut(final ByteBuffer buffer, final int size) {
        if (debug) {
            System.out.println(System.currentTimeMillis() + " (" + size + ") <" + Hexdump.bytesToHex(buffer, size));
        }
    }

    public void run() {
        log.info("opening {} mtu: {}",dev,mtu);
        try {
            tundev.open();
        } catch (final Exception e) {
            log.error("ooeps... {}", e);
            return;
        }
        running = true;
        while (running) {
            try {
                //we must clear else we get issues
                bbuffer.clear();
                int size = tundev.readByteBuffer(bbuffer); //this will block until a packet is available
                pktcnt++;
                hexDumpIn(bbuffer, size);
                IpPacket ip = IPFactory.processRawPacket(bbuffer, size);
                if (ip != null) {
                    ip.getDstRoutingHash();//whats my hash
                    bits = bits + size;
                    cnt++;
                    if (ip.getProtocol() == IpPacket.ICMP) {
                        if (((ICMPPacket) ip).isEchoRequest()) {
                            ((ICMPPacket) ip).convertToEchoReply();
                            //write raw packet back to network
                            tundev.writeByteBuffer(ip.getRawPacket(), ip.getRawSize());
                            //System.out.println(ip.toString());
                            hexDumpOut(ip.getRawPacket(), ip.getRawSize());
                        }
                    } else if (ip.getProtocol() == IpPacket.UDP) {
                        //we must reset Buffer before manipulating it !!
                        //System.out.println("IP: " + ip.toString());
                        ip.resetBuffer();
                        //echo packet back to owner
                        ((UDPPacket) ip).swapSourceDestination();
                        ((UDPPacket) ip).updateChecksum();
                        ((UDPPacket) ip).swapSourceDestinationPort();
                        tundev.writeByteBuffer(ip.getRawPacket(), ip.getRawSize());
                        //hexDumpOut(ip.getRawPacket(), ip.getRawSize());
                    } else if (ip.getProtocol() == IpPacket.TCP) {
                        //we must reset Buffer before manipulating it !!
                        log.info("{}",ip);
                    } else {
                        log.info("{}", ip);
                    }
                }
            } catch (Throwable t) {
                //hell , it still java so it will break
                log.error("oeps..", t);
            }
        }

    }

    public void dumpSpeed() {
        if (bits > 0) {
            System.out.println("goodput: " + String.format("%.3f mbit/sec", (double) (bits * 8) / (1024 * 1024)) + " bytes: "+bits+" "+ cnt + " pkts: " + pktcnt);
        }
        bits = 0;
        cnt = 0;
    }
}
