package com.webVueBlog.protocol.util;

import java.util.Map;

/**
 * 键值对
 */
public class KeyValuePair<K, V> implements Map.Entry<K, V> {

    private K key;
    private V value;

    public KeyValuePair() {
    }

    public KeyValuePair(K key) {
        this.key = key;
    }

    @Override
    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        this.value = value;
        return value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(32);
        sb.append("KeyValuePair{key=").append(key);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
