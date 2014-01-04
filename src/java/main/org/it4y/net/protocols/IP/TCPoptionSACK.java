package org.it4y.net.protocols.IP;

/**
 * Created by luc on 12/26/13.
 */
public class TCPoptionSACK implements TCPOption {

    public TCPoptionSACK() {
    }

    public String getName() {
        return "SACK";
    }

    public int getLength() {
        return 2;
    }

    public String toString() {
        return getName();
    }
}
