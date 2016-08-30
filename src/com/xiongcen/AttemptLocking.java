package com.xiongcen;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock允许你尝试着获取但最终未获取锁,这样如果其他人已经获取了这个锁,
 * 那你就可以决定离开去执行其他一些事情,而不是等待直至这个锁被释放,就像在untimed()方法中所看到的.
 *
 * Created by xiongcen on 16/8/27.
 */
public class AttemptLocking {
    private ReentrantLock lock = new ReentrantLock();

    public void untimed() {
        boolean captured = lock.tryLock();
        try {
            System.out.println("tryLock(): " + captured);
        } finally {
            if (captured)
                lock.unlock();
        }
    }

    public void timed() {
        boolean captured = false;
        try {
            captured = lock.tryLock(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            System.out.println("tryLock(2, TimeUnit.SECONDS): " +
                    captured);
        } finally {
            if (captured)
                lock.unlock();
        }
    }

    public static void main(String[] args) {
        final AttemptLocking al = new AttemptLocking();
        al.untimed(); // True -- lock is available
        al.timed();   // True -- lock is available
        // Now create a separate task to grab the lock:
        new Thread() {
            {
                setDaemon(true);
            }

            public void run() {
                al.lock.lock();
                System.out.println("acquired");
            }
        }.start();
        // Give the 2nd task a chance
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        al.untimed(); // False -- lock grabbed by task
        al.timed();   // False -- lock grabbed by task
    }
}/* Output:
tryLock(): true
tryLock(2, TimeUnit.SECONDS): true
acquired
tryLock(): false
tryLock(2, TimeUnit.SECONDS): false
*///:~
