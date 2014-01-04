package org.it4y.net.link;

import org.it4y.jni.libc;
import org.it4y.jni.libnetlink;
import org.it4y.jni.linux.if_arp;

import java.net.InetAddress;

/**
 * Created by luc on 1/3/14.
 */
public class NetworkInterface {
    private String name;
    private int index;
    private int state;
    private String macAddress = "";
    private int mtu;
    private int ipv4Address;
    private int ipv4Gateway;
    private int ipv4P2Paddress;
    private int netmask;
    private int interfaceFlag;
    private int interfaceType;

    public NetworkInterface(String name, int index, int interfaceFlag,int interfaceType) {
        this.name = name;
        this.index = index;
        this.interfaceFlag=interfaceFlag;
        this.interfaceType=interfaceType;
    }

    public int getIndex() {
        return index;
    }

    public int getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public int getMtu() {return mtu;}

    public String getMacAddress() {
        return macAddress;
    }

    protected void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public int getIpv4Address() {
        return ipv4Address;
    }
    public int getIpv4P2Paddress() {
        return ipv4P2Paddress;
    }

    public InetAddress getIpv4AddressAsInetAddress() {
        return libc.toInetAddress(libc.ntol(ipv4Address));
    }
    public InetAddress getIpv4P2PAddressAsInetAddress() {
        return libc.toInetAddress(libc.ntol(ipv4P2Paddress));
    }

    public int getIpv4Gateway() {
        return ipv4Gateway;
    }

    protected void setIpv4Gateway(int address) {
        this.ipv4Gateway = address;
    }

    public InetAddress getIpv4GatewayAsInetAddress() {
        return libc.toInetAddress(libc.ntol(ipv4Gateway));
    }

    public int getInterfaceFlag() {
        return interfaceFlag;
    }

    protected void setmtu(int mtu) {
        this.mtu = mtu;
    }

    public boolean isLoopBack() {
        return (interfaceFlag & 0x00008)>0;
    }
    public boolean isPoint2Point() {
        return (interfaceFlag & 0x00010)>0;
    }
    public boolean isUP() {
        return (interfaceFlag & 0x00001)>0;
    }
    public boolean isLowerUP() {
        return (interfaceFlag & 0x10000)>0;
    }
    public void setInterfaceFlag(int flags) {
        this.interfaceFlag=flags;
    }

    protected void setNetmask(int mask) {
        this.netmask = mask;
    }
    protected void setIpv4P2Paddress(int address) {
        this.ipv4P2Paddress =address;
    }

    protected void setIpv4Address(int address) {
        this.ipv4Address = address;
    }

    protected void setState(int state) {
        this.state = state;
    };
    protected void setInterfaceFlags(int flags) {
        this.interfaceFlag=flags;
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(name).append("[");
        s.append(if_arp.ARPHDR_NAMES.get(interfaceType)).append(",");
        s.append("idx:").append(index).append(",");
        if (this.ipv4Address != 0) {
            s.append("a:").append(getIpv4AddressAsInetAddress().toString().substring(1)).append("/").append(netmask).append(",");
        }
        if (this.ipv4Gateway != 0) {
            s.append("g:").append(getIpv4GatewayAsInetAddress().toString().substring(1)).append(",");
        }
        s.append("mtu:").append(mtu).append(",");
        s.append("state:").append(state).append(",");
        s.append("flags:0x").append(Integer.toHexString(interfaceFlag)).append(" ");
        if(isPoint2Point()) {
            s.append(",P2P");
            if (ipv4P2Paddress != 0) {
                s.append(" ").append(getIpv4P2PAddressAsInetAddress().toString().substring(1));
            }
        }
        if(isUP()) { s.append(",UP");}
        if(isLowerUP()) { s.append(",LOWER_UP");}
        s.append("]");

        return s.toString();
    }

    public boolean isActive() {
        if (isLoopBack())         { return ipv4Address != 0 & mtu != 0 & netmask != 0 & isLowerUP();}
        else if (isPoint2Point()) { return ipv4Address != 0 & mtu != 0 & netmask != 0 & isLowerUP();}
        else {                      return ipv4Address != 0 & mtu != 0 & netmask != 0 & isLowerUP() & state==6; }
    }


}
