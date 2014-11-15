/*
 * Copyright 2014 Luc Willems (T.M.M.)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.it4y.jni;

/**
 * Created by luc on 5/19/14.
 */
public class ntp {

    //some error codes from our jni methods
    public static final int JNI_OK = 0;
    public static final int JNI_ERROR = -1;
    public static final int JNI_ERR_FIND_CLASS_FAILED = -2;
    public static final int JNI_ERR_GET_METHOD_FAILED = -3;
    public static final int JNI_ERR_CALL_METHOD_FAILED = -4;
    public static final int JNI_ERR_BUFFER_TO_SMALL = -5;
    public static final int JNI_ERR_EXCEPTION = -6;

    //Load our native JNI lib
    static {
        //THIS requires libnl3 !!!!
        JNILoader.loadLibrary("libjnintp");
        final int initResult = initlib();
        if (initResult != JNI_OK) {
            throw new RuntimeException("Failed to initialize ntp jni interface : " + initResult);
        }
    }

    //This method should be called first before using the library
    //it's used to initialize internal jni structure to speedup jni lookups
    private static native int initlib();

    public static class timex {
        static final int STA_PLL = 0x0001;	/* enable PLL updates (rw) */
        static final int STA_PPSFREQ = 0x0002;	/* enable PPS freq discipline (rw) */
        static final int STA_PPSTIME = 0x0004;	/* enable PPS time discipline (rw) */
        static final int STA_FLL = 0x0008;	/* select frequency-lock mode (rw) */

        static final int STA_INS = 0x0010;	/* insert leap (rw) */
        static final int STA_DEL = 0x0020;	/* delete leap (rw) */
        static final int STA_UNSYNC = 0x0040;	/* clock unsynchronized (rw) */
        static final int STA_FREQHOLD = 0x0080;	/* hold frequency (rw) */

        static final int STA_PPSSIGNAL = 0x0100;	/* PPS signal present (ro) */
        static final int STA_PPSJITTER = 0x0200;	/* PPS signal jitter exceeded (ro) */
        static final int STA_PPSWANDER = 0x0400;	/* PPS signal wander exceeded (ro) */
        static final int STA_PPSERROR = 0x0800;	/* PPS signal calibration error (ro) */

        static final int STA_CLOCKERR = 0x1000;	/* clock hardware fault (ro) */
        static final int STA_NANO = 0x2000;	/* resolution (0 = us, 1 = ns) (ro) */
        static final int STA_MODE = 0x4000;	/* mode (0 = PLL, 1 = FLL) (ro) */
        static final int STA_CLK = 0x8000;	/* clock source (0 = A, 1 = B) (ro) */

        public long tv_sec;
        public long tv_usec;
        public long offset;     /* time offset (usec) */
        public long freq;   	/* frequency offset (scaled ppm) */

        public long maxerror;  	/* maximum error (usec) */
        public long esterror;   /* estimated error (usec) */
        public long constant;   /* pll time constant */
        public long precision;  /* clock precision (usec) (ro) */
        public long tolerance;  /* clock frequency tolerance (ppm) (ro) */
        public long tick;       /* (modified) usecs between clock ticks */
        public long ppsfreq;    /* pps frequency (scaled ppm) (ro) */
        public long jitter;     /* pps jitter (us) (ro) */
        public long stabil;     /* pps stability (scaled ppm) (ro) */
        public long jitcnt;     /* jitter limit exceeded (ro) */
        public long calcnt;     /* calibration intervals (ro) */
        public long errcnt;     /* calibration errors (ro) */
        public long stbcnt;     /* stability limit exceeded (ro) */
        public int status;     /* clock command/status */
        public int tai;        /* TAI offset (ro) */
        public int shift;      /* interval duration (s) (shift) (ro) */

        @Override
        public String toString() {
            final StringBuilder stringBuilder = new StringBuilder(128);
            stringBuilder.append("\ntv_sec: ").append(tv_sec);
            stringBuilder.append("\ntv_usec: ").append(tv_usec);
            stringBuilder.append("\nmax error: ").append(maxerror).append("usec");
            stringBuilder.append("\nestimated error: ").append(esterror).append(" usec");
            stringBuilder.append("\noffset: ").append(offset).append(" usec");
            stringBuilder.append("\nfreq: ").append(freq).append(" ppm");
            stringBuilder.append("\nconstant: ").append(constant);
            stringBuilder.append("\nstatus: ").append(Integer.toHexString(status));
            return stringBuilder.toString();
        }
    }

    private static native int ntp_gettime(timex time);

    public static timex getNtpTime() {
        final timex time = new timex();
        ntp_gettime(time);
        return time;
    }

}
