/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.protocols.IP.TCP;

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
        s.append(getName()).append(":(");
        if (tsval != 0) {
            s.append((long) tsval & 0xffffffffL);
        }
        if (tsval != 0 & tsecr != 0) {
            s.append(",");
        }
        if (tsecr != 0) {
            s.append((long) tsecr & 0xffffffffL);
        }
        s.append(")");
        return s.toString();
    }

    public int getTsval() {
        return tsval;
    }
    public int getTsecr() {
        return tsecr;
    }
}
