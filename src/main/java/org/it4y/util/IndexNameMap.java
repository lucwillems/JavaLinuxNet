package org.it4y.util;

import java.util.HashMap;

/**
 * Created by luc on 1/4/14.
 */
public class IndexNameMap<K,V> extends HashMap<K,V>{

    public IndexNameMap() {
        super();
    }

    public V get(Object k) {
        V s=super.get(k);
        return s==null ? (V) String.format("[%s]", k.toString()) : (V) s;
    }
}
