/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.tuntap;

import org.it4y.jni.tuntap;

public class TunTapDevice extends tuntap {

    public TunTapDevice(String device) {
        super();
        this.device = device;
    }

    public void open() throws Exception {
        int errno;
        //open on name or let kernel chouse
        if (device != null) {
            errno = openTunDevice(device);
        } else {
            errno = openTun();
        }
        if (errno != 0) {
            throw new Exception("Could not open '/dev/net/tun!'\n" + "errno=" + errno);
        }
    }

    public String getDevice() {
        return device;
    }

}
