#include <sys/socket.h>
#include <errno.h>
#include <netinet/in.h>
#include <jni.h>
#include <string.h>

#include "org_it4y_jni_tproxy.h"

 /*
  * Class:     org_it4y_jni_tproxy
  * Method:    setIPTransparant
  * Signature: (I)I
  */
JNIEXPORT jint JNICALL Java_org_it4y_jni_tproxy_setIPTransparant(JNIEnv *env, jobject this, jint fd) {
    int yes = 1;
    if (setsockopt(fd, SOL_IP, IP_TRANSPARENT, &yes, sizeof(yes)) != 0) {
        perror("setsockopt IP_TRANSPARENT");
        return errno;
    }
    return 0;
}

/*
 * Class:     org_it4y_jni_tproxy
 * Method:    getOriginalDestination
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_tproxy_getOriginalDestination(JNIEnv *env, jobject this, jint fd) {

    jfieldID jremoteIp, jremotePort;
    jclass jclass;
    struct sockaddr_in orig_dst;
    char orig_dst_str[INET6_ADDRSTRLEN];
    socklen_t addrlen = sizeof(orig_dst);

    memset(&orig_dst, 0, addrlen);

    //Socket is bound to original destination
    if(getsockname(fd, (struct sockaddr*) &orig_dst, &addrlen) < 0){
        perror("getsockname: ");
        return -1;
    } else {
        if(orig_dst.sin_family == AF_INET){
            inet_ntop(AF_INET, &(orig_dst.sin_addr.s_addr),&orig_dst_str, INET_ADDRSTRLEN);
	    uint16_t port=ntohs(orig_dst.sin_port);
	    
            //fprintf(stderr, "Original destination %s:%d\n", orig_dst_str,port);
	    jclass = (*env)->GetObjectClass(env, this); 
            jremoteIp = (*env)->GetFieldID(env, jclass, "remoteIp", "I"); 
            (*env)->SetIntField(env, this, jremoteIp ,(jint)ntohl(orig_dst.sin_addr.s_addr));
            jremotePort = (*env)->GetFieldID(env, jclass, "remotePort", "I"); 
            (*env)->SetIntField(env, this, jremotePort ,(jint) port);
       } else {
	    fprintf(stderr," IPv6 not supported!!!\n");
       }
    }
    return 0;
}
