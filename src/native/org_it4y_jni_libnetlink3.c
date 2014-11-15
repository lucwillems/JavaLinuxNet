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
//libnet
#include <libnetlink.h>
#include <netlink/cache.h>
#include "org_it4y_jni_libnetlink3.h"
 /* Amount of characters in the error message buffer */
#define ERROR_SIZE 254

//some errorcodes
#define OK 0;
#define ERR_JNI_ERROR -1;
#define ERR_FIND_CLASS_FAILED -2;
#define ERR_GET_METHOD_FAILED -3;
#define ERR_CALL_METHOD_FAILED -4;
#define ERR_BUFFER_TO_SMALL -5;
#define ERR_EXCEPTION -6;



// Cached Object,Field,Method ID's needed
jclass errnoexception_class;
jclass rtnl_accept_class;
jmethodID errnoexception_ctorID;
jmethodID rtnl_accept_acceptID;
/*
 * Initialize JNI interfaces for fast lookup. this method must be called first before anything will work
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_libnetlink3_initlib  (JNIEnv *env, jclass obj) {
   fprintf(stderr,"libjninetlink3 init...\n");

   //ErrnoException class
   errnoexception_class = (*env)->FindClass( env, "org/it4y/jni/libc$ErrnoException");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_CLASS_FAILED;

   errnoexception_ctorID  = (*env)->GetMethodID(env, errnoexception_class, "<init>","(Ljava/lang/String;I)V");
   if((*env)->ExceptionOccurred(env))
      return ERR_GET_METHOD_FAILED;

   //rtln_accept interface class
   rtnl_accept_class = (*env)->FindClass( env, "org/it4y/jni/libnetlink3$rtnl_accept");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_CLASS_FAILED;

   //rtl_accept.accept(ByteBuffer) method
   rtnl_accept_acceptID = (*env)->GetMethodID(env,rtnl_accept_class, "accept", "(Ljava/nio/ByteBuffer;)I");
   if((*env)->ExceptionOccurred(env))
      return ERR_GET_METHOD_FAILED;

  //init ok
  return OK;
}

/*
 * This will create a ErrnoException based on returned errno
 *
 */
