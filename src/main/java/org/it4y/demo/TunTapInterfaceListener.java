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

package org.it4y.demo;

import com.google.common.util.concurrent.RateLimiter;
import org.it4y.net.protocols.IP.ICMP.ICMPPacket;
import org.it4y.net.protocols.IP.IPFactory;
import org.it4y.net.protocols.IP.IpPacket;
import org.it4y.net.protocols.IP.TCP.TCPPacket;
import org.it4y.net.protocols.IP.UDP.UDPPacket;
import org.it4y.net.tuntap.TunDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by luc on 12/27/13.
 */
public class TunTapInterfaceListener extends TestRunner {
    private final Logger log= LoggerFactory.getLogger(TunTapInterfaceListener.class);
    private final ByteBuffer bbuffer;
    private final TunDevice tundev;
    private final String dev;
    private final int mtu;
    private long cnt;
    private final AtomicLong bytes = new AtomicLong(0L);
    private static final boolean debug=false;
    private long pktcnt;

    //private BPFProgram program;
    //private BpfVM virtualMachine;

    //rate limit
    static final long rateInMbit=50000000; //100Mbit/sec
    final RateLimiter Commonlimiter = RateLimiter.create(rateInMbit*100/800);
    final RateLimiter icmplimiter   = RateLimiter.create(rateInMbit*50/800); // 50%

    // IPV4 : icmp[icmptype]==8  || icmp[icmptype]==0
    public static byte[] bpf_PING = {
            //size opcode:16  jt:8  jf:8  k:32  (8 bytes)
            //0001 : 0x0 0x0 0x0 0x0
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            //0002 : 0x30 0x0 0x0 0x9
            (byte) 0x00, (byte) 0x30, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x09,
            //0003 : 0x15 0x0 0x7 0x1
            (byte) 0x00, (byte) 0x15, (byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01,
            //0004 : 0x28 0x0 0x0 0x6
            (byte) 0x00, (byte) 0x28, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x06,
            //0005 : 0x45 0x5 0x0 0x1fff
            (byte) 0x00, (byte) 0x45, (byte) 0x05, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1f, (byte) 0xff,
            //0006 : 0xb1 0x0 0x0 0x0
            (byte) 0x00, (byte) 0xb1, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            //0007 : 0x50 0x0 0x0 0x0
            (byte) 0x00, (byte) 0x50, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            //0008 : 0x15 0x1 0x0 0x8
            (byte) 0x00, (byte) 0x15, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08,
            //0009 : 0x15 0x0 0x1 0x0
            (byte) 0x00, (byte) 0x15, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            //0010 : 0x6 0x0 0x0 0xffff
            (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff,
            //0011 : 0x6 0x0 0x0 0x0
            (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
    };


    public TunTapInterfaceListener(final String dev, final int mtu) {
        super("tuntapListener-" + dev);
        this.dev = dev;
        this.mtu = mtu;
        tundev = new TunDevice(dev);
        bbuffer = ByteBuffer.allocateDirect(mtu);
//        try {
//            program=new BPFProgram(bpf_PING);
//        } catch (IllegalInstructionException e) {
//            e.printStackTrace();
//        }
//        virtualMachine= BpfFactory.getForThread();
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
                final int size = tundev.readByteBuffer(bbuffer); //this will block until a packet is available
                Commonlimiter.acquire(size);
                pktcnt++;
                final IpPacket ip = IPFactory.processRawPacket(bbuffer, size);
                if (ip != null) {
                    ip.getDstRoutingHash();//whats my hash
                    bytes.addAndGet(size);
                    cnt++;
                    if (ip.getProtocol() == ICMPPacket.PROTOCOL) {
                        icmplimiter.acquire(size);
                        //log.info("{}",ip);
                        if (((ICMPPacket) ip).isEchoRequest()) {
                        //if (virtualMachine.execute(program,ip.getRawPacket(),ip.getRawSize(),ip.getRawSize()) == 0xffff) {
                            //if (pktcnt % 100 != 0)  {
                                ((ICMPPacket) ip).convertToEchoReply();
                                //write raw packet back to network
                                tundev.writeByteBuffer(ip.getRawPacket(), ip.getRawSize());
                                //System.out.println(ip.toString());
                                //hexDumpOut(ip.getRawPacket(), ip.getRawSize());
                            //} else {
                            //    log.info("Dropped: {}",ip);
                            //    hexDumpOut(ip.getRawPacket(),ip.getRawSize());
                            //}
                        }
                    } else if (ip.getProtocol() == UDPPacket.PROTOCOL) {
                        //we must reset Buffer before manipulating it !!
                        //System.out.println("IP: " + ip.toString());
                        ip.resetBuffer();
                        //echo packet back to owner
                        ip.swapSourceDestination();
                        ip.updateChecksum();
                        ((UDPPacket) ip).swapSourceDestinationPort();
                        tundev.writeByteBuffer(ip.getRawPacket(), ip.getRawSize());
                        //hexDumpOut(ip.getRawPacket(), ip.getRawSize());
                    } else if (ip.getProtocol() == TCPPacket.PROTOCOL) {
                        //we must reset Buffer before manipulating it !!
                        log.info("{}",ip);
                    } else {
                        log.info("{}", ip);
                    }
                }
            } catch (final Throwable t) {
                //hell , it still java so it will break
                log.error("oeps..", t);
            }
        }

    }

    public void dumpSpeed() {
        if (bytes.intValue()>0) {
            final long v=bytes.getAndSet(0);
            log.info("goodput: " + String.format("%.3f mbit/sec", (double) (v * 8) / (1024 * 1024)) + " bytes: "+ v + ' ' + cnt + " pkts: " + pktcnt);
        }
        cnt = 0;
    }
}
