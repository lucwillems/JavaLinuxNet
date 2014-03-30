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

package org.it4y.net.link;

import org.it4y.jni.libc;
import org.it4y.jni.linux.if_arp;

import java.net.InetAddress;

/**
 * Network interface class
 * Holds all information retrieved from netlink interface
 *
 */
public class NetworkInterface {
    private final String name;
    private final int index;
    private int state;
    private String macAddress = "";
    private int mtu;
    private int ipv4Address;
    private int ipv4Gateway;
    private int ipv4P2Paddress;
    private int netmask;
    private int interfaceFlag;
    private final int interfaceType;

    public NetworkInterface(final String name, final int index, final int interfaceFlag, final int interfaceType) {
        this.name = name;
        this.index = index;
        this.interfaceFlag=interfaceFlag;
        this.interfaceType=interfaceType;
    }

    /**
     * Return kernel index number of this interface
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * Return kernel state of the interface based on RFC2863 state of the interface in numeric representation:
     *  IF_OPER_xxx definitions
     * @return
     */
    public int getState() {
        return state;
    }

    /**
     * Returns kernel interface name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * get interface MTU in bytes
     * @return
     */
    public int getMtu() {return mtu;}

    /**
     * return MAC address from interface (if existing)
     * @return
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * return the primary ipv4 address in 32bits network notation
     * @return
     */
    public int getIpv4Address() {
        return ipv4Address;
    }

    /**
     * return remote ipv4 address in 32bits network notation of remote address P2P
     * @return
     */
    public int getIpv4P2Paddress() {
        return ipv4P2Paddress;
    }

    /**
     * return primary ipv4 address as InetAddress. this is a expensive conversion
     * @return
     */
    public InetAddress getIpv4AddressAsInetAddress() {
        return libc.toInetAddress(libc.ntohi(ipv4Address));
    }

    /**
     * return remote ipv4 address as InetAddress. of remote P2P address
     * @return
     */
    public InetAddress getIpv4P2PAddressAsInetAddress() {
        return libc.toInetAddress(libc.ntohi(ipv4P2Paddress));
    }

    /**
     * Return default gateway listed in main table in 32bit network notation
     * @return
     */
    public int getIpv4Gateway() {
        return ipv4Gateway;
    }

    /**
     * Return default gateway listed in main table in InetAddress, this is a expensive conversion
     * @return
     */
    public InetAddress getIpv4GatewayAsInetAddress() {
        return libc.toInetAddress(libc.ntohi(ipv4Gateway));
    }

    /**
     * return kernel interface flags , see man rtnetlink(7) ifi_flags
     * @return
     */
    public int getInterfaceFlag() {
        return interfaceFlag;
    }

    /**
     * is this a loopback interface
     * @return
     */
    public boolean isLoopBack() {
        return (interfaceFlag & 0x00008)>0;
    }

    /**
     * is P2P tunnel interface
     * @return
     */
    public boolean isPoint2Point() {
        return (interfaceFlag & 0x00010)>0;
    }

    /**
     * is interface up
     * @return
     */
    public boolean isUP() {
        return (interfaceFlag & 0x00001)>0;
    }

    /**
     * is lower technologie UP , for example cable conntected
     * @return
     */
    public boolean isLowerUP() {
        return (interfaceFlag & 0x10000)>0;
    }

    protected void setIpv4Gateway(final int address) {
        ipv4Gateway = address;
    }
    protected void setmtu(final int mtu) {
        this.mtu = mtu;
    }
    protected void setInterfaceFlag(final int flags) {
        interfaceFlag =flags;
    }
    protected void setNetmask(final int mask) {
        netmask = mask;
    }
    protected void setIpv4P2Paddress(final int address) {
        ipv4P2Paddress =address;
    }
    protected void setIpv4Address(final int address) {
        ipv4Address = address;
    }
    protected void setState(final int state) {
        this.state = state;
    }

    protected void setInterfaceFlags(final int flags) {
        interfaceFlag =flags;
    }
    protected void setMacAddress(final String macAddress) {
        this.macAddress = macAddress;
    }

    public String toString() {
        final StringBuilder s = new StringBuilder(128);
        s.append(name).append('[');
        s.append(if_arp.ARPHDR_NAMES.get(interfaceType)).append(',');
        s.append("idx:").append(index).append(',');
        if (ipv4Address != 0) {
            s.append("a:").append(getIpv4AddressAsInetAddress().toString().substring(1)).append('/').append(netmask).append(',');
        }
        if (ipv4Gateway != 0) {
            s.append("g:").append(getIpv4GatewayAsInetAddress().toString().substring(1)).append(',');
        }
        s.append("mtu:").append(mtu).append(',');
        s.append("state:").append(state).append(',');
        s.append("flags:0x").append(Integer.toHexString(interfaceFlag)).append(' ');
        if(isPoint2Point()) {
            s.append(",P2P");
            if (ipv4P2Paddress != 0) {
                s.append(' ').append(getIpv4P2PAddressAsInetAddress().toString().substring(1));
            }
        }
        if(isUP()) { s.append(",UP");}
        if(isLowerUP()) { s.append(",LOWER_UP");}
        s.append(']');
        s.append(isActive());
        return s.toString();
    }

    /**
     * Check if interface is active and usable for traffic
     * @return
     */
    public boolean isActive() {
        if (isLoopBack())         { return ipv4Address != 0 & mtu != 0 & netmask != 0 & isLowerUP();}
        else if (isPoint2Point()) { return ipv4Address != 0 & mtu != 0 & netmask != 0 & isLowerUP();}
        else {                      return ipv4Address != 0 & mtu != 0 & netmask != 0 & isLowerUP() & state==6; }
    }


}
