/*
 * This file is derived from Linux kernel headers
 * All copyrights preserved to original kernel author.
 *
 * more info see https://www.kernel.org/pub/linux/kernel/COPYING
 */

package org.it4y.jni.linux;

/**
 * Created by luc on 1/18/14.
 */
public class jhash {

    public static final int JHASH_INITVAL=0xdeadbeef;

    /**
     * rol32 - rotate a 32-bit value left
     * @word: value to rotate
     * @shift: bits to roll
     */
    public static int rol32(final int word, final int shift) {
        return word << shift | word >> 32 - shift;
    }

    public static int jhash_3words(int a, int b, int c, final int initval) {
      a += JHASH_INITVAL;
      b += JHASH_INITVAL;
      c += initval;

      /* __jhash_final(a, b, c); */
      c ^= b; c -= rol32(b, 14);
      a ^= c; a -= rol32(c, 11);
      b ^= a; b -= rol32(a, 25);
      c ^= b; c -= rol32(b, 16);
      a ^= c; a -= rol32(c, 4);
      b ^= a; b -= rol32(a, 14);
      c ^= b; c -= rol32(b, 24);

      return c;
    }

}
