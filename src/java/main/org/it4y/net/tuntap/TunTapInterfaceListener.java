package org.it4y.net.tuntap;

import org.it4y.net.protocols.IP.UDPPacket;
import org.it4y.net.protocols.ICMPPacket;
import org.it4y.net.protocols.IP.IPFactory;
import org.it4y.net.protocols.IP.IpPacket;
import org.it4y.net.protocols.IP.TCPOption;
import org.it4y.net.protocols.IP.TCPPacket;
import org.it4y.net.tuntap.TunTapLinux;

import java.nio.ByteBuffer;

/**
 * Created by luc on 12/27/13.
 */
public class TunTapInterfaceListener extends Thread {
    private String dev=null;
    private int mtu=0;
    private ByteBuffer bbuffer;
    private TunTapLinux tundev;
    private boolean running=false;
    private static long last=0;
    private static long cnt=0;
    private static long bits=0;


    public TunTapInterfaceListener(String name, int mtu) {
        super("InterfaceListener-"+name);
        this.dev=name;
        this.mtu=mtu;
        tundev=new TunTapLinux(name);
        bbuffer=ByteBuffer.allocateDirect(mtu);
    }

    public void halt() {
      running=false;
    }

    public void run() {
        try {
            tundev.open();
        } catch (Exception e) {
            System.out.println("ooeps..."+e.getMessage());
            return;
        }
        running=true;
        while(running) {
            try {
                //we must clear else we get issues
                bbuffer.clear();
                int size=tundev.readByteBuffer(bbuffer); //this will block until a packet is available
                IpPacket ip= IPFactory.processRawPacket(bbuffer, size);
                if (ip != null) {
                    bits=bits+size;
                    cnt++;
                    ByteBuffer data=ip.getPayLoad();
                    ByteBuffer header=ip.getHeader();
                    //System.out.println(ip.getClass().getCanonicalName()+":"+ip.getRawSize()+" "+header.position()+" "+header.limit()+" "+header.capacity()+" - "+data.position()+" "+data.limit()+" "+data.capacity());
                    if (ip.getProtocol() == ip.ICMP) {
                        if (((ICMPPacket) ip).isEchoRequest()) {
                            ((ICMPPacket)ip).convertToEchoReply();
                            //write raw packet back to network
                            tundev.writeByteBuffer(ip.getRawPacket(),ip.getRawSize());
                        }
                    }
                    else if (ip.getProtocol() == ip.UDP) {
                        //we must reset Buffer before manipulating it !!
                        ip.resetBuffer();
                        //echo packet back to owner
                        ip.swapSourceDestination();
                        ip.updateChecksum();
                        ((UDPPacket)ip).swapSourceDestinationPort();
                        tundev.writeByteBuffer(ip.getRawPacket(),ip.getRawSize());
                    }
                    else if (ip.getProtocol() == ip.TCP) {
                        //we must reset Buffer before manipulating it !!
                        System.out.println(ip.toString());
                        TCPOption x=((TCPPacket)ip).getOptionByType(TCPOption.TIMESTAMP);
                        System.out.println(x);
                    } else {
                        System.out.println(ip.toString());
                    }
                }
            } catch (Throwable t) {
                //hell , it still java so it will break
                System.out.println(t);
                t.printStackTrace();
            }
        }

    }

    public void dumpSpeed() {
        System.out.println("mbit/sec: "+bits*8+" "+cnt);
        bits=0;
        cnt=0;
        last=System.currentTimeMillis();
    }
}
