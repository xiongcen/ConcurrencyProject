package com.xiongcen.art.eight;

/**
 * Java并发编程艺术 8.1等待多线程完成的CountDownLatch 代码清单8-1
 * join用于让当前执行线程等待调用join()方的parser1,parser2线程执行结束.
 * 其原理是不停检查调用join()方的parser1,parser2线程是否存活,如果存活则让当前线程永远等待.
 * 直到调用join()方的parser1,parser2线程中止后,线程的this.notifyAll()方法会被调用,
 * 调用notifyAll()方法是在JVM里实现的.所以在JDK中看不到.
 * Created by xiongcen on 16/10/31.
 */
public class JoinCountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        final Object lock = new Object();
        Thread parser1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    try {
                        lock.wait(1000 * 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("parser1 finish");
            }
        });

        Thread parser2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("parser2 finish");
            }
        });

        parser1.start();
        parser2.start();
        parser1.join();
        parser2.join();
        System.out.println("all parser finish");
    }
}
