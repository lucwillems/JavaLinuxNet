package org.it4y.net.link;

import org.it4y.jni.libc;
import org.it4y.jni.linux.if_arp;

import java.net.InetAddress;

/**
 * Network interface class
 * Holds all information retrieved from netlink interface
 *
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
        return libc.toInetAddress(libc.ntol(ipv4Address));
    }

    /**
     * return remote ipv4 address as InetAddress. of remote P2P address
     * @return
     */
    public InetAddress getIpv4P2PAddressAsInetAddress() {
        return libc.toInetAddress(libc.ntol(ipv4P2Paddress));
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
        return libc.toInetAddress(libc.ntol(ipv4Gateway));
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

    protected void setIpv4Gateway(int address) {
        this.ipv4Gateway = address;
    }
    protected void setmtu(int mtu) {
        this.mtu = mtu;
    }
    protected void setInterfaceFlag(int flags) {
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
    protected void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
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
