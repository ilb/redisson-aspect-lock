package com.github.sadstool.redissonaspectlock.lock;

import com.github.sadstool.redissonaspectlock.attributes.LockAttributes;
import com.google.common.util.concurrent.Striped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(LockFactory.class);
    
    private Striped<java.util.concurrent.locks.Lock> striped; 


    public LockFactory() {
        striped = Striped.lazyWeakLock(32); //FIXME
    }

    public Lock create(LockAttributes attributes) {
        String path = attributes.getPath();
        long waitTime = attributes.getWaitTime();
        long leaseTime = attributes.getLeaseTime();

        LOGGER.trace("Creating lock with name '{}', path '{}', waitTime {} and leaseTime {}", attributes.getName(),
                path, waitTime, leaseTime);
        //RLock lock = redissonClient.getLock(path);
        java.util.concurrent.locks.Lock lock=striped.get(path);
        return new JavaLock(lock, waitTime, leaseTime);
    }
}
