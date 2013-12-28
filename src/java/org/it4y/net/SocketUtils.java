package org.it4y.net;

import java.io.FileDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;

/**
 * Created by luc on 12/27/13.
 */
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
            SocketImpl impl=getImplementation(socket);
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
            SocketImpl impl=getImplementation(socket);
            Method method = SocketImpl.class.getDeclaredMethod("getFileDescriptor");
            method.setAccessible(true);
            return (FileDescriptor) method.invoke(impl);
        } catch (Exception ignore) {
            System.out.println(ignore);
        }
        return null;
    }

    public static int getFd(ServerSocket socket) {
        FileDescriptor fd=getFileDescriptor(socket);
        if (fd==null) { throw new RuntimeException("No FD found..");}
        //Get FD field value
        try {
            Field privateFd = FileDescriptor.class.getDeclaredField("fd");
            privateFd.setAccessible(true);
            return ((Integer)privateFd.get(fd)).intValue();
        } catch (Exception ignore) {
            System.out.println(ignore);
        }
        return -1;
    }

    public static int getFd(Socket socket) {
        FileDescriptor fd=getFileDescriptor(socket);
        if (fd==null) { throw new RuntimeException("No FD found..");}
        //Get FD field value
        try {
            Field privateFd = FileDescriptor.class.getDeclaredField("fd");
            privateFd.setAccessible(true);
            return ((Integer)privateFd.get(fd)).intValue();
        } catch (Exception ignore) {
            System.out.println(ignore);
        }
        return -1;
    }

}
