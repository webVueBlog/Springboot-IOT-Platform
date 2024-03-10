package com.webVueBlog.protocol.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用数组实现key为int的Map，数组的长度等于key的最大值减去最小值
 * 因此key的区间跨度太大会造成内存空间上的浪费，仅适用于小范围且连续的key
 * 可指定value为null的缺省返回值，该返回值位于数组的末位,key为int的最大值
 */
public class ArrayMap<T> {

    public static final int DEFAULT_KEY = Integer.MAX_VALUE;
    private T[] array = (T[]) new Object[2];
    private boolean init = true;
    private int min;
    private int max;

    public T get(int key) {
        if (key < min || key > max) {
            return null;
        }
        return array[key - min];
    }

    public T getOrDefault(int key) {
        if (key < min || key > max) {
            return array[array.length - 1];
        }
        return array[key - min];
    }

    public void defaultValue(T value) {
        array[array.length - 1] = value;
    }

    public void put(int key, T value) {
        if (key == DEFAULT_KEY) {
            defaultValue(value);
            return;
        }
        if (this.init) {
            this.init = false;
            this.min = key;
            this.max = key;
        }

        int offset = Math.max(0, min - key);
        if (key < min) {
            min = key;
        }
        if (key > max) {
            max = key;
        }

        ensureCapacityInternal(offset);
        array[key - min] = value;
    }

    private void ensureCapacityInternal(int offset) {
        final int minCapacity = max - min + 2;
        if (minCapacity > 256) {
            throw new IllegalArgumentException("min:" + min + ", max:" + max + "key的区间过大");
        }
        final int length = array.length - 1;
        if (minCapacity >= length) {
            T[] temp = (T[]) new Object[minCapacity];
            System.arraycopy(array, 0, temp, offset, length);
            temp[temp.length - 1] = array[length];
            array = temp;
        } else {
            if (offset > 0) {
                System.arraycopy(array, 0, array, offset, length);
            }
        }
    }

    public List<T> values() {
        List<T> values = new ArrayList<>(array.length);
        for (T t : array) {
            if (t != null) {
                values.add(t);
            }
        }
        return values;
    }

    public int[] keys() {
        int length = size();
        int[] keys = new int[length];

        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                keys[count++] = i + min;
            }
        }

        if (array[array.length - 1] != null) {
            keys[keys.length - 1] = DEFAULT_KEY;
        }
        return keys;
    }

    public int size() {
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                count++;
            }
        }
        return count;
    }

    public ArrayMap<T> fillDefaultValue() {
        if (array.length == 2) {
            if (array[0] == null) {
                array[0] = array[1];
            }
        } else {
            for (int i = 0; i < array.length - 2; i++) {
                if (array[i] == null) {
                    array[i] = array[array.length - 1];
                }
            }
        }
        return this;
    }
}