jint throwErrnoExceptionfError(JNIEnv *env, int error) {

   jstring jmessage = (*env)->NewStringUTF(env,strerror(error));
   jobject errnoexception_obj = (*env)->NewObject(env, errnoexception_class, errnoexception_ctorID,jmessage,error);
   if((*env)->ExceptionOccurred(env)) { return ERR_JNI_ERROR;}

   //yes it did ;-)
   (*env)->Throw( env, errnoexception_obj );
   return OK;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    rtnl_open
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_libnetlink3_rtnl_1open(JNIEnv *env, jclass this, jbyteArray handle, jint subscriptions) {
  struct rtnl_handle *rth;
  jbyte *b;

//  struct nl_cache_mngr *mngr;
//  int len=sizeof(mngr);
//  fprintf(stderr,"cache manager: %d\n",len);

  //get pointer to handler byte[] structure
  b = (*env)->GetByteArrayElements(env, handle, NULL);
  rth = (struct rtnl_handle *)b;
  int result=rtnl_open(rth,subscriptions);

  //release it before it leaks ...
  (*env)->ReleaseByteArrayElements(env, handle, b, 0);

  //exception handling
  if ((*env)->ExceptionCheck(env))
      return ERR_EXCEPTION;

  //ok
  return result;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    rtnl_open_byproto
 * Signature: ([BII)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_libnetlink3_rtnl_1open_1byproto(JNIEnv *env, jclass this , jbyteArray handle , jint subscriptions , jint protocol) {
  struct rtnl_handle *rth;
  jbyte *b;

  //get pointer to handler byte[] structure
  b = (*env)->GetByteArrayElements(env, handle, NULL);
  rth = (struct rtnl_handle *)b;
  int result=rtnl_open_byproto(rth,subscriptions,protocol);

  //release it before it leaks ...
  (*env)->ReleaseByteArrayElements(env, handle, b, 0);

  //exception handling
  if ((*env)->ExceptionCheck(env))
     return ERR_EXCEPTION;

  //ok
  return result;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    rtnl_close
 * Signature: ([B)I
 */
JNIEXPORT void JNICALL Java_org_it4y_jni_libnetlink3_rtnl_1close(JNIEnv *env , jclass this , jbyteArray handle) {

  struct rtnl_handle *rth;
  jbyte *b;

  //get pointer to handler byte[] structure
  b = (*env)->GetByteArrayElements(env, handle, NULL);
  rth = (struct rtnl_handle *)b;
  rtnl_close(rth);

  //release it before it leaks ...
  (*env)->ReleaseByteArrayElements(env, handle, b, 0);

  //exception handling
  ((*env)->ExceptionCheck(env));
 }

 /*
  * Class:     org_it4y_jni_linuxutils
  * Method:    rtnl_wilddump_request
  * Signature: ([BII)I
  */
JNIEXPORT jint JNICALL Java_org_it4y_jni_libnetlink3_rtnl_1wilddump_1request(JNIEnv *env, jclass this, jbyteArray handle , jint family, jint type) {

  struct rtnl_handle *rth;
  jbyte *b;

  //get pointer to handler byte[] structure
  b = (*env)->GetByteArrayElements(env, handle, NULL);
  rth = (struct rtnl_handle *)b;
  int result=rtnl_wilddump_request(rth,family,type);

  //release it before it leaks ...
  (*env)->ReleaseByteArrayElements(env, handle, b, 0);

  //exception handling
  if ((*env)->ExceptionCheck(env))
     return ERR_EXCEPTION;

  //ok
  return result;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    rtnl_send
 * Signature: (Ljava/nio/ByteBuffer;I)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_libnetlink3_rtnl_send(JNIEnv *env, jclass this, jobject bytebuffer, jint len) {
  return OK;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    rtnl_dump_request
 * Signature: (ILjava/nio/ByteBuffer;I)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_libnetlink3_rtnl_dump_request(JNIEnv *env, jclass this , jint type , jobject req , jint len) {
  return OK;
}


struct listen_jni_callback
{
    JNIEnv *        env;
    jclass          this;
	jobject         messageBuffer; //ByteBuffer
    jobject         listener;      //rtnl_listen_interface
};

#define MIN(a,b) (((a)<(b))?(a):(b))

/*
 * callback function for listen
 */
static int accept_msg(const struct sockaddr_nl *who,struct nlmsghdr *n, void *arg) {

   //get access to jni environment to implement callback to java
   struct listen_jni_callback* jni=(struct listen_jni_callback*)arg;

   char* b = (char *)(*jni->env)->GetDirectBufferAddress(jni->env,jni->messageBuffer);
   jlong capacity = (*jni->env)->GetDirectBufferCapacity(jni->env,jni->messageBuffer);
   //make sure our buffer is big enough
   if  (n->nlmsg_len > capacity) {
       fprintf(stderr,"rtnl_listen.accept() : buffer to small , need %d , have %d\n",n->nlmsg_len,(u_int)capacity);
       return ERR_BUFFER_TO_SMALL;
   }

   //copy message into ByteBuffer
   memcpy(b,n,MIN(n->nlmsg_len,capacity));

   // Call the int accept(ByteBuffer)
   jint result = (jint) (*jni->env)->CallIntMethod(jni->env, jni->listener, rtnl_accept_acceptID, jni->messageBuffer);
   if((*jni->env)->ExceptionOccurred(jni->env))
      return ERR_CALL_METHOD_FAILED;

   //all ok
   return result;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    rtnl_listen
 * Signature: (Ljava/nio/ByteBuffer;)I
 */
 JNIEXPORT jint JNICALL Java_org_it4y_jni_libnetlink3_rtnl_1listen(JNIEnv *env, jclass this, jbyteArray handle, jobject messageBuffer, jobject listener) {
   struct rtnl_handle *rth;
   struct listen_jni_callback callback;

   //check buffer
   if (messageBuffer == 0) {
       //We ByteBuffer !!!
       fprintf(stderr,"rtln_listen : NO messageBuffer !!!!");
       return ERR_JNI_ERROR;
   }
   if (listener == 0) {
      //We need listener !!!
      fprintf(stderr,"rtln_listen : NO java callback listener !!!!");
      return ERR_JNI_ERROR;
   }

   //get pointer to handler byte[] structure
   jbyte *b = (*env)->GetByteArrayElements(env, handle, NULL);
   rth = (struct rtnl_handle *)b;

   //java listener callback stuff
   callback.env=env;
   callback.this=this;
   callback.messageBuffer=messageBuffer;
   callback.listener=listener;
   //this method blocks until something really bad happens
   int result=rtnl_listen(rth, accept_msg, &callback);

   //release it before it leaks ...
   (*env)->ReleaseByteArrayElements(env, handle, b, 0);

  //exception handling
  if ((*env)->ExceptionCheck(env))
     return ERR_EXCEPTION;

   return result;
}

