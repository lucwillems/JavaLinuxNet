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

#include <sys/timex.h>
#include <time.h>
 #include "org_it4y_jni_libnetlink3.h"

//some errorcodes
#define OK 0;
#define ERR_JNI_ERROR -1;
#define ERR_FIND_CLASS_FAILED -2;
#define ERR_FIND_FIELD_FAILED -3;
#define ERR_CALL_METHOD_FAILED -4;
#define ERR_BUFFER_TO_SMALL -5;
#define ERR_EXCEPTION -6;



// Cached Object,Field,Method ID's needed
jclass jtimex_class;
jfieldID jtimex_freqID;
jfieldID jtimex_offsetID;
jfieldID jtimex_tvsecID;
jfieldID jtimex_tvusecID;
jfieldID jtimex_maxerrorID;
jfieldID jtimex_esterrorID;
jfieldID jtimex_constantID;
jfieldID jtimex_precisionID;
jfieldID jtimex_toleranceID;
jfieldID jtimex_tickID;
jfieldID jtimex_ppsfreqID;
jfieldID jtimex_jitterID;
jfieldID jtimex_stabilID;
jfieldID jtimex_jitcntID;
jfieldID jtimex_calcntID;
jfieldID jtimex_errcntID;
jfieldID jtimex_stbcntID;
jfieldID jtimex_taiID;
jfieldID jtimex_shiftID;
jfieldID jtimex_statusID;

/*
 * Initialize JNI interfaces for fast lookup. this method must be called first before anything will work
 */
/*
 * Class:     org_it4y_jni_ntp
 * Method:    initlib
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_ntp_initlib(JNIEnv *env , jclass this) {

   fprintf(stderr,"libjnintp init...\n");

   //tuntap class
   jtimex_class = (*env)->FindClass( env, "org/it4y/jni/ntp$timex");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_CLASS_FAILED;
   jtimex_tvsecID = (*env)->GetFieldID(env, jtimex_class, "tv_sec", "J");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;
   jtimex_tvusecID = (*env)->GetFieldID(env, jtimex_class, "tv_usec", "J");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;
   jtimex_freqID = (*env)->GetFieldID(env, jtimex_class, "freq", "J");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;
   jtimex_offsetID = (*env)->GetFieldID(env, jtimex_class, "offset", "J");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;
   jtimex_maxerrorID = (*env)->GetFieldID(env, jtimex_class, "maxerror", "J");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;
   jtimex_esterrorID = (*env)->GetFieldID(env, jtimex_class, "esterror", "J");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;
   jtimex_constantID = (*env)->GetFieldID(env, jtimex_class, "constant", "J");
   if((*env)->ExceptionOccurred(env))
        return ERR_FIND_FIELD_FAILED;
   jtimex_precisionID = (*env)->GetFieldID(env, jtimex_class, "precision", "J");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;
   jtimex_toleranceID = (*env)->GetFieldID(env, jtimex_class, "tolerance", "J");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;
   jtimex_tickID = (*env)->GetFieldID(env, jtimex_class, "tick", "J");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;
   jtimex_ppsfreqID = (*env)->GetFieldID(env, jtimex_class, "ppsfreq", "J");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;
   jtimex_jitterID = (*env)->GetFieldID(env, jtimex_class, "jitter", "J");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;
   jtimex_stabilID = (*env)->GetFieldID(env, jtimex_class, "stabil", "J");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;
   jtimex_jitcntID = (*env)->GetFieldID(env, jtimex_class, "jitcnt", "J");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;
   jtimex_calcntID = (*env)->GetFieldID(env, jtimex_class, "calcnt", "J");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;
   jtimex_errcntID = (*env)->GetFieldID(env, jtimex_class, "errcnt", "J");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;
   jtimex_stbcntID = (*env)->GetFieldID(env, jtimex_class, "stbcnt", "J");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;
   jtimex_shiftID = (*env)->GetFieldID(env, jtimex_class, "shift", "I");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;
   jtimex_taiID = (*env)->GetFieldID(env, jtimex_class, "tai", "I");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;
   jtimex_statusID = (*env)->GetFieldID(env, jtimex_class, "status", "I");
   if((*env)->ExceptionOccurred(env))
      return ERR_FIND_FIELD_FAILED;

   //init ok
   return OK;
}


/*
 * Class:     org_it4y_jni_ntp
 * Method:    ntp_gettime
 * Signature: (Lorg/it4y/jni/ntp/ntpTime;)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_ntp_ntp_1gettime(JNIEnv *env , jclass this, jobject jtimex) {
   struct timex now;
   int result;
   /* mode 0=get time */
   now.modes=0;
   result=adjtimex(&now);

   //set data into ntpTime
   //fprintf(stderr,"%x %x",now.status,now.time.tv_sec);

   //copy result in ntpTime
   (*env)->SetLongField(env, jtimex, jtimex_tvsecID,now.time.tv_sec);
   (*env)->SetLongField(env, jtimex, jtimex_tvusecID,now.time.tv_usec);
   (*env)->SetLongField(env, jtimex, jtimex_offsetID,now.offset);
   (*env)->SetLongField(env, jtimex, jtimex_freqID,now.freq);
   (*env)->SetLongField(env, jtimex, jtimex_maxerrorID,now.maxerror);
   (*env)->SetLongField(env, jtimex, jtimex_esterrorID,now.esterror);
   (*env)->SetLongField(env, jtimex, jtimex_constantID,now.constant);
   (*env)->SetLongField(env, jtimex, jtimex_precisionID,now.precision);
   (*env)->SetLongField(env, jtimex, jtimex_toleranceID,now.tolerance);
   (*env)->SetLongField(env, jtimex, jtimex_tickID,now.tick);
   (*env)->SetLongField(env, jtimex, jtimex_ppsfreqID,now.ppsfreq);
   (*env)->SetLongField(env, jtimex, jtimex_jitterID,now.jitter);
   (*env)->SetLongField(env, jtimex, jtimex_stabilID,now.stabil);
   (*env)->SetLongField(env, jtimex, jtimex_jitcntID,now.jitcnt);
   (*env)->SetLongField(env, jtimex, jtimex_calcntID,now.calcnt);
   (*env)->SetLongField(env, jtimex, jtimex_errcntID,now.errcnt);
   (*env)->SetLongField(env, jtimex, jtimex_stbcntID,now.stbcnt);
   (*env)->SetIntField(env,  jtimex, jtimex_statusID,now.status);
   (*env)->SetIntField(env,  jtimex, jtimex_shiftID,now.shift);
   (*env)->SetIntField(env,  jtimex, jtimex_taiID,now.tai);

   return result;
}
