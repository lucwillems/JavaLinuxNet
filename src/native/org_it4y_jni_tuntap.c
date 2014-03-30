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
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <linux/ioctl.h>
#include <linux/if.h>
#include <linux/if_tun.h>
#include <errno.h>
#include <unistd.h>
#include <jni.h> 
#include <sys/ioctl.h>

#include "org_it4y_jni_tuntap.h"

//some errorcodes
#define OK 0;
#define ERR_JNI_ERROR -1;
#define ERR_FIND_CLASS_FAILED -2;
#define ERR_FIND_FIELD_FAILED -3;
#define ERR_GET_METHOD_FAILED -4;
#define ERR_CALL_METHOD_FAILED -5;
#define ERR_BUFFER_TO_SMALL -6;
#define ERR_EXCEPTION -7;


// Cached Object,Field,Method ID's needed
jclass tuntap_class;
jfieldID tuntap_fdID;
jfieldID tuntap_deviceID;

/*
 * Class:     org_it4y_jni_tuntap
 * Method:    initLib
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_tuntap_initLib(JNIEnv *env , jclass this) {
   fprintf(stderr,"libjnituntap init...\n");

   //tuntap class
   tuntap_class = (*env)->FindClass( env, "org/it4y/jni/tuntap");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_CLASS_FAILED;

   tuntap_fdID = (*env)->GetFieldID(env, tuntap_class, "fd", "I");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;

   tuntap_deviceID = (*env)->GetFieldID(env, tuntap_class, "device", "Ljava/lang/String;");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;

   return OK;
}

/*
 * This will create a ErrnoException based on returned errno
 *
 */
jint throwErrnoExceptionError(JNIEnv *env, int error) {

   jclass errnoexception_class = (*env)->FindClass( env, "org/it4y/jni/libc$ErrnoException");
   jmethodID errnoexception_ctorID  = (*env)->GetMethodID(env, errnoexception_class, "<init>","(Ljava/lang/String;I)V");
   jstring jmessage = (*env)->NewStringUTF(env,strerror(error));
   jobject errnoexception_obj = (*env)->NewObject(env, errnoexception_class, errnoexception_ctorID,jmessage,error);
   if((*env)->ExceptionOccurred(env)) { return -1;}

   //yes it did ;-)
   (*env)->Throw( env, errnoexception_obj );
   return 0;
}

/*
 * Store filediscriptor and device name
 *
 */
void setFdDev(JNIEnv *env, jobject this, int fd, char* dev) {

    jstring jstr;
    
    /*store fd in object */
    (*env)->SetIntField(env, this, tuntap_fdID , fd);
    
    /*store device name in obhect */
    jstr = (*env)->NewStringUTF(env, dev);
    (*env)->SetObjectField(env, this, tuntap_deviceID, jstr);
}

/*
 * Retrieve filediscriptor from class
 */
int getFd(JNIEnv *env, jobject this) {

    return (*env)->GetIntField(env, this, tuntap_fdID);
}

/*
 *  Open tunnel device, let kernel decide on name.
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_tuntap_openTun(JNIEnv *env, jobject this) {
    struct ifreq ifr;
    int fd;
    
    if ((fd = open("/dev/net/tun", O_RDWR)) < 0) {
        perror("open: ");
        throwErrnoExceptionError(env,errno);
        return errno;
    }
    
    memset(&ifr, 0, sizeof(ifr));
    ifr.ifr_flags = IFF_TUN | IFF_NO_PI;
    
    if (ioctl(fd, TUNSETIFF, (void*)&ifr) < 0) {
        close(fd);
        perror("TUNSETIFF: ");
        throwErrnoExceptionError(env,errno);
        return errno;
    }
    
    setFdDev(env, this, fd, ifr.ifr_name);

    //exception handling
    if((*env)->ExceptionOccurred(env))
      return ERR_JNI_ERROR;

    return OK;
}

/*
 *  Open tunnel device using our own name, device must already be created.
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_tuntap_openTunDevice(JNIEnv *env, jobject this, jstring jdev) {
    struct ifreq ifr;
    int fd;
    const char *dev;

    if ((fd = open("/dev/net/tun", O_RDWR)) < 0) {
        perror("open: ");
        throwErrnoExceptionError(env,errno);
        return errno;
    }
    
    memset(&ifr, 0, sizeof(ifr));

    /* get device name from java */
    dev= (*env)->GetStringUTFChars(env,jdev, 0);
    memcpy(ifr.ifr_name, dev, IFNAMSIZ);
    (*env)->ReleaseStringUTFChars(env, jdev, dev);
   
    ifr.ifr_flags = IFF_TUN| IFF_NO_PI;
    if (ioctl(fd, TUNSETIFF, (void*)&ifr) < 0) {
        close(fd);
        perror("TUNSETIFF: ");
        throwErrnoExceptionError(env,errno);
        return errno;
    }
    setFdDev(env, this, fd, ifr.ifr_name);

    //exception handling
    if((*env)->ExceptionOccurred(env))
      return ERR_JNI_ERROR;

    return OK;
}


