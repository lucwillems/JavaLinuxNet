#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <string.h>
#include <sys/socket.h>
#include <linux/ioctl.h>
#include <linux/if.h>
#include <linux/if_tun.h>
#include <errno.h>
#include <jni.h>
#include "org_it4y_net_tuntap_TunTapLinux.h"

/*
 * Store filediscriptor and device name
 *
 */
void setFdDev(JNIEnv *env, jobject this, int fd, char* dev) {

    jfieldID jfd, jdevice;
    jclass jclass; 
    jstring jstr;
    
    jclass = (*env)->GetObjectClass(env, this); 

    /*store fd in class */
    jfd = (*env)->GetFieldID(env, jclass, "fd", "I");
    (*env)->SetIntField(env, this, jfd , fd);
    
    /*store device name in class */
    jstr = (*env)->NewStringUTF(env, dev);
    jdevice = (*env)->GetFieldID(env, jclass, "device", "Ljava/lang/String;");
    (*env)->SetObjectField(env, this, jdevice , jstr);
}

/*
 * Retrieve filediscriptor from class
 */
int getFd(JNIEnv *env, jobject this) {
    jfieldID jfd; 
    jclass jclass; 
    /* get class */
    jclass = (*env)->GetObjectClass(env, this);
    /* get fd field */
    jfd = (*env)->GetFieldID(env, jclass, "fd", "I"); 
    return (*env)->GetIntField(env, this, jfd);
}

/*
 *  Open tunnel device, let kernel decide on name.
 */
JNIEXPORT jint JNICALL Java_org_it4y_net_tuntap_TunTapLinux_openTun(JNIEnv *env, jobject this) {
    struct ifreq ifr;
    int fd;
    
    if ((fd = open("/dev/net/tun", O_RDWR)) < 0) {
        perror("open tun device");
        return errno;
    }
    
    memset(&ifr, 0, sizeof(ifr));
    ifr.ifr_flags = IFF_TUN | IFF_NO_PI;
    
    if (ioctl(fd, TUNSETIFF, (void*)&ifr) < 0) {
        close(fd);
        perror("tun ioctl TUNSETIFF");
        return errno;
    }
    
    setFdDev(env, this, fd, ifr.ifr_name);
    return 0;
}

/*
 *  Open tunnel device using our own name, device must already be created.
 */
JNIEXPORT jint JNICALL Java_org_it4y_net_tuntap_TunTapLinux_openTunDevice(JNIEnv *env, jobject this, jstring jdev) {
    struct ifreq ifr;
    int fd;
    const char *dev;

    if ((fd = open("/dev/net/tun", O_RDWR)) < 0) {
        perror("open tun device ");
        return errno;
    }
    
    memset(&ifr, 0, sizeof(ifr));

    /* get device name from java */
    dev= (*env)->GetStringUTFChars(env,jdev, 0);
    printf("device: %s\n", dev);
    memcpy(ifr.ifr_name, dev, IFNAMSIZ);
    (*env)->ReleaseStringUTFChars(env, jdev, dev);
   
    ifr.ifr_flags = IFF_TUN| IFF_NO_PI;
    if (ioctl(fd, TUNSETIFF, (void*)&ifr) < 0) {
        close(fd);
        perror("tun ioctl TUNSETIFF");
        return errno;
    }
    setFdDev(env, this, fd, ifr.ifr_name);
    return 0;
}


/*
 * Close tunnel device
 */
JNIEXPORT void JNICALL Java_org_it4y_net_tuntap_TunTapLinux_close(JNIEnv *env, jobject this) {
    jfieldID jfd;
    jclass jclass;

    int fd=getFd(env, this);
    close(fd);
    /* clear FD field so we know it is closed */
    jclass = (*env)->GetObjectClass(env, this); 
    /*store fd in class */
    jfd = (*env)->GetFieldID(env, jclass, "fd", "I");
    (*env)->SetIntField(env, this, jfd , -1);
}

/*
 * write byte array to tunnel device
 */
JNIEXPORT void JNICALL Java_org_it4y_net_tuntap_TunTapLinux_write(JNIEnv *env, jobject this, jbyteArray jb, jint len) {
    int fd;
    jbyte *b;
    
    fd = getFd(env, this);
    b = (*env)->GetByteArrayElements(env, jb, NULL);
    
    write(fd, b, len);
    
    (*env)->ReleaseByteArrayElements(env, jb, b, JNI_ABORT);
}

/*
 * Class:     jtuntap_TunTap
 * Method:    write
 * Signature: ([BI)V
 */
JNIEXPORT void JNICALL Java_org_it4y_net_tuntap_TunTapLinux_writeByteBuffer(JNIEnv *env, jobject this, jobject buffer, jint len) {
    int fd;

    char* b = (char *)(*env)->GetDirectBufferAddress(env,(jobject)buffer);
    fd = getFd(env, this);
    write(fd, b, len);
}

/*
 * Class:     jtuntap_TunTap
 * Method:    read
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_net_tuntap_TunTapLinux_read(JNIEnv *env, jobject this, jbyteArray jb) {
    int fd;
    jbyte *b;
    int len;
    
    fd = getFd(env, this);
    b = (*env)->GetByteArrayElements(env, jb, NULL);
    
    len = read(fd, b, (*env)->GetArrayLength(env, jb));
    
    (*env)->ReleaseByteArrayElements(env, jb, b, 0);
    return len;
}

/*
 * Class:     jtuntap_TunTap
 * Method:    write
 * Signature: ([BI)V
 */
JNIEXPORT jint JNICALL Java_org_it4y_net_tuntap_TunTapLinux_readByteBuffer(JNIEnv *env, jobject this, jobject buffer) {

    char* b = (char *)(*env)->GetDirectBufferAddress(env,(jobject)buffer);
    jlong capacity = (*env)->GetDirectBufferCapacity(env,buffer);
    int fd = getFd(env, this);
    jint len = read(fd, b, capacity);
    return len;
}
