package com.xiongcen.art.five;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Java并发编程艺术 5.2.1队列同步器的实现分析->5.自定义同步组件:
 * 在同一时刻只允许至多两个线程同时访问,超过两个线程的访问将被阻塞.
 * 代码清单5-10
 * Created by xiongcen on 16/10/19.
 */
public class TwinsLock implements Lock {
    private final Sync sync = new Sync(2);

    public static final class Sync extends AbstractQueuedSynchronizer {
        Sync(int count) {
            if (count <= 0) {
                throw new IllegalArgumentException("count must be large than zero.");
            }
            setState(count);
        }

        @Override
        protected int tryAcquireShared(int readCount) {
            for (; ; ) {
                int current = getState();
                int newCount = current - readCount;
                if (newCount < 0 || compareAndSetState(current, newCount)) {
                    return newCount;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int readCount) {
            for (; ; ) {
                int current = getState();
                int newCount = current + readCount;
                if (compareAndSetState(current, newCount)) {
                    return true;
                }
            }
        }
    }

    @Override
    public void lock() {
        sync.acquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        sync.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
