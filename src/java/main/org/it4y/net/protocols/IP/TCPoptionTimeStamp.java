package org.it4y.net.protocols.IP;

/**
 * Created by luc on 12/26/13.
 */
public class TCPoptionTimeStamp implements TCPOption {
    private int tsval = 0;
    private int tsecr = 0;

    public TCPoptionTimeStamp(int tsval, int tsecr) {
        this.tsval = tsval;
        this.tsecr = tsecr;
    }

    public String getName() {
        return "timestamps";
    }

    public int getLength() {
        return 10;
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(getName()).append("[");
        if (tsval != 0) {
            s.append((long) tsval);
        }
        if (tsval != 0 & tsecr != 0) {
            s.append(",");
        }
        if (tsecr != 0) {
            s.append((long) tsecr);
        }
        s.append("]");
        return s.toString();
    }
}
