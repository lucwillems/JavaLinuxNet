/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */
#include <sys/socket.h>
#include <errno.h>
#include <netinet/in.h>
#include <jni.h>
#include <string.h>
#include <linux/tcp.h>
//libnet
#include <libnetlink.h>
//ioctl
#include <sys/ioctl.h>
#include <net/if.h>
#include <arpa/inet.h>


#include "org_it4y_jni_linuxutils.h"
 /* Amount of characters in the error message buffer */
#define ERROR_SIZE 254
//some errorcodes
#define OK 0;
#define ERR_JNI_ERROR -1;
#define ERR_FIND_CLASS_FAILED -2;
#define ERR_FIND_FIELD_FAILED -3;
#define ERR_GET_METHOD_FAILED -4;
#define ERR_CALL_METHOD_FAILED -5;
#define ERR_BUFFER_TO_SMALL -6;
#define ERR_EXCEPTION -7;


void tostring(JNIEnv *env, jobject obj) {

  if ( obj > 0) {
   jclass cls = (*env)->GetObjectClass(env,obj);

   // First get the class object
   jmethodID mid = (*env)->GetMethodID(env,cls, "getClass", "()Ljava/lang/Class;");
   jobject clsObj = (*env)->CallObjectMethod(env,obj, mid);

   // Now get the class object's class descriptor
   cls = (*env)->GetObjectClass(env,clsObj);

   // Find the getName() method on the class object
   mid = (*env)->GetMethodID(env,cls, "getName", "()Ljava/lang/String;");

   // Call the getName() to get a jstring object back
   jstring strObj = (jstring)(*env)->CallObjectMethod(env,clsObj, mid);

    // Now get the c string from the java jstring object
    const char* str = (*env)->GetStringUTFChars(env,strObj, NULL);

   // Print the class name
   fprintf(stderr,"Calling class is: %s %x\n", str,(u_int)obj);

   // Release the memory pinned char array
   (*env)->ReleaseStringUTFChars(env,strObj, str);
   } else {
     fprintf(stderr,"Calling class is: null\n");
   }
}

/*
 * This will create a ErrnoException based on returned errno
 *
 */
