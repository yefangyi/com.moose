package com.moose.common.support.cache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

public class DefaultCache<K, V> implements Cache<K, V> {

    private static final int MAX_SIZE = 1 << 30; //确保数组长度在int范围内
    private static final int MAX_NODE_SIZE = 1 << 10; //确保并发等级

    private int initialCapacity;//初始缓存大小
    private int concurrencyLevel;//并发等级
    private long expireAfterWriteNanos; //写入之后缓存过期时间
    private Function<? super K, V> initFunction; //初始化函数
    private Node<K, V>[] data;
    private ExecutorService executorService = null;

    public DefaultCache(DefaultCacheBuilder<? super K, ? super V> builder, Function<? super K, V> initFunction) {
        this.initialCapacity = Math.min(builder.getInitialCapacity(), MAX_SIZE);
        this.concurrencyLevel = Math.min(builder.getConcurrencyLevel(), MAX_NODE_SIZE);
        this.expireAfterWriteNanos = builder.getExpireAfterWrite();
        this.initFunction = initFunction;
        this.executorService = builder.getExecutorService();
        this.data = initNodes();
    }

    @Override
    public void put(K k, V v) {
        nodeIndexFor(k).put(k, v);
    }

    @Override
    public V get(K k) {
        try {
            return nodeIndexFor(k).get(k, initFunction);
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    private Node<K, V> nodeIndexFor(K key) {
        return data[key.hashCode() % concurrencyLevel];
    }

    public boolean isExpire(long now, long time) {
        return expireAfterWriteNanos > 0 && (now - time >= expireAfterWriteNanos);
    }

    @SuppressWarnings("unchecked")
    private Node<K, V>[] initNodes() {
        Node<K, V>[] nodes = new Node[concurrencyLevel];

        int nodeSize = initialCapacity / concurrencyLevel;
        nodeSize = nodeSize * concurrencyLevel < initialCapacity ? nodeSize + 1 : nodeSize;
        for(int i = 0; i < concurrencyLevel; i++) {
            nodes[i] = new Node<>(nodeSize, this);
        }
        return nodes;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}