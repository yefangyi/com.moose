package com.moose.common.support.cache;

import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.concurrent.ExecutionException;

public class ValueWrapper<V> {

    private V value;
    private volatile long createTime;
    private volatile V beforeValue;
    private volatile boolean isLoading;
    //在加载数据时，提前暴露futere，在数据加载完成时，手动设置值，唤醒其它等待的线程
    private SettableListenableFuture<V> future = new SettableListenableFuture<>();

    public ValueWrapper() {
    }

    public ValueWrapper(V value, long createTime) {
        this.value = value;
        this.createTime = createTime;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return value.equals(obj);
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public void setNewValue(V value, long createTime) {
        this.beforeValue = this.value;
        this.value = value;
        this.isLoading = false;
        this.createTime = createTime;
    }

    public V waitForFuture() throws ExecutionException {
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

    public V getBeforeValue() {
        return beforeValue;
    }

    public void setBeforeValue(V beforeValue) {
        this.beforeValue = beforeValue;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void setFutureValue(V value, long createTime ) {
        this.future.set(value);
        this.value = value;
        this.createTime = createTime;
        this.isLoading = false;
    }
}
