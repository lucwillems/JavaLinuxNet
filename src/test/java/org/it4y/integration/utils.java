/*
 * Copyright 20124 Luc Willems (T.M.M.)
 *
 * We licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.it4y.integration;

import org.it4y.net.protocols.IP.ICMP.ICMPPacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

/**
 * Created by luc on 1/18/14.
 */
public class utils {

    //See setup-test.sh script
    public static String TESTIP="8.8.4.4";
    public static int TESTPORT=1024;

    public static void sendTestUDP(int size) throws IOException {
        byte[] buffer = new byte[size];
        InetAddress address = InetAddress.getByName(TESTIP);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, TESTPORT);
        DatagramSocket socket = new DatagramSocket();
        socket.send(packet);
    }

    public static ByteBuffer getBadIpPacket(byte ipProtocol, int size) {
        //we need to write some bytes to tun device
        ByteBuffer buf=ByteBuffer.allocateDirect(size);
        //create dummy ICMP packet,
        buf.clear();
        buf.put((byte) 0x55);  //IPv5 + header size , this doesn't exist ofcourse
        buf.put((byte) 0x00);  //dscp
        buf.put((byte)60);    //size
        buf.putShort((byte) 0x00);
        buf.putShort((byte) 0x00);
        buf.put((byte) 0x40); //TTL
        buf.put(ipProtocol); //protocol
        return buf;
    }

    public static ICMPPacket getIcmpPing(int size) {
        //we need to write some bytes to tun device
        ByteBuffer buf=ByteBuffer.allocateDirect(size+28);
        //create dummy ICMP packet,
        ICMPPacket icmp=new ICMPPacket(buf,size);
        icmp.initIpHeader();
        icmp.setType(ICMPPacket.ECHO_REQUEST);
        icmp.setSourceAddress(0x7f000002);
        icmp.setDestinationAddress(0x7f000001);
        icmp.updateChecksum();
        return icmp;
    }

}
