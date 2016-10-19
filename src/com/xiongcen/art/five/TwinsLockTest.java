package com.xiongcen.art.five;

import com.xiongcen.art.SleepUtils;

import java.util.concurrent.locks.Lock;

/**
 * Java并发编程艺术 5.2.1队列同步器的实现分析->5.自定义同步组件 代码清单5-11
 * Created by xiongcen on 16/10/20.
 */
public class TwinsLockTest {

    public void test() {
        final Lock lock = new TwinsLock();
        class Worker extends Thread {
            @Override
            public void run() {
                while (true) {
                    try {
                        lock.lock();
                        SleepUtils.second(1);
                        System.out.println(Thread.currentThread().getName());
                        SleepUtils.second(1);
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }

        // 启动10个线程
        for (int i = 0; i < 10; i++) {
            Worker w = new Worker();
            w.setDaemon(true);
            w.start();
        }

        // 每隔1秒换行
        for (int i = 0; i < 10; i++) {
            SleepUtils.second(1);
            System.out.println();;
        }
    }

    public static void main(String[] args) {
        new TwinsLockTest().test();
    }
}
