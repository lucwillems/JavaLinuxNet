/*
 * Copyright 2014 Luc Willems (T.M.M.)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.it4y.net.protocols.IP.TCP;

public class TCPoptionTimeStamp implements TCPOption {
    public static final String name="timestamps";
    public static final int length=10;

    private int tsval = 0;
    private int tsecr = 0;

    public TCPoptionTimeStamp(int tsval, int tsecr) {
        this.tsval = tsval;
        this.tsecr = tsecr;
    }

    public String getName() {return name; }

    public int getLength() {
        return length;
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
