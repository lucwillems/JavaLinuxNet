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

package org.it4y.jni.linux;

/**
 * Created by luc on 5/14/14.
 */
public class time {
    /* Identifier for system-wide realtime clock.  */
    public static final int CLOCK_REALTIME=0;

    /* Monotonic system-wide clock.  */
    public static final int CLOCK_MONOTONIC=1;

    /* High-resolution timer from the CPU.  */
    public static final int CLOCK_PROCESS_CPUTIME_ID=2;

    /* Thread-specific CPU-time clock.  */
    public static final int CLOCK_THREAD_CPUTIME_ID=3;

    /* Monotonic system-wide clock, not adjusted for frequency scaling.  */
    public static final int CLOCK_MONOTONIC_RAW=4;

    /* Identifier for system-wide realtime clock, updated only on ticks.  */
    public static final int CLOCK_REALTIME_COARSE=5;

    /* Monotonic system-wide clock, updated only on ticks.  */
    public static final int CLOCK_MONOTONIC_COARSE=6;

    /* Monotonic system-wide clock that includes time spent in suspension.  */
    public static final int CLOCK_BOOTTIME=7;

    /* Like CLOCK_REALTIME but also wakes suspended system.  */
    public static final int CLOCK_REALTIME_ALARM=8;

    /* Like CLOCK_BOOTTIME but also wakes suspended system.  */
    public static final int CLOCK_BOOTTIME_ALARM=9;
}
