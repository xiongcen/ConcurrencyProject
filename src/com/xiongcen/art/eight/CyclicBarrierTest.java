package com.xiongcen.art.eight;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Java并发编程艺术 8.2同步屏障CyclicBarrier 代码清单8-3
 * CyclicBarrier的字面意思是可循环使用(Cyclic)的屏障(Barrier).它要做的事情是,
 * 让一组线程到达一个屏障(也可以叫同步点)时被阻塞,直到最后一个线程到达屏障时,屏障才会开门,
 * 所有被屏障拦截的线程才会继续运行.
 * Created by xiongcen on 16/10/31.
 */
public class CyclicBarrierTest {

    // 参数表示屏障拦截的线程数量
    static CyclicBarrier c = new CyclicBarrier(2);

    public static void main(String[] args) {
        final Object lock = new Object();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    try {
                        lock.wait(1000 * 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    // 每个线程调用await方法告诉CyclicBarrier我已经到达了屏障,然后当前线程被阻塞
                    c.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(1);
            }
        }).start();

        try {
            c.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(2);
    }
}
/**
 * 主线程子线程的调度由CPU决定,两个线程都有可能先执行,所以会产生两种输出.
 * <p/>
 * <p/>
 * 如果把new CyclicBarrier(2)修改成new CyclicBarrier(3),则主线程和子线程会永远等待,
 * 因为没有第三个线程执行await()方法,即没有第三个线程到达屏障,所以之前到达屏障的线程都不会继续执行.
 */
