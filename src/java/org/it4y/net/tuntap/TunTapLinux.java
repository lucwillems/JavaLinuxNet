/*
    Copyright 2008, 2009 Wolfgang Ginolas

    This file is part of P2PVPN.

    P2PVPN is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Foobar is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.it4y.net.tuntap;

import org.it4y.jni.JNILoader;

import java.nio.ByteBuffer;

public class TunTapLinux {
    /* Load libtuntap.so */
    static {
    	JNILoader.loadLibrary("libtuntap.so");
    }

    private int fd;
    private String device;

    public TunTapLinux(String device)  {
        this.device=device;
        this.fd=-1;
    }

    public void open() throws Exception {
        int errno;
        //open on name or let kernel chouse
        if (device != null) {
            errno=openTunDevice(device);
        } else {
            errno=openTun();
        }
        if (errno != 0) {
          throw new Exception("Could not open '/dev/net/tun!'\n" +
                "errno="+errno);
        }
    }

    public String getDevice() {
        return device;
    }

    private native int openTunDevice(String device);
    private native int openTun();
    public  native void close();
    public native void write(byte[] b, int len);
    public native void writeByteBuffer(ByteBuffer buffer,int len);
    public native int read(byte[] b);
    public native int readByteBuffer(ByteBuffer buffer);

}
