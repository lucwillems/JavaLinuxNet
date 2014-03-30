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

/**
 * Created by luc on 1/4/14.
 */
//from include<linux/if_neighbour.h>
public final class if_neighbour {
    public static final Map<Integer, String> NDA_NAMES =
            Collections.unmodifiableMap(new IndexNameMap<Integer, String>() {
                {
                    put(NDA_UNSPEC,"unspec");
                    put(NDA_DST,"dst");
                    put(NDA_LLADDR,"lladdr");
                    put(NDA_CACHEINFO,"cacheinfo");
                    put(NDA_PROBES,"probes");
                    put(NDA_VLAN,"vlan");
                    put(NDA_PORT,"port");
                    put(NDA_VNI,"vni");
                    put(NDA_VNI,"ifindex");
                }});

                    public static final int NDA_UNSPEC = 0;
                    public static final int NDA_DST = 1;
                    public static final int NDA_LLADDR = 2;
                    public static final int NDA_CACHEINFO = 3;
                    public static final int NDA_PROBES = 4;
                    public static final int NDA_VLAN = 5;
                    public static final int NDA_PORT = 6;
                    public static final int NDA_VNI = 7;
                    public static final int NDA_IFINDEX = 8;
                    public static final int __NDA_MAX = NDA_IFINDEX;
                }
