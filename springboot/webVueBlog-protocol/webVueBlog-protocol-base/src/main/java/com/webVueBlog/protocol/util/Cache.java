package com.webVueBlog.protocol.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Cache<K,V> {// 缓存类
    private volatile Map<K, V> cache;// 缓存数据

    public Cache() {
        this(32);
    }/** 构造函数，初始化缓存 */

    public Cache(Map<K, V> cache) {
        this.cache = cache;
    }/** 构造函数，传入一个Map实例 */

    public Cache(int initialCapacity) {
        this.cache = new HashMap<>((int) (initialCapacity / 0.75) + 1);
    }/** 构造函数，传入一个初始容量 */

    public V get(K key) {
        return cache.get(key);
    }/** 根据键获取值 */

    public V get(K key, Supplier<V> function) {// 根据键获取值，如果不存在则调用Supplier函数获取 */
        V value = cache.get(key);// 先从缓存中获取值 */
        if (value == null) {// 如果缓存中不存在 */
            synchronized (cache) {// 加锁，保证线程安全 */
                value = cache.get(key);// 再次检查缓存，可能其他线程已经添加了该键值对 */
                if (value == null) {// 如果缓存中还是不存在 */
                    cache.put(key, value = function.get());// 调用Supplier函数获取值，并添加到缓存中 */
                }
            }
        }
        return value;
    }

    public V put(K key, V value) {/** 添加键值对 */
        synchronized (cache) {// 加锁，保证线程安全 */
            cache.put(key, value);// 添加键值对 */
        }/** 解锁 */
        return value;/** 返回添加的值 */
    }

    @Override
    public String toString() {
        return cache.toString();
    }/** 返回缓存对象的字符串表示 */
}
