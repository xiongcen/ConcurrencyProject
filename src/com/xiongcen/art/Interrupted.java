package com.xiongcen.art;

/**
 * Java并发编程艺术 4.2.3理解中断 代码清单4-7
 *
 * interrupt()方法并不会中断线程,它只是一个标志位
 * 在某些方法(如休眠,具体参见interrupt()方法方法注释,
 * !!注意:进行不能中断的IO操作而阻塞和要获得对象的锁调用对象的synchronized方法而阻塞时不会抛出InterruptedException!!)
 * 抛出InterruptedException之前,JVM会将该线程的中断标志移除,
 * 然后再抛出InterruptedException,所以isInterrupted()方法返回false
 * Created by xiongcen on 16/10/15.
 */
public class Interrupted {
    public static void main(String[] args) {
        // sleepThread不停的尝试睡眠
        Thread sleepThread = new Thread(new SleepRunner(), "SleepThread");
        sleepThread.setDaemon(true);
        // busyThread不停地运行
        Thread busyThread = new Thread(new BusyRunner(), "BusyThread");
        busyThread.setDaemon(true);
        // blockedThread1不停地运行
        Thread blockedThread1 = new Thread(new BlockedRunner(), "BlockedThread-1");
        blockedThread1.setDaemon(true);
        // blockedThread2会因为锁被占用而阻塞
        Thread blockedThread2 = new Thread(new BlockedRunner(), "BlockedThread-2");
        blockedThread2.setDaemon(true);

        sleepThread.start();
        busyThread.start();
        blockedThread1.start();
        blockedThread2.start();

        // 休眠5s 让sleepThread和busyThread充分运行
        SleepUtils.second(5);

//        sleepThread.interrupt();
        busyThread.interrupt();
        blockedThread1.interrupt();
        blockedThread2.interrupt();

        System.out.println("SleepThread interrupted is " + sleepThread.isInterrupted());
        System.out.println("BusyThread interrupted is " + busyThread.isInterrupted());
        System.out.println("BlockedThread-1 interrupted is " + blockedThread1.isInterrupted());
        System.out.println("BlockedThread-2 interrupted is " + blockedThread2.isInterrupted());

        // 防止sleepThread和busyThread立刻退出
        SleepUtils.second(2);
    }

    static class SleepRunner implements Runnable {
        @Override
        public void run() {
            while (true) {
                SleepUtils.second(10);
            }
        }
    }

    static class BusyRunner implements Runnable {
        @Override
        public void run() {
            while (true) {
            }
        }
    }

    static class BlockedRunner implements Runnable {
        @Override
        public void run() {
            synchronized (BlockedRunner.class) {
                while (true) {
                }
            }
        }
    }
}
