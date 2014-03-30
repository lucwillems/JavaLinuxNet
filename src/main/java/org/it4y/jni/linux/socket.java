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
 * Created by luc on 1/10/14.
 */
//from include<bits/socket.h>
public final class socket {
    public final static short AF_UNSPEC=0;
    public final static short AF_LOCAL=1;
    public final static short AF_UNIX=AF_LOCAL;
    public final static short AF_FILE=AF_LOCAL;
    public final static short AF_INET=2;
    public final static short AF_INET6=10;

}
