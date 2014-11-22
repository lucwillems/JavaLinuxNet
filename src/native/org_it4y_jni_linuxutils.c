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
#include <sys/socket.h>
#include <errno.h>
#include <netinet/in.h>
#include <jni.h>
#include <string.h>
#include <linux/tcp.h>
//ioctl
#include <sys/ioctl.h>
#include <net/if.h>
#include <arpa/inet.h>
//time
#include <time.h>

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

// Cached Object,Field,Method ID's needed
jclass ctcp_inf;
jmethodID tcp_info_setdataID;

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    initlib
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_linuxutils_initlib(JNIEnv *env, jclass this) {
  fprintf(stderr,"libjnilinuxutils init...\n");


  jclass ctcp_inf = (*env)->FindClass(env,"org/it4y/jni/libc$tcp_info");
  if((*env)->ExceptionOccurred(env)) {
      fprintf(stderr,"initlibError3");
      return ERR_GET_METHOD_FAILED;
  }

  tcp_info_setdataID = (*env)->GetMethodID(env, ctcp_inf, "setdata","(BBBBBBBBIIIIIIIIIIIIIIIIIIIIIIII)V");
  if((*env)->ExceptionOccurred(env)) {
     fprintf(stderr,"initlibError4");
     return ERR_GET_METHOD_FAILED;
  }

  fprintf(stderr,"libjnilinuxutils ok\n");
  return OK;
}

/*
 * This will create a ErrnoException based on returned errno
 *
 */
