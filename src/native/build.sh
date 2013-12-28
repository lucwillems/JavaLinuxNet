#!/bin/bash
#
echo "build so"
JAVA_HOME=/usr/java/jdk1.6.0_45

set -x
gcc -I$JAVA_HOME/include/linux -I$JAVA_HOME/include \
    -shared -o libtproxy.so org_it4y_net_TProxyServerSocket.c

gcc -I$JAVA_HOME/include/linux -I$JAVA_HOME/include \
    -shared -o libtuntap.so org_it4y_net_tuntap_TunTapLinux.c

cp libtproxy.so libtuntap.so ../../nlib

