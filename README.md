Java Linux Network integration
========================

This is a set extensions on Java Classes to implement and use
native Linux network features.

supported features :
* TProxy based Sockets
* TunTap devices
* libnet3 integration to get network link events and information
* Basic raw packet handling

Building it:
============
* java stuf : just run mvn clean install
* to build native code , you need to install some dev packages. on ubuntu this is

ubuntu 12.04:
```
    sudo apt-get install pkg-config build-essential openjdk-7-jdk maven2 git
    sudo apt-get iproute-dev libpcap0.8-dev libnl-3-dev libcap2-bin 
``` 

ubuntu 14.04:

Trusty is missing iproute-dev. iproute is replaced by iproute2 but doesn't provide
the iproute2-dev packages. this is required because it includes /usr/lib/libnetlink.a which is required. I have created a update iproute2 packages in my lauchpad repository for trusty.
this is only required for building.
```
    sudo apt-get install software-properties-common
    sudo add-apt-repository ppa:luc-willems/backport
    sudo apt-get update
    sudo apt-get install pkg-config build-essential git maven2 openjdk-7-jdk 
    sudo apt-get install iproute2-dev libpcap0.8-dev libnl-3-dev libcap2-bin
``` 

opensuse 13.2:
```
    TODO
```
centos x:
```
    TODO
```
after you have installed the packages you can use git to clone the source repository
and run following commands to build it
```
     git clone <url of git repository>
     cd  JavaLinixNet
     sudo src/test/scripts/setup-test.sh
     mvn clean install
```

Testing it:
============
* Unit test can run on any system without changes.
* Integration test under /org/it4y/integration requires
  * Linux with kernel 3.5 or better
  * run the src/test/scripts/setup-test.sh before running the test.

IPV6 :
======
currently IPV6 is not supported. IPv6 addresses will be ignored.

Required permissions :
======================
The integration test require additional capabilities or be run as root (which i would not do !!!)

To run the script as normal user, you require to
 * Have a recent 3.x Linux kernel
 * linux capabilities https://www.kernel.org/pub/linux/libs/security/linux-privs/kernel-2.2/capfaq-0.2.txt
 * java JDK 7 or better (it will not work with jdk6)

run following command on your JDK java command
```
 setcap "cap_net_raw=+eip cap_net_admin=+eip" <path to your java executable>
```
You MUST read following link to understand the issues :

   http://bugs.sun.com/view_bug.do?bug_id=7076745

Security note:
==============
When setting capabilities on the java JVM, this JVM can also be used by other java application and will
have the same capabilities. This could be security risk because capabilities are given to
java application , not your jars , and any user can use this jvm.

when using this , you should install your jre into a seperated JAVA_HOME and set user permissions for files
and directories so execution is limited to he user which run's your application.


TODO:
=====
* release it
* allot , linux is big...
