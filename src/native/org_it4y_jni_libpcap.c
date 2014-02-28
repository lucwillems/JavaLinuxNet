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
#include <stdio.h>
#include <string.h>

#include "org_it4y_jni_libpcap.h"

//some errorcodes
#define OK 0;
#define ERR_JNI_ERROR -1;
#define ERR_FIND_CLASS_FAILED -2;
#define ERR_GET_METHOD_FAILED -3;
#define ERR_CALL_METHOD_FAILED -4;
#define ERR_BUFFER_TO_SMALL -5;
#define ERR_EXCEPTION -6;


// Cached Object,Field,Method ID's needed
jclass bpfProgram_class;
jmethodID bpfProgram_initID;

/*
 * Class:     org_it4y_jni_libpcap
 * Method:    initlib
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_libpcap_initlib(JNIEnv *env, jclass this) {
   fprintf(stderr,"libjninetlink3 init...\n");

   //bpfProgram class
  bpfProgram_class = (*env)->FindClass( env, "org/it4y/jni/libpcap$bpfPprogram");
  if((*env)->ExceptionOccurred(env))
      return ERR_FIND_CLASS_FAILED;

  //bpfProgram_initID method
  bpfProgram_initID = (*env)->GetMethodID(env,bpfProgram_class, "init", "(I)Ljava/nio/ByteBuffer;");
  if((*env)->ExceptionOccurred(env))
      return ERR_GET_METHOD_FAILED;


  //init ok
  fprintf(stderr,"libjninetlink3 ok\n");
  return OK;

}

/*
 * Class:     org_it4y_jni_libpcap
 * Method:    pcap_datalink_name_to_val
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_libpcap_pcap_1datalink_1name_1to_1val(JNIEnv *env, jclass this, jstring device) {
    const char *dev;

    /* get device name from java */
    dev= (*env)->GetStringUTFChars(env,device, 0);
    int result=pcap_datalink_name_to_val(dev);
    (*env)->ReleaseStringUTFChars(env, device, dev);

    return result;
}

/*
 * Class:     org_it4y_jni_libpcap
 * Method:    pcap_compile_nopcap
 * Signature: (IILorg/it4y/jni/libpcap/bpfPprogram;Ljava/lang/String;ZI)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_libpcap_pcap_1compile_1nopcap(JNIEnv *env, jclass this , jint snaplen , jint linktype , jobject bpf, jstring filter, jboolean optimize, jint mask) {

	struct bpf_program program;
	int opt=0;
    const char *filterexpr;
    jfieldID bpfProgram_bufferFieldID;

    if (optimize) {
       opt=1;
    }
    filterexpr= (*env)->GetStringUTFChars(env,filter, 0);
    int result=pcap_compile_nopcap(snaplen,linktype, &program, filterexpr, opt,mask);
    (*env)->ReleaseStringUTFChars(env, filter,filterexpr);
    if(result) {
       //#error during compile
       return result;
    }

    //insert data into the bpf program class
    //init bytebuffer
    jobject buffer=(*env)->CallObjectMethod(env, bpf, bpfProgram_initID, program.bf_len);
    if((*env)->ExceptionOccurred(env))
       return ERR_FIND_CLASS_FAILED;

    char* b = (char *)(*env)->GetDirectBufferAddress(env,(jobject)buffer);
    memcpy(b, program.bf_insns, program.bf_len*sizeof(struct bpf_insn));

    //for testing
    //int i;
  	//struct bpf_insn *ins = program.bf_insns;
	//for (i = 0; i < program.bf_len; ++ins, ++i) {
	//	printf("  //%04d : 0x%x 0x%x 0x%x 0x%x\n",
	//	        i,
	//	        ins->code,
	//               ins->jt, ins->jf,
	//                ins->k
	//	      );
    //}
    return 0;
}

/*
 * Class:     org_it4y_jni_libpcap
 * Method:    bpf_filter
 * Signature: ([BIILjava/nio/ByteBuffer;)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_libpcap_bpf_1filter(JNIEnv *env, jclass this , jobject program, jint snaplen, jint pktlen, jobject packet) {
  struct pcap_pkthdr hdr;
  struct bpf_insn *pc;
  char* pkt;

  pc= (struct bpf_insn *)(*env)->GetDirectBufferAddress(env,(jobject)program);
  pkt = (char *)(*env)->GetDirectBufferAddress(env,(jobject)packet);
  int result=bpf_filter(pc,pkt,snaplen,pktlen);
  return result;
}

/*
 * Class:     org_it4y_jni_libpcap
 * Method:    bpf_validate
 * Signature: (Ljava/nio/ByteBuffer;I)I
 */
JNIEXPORT jint JNICALL Java_org_it4y_jni_libpcap_bpf_1validate(JNIEnv *env, jclass this, jobject program, jint size) {
  struct bpf_insn *pc;

  if (program != NULL) {
      pc= (struct bpf_insn *)(*env)->GetDirectBufferAddress(env,(jobject)program);
  } else {
    return -1;
  }
  int result=bpf_validate(pc,size);
  return result;
}

