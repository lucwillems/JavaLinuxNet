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
  apt-get install pkg-config build-essential iproute-dev libnl-3-dev

Testing it:
============
* Unit test can run on any system without changes.
* Integration test under /org/it4y/integration requires
  * Linux with kernel 3.5 or better
  * run the src/test/scripts/setup-test.sh before running the test.

Required permissions :
======================
The integration test require additional capabilities or be run as root (which i would not do !!!)

To run the script as normal user, you require to
 * Have a recent 3.x Linux kernel
 * linux capabilities https://www.kernel.org/pub/linux/libs/security/linux-privs/kernel-2.2/capfaq-0.2.txt
 * java JDK 7 or better (it will not work with jdk6)

run following command on your JDK java command

 setcap "cap_net_raw=+eip cap_net_admin=+eip" <path to your java executable>

You MUST read following link to understand the issues :

   http://bugs.sun.com/view_bug.do?bug_id=7076745

TODO:
=====
* release it
* allot , linux is big...
