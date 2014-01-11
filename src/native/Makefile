CC=gcc
CFLAGS=-Wall
JDK_HOME=$(shell readlink -f `which javac` | sed "s:bin/javac::")
INCLUDES = -I$(JDK_HOME)include/linux -I$(JDK_HOME)include
LFLAGS = -shared
JAVAH=javah
CLASSPATH=../../target/classes
NLIB=../../nlib

all: prepare javah build 

javah: tproxy.h tuntap.h linuxutils.h libnetlink3.h

tproxy.h: org_it4y_jni_tproxy.c
	$(JAVAH) -classpath $(CLASSPATH) org.it4y.jni.tproxy

tuntap.h: org_it4y_jni_tuntap.c
	$(JAVAH) -classpath $(CLASSPATH) org.it4y.jni.tuntap

linuxutils.h: org_it4y_jni_linuxutils.c
	$(JAVAH) -classpath $(CLASSPATH) org.it4y.jni.linuxutils

libnetlink3.h: org_it4y_jni_libnetlink3.c
	$(JAVAH) -classpath $(CLASSPATH) org.it4y.jni.libnetlink3

build:  libjnitproxy.so libjnituntap.so libjnilinuxutils.so libjninetlink3.so

libjnitproxy.so: org_it4y_jni_tproxy.c
	$(CC) $(CFLAGS) $(INCLUDES) $(LFLAGS) -o libjnitproxy.so org_it4y_jni_tproxy.c
	install libjnitproxy.so $(NLIB)

libjnituntap.so: org_it4y_jni_tuntap.c
	$(CC) $(CFLAGS) $(INCLUDES) $(LFLAGS) -o libjnituntap.so org_it4y_jni_tuntap.c
	install libjnituntap.so $(NLIB)

libjnilinuxutils.so: org_it4y_jni_linuxutils.c
	$(CC) $(CFLAGS) $(INCLUDES) $(LFLAGS) -o libjnilinuxutils.so org_it4y_jni_linuxutils.c
	install libjnilinuxutils.so $(NLIB)

libjninetlink3.so: org_it4y_jni_libnetlink3.c
	$(CC) $(CFLAGS)  $(INCLUDES) `pkg-config --cflags --libs libnl-3.0` $(LFLAGS) \
	 -o libjninetlink3.so -Wl,-whole-archive /usr/lib/libnetlink.a -Wl,-no-whole-archive org_it4y_jni_libnetlink3.c
	 install libjninetlink3.so $(NLIB)

prepare:
	install -d $(NLIB)

clean:
	rm -rf *.so
	rm -rf $(NLIB)/*.so
