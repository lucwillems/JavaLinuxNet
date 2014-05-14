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

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by luc on 5/14/14.
 */
public class linuxutilsTest {
    Logger logger= LoggerFactory.getLogger(linuxutilsTest.class);

    @Test
    public void testClockgetTime() {
        long time=linuxutils.clock_getTime(org.it4y.jni.linux.time.CLOCK_REALTIME);
        logger.info("Real time: {} nsec",time);
        Assert.assertTrue(time>0);
        time=linuxutils.clock_getTime(org.it4y.jni.linux.time.CLOCK_MONOTONIC);
        logger.info("monotonic time: {} nsec",time);
        Assert.assertTrue(time>0);
        time=linuxutils.clock_getTime(org.it4y.jni.linux.time.CLOCK_PROCESS_CPUTIME_ID);
        logger.info("process time: {} nsec",time);
        Assert.assertTrue(time > 0);
        time=linuxutils.clock_getTime(org.it4y.jni.linux.time.CLOCK_THREAD_CPUTIME_ID);
        logger.info("thread time: {} nsec",time);
        Assert.assertTrue(time > 0);
        time=linuxutils.clock_getTime(org.it4y.jni.linux.time.CLOCK_MONOTONIC_RAW);
        logger.info("monotonic raw time: {} nsec",time);
        Assert.assertTrue(time > 0);
        time=linuxutils.clock_getTime(org.it4y.jni.linux.time.CLOCK_REALTIME_COARSE);
        logger.info("realtime coarse time: {} nsec",time);
        Assert.assertTrue(time > 0);
        time=linuxutils.clock_getTime(org.it4y.jni.linux.time.CLOCK_MONOTONIC_COARSE);
        logger.info("monotonic coarse time: {} nsec",time);
        Assert.assertTrue(time > 0);
        time=linuxutils.clock_getTime(org.it4y.jni.linux.time.CLOCK_BOOTTIME);
        logger.info("boot time: {} nsec",time);
        Assert.assertTrue(time > 0);
        time=linuxutils.clock_getTime(org.it4y.jni.linux.time.CLOCK_REALTIME_ALARM);
        logger.info("realtime alarm time: {} nsec",time);
        Assert.assertTrue(time > 0);
        time=linuxutils.clock_getTime(org.it4y.jni.linux.time.CLOCK_BOOTTIME_ALARM);
        logger.info("boottime alarm time: {} nsec",time);
        Assert.assertTrue(time > 0);

    }
}
