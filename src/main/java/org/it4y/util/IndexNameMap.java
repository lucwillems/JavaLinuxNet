/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */
package org.it4y.util;

import java.util.HashMap;

public class IndexNameMap<K,V> extends HashMap<K,V>{

    public IndexNameMap() {
        super();
    }

    @SuppressWarnings("unchecked")
    public V get(Object k) {
        V s=super.get(k);
        return s==null ? (V) String.format("[%s]", k.toString()) : (V) s;
    }
}
