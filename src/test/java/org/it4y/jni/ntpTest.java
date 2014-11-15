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

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by luc on 5/19/14.
 */
public class ntpTest {
   private Logger logger= LoggerFactory.getLogger(ntpTest.class);

    @Ignore
    @Test
    public void testNtptime2() throws InterruptedException {
        while(true) {
            ntp.timex time = ntp.getNtpTime();
            logger.info("time: {}", time);
            Thread.sleep(1000);
        }
    }
}
