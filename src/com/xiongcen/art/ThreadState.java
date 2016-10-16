package com.xiongcen.art;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Java并发编程艺术 4.1.4线程的状态 代码清单4-3
 * 使用终端:1.jps 2.jstack 进程id 查看线程状态
 * Created by xiongcen on 16/10/15.
 */
public class ThreadState {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        new Thread(new Running(), "RunningThread").start();
        new Thread(new TimeWaiting(), "TimeWaitingThread").start();
        new Thread(new Waiting(), "WaitingThread").start();
        new Thread(new Blocked(), "BlockedThread-1").start();
        new Thread(new Blocked(), "BlockedThread-2").start();
        new Thread(new Locked(), "LockedThread-1").start();
        new Thread(new Locked(), "LockedThread-2").start();
    }

    static class Running implements Runnable {
        @Override
        public void run() {
            while (true) {
                System.out.println("Running.run");
            }
        }
    }

    // 该线程不断地进行睡眠
    static class TimeWaiting implements Runnable {
        @Override
        public void run() {
            while (true) {
                SleepUtils.second(100);
            }
        }
    }

    // 该线程在Waiting.class实例上等待
    static class Waiting implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (Waiting.class) {
                    try {
                        Waiting.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // 该线程在Block.class实例上加锁后,不会释放该锁
    static class Blocked implements Runnable {
        @Override
        public void run() {
            synchronized (Blocked.class) {
                while (true) {
                    SleepUtils.second(100);
                }
            }
        }
    }

    // 该线程在通过java.util.concurrent.locks.Lock接口加上锁后,线程状态不是进入BLOCKED状态,而是WAITING状态
    // 因为Lock接口对于阻塞的实现使用了LockSupport类中的相关方法,具体参考Java线程状态变迁图
    static class Locked implements Runnable {
        @Override
        public void run() {
            while (true) {
                lock.lock();
            }
        }
    }
}
