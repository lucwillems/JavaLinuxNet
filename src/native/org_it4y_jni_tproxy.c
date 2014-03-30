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
#include <arpa/inet.h>

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
    socklen_t addrlen = sizeof(orig_dst);

    memset(&orig_dst, 0, addrlen);

    //Socket is bound to original destination
    if(getsockname(fd, (struct sockaddr*) &orig_dst, &addrlen) < 0){
        perror("getsockname: ");
        return -1;
    } else {
        if(orig_dst.sin_family == AF_INET){
	    uint16_t port=ntohs(orig_dst.sin_port);
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
