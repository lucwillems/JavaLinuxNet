#!/bin/bash
#
# seems java maps i[3..9]86 to i386
# TODO : test this on amd64/ia64
# the output should match java os.arch system property
ARCH=`uname -m`
case "$ARCH" in
  i[3-9]86) echo "i386"
  ;;
  x86_64) echo "amd64"
  ;;
  *) echo $ARCH
  ;;
esac

