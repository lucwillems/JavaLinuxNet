package org.it4y.net.netlink;

import org.it4y.jni.linux.if_link;

import java.nio.ByteBuffer;

/**
 * Created by luc on 1/2/14.
 */
public class InterfaceRTAMessages extends RTAMessage {

    public InterfaceRTAMessages(final int pos, final ByteBuffer buffer) {
        super(pos, buffer);
    }

    public String getRTAName() {
        return if_link.IFLA_NAMES.get((int)type&0xffff);
    }
}
