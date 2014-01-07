#/bin/bash
#
CLASSPATH=../../target/classes/
javah -classpath $CLASSPATH org.it4y.jni.tuntap
javah -classpath $CLASSPATH org.it4y.jni.tproxy
javah -classpath $CLASSPATH org.it4y.jni.linuxutils
javah -classpath $CLASSPATH org.it4y.jni.libnetlink3
