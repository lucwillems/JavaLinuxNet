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

#include "org_it4y_jni_linuxutils.h"
 /* Amount of characters in the error message buffer */
#define ERROR_SIZE 254


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
jint throwErrnoExceptionfError(JNIEnv *env, int error) {

   jclass errnoexception_class = (*env)->FindClass( env, "org/it4y/jni/libc$ErrnoException");
   if((*env)->ExceptionOccurred(env)) { return;}
   jmethodID errnoexception_ctorID  = (*env)->GetMethodID(env, errnoexception_class, "<init>","(Ljava/lang/String;I)V");
   if((*env)->ExceptionOccurred(env)) { return;}
   jstring jmessage = (*env)->NewStringUTF(env,strerror(error));
   if((*env)->ExceptionOccurred(env)) { return;}
   jobject errnoexception_obj = (*env)->NewObject(env, errnoexception_class, errnoexception_ctorID,jmessage,error);
   if((*env)->ExceptionOccurred(env)) { return;}

   //yes it did ;-)
   (*env)->Throw( env, errnoexception_obj );
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
     throwErrnoExceptionfError(env,errno);
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
      throwErrnoExceptionfError(env,errno);
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
      throwErrnoExceptionfError(env,errno);
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
     throwErrnoExceptionfError(env,errno);
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
     throwErrnoExceptionfError(env,errno);
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
     throwErrnoExceptionfError(env,errno);
     return 0;
  }
  return (*env)->NewStringUTF(env,value);
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    getsockname
 * Signature: (I)I
 */
JNIEXPORT jobject JNICALL Java_org_it4y_jni_linuxutils_getsockname(JNIEnv *env, jclass this, jint fd) {

    struct sockaddr_in orig_dst;
    socklen_t addrlen = sizeof(orig_dst);
    struct tcp_info info;
    int tcpinfolen=sizeof(info);

    //fprintf(stderr,"tcp_info: %d",tcpinfolen);
    memset(&orig_dst, 0, addrlen);
    //get socket bound address/port
    if(getsockname(fd, (struct sockaddr*) &orig_dst, &addrlen) < 0){
        perror("getsockname: ");
        throwErrnoExceptionfError(env,errno);
        return 0;
    } else {
        if(orig_dst.sin_family == AF_INET) {
           /* TODO : use cached ID's */
          jclass csockaddr_in = (*env)->FindClass(env,"org/it4y/jni/libc$sockaddr_in");
          if((*env)->ExceptionOccurred(env)) { return;}
          jfieldID sockaddr_in_addressID = (*env)->GetFieldID(env, csockaddr_in, "address", "I");
          if((*env)->ExceptionOccurred(env)) { return;}
          jfieldID sockaddr_in_familyID  = (*env)->GetFieldID(env, csockaddr_in, "family", "I");
          if((*env)->ExceptionOccurred(env)) { return;}
          jfieldID sockaddr_in_portID   = (*env)->GetFieldID(env, csockaddr_in, "port", "I");
          if((*env)->ExceptionOccurred(env)) { return;}
          jmethodID sockaddr_in_ctorID  = (*env)->GetMethodID(env, csockaddr_in, "<init>","()V");
          if((*env)->ExceptionOccurred(env)) { return;}

          /* create instance of sockaddr_in and fillup */
         jobject jsockaddr_in_Obj = (*env)->NewObject(env, csockaddr_in, sockaddr_in_ctorID);
         if((*env)->ExceptionOccurred(env)) { return;}
         (*env)->SetIntField(env, jsockaddr_in_Obj, sockaddr_in_addressID ,(jint)ntohl(orig_dst.sin_addr.s_addr));
         if((*env)->ExceptionOccurred(env)) { return;}
         (*env)->SetIntField(env, jsockaddr_in_Obj, sockaddr_in_portID ,(jint)ntohs(orig_dst.sin_port));
         if((*env)->ExceptionOccurred(env)) { return;}
         (*env)->SetIntField(env, jsockaddr_in_Obj, sockaddr_in_familyID ,orig_dst.sin_family);
         if((*env)->ExceptionOccurred(env)) { return;}
         return jsockaddr_in_Obj;
       } else {
         fprintf(stderr," IPv6 not supported!!!\n");
       }
    }
    return 0;
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
     throwErrnoExceptionfError(env,errno);
     return 0;
  }
  //fprintf(stderr,"retrieved tcp_info from %d size %d",fd,len);
  //we need to push data to tcp_info java class. we have 1 big setdata method with 32 parameters (if i counted correctly)
  jclass ctcp_inf = (*env)->FindClass(env,"org/it4y/jni/libc$tcp_info");
  if((*env)->ExceptionOccurred(env)) { return;}
  jmethodID tcp_info_setdataID = (*env)->GetMethodID(env, ctcp_inf, "setdata","(BBBBBBBBIIIIIIIIIIIIIIIIIIIIIIII)V");
  if((*env)->ExceptionOccurred(env)) { return;}
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
  if((*env)->ExceptionOccurred(env)) { return;}
  jclass ia_holderclass = (*env)->NewGlobalRef(env, cholder);
  if((*env)->ExceptionOccurred(env)) { return;}
  jfieldID ia_addressID = (*env)->GetFieldID(env, ia_holderclass, "address", "I");
  if((*env)->ExceptionOccurred(env)) { return;}
  jfieldID ia_familyID = (*env)->GetFieldID(env, ia_holderclass, "family", "I");
  if((*env)->ExceptionOccurred(env)) { return;}

  jclass cia = (*env)->FindClass(env,"java/net/Inet4Address");
  if((*env)->ExceptionOccurred(env)) { return;}
  jclass ia_class = (*env)->NewGlobalRef(env, cia);
  if((*env)->ExceptionOccurred(env)) { return;}
  jfieldID ia_holderID = (*env)->GetFieldID(env, ia_class, "holder", "Ljava/net/InetAddress$InetAddressHolder;");
  if((*env)->ExceptionOccurred(env)) { return;}
  jmethodID ia_ctrID = (*env)->GetMethodID(env, ia_class, "<init>", "()V");
  if((*env)->ExceptionOccurred(env)) { return;}

  //create new Inet4Address with correct address
  jobject iaObj = (*env)->NewObject(env, ia_class, ia_ctrID);
  //get access to holder object
  jobject iaholder=(*env)->GetObjectField(env,iaObj,ia_holderID);
  if((*env)->ExceptionOccurred(env)) { return;}
  //set address of holder
  (*env)->SetIntField(env, iaholder, ia_addressID,0x7f000001);
  if((*env)->ExceptionOccurred(env)) { return;}

  //Create new InetSocketAddress
  jclass c_isa = (*env)->FindClass(env, "java/net/InetSocketAddress");
  if((*env)->ExceptionOccurred(env)) { return;}
  jclass isa_class = (*env)->NewGlobalRef(env, c_isa);
  if((*env)->ExceptionOccurred(env)) { return;}
  jmethodID isa_ctorID = (*env)->GetMethodID(env, c_isa, "<init>","(Ljava/net/InetAddress;I)V");
  if((*env)->ExceptionOccurred(env)) { return;}
  jobject isa = (*env)->NewObject(env, isa_class, isa_ctorID, iaObj, port);
  if((*env)->ExceptionOccurred(env)) { return;}


  return isa;
  //return null
  //return (*env)->NewGlobalRef(env,NULL);
}

