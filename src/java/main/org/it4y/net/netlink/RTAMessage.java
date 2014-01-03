package org.it4y.net.netlink;

import org.it4y.jni.libc;
import org.it4y.jni.libnetlink;
import org.it4y.util.Hexdump;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by luc on 1/2/14.
 */
public abstract class RTAMessage {
    short size;
    short type;
    ByteBuffer data;

    public RTAMessage(int pos ,ByteBuffer buffer) {
       size=buffer.getShort(pos);
       type=buffer.getShort(pos+2);
       //get size bytes from buffer for data
       int old_position=buffer.position();
       int old_limit=buffer.limit();
       buffer.position(pos+4);
       buffer.limit(pos+size);
       data=buffer.slice();
       buffer.position(old_position);
       buffer.limit(old_limit);
    }
    public short getSize() {
        return size;
    }
    public short getType() {
        return type;
    }

    public int getPaddedSize() {
        //roundup to 4 bytes padding
        return (int)Math.ceil(size/4.0)*4;
    }

    public abstract String getRTAName() ;

    public String toString() {
        StringBuffer s=new StringBuffer();
        s.append("RTA[").append(size).append("(").append(getPaddedSize()).append("):");
        s.append(type);
        try {
            if (getRTAName() != null ) {
                s.append(" ").append(getRTAName());
            }
        } catch(IndexOutOfBoundsException oeps) {
            s.append(" ").append("???? unknown");
        }
        s.append("] ");
        s.append(Hexdump.bytesToHex(data,size));
        return s.toString();
    }

    public byte get() {
        return data.get();
    }
    public short getShort() {
        return data.getShort();
    }
    public int getInt() {
        return data.getInt();
    }
    public String getString() {
        //Strings are always UTF-8 null terminated string so convert it that way
        byte[] result = new byte[data.capacity()-1];
        data.get(result);
        String s=new String(result, Charset.forName("UTF-8"));
        return s;
    }

    public String getHexString() {
        return Hexdump.bytesToHex(data,6);
    }


    public InetAddress getInetAddress() {
        return libc.toInetAddress(data.getInt());
    }
}
