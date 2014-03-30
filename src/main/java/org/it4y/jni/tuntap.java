/*
 * Copyright 2014 Luc Willems (T.M.M.)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.it4y.jni;

import org.it4y.jni.libc.ErrnoException;

import java.nio.ByteBuffer;

/**
 * Created by luc on 12/28/13.
 * this class encapsulate the native methods for tuntap access
 * DO NOT REFACTORY this class our you are scruwed
 */

public class tuntap {
    /* Load libjnituntap.so */
    static {
        JNILoader.loadLibrary("libjnituntap");
        final int initResult=initLib();
        if (initResult != 0) {
            throw new RuntimeException("Error initialiaze libjnituntap : "+initResult);
        }
    }


    protected int fd = -1;
    protected String device;

    /***
     * Init JNI library
     * @return
     */
    private static native int initLib();

    /***
     * Open tun device on name, device must be created with ip tunnel command
     * @param device
     * @return 0 if ok
     */
    protected native int openTunDevice(String device) throws ErrnoException;

    /***
     * Open tun device, let kernel choose unique name
     * @return 0 if ok
     */
    protected native int openTun() throws ErrnoException;

    /***
     * close tunnel device, this will bring the interface up
     */
    public native void close();

    /***
     * Write @len bytes of @buffer data into tunnel
     * @param buffer
     * @param len
     */
    public native int writeByteBuffer(ByteBuffer buffer, int len) throws ErrnoException;

    /***
     * Read data from tunnel, incase tunnel is in  nonblocking, and nonblocking =true , we will not throw exception
     * @param buffer
     * @param nonBlocking
     * @return size of EAGAIN in case of non blocking and no data
     * @throws ErrnoException
     */
    public native int readByteBuffer(ByteBuffer buffer, boolean nonBlocking) throws ErrnoException;

    /***
     * Read data from tunnel device in buffer in blocking mode
     * @param buffer
     * @return number of bytes read
     */
    public int readByteBuffer(final ByteBuffer buffer) throws ErrnoException {
        return readByteBuffer(buffer,false);
    }

    /***
     * set tun device in block/non blocking mode
     * @param value
     * @return
     * @throws ErrnoException
     */
    public native int setNonBlocking(boolean value) throws ErrnoException;

    /***
     * Return true/false in data is ready . timeout is in msec
     * @param timeout
     * @return
     */
    public native boolean isDataReady(int timeout);

    /***
     * enable Queue
     * @param timeout
     * @return
     */
    /* not supported until 3.12 kernel
    public native int enableQueue(boolean enabled);
    */

}
