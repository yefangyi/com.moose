package com.moose.common.support.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class Node<K, V> {

   private static Logger logger = LoggerFactory.getLogger(Node.class);
   private static final int DEFAULT_SIZE = 16;
   private ReentrantLock lock;//锁
   private Map<K, ValueWrapper<V>> data;
   private volatile int count = 0;//所有的数量
   private DefaultCache<K, V> cache;

   public Node(DefaultCache<K, V> cache) {
        this(DEFAULT_SIZE, cache);
   }

   public Node(int size, DefaultCache<K, V> cache) {
        this.cache = cache;
        this.lock = new ReentrantLock();
        this.data = new HashMap<>(size);
   }

   public void put(K k, V v) {
        lock.lock();
        try {
            if(logger.isDebugEnabled()) {
                logger.debug(String.format("%s lock %s, time: %s", Thread.currentThread().getName(), k, now()));
            }
            ValueWrapper<V> value = data.get(k);
            int newCount = value == null ? this.count : this.count + 1;
            if(value != null ) {
                value.setNewValue(v, now());
            } else {
                data.put(k, new ValueWrapper<>(v, now()));
            }
            this.count = newCount;
        } finally {
            lock.unlock();
        }
   }

    public V get(K k, Function<? super K, V> apply) throws ExecutionException {
        ValueWrapper<V> value = null;
        boolean isExpired = false;
        if(count != 0 && (value = data.get(k)) != null) {
            isExpired = cache.isExpire(now(), value.getCreateTime());
            if(!isExpired) { //未过期直接返回
                return value.getValue();
            }
            if(value.isLoading()) {
                return value.waitForFuture();
            }
        }
        return getFromLockOrLoad(k, apply);
    }

    public V getFromLockOrLoad(K k, Function<? super K, V> apply) throws ExecutionException {
        boolean isCreate = true;
        ValueWrapper<V> value = null;
        lock.lock();
        try {
            if(logger.isDebugEnabled()) {
                logger.debug(String.format("%s lock %s, time: %s", Thread.currentThread().getName(), k, now()));
            }
            value = data.get(k);
            if(value != null) {
                if(!cache.isExpire(now(), value.getCreateTime())) {//值未过期
                    return value.getValue();
                } else if(value.isLoading()) { //正在加载中，不更新，等待结果
                    isCreate = false;
                } else if(apply == null) {//无法加载，删除过期
                    int newCount = this.count - 1;
                    data.remove(k);
                    this.count = newCount;
                    return null;
                }
            }
            if(isCreate) {
                if(value == null) {//新增节点
                    int newCount = this.count + 1;
                    value = new ValueWrapper<>();
                    value.setLoading(true);
                    data.put(k, value);
                    this.count = newCount;
                } else {//加载
                    value.setLoading(true);
                }
            }
        } finally {
            if(logger.isDebugEnabled()) {
                logger.debug(String.format("%s unlock %s, time: %s", Thread.currentThread().getName(), k, now()));
            }
            lock.unlock();
        }

        if(isCreate) {//线程池
            V newValue = asycLoad(k, apply);
            value.setFutureValue(newValue, now());
            return newValue;
        } else {
            return value.waitForFuture();
        }
    }

    private V asycLoad(K k, Function<? super K, V> apply) throws ExecutionException {
        Future<V> future = cache.getExecutorService().submit(() -> {
            if(logger.isDebugEnabled()) {
                logger.debug(String.format("%s load %s, time: %s", Thread.currentThread().getName(), k, now()));
            }
            return apply.apply(k);
        });
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    return future.get();
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private long now() {
        return System.nanoTime();
    }
}
