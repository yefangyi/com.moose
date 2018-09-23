package com.moose.common.support.cache;

import org.springframework.util.Assert;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class DefaultCacheBuilder<K, V> implements InitBuildable<DefaultCache, K, V>  {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int DEFAULT_INIT_CAPACITY = 256;
    private static final int DEFAULT_CONCURRENT_LEVEL = 16;
    private int initialCapacity = DEFAULT_INIT_CAPACITY;
    private int concurrencyLevel = DEFAULT_CONCURRENT_LEVEL;
    private long expireAfterWrite;
    private ExecutorService executorService = Executors.newFixedThreadPool(CPU_COUNT);

    public DefaultCacheBuilder<K, V> initialCapacity(int initialCapacity) {
        Assert.state(initialCapacity > 0, "initialCapacity");

        this.initialCapacity = initialCapacity;
        return this;
    }

    public DefaultCacheBuilder<K, V> concurrencyLevel(int concurrencyLevel) {
        Assert.state(concurrencyLevel > 0, "concurrencyLevel");

        this.concurrencyLevel = concurrencyLevel;
        return this;
    }

    public DefaultCacheBuilder<K, V> expireAfterWrite(int time, TimeUnit unit) {
        Assert.state(time > 0, "time");
        Assert.notNull(unit, "unit");

        this.expireAfterWrite = unit.toNanos(time);
        return this;
    }

    public DefaultCacheBuilder<K, V> executorService(ExecutorService executorService) {
        Assert.notNull(executorService, "executorService");

        this.executorService = executorService;
        return this;
    }

    public DefaultCache<K, V> build() {
        return build(null);
    }

    public <K1 extends K, V1 extends V> DefaultCache<K1, V1> build(Function<? super K1, V1> loader) {
        return new DefaultCache<K1, V1>(this, loader);
    }

    public int getInitialCapacity() {
        return initialCapacity;
    }

    public void setInitialCapacity(int initialCapacity) {
        this.initialCapacity = initialCapacity;
    }

    public long getExpireAfterWrite() {
        return expireAfterWrite;
    }

    public void setExpireAfterWrite(long expireAfterWrite) {
        this.expireAfterWrite = expireAfterWrite;
    }

    public int getConcurrencyLevel() {
        return concurrencyLevel;
    }

    public void setConcurrencyLevel(int concurrencyLevel) {
        this.concurrencyLevel = concurrencyLevel;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}
