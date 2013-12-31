#!/bin/bash
#
echo "build so"
JAVA_HOME=/usr/java/jdk1.6.0_45

set -x
gcc -I$JAVA_HOME/include/linux -I$JAVA_HOME/include \
    -shared -o libtproxy.so org_it4y_jni_tproxy.c

gcc -I$JAVA_HOME/include/linux -I$JAVA_HOME/include \
    -shared -o libtuntap.so org_it4y_jni_tuntap.c

gcc -I$JAVA_HOME/include/linux -I$JAVA_HOME/include $(pkg-config --cflags --libs libnl-3.0) -lnetlink\
    -shared -o liblinuxutils.so -Wl,-whole-archive /usr/lib/libnetlink.a -Wl,-no-whole-archive \
    org_it4y_jni_linuxutils.c


cp libtproxy.so libtuntap.so liblinuxutils.so ../../nlib

