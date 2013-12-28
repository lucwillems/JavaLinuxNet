#include <sys/socket.h>
#include <errno.h>
#include <netinet/in.h>
#include <jni.h>
#include <string.h>

#include "org_it4y_jni_linuxutils.h"

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    setbooleanSockOption
 * Signature: (IIIB)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_linuxutils_setbooleanSockOption(JNIEnv *env, jclass this, jint fd, jint level , jint option , jboolean x) {

 //fprintf(stderr,"setbooleanSockOption %d %d %d %d\n",fd,level,option,x);
 int value=0;
 if (x) { value = 1;}
 //set socket boolean option
 if (setsockopt(fd, level,option, &value, sizeof(value)) != 0) {
     perror("setsockopt");
     return errno;
 }
 return 0;
}


/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    setuint16SockOption
 * Signature: (IIII)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_linuxutils_setuint16SockOption(JNIEnv *env, jclass this, jint fd, jint level, jint option, jint x) {
  fprintf(stderr,"setuint16SockOption %d %d %d %d\n",fd,level,option,x);
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    setuint32SockOption
 * Signature: (IIII)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_linuxutils_setuint32SockOption(JNIEnv *env , jclass this , jint fd, jint level , jint option, jint x) {
  fprintf(stderr,"setuint32SockOption %d %d %d %d\n",fd,level,option,x);
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    setstringSockOption
 * Signature: (IIILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_linuxutils_setstringSockOption(JNIEnv *env, jclass this, jint fd, jint level,jint option , jstring s) {
  fprintf(stderr,"setStringSockOption %d %d %d\n",fd,level,option);
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
     perror("getsockopt");
     return errno;
 }
 return value;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    getuint16SockOption
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_linuxutils_getuint16SockOption(JNIEnv *env, jclass this, jint fd, jint level, jint option) {
  fprintf(stderr,"getuint16SockOption %d %d %d\n",fd,level,option);
  return 0;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    getuint32SockOption
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_linuxutils_getuint32SockOption(JNIEnv *env, jclass this, jint fd, jint level, jint option) {
  fprintf(stderr,"getuint32SockOption %d %d %d\n",fd,level,option);
  return 0;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    getstringSockOption
 * Signature: (III)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_org_it4y_jni_linuxutils_getstringSockOption (JNIEnv *env, jclass this, jint fd, jint level , jint option) {
  fprintf(stderr,"getstringSockOption %d %d %d\n",fd,level,option);
  return  (*env)->NewGlobalRef(env, NULL);
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    getsockname
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_linuxutils_getsockname(JNIEnv *env, jclass this, jint fd) {
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
