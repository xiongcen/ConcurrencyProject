package com.xiongcen.art.eight;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Java并发编程艺术 8.1等待多线程完成的CountDownLatch 代码清单8-2
 * Created by xiongcen on 16/10/31.
 */
public class CountDownLatchTest {

    // 构造函数接受一个int类型的参数作为计数器,如果想等待N个完成,这里就传入N;
    // 由于countDown()方法可以用在任何地方,所以这里说的N个点,可以是N个线程,也可以是1个线程里的N个步骤;
    // 用在多个线程时,只需要把这个CountDownLatch的引用传递到线程里即可;
    // 计数器必须大于等于0,只是等于0时,计数器就是0,调用await()方法时不会阻塞当前线程.
    static CountDownLatch c = new CountDownLatch(2);

    public static void main(String[] args) throws InterruptedException {
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
                System.out.println("1");
                // 调用countDown()方法N就会减1
                c.countDown();
                System.out.println("2");
                c.countDown();
            }
        }).start();
        // CountDownLatch的await()方法会阻塞当前线程,直到N变成0;
        c.await();
        // 也可以让其等待一段时间,调用CountDownLatch的await(long timeout, TimeUnit unit)方法.
//        c.await(5, TimeUnit.SECONDS);
        System.out.println("3");
    }
}
