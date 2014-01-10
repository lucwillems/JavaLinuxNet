/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.netlink;

import java.nio.ByteBuffer;

/**
 * Created by luc on 1/3/14.
 */
public class NLDoneMessage extends NlMessage {
    public NLDoneMessage(ByteBuffer msg) {
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

    @Override
    public boolean moreMessages() {
        return false;
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(super.toString());
        s.append("netlink done");
        return s.toString();
    }
}
