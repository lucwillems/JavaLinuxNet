package org.it4y.net.protocols.IP.TCP;

import org.it4y.net.protocols.IP.TCP.TCPOption;

/**
 * Created by luc on 12/26/13.
 */
public class TCPoptionEnd implements TCPOption {
    public TCPoptionEnd() {
    }

    ;

    public String getName() {
        return "end";
    }

    public int getLength() {
        return 1;
    }

    public String toString() {
        return getName();
    }
}
