#!/bin/bash
#
set -x
libdir=../../../../../../nlib
cd ../../../../../../out/production/trunk
ln -s $libdir clib
java  -XX:+PrintCompilation -XX:CompileThreshold=100 -XX:+TieredCompilation -server -cp . TestTun.test


