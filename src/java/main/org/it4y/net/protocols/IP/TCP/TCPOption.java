package org.it4y.net.protocols.IP.TCP;

/**
 * Created by luc on 12/26/13.
 */
public interface TCPOption {

    //WELL KNOW TCP options id's
    public final static byte END = 0;
    public final static byte NOP = 1;
    public final static byte MSS = 2;
    public final static byte WSCALE = 3;
    public final static byte SACK_ENABLED = 4;
    public final static byte SACK = 5;
    public final static byte TIMESTAMP = 8;

    public String getName();

    public int getLength();
}
