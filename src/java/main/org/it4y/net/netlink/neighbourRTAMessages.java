package org.it4y.net.netlink;

import org.it4y.jni.libnetlink;

import java.nio.ByteBuffer;

/**
 * Created by luc on 1/2/14.
 */
public class neighbourRTAMessages extends RTAMessage {
    public neighbourRTAMessages(int pos, ByteBuffer buffer) {
        super(pos, buffer);
    }

    @Override
    public String getRTAName() {
        return libnetlink.linux.if_neighbour.RTA_NAMES[type];
    }
}
