package com.xiongcen;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 即使每次调度都有休眠时间,也不能完全保证是顺序执行
 * Created by xiongcen on 16/8/23.
 */
public class SleepingTask extends LiftOff {
    public void run() {
        try {
            while(countDown-- > 0) {
                System.out.print(status());
                // Old-style:
                // Thread.sleep(100);
                // Java SE5/6-style:
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch(InterruptedException e) {
            System.err.println("Interrupted");
        }
    }
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0; i < 5; i++)
            exec.execute(new SleepingTask());
        exec.shutdown();
    }
}/* Output:
#0(9), #4(9), #3(9), #2(9), #1(9), #4(8), #1(8), #0(8), #3(8), #2(8),
#4(7), #3(7), #1(7), #0(7), #2(7), #4(6), #1(6), #0(6), #2(6), #3(6),
#0(5), #3(5), #1(5), #2(5), #4(5), #0(4), #2(4), #4(4), #3(4), #1(4),
#4(3), #0(3), #1(3), #2(3), #3(3), #4(2), #0(2), #3(2), #1(2), #2(2),
#4(1), #1(1), #2(1), #0(1), #3(1), #4(Liftoff!), #2(Liftoff!), #0(Liftoff!), #1(Liftoff!), #3(Liftoff!),
*///:~
