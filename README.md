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
  * run the src/test/scripts/setup-test.sh to be run in front.

Note : test are run like normal users. Some test require additional priviledges which are only
granted to linux or using linux capabilities. You can use setcap to give you more rights by

 setcap "cap_net_raw=+eip cap_net_admin=+eip" <path to your jvm>

You MUST read following link to understand the issues :

    http://bugs.sun.com/view_bug.do?bug_id=7076745


TODO:
=====
allot...
