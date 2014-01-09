#include <sys/socket.h>
#include <errno.h>
#include <netinet/in.h>
#include <jni.h>
#include <string.h>
#include <linux/tcp.h>
//libnet
#include <libnetlink.h>
#include <netlink/cache.h>

#include "org_it4y_jni_libnetlink3.h"
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
   fprintf(stderr,"Calling class is: %s %x\n", str,obj);

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
 * Method:    rtnl_open
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_libnetlink3_rtnl_1open(JNIEnv *env, jclass this, jbyteArray handle, jint subscriptions) {
  struct rtnl_handle *rth;
  jbyte *b;

  struct nl_cache_mngr *mngr;
  int len=sizeof(mngr);
  fprintf(stderr,"cache manager: %d\n",len);

  //get pointer to handler byte[] structure
  b = (*env)->GetByteArrayElements(env, handle, NULL);
  rth = (struct rtnl_handle *)b;
  int result=rtnl_open(rth,subscriptions);
  //fprintf(stderr,"rtnl_handle: %d\n",rth->fd);
  //fprintf(stderr,"rtnl_handle: local %d\n",rth->local.nl_pid);
  //fprintf(stderr,"rtnl_handle: peer %d\n",rth->peer.nl_pid);

  //release it before it leaks ...
  (*env)->ReleaseByteArrayElements(env, handle, b, 0);
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
  //fprintf(stderr,"rtnl_handle: %d\n",rth->fd);
  //fprintf(stderr,"rtnl_handle: local %d\n",rth->local.nl_pid);
  //fprintf(stderr,"rtnl_handle: peer %d\n",rth->peer.nl_pid);
  //release it before it leaks ...
  (*env)->ReleaseByteArrayElements(env, handle, b, 0);
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
  return result;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    rtnl_send
 * Signature: (Ljava/nio/ByteBuffer;I)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_libnetlink3_rtnl_send(JNIEnv *env, jclass this, jobject bytebuffer, jint len) {
  return -1;
}

/*
 * Class:     org_it4y_jni_linuxutils
 * Method:    rtnl_dump_request
 * Signature: (ILjava/nio/ByteBuffer;I)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_libnetlink3_rtnl_dump_request(JNIEnv *env, jclass this , jint type , jobject req , jint len) {
  return -1;
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
   //fprintf(stderr,"accept_msg: %d %d\n",who->nl_pid,who->nl_groups);
   //fprintf(stderr,"jni: %x %x %x %x\n",jni->env,jni->this,jni->messageBuffer,jni->listener);
   //fprintf(stderr,"nl : size %d\n",n->nlmsg_len);
   //tostring(jni->env,jni->messageBuffer);
   //tostring(jni->env,jni->listener);


   char* b = (char *)(*jni->env)->GetDirectBufferAddress(jni->env,jni->messageBuffer);
   jlong capacity = (*jni->env)->GetDirectBufferCapacity(jni->env,jni->messageBuffer);
   //make sure our buffer is big enough
   if  (n->nlmsg_len > capacity) {
       fprintf(stderr,"rtnl_listen.accept() : buffer to small , need %d , have %d\n",n->nlmsg_len,capacity);
       return -4;
   }

   //copy message into ByteBuffe
   memcpy(b,n,MIN(n->nlmsg_len,capacity));

   //do java callback
   jclass cls = (*jni->env)->GetObjectClass(jni->env,jni->listener);
   jmethodID acceptID = (*jni->env)->GetMethodID(jni->env,cls, "accept", "(Ljava/nio/ByteBuffer;)I");
   if((*jni->env)->ExceptionOccurred(jni->env)) { return -5;}
   // Call the int accept(ByteBuffer)
   jint result = (jint) (*jni->env)->CallIntMethod(jni->env, jni->listener, acceptID, jni->messageBuffer);
   if((*jni->env)->ExceptionOccurred(jni->env)) { return -5;}
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
       return -2;
   }
   if (listener == 0) {
      //We need listener !!!
      fprintf(stderr,"rtln_listen : NO java callback listener !!!!");
      return -3;
   }

   //get pointer to handler byte[] structure
   jbyte *b = (*env)->GetByteArrayElements(env, handle, NULL);
   if((*env)->ExceptionOccurred(env)) { return;}
   rth = (struct rtnl_handle *)b;

   char *buffer = (char*)(*env)->GetDirectBufferAddress(env,messageBuffer);
   jlong len = (*env)->GetDirectBufferCapacity(env,messageBuffer);
   if((*env)->ExceptionOccurred(env)) { return;}

   //java listener callback stuff
   callback.env=env;
   callback.this=this;
   callback.messageBuffer=messageBuffer;
   callback.listener=listener;
   //this method blocks until something really bad happens
   int result=rtnl_listen(rth, accept_msg, &callback);

   //release it before it leaks ...
   (*env)->ReleaseByteArrayElements(env, handle, b, 0);
   return result;
}