jint throwErrnoExceptionError(JNIEnv *env, int error) {

   jclass errnoexception_class = (*env)->FindClass( env, "org/it4y/jni/libc$ErrnoException");
   if((*env)->ExceptionOccurred(env)) { return -1;}
   jmethodID errnoexception_ctorID  = (*env)->GetMethodID(env, errnoexception_class, "<init>","(Ljava/lang/String;I)V");
   if((*env)->ExceptionOccurred(env)) { return -1;}
   jstring jmessage = (*env)->NewStringUTF(env,strerror(error));
   if((*env)->ExceptionOccurred(env)) { return -1;}
   jobject errnoexception_obj = (*env)->NewObject(env, errnoexception_class, errnoexception_ctorID,jmessage,error);
   if((*env)->ExceptionOccurred(env)) { return -1;}

   //yes it did ;-)
   (*env)->Throw( env, errnoexception_obj );
   return 0;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    setbooleanSockOption
 * Signature: (IIIB)I
 */
JNIEXPORT void JNICALL Java_org_it4y_jni_linuxutils_setbooleanSockOption(JNIEnv *env, jclass this, jint fd, jint level , jint option , jboolean x) {

 //fprintf(stderr,"setbooleanSockOption %d %d %d %d\n",fd,level,option,x);
 int value=0;
 if (x) { value = 1;}
 //set socket boolean option
 if (setsockopt(fd, level,option, &value, sizeof(value)) != 0) {
     perror("setsockopt_boolean");
     throwErrnoExceptionError(env,errno);
 }
}


/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    setuint16SockOption
 * Signature: (IIII)I
 */
JNIEXPORT void JNICALL Java_org_it4y_jni_linuxutils_setintSockOption(JNIEnv *env, jclass this, jint fd, jint level, jint option, jint x) {
   int value=x;
   fprintf(stderr,"setuint16SockOption %d %d %d %d\n",fd,level,option,value);
   //set socket int option
   if (setsockopt(fd, level,option, &value, sizeof(value)) != 0) {
      perror("setsockopt_uint16");
      throwErrnoExceptionError(env,errno);
   }
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    setstringSockOption
 * Signature: (IIILjava/lang/String;)I
 */
JNIEXPORT void JNICALL Java_org_it4y_jni_linuxutils_setstringSockOption(JNIEnv *env, jclass this, jint fd, jint level,jint option , jstring s) {

  const char *value = (*env)->GetStringUTFChars(env, s, 0);


   // use your string
  fprintf(stderr,"setStringSockOption %d %d %d [%s]\n",fd,level,option,value);

   //set socket int option
   if (setsockopt(fd, level,option, value, sizeof(value)+1) != 0) {
      perror("setsockopt_string");
      throwErrnoExceptionError(env,errno);
   }
  //must always be done !!!
  (*env)->ReleaseStringUTFChars(env, s, value);

}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    getbooleanSockOption
 * Signature: (III)B
 */
JNIEXPORT jboolean JNICALL Java_org_it4y_jni_linuxutils_getbooleanSockOption(JNIEnv *env, jclass this , jint fd, jint level , jint option) {
  //fprintf(stderr,"getbooleanSockOption %d %d %d\n",fd,level,option);

  jboolean value=0;
  socklen_t len=sizeof(value);

  //get socket boolean option
  if (getsockopt(fd, level,option, &value,&len) != 0) {
     perror("getsockopt boolean");
     throwErrnoExceptionError(env,errno);
     return 0;
 }
 return value;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    getuint16SockOption
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_linuxutils_getintSockOption(JNIEnv *env, jclass this, jint fd, jint level, jint option) {
  fprintf(stderr,"getuint16SockOption %d %d %d\n",fd,level,option);
  int value=0;
  socklen_t len=sizeof(value);

  //get socket boolean option
  if (getsockopt(fd, level,option, &value,&len) != 0) {
     perror("getsockopt int");
     throwErrnoExceptionError(env,errno);
     return 0;
  }
  return value;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    getstringSockOption
 * Signature: (III)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_org_it4y_jni_linuxutils_getstringSockOption (JNIEnv *env, jclass this, jint fd, jint level , jint option) {
  fprintf(stderr,"getstringSockOption %d %d %d\n",fd,level,option);
  //we should limit buffer size here so we stick to 255 for now
  char value[255];
  socklen_t len=sizeof(value)+1;
  //get socket string option
  if (getsockopt(fd,level,option, &value,&len) != 0) {
     perror("getsockopt string");
     throwErrnoExceptionError(env,errno);
     return 0;
  }
  return (*env)->NewStringUTF(env,value);
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    getsockname
 * Signature: (I)I
 */
JNIEXPORT jbyteArray JNICALL Java_org_it4y_jni_linuxutils__1getsockname(JNIEnv *env, jclass this, jint fd) {

    struct sockaddr_storage orig_dst;
    //struct sockaddr_in  *sa4 = (struct sockaddr_in *)&orig_dst;
    struct sockaddr_in6 *sa6 = (struct sockaddr_in6 *)&orig_dst;
    socklen_t addrlen = sizeof(orig_dst);
  
    memset(&orig_dst, 0, addrlen);
    //get socket bound address/port
    if(getsockname(fd, (struct sockaddr*) &orig_dst, &addrlen) < 0){
        perror("getsockname: ");
        throwErrnoExceptionError(env,errno);
        return 0;
    }
    //IPV4 socket
    if(orig_dst.ss_family == AF_INET) {
           jbyteArray result;
           result = (*env)->NewByteArray(env,sizeof(struct sockaddr_in));
           //do stuff to raw bytes
           jboolean isCopy;
           jbyte* rawjBytes = (*env)->GetByteArrayElements(env, result, &isCopy);
           memcpy(rawjBytes,&orig_dst,sizeof(struct sockaddr_in));
           (*env)->ReleaseByteArrayElements(env, result, rawjBytes, 0);
           return result;
    }
    //IPV6 socket , only if ipv4 is mapped in ipv6 for know
    if(orig_dst.ss_family == AF_INET6 && IN6_IS_ADDR_V4MAPPED(&sa6->sin6_addr)) {
           //Convert MAPPED IPV4 in IPV6 to IPV4
           struct sockaddr_in ipv4;
           //get last 4 bytes=ipv4 address
           ipv4.sin_addr = *((struct in_addr *)&(sa6->sin6_addr.s6_addr[12]));
           ipv4.sin_port=sa6->sin6_port;
           ipv4.sin_family=AF_INET;

           jbyteArray result;
           result = (*env)->NewByteArray(env,sizeof(struct sockaddr_in));
           //do stuff to raw bytes
           jboolean isCopy;
           jbyte* rawjBytes = (*env)->GetByteArrayElements(env, result, &isCopy);
           memcpy(rawjBytes,&ipv4,sizeof(struct sockaddr_in));
           (*env)->ReleaseByteArrayElements(env, result, rawjBytes, 0);
           return result;
    }
    fprintf(stderr,"getsockname: unsupported sock_storage format IPv6!\n");
    return NULL;
}


/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    gettcpinfo
 * Signature: (ILorg/it4y/jni/libc/tcp_info;)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_linuxutils_gettcpinfo (JNIEnv *env, jclass this, jint fd, jobject tcpInfo_Obj) {

   struct tcp_info value;
   socklen_t len = sizeof(value);

  //get socket string option
  if (getsockopt(fd,IPPROTO_TCP,TCP_INFO, &value,&len) != 0) {
     perror("getsockopt tcp_info");
     throwErrnoExceptionError(env,errno);
     return 0;
  }
  //fprintf(stderr,"retrieved tcp_info from %d size %d",fd,len);
  //we need to push data to tcp_info java class. we have 1 big setdata method with 32 parameters (if i counted correctly)
  jclass ctcp_inf = (*env)->FindClass(env,"org/it4y/jni/libc$tcp_info");
  if((*env)->ExceptionOccurred(env)) { return -1;}
  jmethodID tcp_info_setdataID = (*env)->GetMethodID(env, ctcp_inf, "setdata","(BBBBBBBBIIIIIIIIIIIIIIIIIIIIIIII)V");
  if((*env)->ExceptionOccurred(env)) { return -1;}
  //callback
  if (len==104) {
       (*env)->CallVoidMethod(env, tcpInfo_Obj,tcp_info_setdataID,
       value.tcpi_state,
       value.tcpi_ca_state,
       value.tcpi_retransmits,
       value.tcpi_probes,
       value.tcpi_backoff,
       value.tcpi_options,
       value.tcpi_snd_wscale,
       value.tcpi_rcv_wscale,
       value.tcpi_rto,
       value.tcpi_ato,
       value.tcpi_snd_mss,
       value.tcpi_rcv_mss,
       value.tcpi_unacked,
       value.tcpi_sacked,
       value.tcpi_lost,
       value.tcpi_retrans,
       value.tcpi_fackets,
       value.tcpi_last_data_sent,
       value.tcpi_last_ack_sent,
       value.tcpi_last_data_recv,
       value.tcpi_last_ack_recv,
       value.tcpi_pmtu,
       value.tcpi_rcv_ssthresh,
       value.tcpi_rtt,
       value.tcpi_rttvar,
       value.tcpi_snd_ssthresh,
       value.tcpi_snd_cwnd,
       value.tcpi_advmss,
       value.tcpi_reordering,
       value.tcpi_rcv_rtt,
       value.tcpi_rcv_space,
       value.tcpi_total_retrans
       );
  }
  return len;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    getLocalHost
 * Signature: ()Ljava/net/InetSocketAddress;
 */
JNIEXPORT jobject JNICALL Java_org_it4y_jni_linuxutils_getLocalHost  (JNIEnv *env, jclass this) {

  int port=22;

  jclass cholder = (*env)->FindClass(env,"java/net/InetAddress$InetAddressHolder");
  if((*env)->ExceptionOccurred(env)) { return (jobject)0;}
  jclass ia_holderclass = (*env)->NewGlobalRef(env, cholder);
  if((*env)->ExceptionOccurred(env)) { return (jobject)0;}
  jfieldID ia_addressID = (*env)->GetFieldID(env, ia_holderclass, "address", "I");
  if((*env)->ExceptionOccurred(env)) { return (jobject)0;}
  //jfieldID ia_familyID = (*env)->GetFieldID(env, ia_holderclass, "family", "I");
  //if((*env)->ExceptionOccurred(env)) { return (jobject)0;}

  jclass cia = (*env)->FindClass(env,"java/net/Inet4Address");
  if((*env)->ExceptionOccurred(env)) { return (jobject)0;}
  jclass ia_class = (*env)->NewGlobalRef(env, cia);
  if((*env)->ExceptionOccurred(env)) { return (jobject)0;}
  jfieldID ia_holderID = (*env)->GetFieldID(env, ia_class, "holder", "Ljava/net/InetAddress$InetAddressHolder;");
  if((*env)->ExceptionOccurred(env)) { return (jobject)0;}
  jmethodID ia_ctrID = (*env)->GetMethodID(env, ia_class, "<init>", "()V");
  if((*env)->ExceptionOccurred(env)) { return (jobject)0;}

  //create new Inet4Address with correct address
  jobject iaObj = (*env)->NewObject(env, ia_class, ia_ctrID);
  //get access to holder object
  jobject iaholder=(*env)->GetObjectField(env,iaObj,ia_holderID);
  if((*env)->ExceptionOccurred(env)) { return (jobject)0;}
  //set address of holder
  (*env)->SetIntField(env, iaholder, ia_addressID,0x7f000001);
  if((*env)->ExceptionOccurred(env)) { return (jobject)0;}

  //Create new InetSocketAddress
  jclass c_isa = (*env)->FindClass(env, "java/net/InetSocketAddress");
  if((*env)->ExceptionOccurred(env)) { return (jobject)0;}
  jclass isa_class = (*env)->NewGlobalRef(env, c_isa);
  if((*env)->ExceptionOccurred(env)) { return (jobject)0;}
  jmethodID isa_ctorID = (*env)->GetMethodID(env, c_isa, "<init>","(Ljava/net/InetAddress;I)V");
  if((*env)->ExceptionOccurred(env)) { return (jobject)0;}
  jobject isa = (*env)->NewObject(env, isa_class, isa_ctorID, iaObj, port);
  if((*env)->ExceptionOccurred(env)) { return (jobject)0;}


  return isa;
  //return null
  //return (*env)->NewGlobalRef(env,NULL);
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    ioctl_SIOCGIFFLAGS
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jshort JNICALL Java_org_it4y_jni_linuxutils_ioctl_1SIOCGIFFLAGS(JNIEnv *env, jclass this, jstring jdevice) {
    const char *dev;
    int sockfd;
    struct ifreq ifr;

    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sockfd < 0) {
           perror("ioctl_SIOCGIFFLAGS");
           throwErrnoExceptionError(env,errno);
           return 0;
    }
    memset(&ifr, 0, sizeof ifr);
    /* get device name from java */
    dev= (*env)->GetStringUTFChars(env,jdevice, 0);
    memcpy(ifr.ifr_name, dev, IFNAMSIZ);
    (*env)->ReleaseStringUTFChars(env, jdevice, dev);

    if (ioctl(sockfd, SIOCGIFFLAGS, &ifr) < 0) {
         perror("ioctl_SIOCGIFFLAGS: ");
         throwErrnoExceptionError(env,errno);
         return errno;
    }

    return ifr.ifr_flags;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    ioctl_SIOCSIFFLAGS
 * Signature: (Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_linuxutils_ioctl_1SIOCSIFFLAGS(JNIEnv *env, jclass this, jstring jdevice, jshort flags) {

    const char *dev;
    int sockfd;
    struct ifreq ifr;

    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sockfd < 0) {
         perror("ioctl_1SIOCSIFFLAGS");
         throwErrnoExceptionError(env,errno);
         return 0;
    }

    memset(&ifr, 0, sizeof ifr);
    /* get device name from java */
    dev= (*env)->GetStringUTFChars(env,jdevice, 0);
    memcpy(ifr.ifr_name, dev, IFNAMSIZ);
    (*env)->ReleaseStringUTFChars(env, jdevice, dev);
    //set flags
    ifr.ifr_flags=flags;
    if (ioctl(sockfd, SIOCGIFFLAGS, &ifr) < 0) {
         perror("ioctl_SIOCGIFFLAGS: ");
         throwErrnoExceptionError(env,errno);
         return errno;
    }

    return OK;
}


/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    ioctl_ifupdown
 * Signature: (Ljava/lang/String;Z)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_linuxutils_ioctl_1ifupdown(JNIEnv *env, jclass this, jstring jdevice, jboolean state) {

    const char *dev;
    int sockfd;
    struct ifreq ifr;

    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sockfd < 0) {
         perror("socket ifupdown");
         throwErrnoExceptionError(env,errno);
         return 0;
    }

    memset(&ifr, 0, sizeof ifr);
    /* get device name from java */
    dev= (*env)->GetStringUTFChars(env,jdevice, 0);
    memcpy(ifr.ifr_name, dev, IFNAMSIZ);
    (*env)->ReleaseStringUTFChars(env, jdevice, dev);
    /* get current interface state */
    if (ioctl(sockfd, SIOCGIFFLAGS, &ifr) < 0) {
         perror("SIOCGIFFLAGS: ");
         throwErrnoExceptionError(env,errno);
         return errno;
    }
    /* set state up/down */
    if (state) {
        ifr.ifr_flags |= IFF_UP;
    } else {
        ifr.ifr_flags &= ~IFF_UP;
    }

    if(ioctl(sockfd, SIOCSIFFLAGS, &ifr) < 0) {
         perror("SIOCSIFFLAGS: ");
         throwErrnoExceptionError(env,errno);
         return errno;
   }
   return OK;
  }


/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    _ioctl_SIOCGIFADDR
 * Signature: (Ljava/lang/String;)[B
 */
JNIEXPORT jbyteArray JNICALL Java_org_it4y_jni_linuxutils__1ioctl_1SIOCGIFADDR(JNIEnv *env, jclass this, jstring jdevice) {
    const char *dev;
    int sockfd;
    struct ifreq ifr;

    jbyteArray result;
    result = (*env)->NewByteArray(env,sizeof(struct sockaddr_in));

    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sockfd < 0) {
         perror("socket ioctl_1SIOCGIFADDRipv4");
         throwErrnoExceptionError(env,errno);
         return NULL;
    }

    memset(&ifr, 0, sizeof ifr);
    /* get device name from java */
    dev= (*env)->GetStringUTFChars(env,jdevice, 0);
    memcpy(ifr.ifr_name, dev, IFNAMSIZ);
    (*env)->ReleaseStringUTFChars(env, jdevice, dev);
    /* get current interface state */
    if (ioctl(sockfd, SIOCGIFADDR, &ifr) < 0) {
         perror("SIOCGIFADDR: ");
         throwErrnoExceptionError(env,errno);
         return NULL;
    }
    struct sockaddr_in* ipaddr = (struct sockaddr_in*)&ifr.ifr_addr;
    //fprintf(stderr,"IP address: %s\n",inet_ntoa(ipaddr->sin_addr));
    //do stuff to raw bytes
    jboolean isCopy;
    jbyte* rawjBytes = (*env)->GetByteArrayElements(env, result, &isCopy);
    memcpy(rawjBytes, (void *)ipaddr,sizeof(struct sockaddr_in));
    (*env)->ReleaseByteArrayElements(env, result, rawjBytes, 0);

    return result;

}


/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    _ioctl_SIOCSIFADDR
 * Signature: (Ljava/lang/String;[B)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_linuxutils__1ioctl_1SIOCSIFADDR(JNIEnv *env, jclass this, jstring device, jbyteArray sockaddr) {
  return OK;
}