jint throwErrnoExceptionError(JNIEnv *env, int error) {
   fprintf(stderr,"libc.errnoexception: %d\n",error);
   // Somehow preloading the jclass & jmethodId doesn work with this exception
   // as this is exception, we should not see alot of them so it doesn't really matter if it takes some time.

   jstring jmessage = (*env)->NewStringUTF(env,strerror(error));
   if((*env)->ExceptionOccurred(env)) {
     fprintf(stderr,"throwErrnoExceptionError1 %d",error);
     return ERR_JNI_ERROR;
   }

   jclass errnoexception_class  = (*env)->FindClass( env, "org/it4y/jni/libc$ErrnoException");
   if((*env)->ExceptionOccurred(env)) {
     fprintf(stderr,"throwErrnoExceptionError2  %d",error);
     return ERR_FIND_CLASS_FAILED;
   }

   jmethodID errnoexception_ctorID = (*env)->GetMethodID(env, errnoexception_class, "<init>","(Ljava/lang/String;I)V");
   if((*env)->ExceptionOccurred(env)) {
     fprintf(stderr,"throwErrnoExceptionError3  %d",error);
     return ERR_FIND_CLASS_FAILED;
   }

   jobject errnoexception_obj = (*env)->NewObject(env, errnoexception_class, errnoexception_ctorID,jmessage,error);
   if((*env)->ExceptionOccurred(env)) {
     fprintf(stderr,"throwErrnoExceptionError4  %d",error);
     return ERR_FIND_CLASS_FAILED;
   }
   //yes it did ;-)
   (*env)->Throw( env, errnoexception_obj );
   return OK;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    setbooleanSockOption
 * Signature: (IIIB)I
 */
JNIEXPORT void JNICALL Java_org_it4y_jni_linuxutils_setbooleanSockOption(JNIEnv *env, jclass this, jint fd, jint level , jint option , jboolean x) {

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
  //fprintf(stderr,"setStringSockOption %d %d %d [%s]\n",fd,level,option,value);

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

    jbyteArray result;
    result = (*env)->NewByteArray(env,sizeof(struct sockaddr_in));

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
JNIEXPORT jint JNICALL Java_org_it4y_jni_linuxutils__1ioctl_1SIOCSIFADDR(JNIEnv *env, jclass this, jstring jdevice, jbyteArray sockaddr) {

    const char *dev;
    int sockfd;
    struct ifreq ifr;

    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sockfd < 0) {
         perror("socket ioctl_1SIOCGIFADDRipv4");
         throwErrnoExceptionError(env,errno);
         return errno;
    }

    memset(&ifr, 0, sizeof ifr);
    /* get device name from java */
    dev= (*env)->GetStringUTFChars(env,jdevice, 0);
    memcpy(ifr.ifr_name, dev, IFNAMSIZ);
    (*env)->ReleaseStringUTFChars(env, jdevice, dev);

    struct sockaddr_in* ipaddr = (struct sockaddr_in*)&ifr.ifr_addr;
    //do stuff to raw bytes
    jboolean isCopy;
    jbyte* rawjBytes = (*env)->GetByteArrayElements(env, sockaddr, &isCopy);
    memcpy((void *)ipaddr, rawjBytes, sizeof(struct sockaddr_in));
    (*env)->ReleaseByteArrayElements(env, sockaddr, rawjBytes, 0);

    //fprintf(stderr,"IP address: %s\n",inet_ntoa(ipaddr->sin_addr));

    if (ioctl(sockfd, SIOCSIFADDR, &ifr) < 0) {
         perror("SIOCSIFADDR: ");
         throwErrnoExceptionError(env,errno);
         return errno;
    }
    return OK;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    _ioctl_SIOCGIFNETMASK
 * Signature: (Ljava/lang/String;)[B
 */
JNIEXPORT jbyteArray JNICALL Java_org_it4y_jni_linuxutils__1ioctl_1SIOCGIFNETMASK(JNIEnv *env, jclass this, jstring jdevice) {
    const char *dev;
    int sockfd;
    struct ifreq ifr;

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
    if (ioctl(sockfd, SIOCGIFNETMASK, &ifr) < 0) {
         perror("SIOCGIFNETMASK: ");
         throwErrnoExceptionError(env,errno);
         return NULL;
    }

    jbyteArray result;
    result = (*env)->NewByteArray(env,sizeof(struct sockaddr_in));

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
 * Method:    _ioctl_SIOCSIFNETMASK
 * Signature: (Ljava/lang/String;[B)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_linuxutils__1ioctl_1SIOCSIFNETMASK(JNIEnv *env, jclass this, jstring jdevice, jbyteArray ipv4) {
    const char *dev;
    int sockfd;
    struct ifreq ifr;

    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sockfd < 0) {
         perror("socket ioctl_1SIOCGIFADDRipv4");
         throwErrnoExceptionError(env,errno);
         return errno;
    }

    memset(&ifr, 0, sizeof ifr);
    /* get device name from java */
    dev= (*env)->GetStringUTFChars(env,jdevice, 0);
    memcpy(ifr.ifr_name, dev, IFNAMSIZ);
    (*env)->ReleaseStringUTFChars(env, jdevice, dev);

    struct sockaddr_in* ipaddr = (struct sockaddr_in*)&ifr.ifr_addr;
    //do stuff to raw bytes
    jboolean isCopy;
    jbyte* rawjBytes = (*env)->GetByteArrayElements(env, ipv4, &isCopy);
    memcpy((void *)ipaddr, rawjBytes, sizeof(struct sockaddr_in));
    (*env)->ReleaseByteArrayElements(env, ipv4, rawjBytes, 0);

    //fprintf(stderr,"IP address: %s\n",inet_ntoa(ipaddr->sin_addr));

    if (ioctl(sockfd, SIOCSIFNETMASK, &ifr) < 0) {
         perror("SIOCSIFNETMASK: ");
         throwErrnoExceptionError(env,errno);
         return errno;
    }
    return OK;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    ioctl_SIOCGIFMTU
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_linuxutils_ioctl_1SIOCGIFMTU(JNIEnv *env, jclass this, jstring jdevice) {
    const char *dev;
    int sockfd;
    struct ifreq ifr;

    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sockfd < 0) {
         perror("socket ioctl_1SIOCGIFMTU");
         throwErrnoExceptionError(env,errno);
         return 0;
    }

    memset(&ifr, 0, sizeof ifr);
    /* get device name from java */
    dev= (*env)->GetStringUTFChars(env,jdevice, 0);
    memcpy(ifr.ifr_name, dev, IFNAMSIZ);
    (*env)->ReleaseStringUTFChars(env, jdevice, dev);
    /* get current interface state */
    if (ioctl(sockfd, SIOCGIFMTU, &ifr) < 0) {
         perror("SIOCGIFMTU: ");
         throwErrnoExceptionError(env,errno);
         return 0;
    }

    return ifr.ifr_metric;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    ioctl_SIOCSIFMTU
 * Signature: (Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_linuxutils_ioctl_1SIOCSIFMTU(JNIEnv *env, jclass this, jstring jdevice, jint mtu) {
    const char *dev;
    int sockfd;
    struct ifreq ifr;

    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sockfd < 0) {
         perror("socket ioctl_1SIOCSIFMTU");
         throwErrnoExceptionError(env,errno);
         return 0;
    }

    memset(&ifr, 0, sizeof ifr);
    /* get device name from java */
    dev= (*env)->GetStringUTFChars(env,jdevice, 0);
    memcpy(ifr.ifr_name, dev, IFNAMSIZ);
    (*env)->ReleaseStringUTFChars(env, jdevice, dev);
    ifr.ifr_mtu=mtu;

    /* get current interface state */
    if (ioctl(sockfd, SIOCSIFMTU, &ifr) < 0) {
         perror("SIOCSIFMTU: ");
         throwErrnoExceptionError(env,errno);
         return 0;
    }
    return OK;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    clock_gettime
 * Signature: (I)[J
 */
JNIEXPORT jlongArray JNICALL Java_org_it4y_jni_linuxutils_clock_1gettime(JNIEnv *env , jclass this, jint clockid) {

    struct timespec now;
    if(clock_gettime(clockid, &now)<0) {
         perror("clock_gettime: ");
         throwErrnoExceptionError(env,errno);
         return 0;
    }

    //convert to long[2] array to return timespec
    jlongArray x = (*env)->NewLongArray(env,2);
    if (x != NULL) {
      jlong *xr = (*env)->GetLongArrayElements(env,x,0);
      xr[0]=now.tv_sec;
      xr[1]=now.tv_nsec;
      (*env)->ReleaseLongArrayElements(env,x,xr,0);
    }
    return x;
}

JNIEXPORT jlong JNICALL Java_org_it4y_jni_linuxutils_usecTime(JNIEnv *env, jclass this) {

    struct timespec now;
    if(clock_gettime(CLOCK_BOOTTIME, &now)<0) {
         perror("clock_gettime: ");
         throwErrnoExceptionError(env,errno);
         return 0;
    }
    return (jlong)(now.tv_sec*1000000+(now.tv_nsec/1000));
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    usecTime
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_org_it4y_jni_linuxutils_usecTime__I (JNIEnv *env , jclass this , jint clockId) {
    struct timespec now;
    if(clock_gettime(clockId, &now)<0) {
         perror("clock_gettime: ");
         throwErrnoExceptionError(env,errno);
         return 0;
    }
    return (jlong)(now.tv_sec*1000000+(now.tv_nsec/1000));
}

JNIEXPORT jlong JNICALL Java_org_it4y_jni_linuxutils_clock_1getres(JNIEnv *env, jclass this , jint clockid) {
    struct timespec now;
    if(clock_getres(clockid, &now)<0) {
         perror("clock_getres: ");
         throwErrnoExceptionError(env,errno);
         return 0;
    }
    return (jlong)(now.tv_nsec);
}

