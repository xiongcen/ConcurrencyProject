package com.xiongcen.art.eight;

import java.util.concurrent.CyclicBarrier;

/**
 * Java并发编程艺术 8.2.3CyclicBarrier和CountDownLatch的区别 代码清单8-6
 * CountDownLatch计数器只能使用一次,而CyclicBarrier的计数器可以使用reset()方法重置.所以CyclicBarrier可以处理更复杂的业务场景.
 * CyclicBarrier.getNumberWaiting():获得CyclicBarrier阻塞的线程数量.
 * CyclicBarrier.isBroken():了解阻塞的线程是否被中断.
 * Created by xiongcen on 16/11/1.
 */
public class CyclicBarrierTest3 {

    static CyclicBarrier c = new CyclicBarrier(2);

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    c.await();
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.interrupt();
        try {
            c.await();
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println(c.isBroken());
        }
    }
}
