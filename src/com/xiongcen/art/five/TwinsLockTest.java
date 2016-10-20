package com.xiongcen.art.five;

import com.xiongcen.art.SleepUtils;

import java.util.concurrent.locks.Lock;

/**
 * Java并发编程艺术 5.2.1队列同步器的实现分析->5.自定义同步组件 代码清单5-11
 *
 * 真正输出的结果只有线程0和线程1在运行.
 * Created by xiongcen on 16/10/20.
 */
public class TwinsLockTest {

    public void test() {
        final Lock lock = new TwinsLock();
        class Worker extends Thread {
            int index;

            Worker(int index) {
                this.index = index;
            }

            @Override
            public void run() {
                while (true) {
                    try {
                        lock.lock();
                        SleepUtils.second(1);
                        System.out.println(Thread.currentThread().getName() + " index:" + index);
                        SleepUtils.second(1);
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }

        // 启动10个线程
        for (int i = 0; i < 10; i++) {
            Worker w = new Worker(i);
            // 设置daemon线程只是为了可以main()方法自动停止
            w.setDaemon(true);
            w.start();
        }

        // 每隔1秒换行
        for (int i = 0; i < 10; i++) {
            SleepUtils.second(1);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        new TwinsLockTest().test();
    }
}
