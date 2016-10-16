package com.xiongcen.art;

import java.util.concurrent.TimeUnit;

/**
 * Java并发编程艺术 4.3.6ThreadLocal的使用 代码清单4-15
 * 该类可复用在方法调用耗时统计的功能上
 * Created by xiongcen on 16/10/16.
 */
public class Profiler {

    // 第一次get()方法调用时会进行初始化(如果set方法没有调用),每个线程会调用一次
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new
            ThreadLocal<Long>() {
                @Override
                protected Long initialValue() {
                    System.out.println("Profiler.initialValue");
                    return System.currentTimeMillis();
                }
            };

    public static final void begin() {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    public static final long end() {
        return System.currentTimeMillis() - TIME_THREADLOCAL.get();
    }

    public static void main(String[] args) throws InterruptedException {
        Profiler.begin();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Cost: " + Profiler.end() + " mills");
    }
}
