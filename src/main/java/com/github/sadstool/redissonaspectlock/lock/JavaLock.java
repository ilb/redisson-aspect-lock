package com.github.sadstool.redissonaspectlock.lock;


import java.util.concurrent.TimeUnit;

public class JavaLock implements Lock {

    private java.util.concurrent.locks.Lock lock;
    private long waitTime;
    private long leaseTime;

    public JavaLock(java.util.concurrent.locks.Lock lock, long waitTime, long leaseTime) {
        this.lock = lock;
        this.waitTime = waitTime;
        this.leaseTime = leaseTime;
    }

    public boolean acquire() {
        try {
            return lock.tryLock(waitTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void release() {
        lock.unlock();
    }
}
