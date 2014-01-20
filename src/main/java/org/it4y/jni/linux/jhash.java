/*
 * This file is derived from Linux kernel headers
 * All copyrights preserved to original kernel author.
 *
 * more info see https://www.kernel.org/pub/linux/kernel/COPYING
 *
/* jhash.h: Jenkins hash support.
 *
 * Copyright (C) 2006. Bob Jenkins (bob_jenkins@burtleburtle.net)
 *
 * http://burtleburtle.net/bob/hash/
 *
 * These are the credits from Bob's sources:
 *
 * lookup3.c, by Bob Jenkins, May 2006, Public Domain.
 */

package org.it4y.jni.linux;

/**
 * Created by luc on 1/18/14.
 */
//based on Linux  include/linux/jhash.h
public class jhash {

    public static final int JHASH_INITVAL=0xdeadbeef;

    /**
     * rol32 - rotate a 32-bit value left
     * @word: value to rotate
     * @shift: bits to roll
     * return (word << shift) | (word >> (32 - shift));
     */

    /* because JAVA DOESN'T UNDERSTAND unsigned integers , we need to do this shit */
    public static int rol32(final int x, final int shift) {
        return (x << shift ) |(int)(((long)x&0xffffffffL) >> (32L - shift));
    }

    public static int jhash_3words(int a, int b, int c, final int initval) {
      a += JHASH_INITVAL;
      b += JHASH_INITVAL;
      c += initval;
      /* __jhash_final(a, b, c); */
      /* we put C macros inline as function calls are slow... */
      c ^= b;
      c -= (b << 14 ) |(int)(((long)b&0xffffffffL) >> (32L - 14)); //rol32(b, 14);
      a ^= c;
      a -= (c << 11 ) |(int)(((long)c&0xffffffffL) >> (32L - 11)); //rol32(c, 11);
      b ^= a;
      b -= (a << 25 ) |(int)(((long)a&0xffffffffL) >> (32L - 25)); //rol32(a, 25);
      c ^= b;
      c -= (b << 16 ) |(int)(((long)b&0xffffffffL) >> (32L - 16)); //rol32(b, 16);
      a ^= c;
      a -= (c << 4  ) |(int)(((long)c&0xffffffffL) >> (32L - 4)); //rol32(c, 4);
      b ^= a;
      b -= (a << 14 ) |(int)(((long)a&0xffffffffL) >> (32L - 14)); //rol32(a, 14);
      c ^= b;
      c -= (b << 24 ) |(int)(((long)b&0xffffffffL) >> (32L - 24)); //rol32(b, 24);

      return c;
    }

}
