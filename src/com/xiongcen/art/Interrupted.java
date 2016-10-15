package com.xiongcen.art;

/**
 * Java并发编程艺术 4.2.3理解中断 代码清单4-7
 *
 * interrupt()方法并不会中断线程,它只是一个标志位
 * 在某些方法抛出InterruptedException之前,JVM会将该线程的中断标志移除,
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

        sleepThread.start();
        busyThread.start();

        // 休眠5s 让sleepThread和busyThread充分运行
        SleepUtils.second(5);

        sleepThread.interrupt();
        busyThread.interrupt();

        System.out.println("SleepThread interrupted is " + sleepThread.isInterrupted());
        System.out.println("BusyThread interrupted is " + busyThread.isInterrupted());

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
                System.out.println("BusyRunner.run");
            }
        }
    }
}
