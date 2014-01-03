package org.it4y.net.netlink;

import org.it4y.jni.libc;

import java.net.Inet4Address;
import java.net.InetAddress;

/**
 * Created by luc on 1/3/14.
 */
public class NetworkInterface {
    private String name;
    private int index;
    private int state;
    private String macAddress="";
    private int mtu;
    private InetAddress ipv4Address;


    public NetworkInterface(String name,int index) {
        this.name=name;
        this.index=index;
    }

    public int getIndex() {return index;}
    public int getState() {return state;};
    public String getName() { return name;};
    public String getMacAddress() { return macAddress;}
    public InetAddress getIpv4Address() { return ipv4Address;}

    protected void setState(int state) {
        this.state=state;
    }
    protected void setMacAddress(String macAddress) {
        this.macAddress=macAddress;
    }
    protected void setmtu(int mtu) {
        this.mtu=mtu;
    }
    protected void setIpv4Address(int address) {
        this.ipv4Address= libc.toInetAddress(address);
    }
}
