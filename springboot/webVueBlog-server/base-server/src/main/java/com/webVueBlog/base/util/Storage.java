package com.webVueBlog.base.util;

/**
 * 存储管理
 * 
 */
public interface Storage<K,V> {

    V push(K key, V value);

    V pop(K key);

    V remove(K key);

    boolean isContains(K key);

    Object getStorage();
}
