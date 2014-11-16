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

package org.it4y.benchmarks;

import org.it4y.jni.linux.time;
import org.it4y.jni.linuxutils;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by luc on 11/16/14.
 */

@Warmup(iterations = 10)
@Fork(1)
@Threads(4)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Measurement(time = 1000,timeUnit = TimeUnit.MILLISECONDS)

public class TimeBenchmark {

    @Benchmark
    public void benchmarkMonotonicCoarse() {
        linuxutils.clock_getTime(time.CLOCK_MONOTONIC_COARSE);
    }
    @Benchmark
    public void benchmarkMonotonicRaw() {
        linuxutils.clock_getTime(time.CLOCK_MONOTONIC_RAW);
    }
    @Benchmark
    public void benchmarkClockMonotonicCoarse() {
        linuxutils.clock_getTime(time.CLOCK_MONOTONIC_COARSE);
    }

    @Benchmark
    public void benchmarkUsecTime() {
        linuxutils.usecTime();
    }
    @Benchmark
    public void benchmarkUsecTimeBaseOnCLOCKMONOTONICCOARSE() {
        linuxutils.usecTime(time.CLOCK_MONOTONIC_COARSE);
    }

    @Benchmark
    public void benchmarknanoTime() {
        System.nanoTime();
    }

    @Benchmark
    public void benchmarkusecCLOCK_MONOTONIC_COARSE_TimeAccurancy() {
        long t=0;
        while(linuxutils.usecTime(time.CLOCK_MONOTONIC_COARSE)==t && t !=0) {}
    }

    @Benchmark
    public void benchmarkgetTimeCLOCK_MONOTONIC_COARSE_TimeAccurancy() {
        long t=0;
        while(linuxutils.clock_getTime(time.CLOCK_MONOTONIC_COARSE)==t && t!=0) {}
    }
}
