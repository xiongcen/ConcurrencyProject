package com.xiongcen.art;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Java并发编程艺术 4.3.2等待/通知机制 代码清单4-11
 * <p/>
 * Created by xiongcen on 16/10/16.
 */
public class WaitNotify {

    static boolean flag = true;
    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread waitThread = new Thread(new Wait(), "WaitThread");
        waitThread.start();
        TimeUnit.SECONDS.sleep(1);
        Thread notifyThread = new Thread(new Notify(), "NotifyThread");
        notifyThread.start();
    }

    static class Wait implements Runnable {
        @Override
        public void run() {
            // 加锁 拥有lock的Monitor
            synchronized (lock) {
                while (flag) {
                    try {
                        System.out.println(Thread.currentThread() + " flag is true.waiting @ " +
                                new SimpleDateFormat("HH:mm:ss").format(new Date()));
                        lock.wait();
                    } catch (InterruptedException e) {

                    }
                }
                // 条件满足时,完成工作
                System.out.println(Thread.currentThread() + " flag is false.running @ " +
                        new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }
        }
    }

    static class Notify implements Runnable {
        @Override
        public void run() {
            // 加锁 拥有lock的Monitor
            synchronized (lock) {
                // 获取lock的锁,然后进行通知,通知时不会释放lock的锁,
                // 直到当前线程释放了lock后,WaitThread才能从wait方法中返回
                System.out.println(Thread.currentThread() + " hold lock.noify @ " +
                        new SimpleDateFormat("HH:mm:ss").format(new Date()));
                lock.notifyAll();
                System.out.println("release lock1");
                flag = false;
                System.out.println("release lock2");
                SleepUtils.second(5);
            }
            synchronized (lock) {
                System.out.println(Thread.currentThread() + " hold lock agin.sleep @ " +
                        new SimpleDateFormat("HH:mm:ss").format(new Date()));
                SleepUtils.second(5);
            }
        }
    }
}
/**
 * Thread[WaitThread,5,main] flag is true.waiting @ 14:55:14
 * Thread[NotifyThread,5,main] hold lock.noify @ 14:55:15
 * release lock1
 * release lock2
 * Thread[WaitThread,5,main] flag is false.running @ 14:55:20
 * Thread[NotifyThread,5,main] hold lock agin.sleep @ 14:55:20
 *
 * 说明:
 * 1.倒数第一行和倒数第二行存在互换的可能性
 * 2.release lock12处都没有让WaitThread调用到条件满足状态,
 * 因为notify()或notifyAll()方法调用后,等待线程依旧不会从wait()返回,
 * 需要调用notify()或notifyAll()的线程释放锁之后,等待线程才有机会从wait()返回.
 */
