package com.xiongcen;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CircularSet持有所生产的所有序列数;
 * SerialChecker确保序列数唯一;
 *
 * 通过创建多个任务竞争序列数,最终发现这些任务最终会得到重复的序列数,如果运行的时间足够长的话.
 * 所以应该在SerialNumberGenerator的nextSerialNumber()方法加同步关键字.
 *
 * 原因:
 * 不加synchronized关键字,可能在执行nextSerialNumber()方法中的++操作之前被中断,
 * 导致出现重复序列数.
 *
 * Created by xiongcen on 16/9/1.
 */

class CircularSet {
    private int[] array;
    private int len;
    private int index = 0;
    public CircularSet(int size) {
        array = new int[size];
        len = size;
        // Initialize to a value not produced
        // by the SerialNumberGenerator:
        for(int i = 0; i < size; i++)
            array[i] = -1;
    }
    public synchronized void add(int i) {
        array[index] = i;
        // Wrap index and write over old elements:
        index = ++index % len;
    }
    public synchronized boolean contains(int val) {
        for(int i = 0; i < len; i++)
            if(array[i] == val) return true;
        return false;
    }
}

public class SerialNumberChecker {
    private static final int SIZE = 10;
    private static CircularSet serials =
            new CircularSet(1000);
    private static ExecutorService exec =
            Executors.newCachedThreadPool();

    static class SerialChecker implements Runnable {
        public void run() {
            while(true) {
                int serial =
                        SerialNumberGenerator.nextSerialNumber();
                if(serials.contains(serial)) {
                    System.out.println("Duplicate: " + serial);
                    System.exit(0);
                }
                serials.add(serial);
            }
        }
    }
    public static void main(String[] args) throws Exception {
        for(int i = 0; i < SIZE; i++)
            exec.execute(new SerialChecker());
        // Stop after n seconds if there's an argument:
        if(args.length > 0) {
            TimeUnit.SECONDS.sleep(new Integer(args[0]));
            System.out.println("No duplicates detected");
            System.exit(0);
        }
    }
}/* Output: (Sample)
Duplicate: 21841
*///:~
