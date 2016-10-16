package com.xiongcen.art;

import java.util.concurrent.TimeUnit;

/**
 * Java并发编程艺术 4.3.5Thread.join()的使用 代码清单4-13
 * Created by xiongcen on 16/10/16.
 */
public class Join {

    public static void main(String[] args) throws InterruptedException {
        Thread previous = Thread.currentThread();
        for (int i = 0; i < 10; i++) {
            // 每个线程拥有前一个线程的引用,需要等待前一个线程终止,才能从等待中返回
            Thread thread = new Thread(new Domino(previous), String.valueOf(i));
            thread.start();
            previous = thread;
        }
        TimeUnit.SECONDS.sleep(5);
        System.out.println(Thread.currentThread().getName() + " terminate.");
    }

    static class Domino implements Runnable {
        private Thread thread;

        public Domino(Thread thread) {
            System.out.println("Domino.Domino");
            this.thread = thread;
        }

        @Override
        public void run() {
            try {
                /* while (thread.isAlive()) {
                 *  Domino.wait(0);
                 * }
                 */
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " terminate.");
        }
    }
}
/**
 * Domino.Domino
 * Domino.Domino
 * Domino.Domino
 * Domino.Domino
 * Domino.Domino
 * Domino.Domino
 * Domino.Domino
 * Domino.Domino
 * Domino.Domino
 * Domino.Domino
 * main terminate.
 * 0 terminate.
 * 1 terminate.
 * 2 terminate.
 * 3 terminate.
 * 4 terminate.
 * 5 terminate.
 * 6 terminate.
 * 7 terminate.
 * 8 terminate.
 * 9 terminate.
 */
