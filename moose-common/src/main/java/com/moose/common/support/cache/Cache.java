package com.moose.common.support.cache;

public interface Cache<K, V> {

    void put(K k, V v);

    V get(K k);

}
