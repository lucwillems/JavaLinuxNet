/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class SocketUtils {
    private static final Logger log= LoggerFactory.getLogger(SocketUtils.class);

    public static SocketImpl getImplementation(final ServerSocket socket) {
        try {
            final Method method = ServerSocket.class.getDeclaredMethod("getImpl");
            method.setAccessible(true);
            return (SocketImpl) method.invoke(socket);
        } catch (final Exception jvmerror) {
            log.error("ServerSocket:",jvmerror);
        }
        return null;
    }

    public static SocketImpl getImplementation(final Socket socket) {
        try {
            final Method method = Socket.class.getDeclaredMethod("getImpl");
            method.setAccessible(true);
            return (SocketImpl) method.invoke(socket);
        } catch (final Exception jvmerror) {
            log.error("Socket:", jvmerror);
        }
        return null;
    }

    public static FileDescriptor getFileDescriptor(final ServerSocket socket) {
        try {
            final SocketImpl impl = getImplementation(socket);
            final Method method = SocketImpl.class.getDeclaredMethod("getFileDescriptor");
            method.setAccessible(true);
            return (FileDescriptor) method.invoke(impl);
        } catch (final Exception jvmerror) {
            log.error("ServerSocket:", jvmerror);
        }
        return null;
    }

    public static FileDescriptor getFileDescriptor(final Socket socket) {
        try {
            final SocketImpl impl = getImplementation(socket);
            final Method method = SocketImpl.class.getDeclaredMethod("getFileDescriptor");
            method.setAccessible(true);
            return (FileDescriptor) method.invoke(impl);
        } catch (final Exception jvmerror) {
            log.error("Socket:", jvmerror);
        }
        return null;
    }

    public static FileDescriptor getFileDescriptor(final FileOutputStream stream) {
        try {
            final Field privateFd = FileOutputStream.class.getDeclaredField("fd");
            privateFd.setAccessible(true);
            return (FileDescriptor) privateFd.get(stream);
        } catch (final Exception jvmerror) {
            log.error("FileOutputStream:", jvmerror);
        }
        return null;
    }

    public static FileDescriptor getFileDescriptor(final FileInputStream stream) {
        try {
            final Field privateFd = FileInputStream.class.getDeclaredField("fd");
            privateFd.setAccessible(true);
            return (FileDescriptor) privateFd.get(stream);
        } catch (final Exception jvmerror) {
            log.error("FileOutputStream:", jvmerror);
        }
        return null;
    }

    public static FileDescriptor getFileDescriptor(final RandomAccessFile random) {
        try {
            final Field privateFd = RandomAccessFile.class.getDeclaredField("fd");
            privateFd.setAccessible(true);
            return (FileDescriptor) privateFd.get(random);
        } catch (final Exception jvmerror) {
            log.error("RandomAccessFile:", jvmerror);
        }
        return null;
    }

    public static int getFileHandle(final FileDescriptor fd) {
        //Get FD field value
        try {
            final Field privateFd = FileDescriptor.class.getDeclaredField("fd");
            privateFd.setAccessible(true);
            return ((Integer) privateFd.get(fd)).intValue();
        } catch (final Exception jvmerror) {
            log.error("RandomAccessFile:", jvmerror);
        }
        return -1;
    }

    public static int getFd(final ServerSocket socket) {
        final FileDescriptor fd = getFileDescriptor(socket);
        if (fd == null) {
            return -1;
        }
        return getFileHandle(fd);
    }



    public static int getFd(final Socket socket) {
        final FileDescriptor fd = getFileDescriptor(socket);
        if (fd == null) {
            return -1;
        }
        return getFileHandle(fd);
    }

    public static int getFd(final ServerSocketChannel socket ){
       return getFd(socket.socket());
    }

    public static int getFd(final SocketChannel socket ){
        return getFd(socket.socket());
    }

    public static int getFd(final FileOutputStream stream ){
        final FileDescriptor fd = getFileDescriptor(stream);
        if (fd == null) {
            return -1;
        }
        return getFileHandle(fd);
    }

    public static int getFd(final FileInputStream stream ){
        final FileDescriptor fd = getFileDescriptor(stream);
        if (fd == null) {
            return -1;
        }
        return getFileHandle(fd);
    }
    public static int getFd(final RandomAccessFile random ){
        final FileDescriptor fd = getFileDescriptor(random);
        if (fd == null) {
            return -1;
        }
        return getFileHandle(fd);
    }

}
