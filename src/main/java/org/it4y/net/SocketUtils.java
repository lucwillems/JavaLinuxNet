/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class SocketUtils {

    public static SocketImpl getImplementation(ServerSocket socket) {
        try {
            Method method = ServerSocket.class.getDeclaredMethod("getImpl");
            method.setAccessible(true);
            return (SocketImpl) method.invoke(socket);
        } catch (Exception ignore) {
            System.out.println(ignore);
        }
        return null;
    }

    public static SocketImpl getImplementation(Socket socket) {
        try {
            Method method = Socket.class.getDeclaredMethod("getImpl");
            method.setAccessible(true);
            return (SocketImpl) method.invoke(socket);
        } catch (Exception ignore) {
            System.out.println(ignore);
        }
        return null;
    }

    public static FileDescriptor getFileDescriptor(ServerSocket socket) {
        try {
            SocketImpl impl = getImplementation(socket);
            Method method = SocketImpl.class.getDeclaredMethod("getFileDescriptor");
            method.setAccessible(true);
            return (FileDescriptor) method.invoke(impl);
        } catch (Exception ignore) {
            System.out.println(ignore);
        }
        return null;
    }

    public static FileDescriptor getFileDescriptor(Socket socket) {
        try {
            SocketImpl impl = getImplementation(socket);
            Method method = SocketImpl.class.getDeclaredMethod("getFileDescriptor");
            method.setAccessible(true);
            return (FileDescriptor) method.invoke(impl);
        } catch (Exception ignore) {
            System.out.println(ignore);
        }
        return null;
    }

    public static FileDescriptor getFileDescriptor(FileOutputStream stream) {
        try {
            Field privateFd = FileOutputStream.class.getDeclaredField("fd");
            privateFd.setAccessible(true);
            return ((FileDescriptor) privateFd.get(stream));
        } catch (Exception ignore) {
            System.out.println(ignore);
        }
        return null;
    }

    public static FileDescriptor getFileDescriptor(FileInputStream stream) {
        try {
            Field privateFd = FileInputStream.class.getDeclaredField("fd");
            privateFd.setAccessible(true);
            return ((FileDescriptor) privateFd.get(stream));
        } catch (Exception ignore) {
            System.out.println(ignore);
        }
        return null;
    }

    public static FileDescriptor getFileDescriptor(RandomAccessFile random) {
        try {
            Field privateFd = RandomAccessFile.class.getDeclaredField("fd");
            privateFd.setAccessible(true);
            return ((FileDescriptor) privateFd.get(random));
        } catch (Exception ignore) {
            System.out.println(ignore);
        }
        return null;
    }

    public static int getFileHandle(FileDescriptor fd) {
        //Get FD field value
        try {
            Field privateFd = FileDescriptor.class.getDeclaredField("fd");
            privateFd.setAccessible(true);
            return ((Integer) privateFd.get(fd)).intValue();
        } catch (Exception ignore) {
            System.out.println(ignore);
        }
        return -1;
    }

    public static int getFd(ServerSocket socket) {
        FileDescriptor fd = getFileDescriptor(socket);
        if (fd == null) {
            throw new RuntimeException("No FD found..");
        }
        return getFileHandle(fd);
    }



    public static int getFd(Socket socket) {
        FileDescriptor fd = getFileDescriptor(socket);
        if (fd == null) {
            return -1;
        }
        return getFileHandle(fd);
    }

    public static int getFd(ServerSocketChannel socket ){
       return getFd(socket.socket());
    }

    public static int getFd(SocketChannel socket ){
        return getFd(socket.socket());
    }

    public static int getFd(FileOutputStream stream ){
        FileDescriptor fd = getFileDescriptor(stream);
        if (fd == null) {
            return -1;
        }
        return getFileHandle(fd);
    }

    public static int getFd(FileInputStream stream ){
        FileDescriptor fd = getFileDescriptor(stream);
        if (fd == null) {
            return -1;
        }
        return getFileHandle(fd);
    }
    public static int getFd(RandomAccessFile random ){
        FileDescriptor fd = getFileDescriptor(random);
        if (fd == null) {
            return -1;
        }
        return getFileHandle(fd);
    }

}
