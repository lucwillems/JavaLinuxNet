package org.it4y.jni.linux;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by luc on 1/20/14.
 */
public class jhashTest {

    /* we use src/native/testjhash.c for verification */
    @Test
    public void rol32Test() {
        //0xffffffff,0xeeeeeeee,0xdddddddd,0x00000000 : 44ca8698
        int a,shift;

        a=0xcd9caddd;
        shift=14;
        Assert.assertEquals(0x2b777367,jhash.rol32(a,shift));

        a=0xe4c9fc99;
        shift=11;
        Assert.assertEquals(0x4fe4cf26,jhash.rol32(a,shift));

        a=0xea7f7351;
        shift=25;
        Assert.assertEquals(0xa3d4fee6,jhash.rol32(a,shift));

        a=0x840edfa6;
        shift=16;
        Assert.assertEquals(0xdfa6840e, jhash.rol32(a, shift));

        a=0x81209f31;
        shift=4;
        Assert.assertEquals(0x1209f318,jhash.rol32(a,shift));

        a=0x5955f948;
        shift=14;
        Assert.assertEquals(0x7e521655,jhash.rol32(a,shift));

        a=0x5f091099;
        shift=24;
        Assert.assertEquals(0x995f0910,jhash.rol32(a,shift));
    }
    
 
    @Test
    public void jhash3wordsTest() {
        int a,b,c,initval;

        //0xffffffff,0xeeeeeeee,0xdddddddd,0x00000000 : 44ca8698
        a=0xffffffff;
        b=0xeeeeeeee;
        c=0xdddddddd;
        initval=0x00000000;
        Assert.assertEquals(0x44ca8698,jhash.jhash_3words(a,b,c,initval));

        //0x11223344,0x44332211,0x01020304,0x12345678 : 0bb15e12
        a=0x11223344;
        b=0x44332211;
        c=0x01020304;
        initval=0x12345678;
        Assert.assertEquals(0x0bb15e12,jhash.jhash_3words(a,b,c,initval));

        //0x00000000,0x00000000,0x00000000,0x00000000 : 0bb15e12
        a=0x00000000;
        b=0x00000000;
        c=0x00000000;
        initval=0x00000000;
        Assert.assertEquals(0xc0b0a2c2,jhash.jhash_3words(a,b,c,initval));

    }
}
