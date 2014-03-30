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

package org.it4y.net.netlink;

import org.it4y.jni.libc;
import org.it4y.util.Hexdump;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * Created by luc on 1/2/14.
 */
public abstract class RTAMessage {
    final short size;
    final short type;
    final ByteBuffer data;

    public RTAMessage(final int pos, final ByteBuffer buffer) {
        size = buffer.getShort(pos);
        type = buffer.getShort(pos + 2);
        //get size bytes from buffer for data
        final int old_position = buffer.position();
        final int old_limit = buffer.limit();
        buffer.position(pos + 4);
        buffer.limit(pos + size);
        data = buffer.slice();
        data.order(ByteOrder.LITTLE_ENDIAN);
        buffer.position(old_position);
        buffer.limit(old_limit);
        //mark start position
        data.mark();
    }

    public short getSize() {
        return size;
    }

    public short getType() {
        return type;
    }

    public int getPaddedSize() {
        //roundup to 4 bytes padding
        return (int) Math.ceil(size / 4.0) * 4;
    }

    public abstract String getRTAName();

    public String toString() {
        final StringBuilder s = new StringBuilder(128);
        s.append("RTA[").append(size).append('(').append(getPaddedSize()).append("):");
        s.append(type);
        try {
            if (getRTAName() != null) {
                s.append(' ').append(getRTAName());
            }
        } catch (final IndexOutOfBoundsException oeps) {
            s.append(' ').append("???? unknown");
        }
        s.append("] ");
        s.append(Hexdump.bytesToHex(data, size));
        return s.toString();
    }

    public byte getByte() {
        return data.get(0);
    }

    public short getShort() {
        return data.getShort(0);
    }

    public int getInt() {
        return data.getInt(0);
    }

    public String getString() {
        //Strings are always UTF-8 null terminated string so convert it that way
        final byte[] result = new byte[data.capacity() - 1];
        data.rewind();
        data.get(result, 0, result.length);
        return new String(result, Charset.forName("UTF-8"));
    }

    public String getHexString() {
        return Hexdump.bytesToHex(data, 6);
    }


    public InetAddress getInetAddress() {
        return libc.toInetAddress(libc.ntohi(data.getInt(0)));
    }
}
