#include <stdio.h>
#include <stdint.h>

typedef uint32_t u32;
typedef uint8_t u8;



/* jhash.h: Jenkins hash support.
 *
 * Copyright (C) 2006. Bob Jenkins (bob_jenkins@burtleburtle.net)
 *
 * http://burtleburtle.net/bob/hash/
 *
 * These are the credits from Bob's sources:
 *
 * lookup3.c, by Bob Jenkins, May 2006, Public Domain.
 *
 * These are functions for producing 32-bit hashes for hash table lookup.
 * hashword(), hashlittle(), hashlittle2(), hashbig(), mix(), and final()
 * are externally useful functions.  Routines to test the hash are included
 * if SELF_TEST is defined.  You can use this free for any purpose.  It's in
 * the public domain.  It has no warranty.
 *
 * Copyright (C) 2009-2010 Jozsef Kadlecsik (kadlec@blackhole.kfki.hu)
 *
 * I've modified Bob's hash to be useful in the Linux kernel, and
 * any bugs present are my fault.
 * Jozsef
 */

/* Best hash sizes are of power of two */
#define jhash_size(n)   ((u32)1<<(n))
/* Mask the hash value, i.e (value & jhash_mask(n)) instead of (value % n) */
#define jhash_mask(n)   (jhash_size(n)-1)

/**
 * rol32 - rotate a 32-bit value left
 * @word: value to rotate
 * @shift: bits to roll
 */
static inline u32 rol32(u32 word, unsigned int shift)
{
        u32 a=(word << shift);
	u32 b=(word >> (32 - shift));
	u32 c=a | b;
        u32 x=(word << shift) | (word >> (32 - shift));
        fprintf(stderr,"rol32: %08x %d : %08x  a=%08x b=%08x c=%08x\n",word,shift,x,a,b,c);
	return x;
}

/* __jhash_mix -- mix 3 32-bit values reversibly. */
#define __jhash_mix(a, b, c)			\
{						\
	a -= c;  a ^= rol32(c, 4);  c += b;	\
	b -= a;  b ^= rol32(a, 6);  a += c;	\
	c -= b;  c ^= rol32(b, 8);  b += a;	\
	a -= c;  a ^= rol32(c, 16); c += b;	\
	b -= a;  b ^= rol32(a, 19); a += c;	\
	c -= b;  c ^= rol32(b, 4);  b += a;	\
}

/* __jhash_final - final mixing of 3 32-bit values (a,b,c) into c */
#define __jhash_final(a, b, c)			\
{						\
	c ^= b; c -= rol32(b, 14);		\
	a ^= c; a -= rol32(c, 11);		\
	b ^= a; b -= rol32(a, 25);		\
	c ^= b; c -= rol32(b, 16);		\
	a ^= c; a -= rol32(c, 4);		\
	b ^= a; b -= rol32(a, 14);		\
	c ^= b; c -= rol32(b, 24);		\
}

/* An arbitrary initial parameter */
#define JHASH_INITVAL		0xdeadbeef


/* jhash_3words - hash exactly 3, 2 or 1 word(s) */
static inline u32 jhash_3words(u32 a, u32 b, u32 c, u32 initval)
{
	a += JHASH_INITVAL;
	b += JHASH_INITVAL;
	c += initval;

	fprintf(stderr,"1 a: %08x b: %08x  c: %08x\n",a,b,c);


	c ^= b; 
	fprintf(stderr,"2 a: %08x b: %08x  c: %08x\n",a,b,c);
	c -= rol32(b, 14);
	fprintf(stderr,"3 a: %08x b: %08x  c: %08x\n",a,b,c);

	a ^= c; 
	fprintf(stderr,"4 a: %08x b: %08x  c: %08x\n",a,b,c);
	a -= rol32(c, 11);
	fprintf(stderr,"5 a: %08x b: %08x  c: %08x\n",a,b,c);
	b ^= a; 
	fprintf(stderr,"6 a: %08x b: %08x  c: %08x\n",a,b,c);
	b -= rol32(a, 25);
	fprintf(stderr,"7 a: %08x b: %08x  c: %08x\n",a,b,c);
	c ^= b; 
	fprintf(stderr,"8 a: %08x b: %08x  c: %08x\n",a,b,c);
	c -= rol32(b, 16);
	fprintf(stderr,"9 a: %08x b: %08x  c: %08x\n",a,b,c);
	a ^= c; 
	fprintf(stderr,"10 a: %08x b: %08x  c: %08x\n",a,b,c);
	a -= rol32(c, 4);
	fprintf(stderr,"11 a: %08x b: %08x  c: %08x\n",a,b,c);
	b ^= a; 
	fprintf(stderr,"12 a: %08x b: %08x  c: %08x\n",a,b,c);
	b -= rol32(a, 14);
	fprintf(stderr,"13 a: %08x b: %08x  c: %08x\n",a,b,c);
	c ^= b; 
	fprintf(stderr,"14 a: %08x b: %08x  c: %08x\n",a,b,c);
	c -= rol32(b, 24);
	fprintf(stderr,"15 a: %08x b: %08x  c: %08x\n",a,b,c);


	fprintf(stderr,"x a: %08x b: %08x  c: %08x\n",a,b,c);
	//__jhash_final(a, b, c);

	return c;
}

static inline u32 jhash_2words(u32 a, u32 b, u32 initval)
{
	return jhash_3words(a, b, 0, initval);
}

static inline u32 jhash_1word(u32 a, u32 initval)
{
	return jhash_3words(a, 0, 0, initval);
}


int main()
{

  fprintf(stderr,"0xffffffff,0xeeeeeeee,0xdddddddd,0x00000000 : %08x\n",jhash_3words(0xffffffff,0xeeeeeeee,0xdddddddd,0x00000000));
  fprintf(stderr,"0x11223344,0x44332211,0x01020304,0x12345678 : %08x\n",jhash_3words(0x11223344,0x44332211,0x01020304,0x12345678));
  fprintf(stderr,"0x00000000,0x00000000,0x00000000,0x00000000 : %08x\n",jhash_3words(0x00000000,0x00000000,0x00000000,0x00000000));
  return 0;
}
