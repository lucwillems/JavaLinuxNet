/*
 * Copyright 20124 Luc Willems (T.M.M.)
 *
 * We licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
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

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by luc on 1/8/14.
 */
public class if_arpTest {
    @Test
    public void ifarpNamesTest() {
        Assert.assertNotNull(if_arp.ARPHDR_NAMES.get(Integer.MAX_VALUE));
        Assert.assertEquals("[" + Integer.MAX_VALUE + "]", if_arp.ARPHDR_NAMES.get(Integer.MAX_VALUE));
        Assert.assertEquals("appletalk",if_arp.ARPHDR_NAMES.get(if_arp.ARPHRD_APPLETLK));
    }

}
