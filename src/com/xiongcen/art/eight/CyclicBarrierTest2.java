package com.xiongcen.art.eight;

import java.util.concurrent.CyclicBarrier;

/**
 * Java并发编程艺术 8.2同步屏障CyclicBarrier 代码清单8-4
 * Created by xiongcen on 16/10/31.
 */
public class CyclicBarrierTest2 {

    // 在线程到达屏障时,优先执行A
    static CyclicBarrier c = new CyclicBarrier(2, new A());

    static class A implements Runnable {
        @Override
        public void run() {
            System.out.println(3);
        }
    }

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    c.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(1);
            }
        }).start();
        try {
            c.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(2);
    }
}/**
 * 本人觉得输出
 * 3
 * 1
 * 2
 * 和
 * 3
 * 2
 * 1
 * 都有可能
 * 但书上认为因为设置拦截线程的数量是2,所以必须等代码中的第一个线程和线程A都执行完之后,才会继续执行主线程,然后输出2.
 * 结果为
 * 3
 * 1
 * 2
 */
