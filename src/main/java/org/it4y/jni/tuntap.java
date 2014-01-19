/*
 * This file is derived from Linux kernel headers
 * All copyrights preserved to original kernel author.
 *
 * more info see https://www.kernel.org/pub/linux/kernel/COPYING
 */

package org.it4y.jni;

import java.nio.ByteBuffer;
import org.it4y.jni.libc.ErrnoException;

/**
 * Created by luc on 12/28/13.
 * this class encapsulate the native methods for tuntap access
 * DO NOT REFACTORY this class our you are scruwed
 */

public class tuntap {
    /* Load libjnituntap.so */
    static {
        JNILoader.loadLibrary("libjnituntap.so");
        final int initResult=initLib();
        if (initResult != 0) {
            throw new RuntimeException("Error initialiaze libjnituntap.so: "+initResult);
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
