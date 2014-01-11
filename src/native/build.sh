#!/bin/bash
#
echo "build so"
JAVA_HOME=/usr/java/jdk1.6.0_45

set -x
gcc -I$JAVA_HOME/include/linux -I$JAVA_HOME/include \
    -shared -o libjnitproxy.so org_it4y_jni_tproxy.c

gcc -I$JAVA_HOME/include/linux -I$JAVA_HOME/include \
    -shared -o libjnituntap.so org_it4y_jni_tuntap.c

gcc -I$JAVA_HOME/include/linux -I$JAVA_HOME/include $(pkg-config --cflags --libs libnl-3.0) -lnetlink\
    -shared -o libjnilinuxutils.so -Wl,-whole-archive /usr/lib/libnetlink.a -Wl,-no-whole-archive \
    org_it4y_jni_linuxutils.c

gcc -I$JAVA_HOME/include/linux -I$JAVA_HOME/include $(pkg-config --cflags --libs libnl-3.0) -lnetlink\
    -shared -o libjninetlink3.so -Wl,-whole-archive /usr/lib/libnetlink.a -Wl,-no-whole-archive \
    org_it4y_jni_libnetlink3.c
if [ ! -d ../../nlib ];then
   mkdir ../../nlib
fi
cp libjnitproxy.so libjnituntap.so libjnilinuxutils.so libjninetlink3.so ../../nlib

