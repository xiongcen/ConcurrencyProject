package com.xiongcen.interview;

/**
 * 得到输出
 * Created by xiongcen on 16/10/25.
 */
public class SynchronizedTest {

    public static void main(String[] args) {
        RW rw = new RW();
        Thread t1 = new Thread(rw, "t1");
        Thread t2 = new Thread(rw, "t2");
        t1.start();
        t2.start();
    }

    static class RW implements Runnable {
        @Override
        public void run() {
            synchronized (this) {
                for (int i = 0; i < 3; i++) {
                    System.out.println(Thread.currentThread().getName() + ": " + i);
                }
            }
        }
    }
}/**
 * t1: 0
 * t1: 1
 * t1: 2
 * t2: 0
 * t2: 1
 * t2: 2
 */
