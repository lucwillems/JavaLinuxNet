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

TODO:
=====
allot...
