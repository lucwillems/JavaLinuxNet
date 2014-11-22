CC=gcc
CFLAGS=-Wall -O3
ARCH=$(shell ./java_arch.sh)

#search JDK if not set
ifndef JDK_HOME
  JDK_HOME=$(shell readlink -f `which javac` | sed "s:/bin/javac::")
endif

INCLUDES = -I$(JDK_HOME)/include/linux -I$(JDK_HOME)/include
LFLAGS = -shared -fPIC
JAVAH=javah
CLASSPATH=../../target/classes

all: javah build maven

javah: tproxy.h tuntap.h linuxutils.h libnetlink3.h libpcap.h ntp.h

tproxy.h: org_it4y_jni_tproxy.c
	$(JAVAH) -classpath $(CLASSPATH) org.it4y.jni.tproxy

tuntap.h: org_it4y_jni_tuntap.c
	$(JAVAH) -classpath $(CLASSPATH) org.it4y.jni.tuntap

linuxutils.h: org_it4y_jni_linuxutils.c
	$(JAVAH) -classpath $(CLASSPATH) org.it4y.jni.linuxutils

libnetlink3.h: org_it4y_jni_libnetlink3.c
	$(JAVAH) -classpath $(CLASSPATH) org.it4y.jni.libnetlink3

libpcap.h: org_it4y_jni_libpcap.c
	$(JAVAH) -classpath $(CLASSPATH) org.it4y.jni.libpcap

ntp.h: org_it4y_jni_ntp.c
	$(JAVAH) -classpath $(CLASSPATH) org.it4y.jni.ntp

libjnitproxy.so: org_it4y_jni_tproxy.c
	$(CC) $(CFLAGS) $(INCLUDES) $(LFLAGS) -o libjnitproxy-$(ARCH).so org_it4y_jni_tproxy.c

libjnituntap.so: org_it4y_jni_tuntap.c
	$(CC) $(CFLAGS) $(INCLUDES) $(LFLAGS) -o libjnituntap-$(ARCH).so org_it4y_jni_tuntap.c

libjnilinuxutils.so: org_it4y_jni_linuxutils.c
	$(CC) $(CFLAGS) $(INCLUDES) $(LFLAGS) -o libjnilinuxutils-$(ARCH).so org_it4y_jni_linuxutils.c -lrt

libjninetlink3.so: org_it4y_jni_libnetlink3.c
	$(CC) $(CFLAGS)  $(INCLUDES) `pkg-config --cflags --libs libnl-3.0` $(LFLAGS) \
	 -o libjninetlink3-$(ARCH).so -Wl,-whole-archive /usr/lib/libnetlink.a -Wl,-no-whole-archive org_it4y_jni_libnetlink3.c

libjnipcap.so: org_it4y_jni_libpcap.c
	$(CC)   $(INCLUDES) `pcap-config --cflags ` $(LFLAGS) \
	org_it4y_jni_libpcap.c -lpcap -o libjnipcap-$(ARCH).so

libjnintp.so: org_it4y_jni_ntp.c
	$(CC)   $(INCLUDES) $(LFLAGS) org_it4y_jni_ntp.c -o libjnintp-$(ARCH).so

testjhash: testjhash.c
	$(CC) $(CFLAGS) $(INCLUDES) testjhash.c -o testjhash

bpf_compile: bpf_compile.c
	$(CC) $(CFLAGS) $(INCLUDES) `pcap-config --cflags ` bpf_compile.c -lpcap -o bpf_compile

build:  libjnitproxy.so libjnituntap.so libjnilinuxutils.so libjninetlink3.so libjnipcap.so libjnintp.so testjhash bpf_compile

maven:
	cp *.so $(CLASSPATH)

clean:
	rm -rf *.so
	rm -rf testjhash
	rm -rf bpf_compile
