package org.it4y.net.protocols.IP;

/**
 * Created by luc on 12/26/13.
 */
public class TCPoptionMSS implements TCPOption {
    short mss;
    public TCPoptionMSS(short mss) {
        this.mss=mss;
    }
    public String getName() {
        return "mss";
    }
    public int getLength() {
        return 4;
    }
    public String toString() {
        return "mss="+((int)mss & 0xffff);
    }
}
