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
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class SocketUtils {
    private static final Logger log= LoggerFactory.getLogger(SocketUtils.class);

    private static final Method ServerSocketGetImpl;
    private static final Method SocketGetImpl;
    private static final Method DatagramSocketGetImpl;
    private static Method SocketGetFileDescriptor;
    private static Method ServerSocketGetFileDescriptor;
    private static Method DatagramChannelImplGetFileDescriptor;
    private static final Method SocketImplGetFileDescriptor;
    private static Method ServerSocketImplGetFileDescriptor;
    private static final Field  FileOutputStreamFileDescriptor;
    private static final Field  FileInputStreamFileDescriptor;
    private static final Field  RandomAccessFileDescriptor;
    private static final Field  privateFd;


    static {
        log.info("init socketUtils");
        try {
            /**
             * This greatly depends on JVM implementations !!!
             * we need access to internal fd of the socket/file. to do that we need
             * to access private fields :-(
             * This will fail if someone changes the little details
             * tested with JDK1.7 and 1.6
             */
            ServerSocketGetImpl = ServerSocket.class.getDeclaredMethod("getImpl");
            SocketGetImpl = Socket.class.getDeclaredMethod("getImpl");
            DatagramSocketGetImpl=DatagramSocket.class.getDeclaredMethod("getImpl");
            SocketImplGetFileDescriptor = SocketImpl.class.getDeclaredMethod("getFileDescriptor");
//            DatagramChannelImplGetFileDescriptor = sun.nio.ch.DaDatagramChannelImpl.class.getDeclaredMethod("getFD");
            FileOutputStreamFileDescriptor=FileOutputStream.class.getDeclaredField("fd");
            FileInputStreamFileDescriptor=FileInputStream.class.getDeclaredField("fd");
            RandomAccessFileDescriptor=RandomAccessFile.class.getDeclaredField("fd");
//            DatagramChannelFileDescriptor =DatagramChannelImpl.getDeclaredField("fd");
            privateFd=FileDescriptor.class.getDeclaredField("fd");
            ServerSocketGetImpl.setAccessible(true);
            SocketGetImpl.setAccessible(true);
            DatagramSocketGetImpl.setAccessible(true);
            SocketImplGetFileDescriptor.setAccessible(true);
 //           DatagramSocketImplGetFileDescriptor.setAccessible(true);
            FileOutputStreamFileDescriptor.setAccessible(true);
            FileInputStreamFileDescriptor.setAccessible(true);
            RandomAccessFileDescriptor.setAccessible(true);
            privateFd.setAccessible(true);
        } catch (final Throwable t) {
            //If we get here, we have a serious problem and can not continue
            //so throw a runtime exception to notify
            log.error("Reflection error on SocketOptions: ",t.getMessage());
            throw new JVMException(t);
        }
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

    public static FileDescriptor getFileDescriptor(final DatagramChannel socket) throws InvocationTargetException, IllegalAccessException {
        try {
            final Field fd=socket.getClass().getDeclaredField("fd");
            fd.setAccessible(true);
            return (FileDescriptor)fd.get(socket);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
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
        } catch (final Exception shoutNotHappen) {
            throw new JVMException(shoutNotHappen);
        }
    }

    public static int getFd(final DatagramChannel socket) {
        try {
            final FileDescriptor fd = getFileDescriptor(socket);
            return getFileHandle(fd);
        } catch (final Exception shoutNotHappen) {
            throw new JVMException(shoutNotHappen);
        }
    }



    public static int getFd(final Socket socket) throws JVMException {
        try {
        final FileDescriptor fd = getFileDescriptor(socket);
        return getFileHandle(fd);
        } catch (final Exception shoutNotHappen) {
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
        } catch (final Exception shoutNotHappen) {
            throw new JVMException(shoutNotHappen);
        }
    }

    public static int getFd(final FileInputStream stream )  {
        try {
            final FileDescriptor fd = getFileDescriptor(stream);
            return getFileHandle(fd);
        } catch (final Exception shoutNotHappen) {
            throw new JVMException(shoutNotHappen);
        }
    }
    public static int getFd(final RandomAccessFile random ) {
        try {
            final FileDescriptor fd = getFileDescriptor(random);
            return getFileHandle(fd);
        } catch (final Exception shoutNotHappen) {
            throw new JVMException(shoutNotHappen);
        }
    }

}
