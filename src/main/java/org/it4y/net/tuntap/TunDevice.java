/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.tuntap;

import org.it4y.jni.libc;
import org.it4y.jni.tuntap;

public class TunDevice extends tuntap {

    public TunDevice() {
        super();
    }

    public TunDevice(String device) {
        super();
        this.device = device;
    }

    public void open() throws libc.ErrnoException {
        int errno;
        //open on name or let kernel chouse
        if (device != null) {
            openTunDevice(device);
        } else {
            openTun();
        }
    }

    public String getDevice() {
        return device;
    }
    public int getFd() {return fd;}

}
