package com.github.sadstool.redissonaspectlock.lock;

import com.github.sadstool.redissonaspectlock.attributes.LockAttributes;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(LockFactory.class);

    //private Striped<java.util.concurrent.locks.Lock> striped;

    //private final static ConcurrentHashMap<String, ReentrantReadWriteLock> locks = new ConcurrentHashMap<String, ReentrantReadWriteLock>();
    private final static ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    public LockFactory() {
        //striped = Striped.lazyWeakLock(32);
    }

    public Lock create(LockAttributes attributes) {
        String path = attributes.getPath();
        long waitTime = attributes.getWaitTime();
        long leaseTime = attributes.getLeaseTime();

        LOGGER.trace("Creating lock with name '{}', path '{}', waitTime {} and leaseTime {}", attributes.getName(),
                path, waitTime, leaseTime);
        //RLock lock = redissonClient.getLock(path);
        java.util.concurrent.locks.Lock lock = getLock(path);
        return new JavaLock(lock, waitTime, leaseTime);
    }

    public java.util.concurrent.locks.Lock getLock(String lockKey) {
        ReentrantLock lock;
        synchronized (locks) {
            lock = locks.get(lockKey);
            if (lock == null) {
                lock = new ReentrantLock(true);
                locks.put(lockKey, lock);
            }
        }
        return lock;
    }

}
