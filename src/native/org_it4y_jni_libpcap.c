/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */
#include <errno.h>
#include <jni.h>
//libpcap
#include <pcap.h>
#include "org_it4y_jni_libpcap.h"

/*
 * Class:     org_it4y_jni_libpcap
 * Method:    initlib
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_libpcap_initlib(JNIEnv *env, jclass this) {
  return 0;
}

/*
 * Class:     org_it4y_jni_libpcap
 * Method:    pcap_datalink_name_to_val
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_libpcap_pcap_1datalink_1name_1to_1val(JNIEnv *env, jclass this, jstring device) {

    return 0;
}

/*
 * Class:     org_it4y_jni_libpcap
 * Method:    pcap_compile_nopcap
 * Signature: (IILorg/it4y/jni/libpcap/pcap_instruction;Ljava/lang/String;ZI)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_libpcap_pcap_1compile_1nopcap(JNIEnv *env, jclass this , jint snaplen , jint linktype , jobject callback, jstring filter, jboolean optimize, jint mask) {

    return 0;
}

/*
 * Class:     org_it4y_jni_libpcap
 * Method:    pcap_offline_filter
 * Signature: ([BIILjava/nio/ByteBuffer;)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_libpcap_pcap_1offline_1filter(JNIEnv *env, jclass this , jbyteArray program, jint snaplen, jint pktlen, jobject pkt) {
  return 0;
}
