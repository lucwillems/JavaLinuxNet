package org.it4y.net.netlink;

import org.it4y.jni.libnetlink;

import java.nio.ByteBuffer;

/**
 * Created by luc on 1/2/14.
 */
public class InterfaceRTAMessages extends RTAMessage {

    public InterfaceRTAMessages(int pos, ByteBuffer buffer) {
        super(pos, buffer);
    }
    public String getRTAName() {
        return libnetlink.linux.if_link.RTA_NAMES[type];
    }
}
