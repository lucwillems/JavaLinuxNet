package org.it4y.net.netlink;

import org.it4y.util.Hexdump;

import java.nio.ByteBuffer;

/**
 * Created by luc on 1/3/14.
 */
public class NlErrorMessage extends NlMessage {

    public NlErrorMessage(ByteBuffer msg) {
        super(msg);
    }

    @Override
    public int getRTAIndex(String name) {
        return -1;
    }

    @Override
    public RTAMessage createRTAMessage(int position, ByteBuffer msg) {
        return null;
    }

    public String toString() {
        StringBuffer s=new StringBuffer();
        s.append(super.toString());
        s.append("netlink ERROR ");
        return s.toString();
    }

}
