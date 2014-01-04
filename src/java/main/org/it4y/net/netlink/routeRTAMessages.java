package org.it4y.net.netlink;

import org.it4y.jni.libnetlink;
import org.it4y.jni.linux.rtnetlink;

import java.nio.ByteBuffer;

/**
 * Created by luc on 1/2/14.
 */
public class routeRTAMessages extends RTAMessage {

    public routeRTAMessages(int pos, ByteBuffer buffer) {
        super(pos, buffer);
    }

    @Override
    public String getRTAName() {
        return rtnetlink.RTA_NAMES[type];
    }
}
