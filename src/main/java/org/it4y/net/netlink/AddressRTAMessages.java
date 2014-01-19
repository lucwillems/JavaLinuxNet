
/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.netlink;

import org.it4y.jni.linux.if_address;

import java.nio.ByteBuffer;

/**
 * Created by luc on 1/2/14.
 */
public class AddressRTAMessages extends RTAMessage {

    public AddressRTAMessages(final int pos, final ByteBuffer buffer) {
        super(pos, buffer);
    }

    @Override
    public String getRTAName() {
        return if_address.IFA_NAMES.get((int)type&0xffff);
    }
}
