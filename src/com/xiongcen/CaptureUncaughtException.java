package com.xiongcen;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 输出结果与21.2.14有出入.
 * HandlerThreadFactory的newThread方法会别调用两次.或许是JDK对ThreadFactory机制的改变.
 * Created by xiongcen on 16/8/26.
 */
class ExceptionThread2 implements Runnable {
    public void run() {
        Thread t = Thread.currentThread();
        System.out.println("run() by " + t);
        System.out.println(
                "eh = " + t.getUncaughtExceptionHandler());
        throw new RuntimeException();
    }
}

class MyUncaughtExceptionHandler implements
        Thread.UncaughtExceptionHandler {
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("caught " + e);
    }
}

class HandlerThreadFactory implements ThreadFactory {
    public Thread newThread(Runnable r) {
        System.out.println(this + " creating new Thread");
        Thread t = new Thread(r);
        System.out.println("created " + t);
        t.setUncaughtExceptionHandler(
                new MyUncaughtExceptionHandler());
        System.out.println(
                "eh = " + t.getUncaughtExceptionHandler());
        return t;
    }
}

public class CaptureUncaughtException {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool(
                new HandlerThreadFactory());
        exec.execute(new ExceptionThread2());
    }
}/* Output: (90% match)
com.xiongcen.HandlerThreadFactory@65be7af creating new Thread
created Thread[Thread-0,5,main]
eh = com.xiongcen.MyUncaughtExceptionHandler@1e235551
run() by Thread[Thread-0,5,main]
eh = com.xiongcen.MyUncaughtExceptionHandler@1e235551
com.xiongcen.HandlerThreadFactory@65be7af creating new Thread
created Thread[Thread-1,5,main]
eh = com.xiongcen.MyUncaughtExceptionHandler@72f60f20
caught java.lang.RuntimeException
*///:~
