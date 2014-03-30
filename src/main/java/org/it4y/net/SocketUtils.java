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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class SocketUtils {
    private static final Logger log= LoggerFactory.getLogger(SocketUtils.class);

    private static Method ServerSocketGetImpl;
    private static Method SocketGetImpl;
    private static Method SocketGetFileDescriptor;
    private static Method ServerSocketGetFileDescriptor;
    private static Method SocketImplGetFileDescriptor;
    private static Method ServerSocketImplGetFileDescriptor;
    private static Field  FileOutputStreamFileDescriptor;
    private static Field  FileInputStreamFileDescriptor;
    private static Field  RandomAccessFileDescriptor;
    private static Field  privateFd;


    static {
        log.info("init socketUtils");
        try {
            /**
             * This greatly depends on JVM implementations !!!
             */
            ServerSocketGetImpl = ServerSocket.class.getDeclaredMethod("getImpl");
            SocketGetImpl = Socket.class.getDeclaredMethod("getImpl");
            SocketImplGetFileDescriptor = SocketImpl.class.getDeclaredMethod("getFileDescriptor");
            FileOutputStreamFileDescriptor=FileOutputStream.class.getDeclaredField("fd");
            FileInputStreamFileDescriptor=FileInputStream.class.getDeclaredField("fd");
            RandomAccessFileDescriptor=RandomAccessFile.class.getDeclaredField("fd");
            privateFd=FileDescriptor.class.getDeclaredField("fd");
        } catch (Throwable t) {
            throw new JVMException(t);
        }
        ServerSocketGetImpl.setAccessible(true);
        SocketGetImpl.setAccessible(true);
        SocketImplGetFileDescriptor.setAccessible(true);
        FileOutputStreamFileDescriptor.setAccessible(true);
        FileInputStreamFileDescriptor.setAccessible(true);
        RandomAccessFileDescriptor.setAccessible(true);
        privateFd.setAccessible(true);
    }


    public static SocketImpl getImplementation(final ServerSocket socket) throws InvocationTargetException, IllegalAccessException {
        return (SocketImpl) ServerSocketGetImpl.invoke(socket);
    }

    public static SocketImpl getImplementation(final Socket socket) throws InvocationTargetException, IllegalAccessException {
        return (SocketImpl) SocketGetImpl.invoke(socket);
    }

    public static FileDescriptor getFileDescriptor(final ServerSocket socket) throws InvocationTargetException, IllegalAccessException {
        final SocketImpl impl = (SocketImpl) ServerSocketGetImpl.invoke(socket);
        return (FileDescriptor) SocketImplGetFileDescriptor.invoke(impl);
    }

    public static FileDescriptor getFileDescriptor(final Socket socket) throws InvocationTargetException, IllegalAccessException {
            final SocketImpl impl = (SocketImpl) SocketGetImpl.invoke(socket);
            return (FileDescriptor) SocketImplGetFileDescriptor.invoke(impl);
    }

    public static FileDescriptor getFileDescriptor(final FileOutputStream stream) throws IllegalAccessException {
           return (FileDescriptor) FileOutputStreamFileDescriptor.get(stream);
    }

    public static FileDescriptor getFileDescriptor(final FileInputStream stream) throws IllegalAccessException {
            return (FileDescriptor) FileInputStreamFileDescriptor.get(stream);
    }

    public static FileDescriptor getFileDescriptor(final RandomAccessFile random) throws IllegalAccessException {
            return (FileDescriptor) RandomAccessFileDescriptor.get(random);
    }

    public static int getFileHandle(final FileDescriptor fd) throws IllegalAccessException {
        //Get FD field value
            return ((Integer) privateFd.get(fd)).intValue();
    }

    public static int getFd(final ServerSocket socket) {
        try {
            final FileDescriptor fd = getFileDescriptor(socket);
            return getFileHandle(fd);
        } catch (Exception shoutNotHappen) {
            throw new JVMException(shoutNotHappen);
        }
    }



    public static int getFd(final Socket socket) throws JVMException {
        try {
        final FileDescriptor fd = getFileDescriptor(socket);
        return getFileHandle(fd);
        } catch (Exception shoutNotHappen) {
            throw new JVMException(shoutNotHappen);
        }
    }

    public static int getFd(final ServerSocketChannel socket ) {
       return getFd(socket.socket());
    }

    public static int getFd(final SocketChannel socket ) {
        return getFd(socket.socket());
    }

    public static int getFd(final FileOutputStream stream )  {
        try {
        final FileDescriptor fd = getFileDescriptor(stream);
        return getFileHandle(fd);
        } catch (Exception shoutNotHappen) {
            throw new JVMException(shoutNotHappen);
        }
    }

    public static int getFd(final FileInputStream stream )  {
        try {
            final FileDescriptor fd = getFileDescriptor(stream);
            return getFileHandle(fd);
        } catch (Exception shoutNotHappen) {
            throw new JVMException(shoutNotHappen);
        }
    }
    public static int getFd(final RandomAccessFile random ) {
        try {
            final FileDescriptor fd = getFileDescriptor(random);
            return getFileHandle(fd);
        } catch (Exception shoutNotHappen) {
            throw new JVMException(shoutNotHappen);
        }
    }

}