/*
  * Class:     org_it4y_jni_tuntap
  * Method:    close
  * Signature: ()V
  */
 JNIEXPORT void JNICALL Java_org_it4y_jni_tuntap_close(JNIEnv *env, jobject this) {

    int fd=getFd(env, this);
    close(fd);
    (*env)->SetIntField(env, this, tuntap_fdID , 0);

    //exception handling
    (*env)->ExceptionOccurred(env);
}

/*
 * Class:     jtuntap_TunTap
 * Method:    write
 * Signature: ([BI)V
 */
JNIEXPORT int JNICALL Java_org_it4y_jni_tuntap_writeByteBuffer(JNIEnv *env, jobject this, jobject buffer, jint len) {
    int fd;

    char* b = (char *)(*env)->GetDirectBufferAddress(env,(jobject)buffer);
    fd = getFd(env, this);
    int size=write(fd, b, len);
    if (size != len) {
        perror("write: ");
        throwErrnoExceptionError(env,errno);
        return size;
    }
    return size;
}

/*
 * Class:     jtuntap_TunTap
 * Method:    write
 * Signature: ([BI)V
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_tuntap_readByteBuffer(JNIEnv *env, jobject this, jobject buffer, jboolean block) {

    char* b = (char *)(*env)->GetDirectBufferAddress(env,(jobject)buffer);
    jlong capacity = (*env)->GetDirectBufferCapacity(env,buffer);
    int fd = getFd(env, this);
    jint len = read(fd, b, capacity);

    //error ?
    if (len <0) {
        //non blocking mode ?
        fprintf(stderr,"len: %d  block %d\n",errno,block);
        if (block==1 && errno == EAGAIN) {
           return -EAGAIN;
        }
        //return error
        perror("read: ");
        throwErrnoExceptionError(env,errno);
        return errno;
    }
    //ok
    return len;
}

/*
 * Class:     org_it4y_jni_tuntap
 * Method:    setNonBlocking
 * Signature: (Z)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_tuntap_setNonBlocking(JNIEnv *env, jobject this , jboolean value) {

 int  yes = value;
 int fd;

 fd = getFd(env, this);
 if(ioctl(fd, FIONBIO, &yes) < 0){
     perror("FIONIO: ");
     throwErrnoExceptionError(env,errno);
     return errno;
 }

 return OK;
}

/*
 * Class:     org_it4y_jni_tuntap
 * Method:    isDataReady
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_it4y_jni_tuntap_isDataReady(JNIEnv *env, jobject this, jint timeout) {

   int fd = getFd(env, this);
   fd_set set;
   struct timeval to;

   /* Initialize the file descriptor set. */
   FD_ZERO (&set);
   FD_SET (fd, &set);

   /* Initialize the timeout data structure. */
   to.tv_sec = timeout/1000;
   to.tv_usec =(timeout % 1000)*1000;

   /* select returns 0 if timeout, 1 if input available, -1 if error. */
   return select (FD_SETSIZE,&set, NULL, NULL,&to);
}


/*
 * Class:     org_it4y_jni_tuntap
 * Method:    enableQueue
 * Signature: (Z)Z
 */
/* not supported in kernel 3.5
JNIEXPORT jint JNICALL Java_org_it4y_jni_tuntap_enableQueue(JNIEnv *env, jobject this, jboolean enable) {
   int fd = getFd(env, this);

   struct ifreq ifr;
   memset(&ifr, 0, sizeof(ifr));
   if (enable)
       ifr.ifr_flags = IFF_ATTACH_QUEUE;
   else
       ifr.ifr_flags = IFF_DETACH_QUEUE;

    return ioctl(fd, TUNSETQUEUE, (void *)&ifr);
}
*/