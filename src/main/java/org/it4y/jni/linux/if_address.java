
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

import org.it4y.util.IndexNameMap;

import java.util.Collections;
import java.util.Map;

//from include<linux/if_address.h>
public final class if_address {

    public static final Map<Integer, String> IFA_NAMES =
            Collections.unmodifiableMap(new IndexNameMap<Integer, String>() {
                {
                    put(IFA_UNSPEC, "unspec");
                    put(IFA_ADDRESS, "address");
                    put(IFA_LOCAL, "local");
                    put(IFA_LABEL, "label");
                    put(IFA_BROADCAST,"broadcast");
                    put(IFA_ANYCAST,"anycast");
                    put(IFA_CACHEINFO,"cacheinfo");
                    put(IFA_MULTICAST,"multicast");
                    }});

                    public static final int IFA_UNSPEC = 0;
                    public static final int IFA_ADDRESS = 1;
                    public static final int IFA_LOCAL = 2;
                    public static final int IFA_LABEL = 3;
                    public static final int IFA_BROADCAST = 4;
                    public static final int IFA_ANYCAST = 5;
                    public static final int IFA_CACHEINFO = 6;
                    public static final int IFA_MULTICAST = 7;
                    public static final int __IFA_MAX = IFA_MULTICAST;
                }
