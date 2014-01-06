package org.it4y.net.protocols.IP.TCP;

import org.it4y.net.protocols.IP.TCP.TCPOption;

/**
 * Created by luc on 12/26/13.
 */
public class TCPoptionWindowScale implements TCPOption {
    byte scale;

    public TCPoptionWindowScale(byte scale) {
        this.scale = scale;
    }

    public String getName() {
        return "wscale";
    }

    public int getLength() {
        return 3;
    }

    public String toString() {
        return "wscale:" + ((int) (scale) & 0x00ff);
    }
}
