package com.xiongcen.art.eight;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Java并发编程艺术 8.4线程间交换数据的Exchanger 代码清单8-8
 * Exchanger(交换者)是一个用于线程间协作的工具类.Exchanger用于进行线程间的数据交换.
 * 它提供一个同步点,在这个同步点,两个线程可以交换彼此的数据.
 * 这两个线程通过exchange()方法交换数据,如果第一个线程先执行exchange()方法,它会一直等待第二个线程也执行exchange()方法,
 * 当两个线程都到达同步点,这两个线程就可以交换数据,将本线程生产出来的数据传递给对方.
 * <p/>
 * 应用场景:
 * 1.Exchanger可以用于遗传算法:遗传算法里需要选出两个人作为交配对象,这时候会交换两人的数据,并使用交叉规则得出2个交配结果;
 * 2.Exchanger可以用于校对工作:如果需要将银行流水通过人工的方式录入成电子银行流水,为了避免错误,采用AB岗两人进行录入,录入到Excel后,系统需要加载这两个Excel,
 * 并对两个Excel的数据进行校对,看是否录入一致.
 * <p/>
 * 注意:如果两个线程有一个没有执行exchange()方法,则会一直等待.如果担心有特殊情况发生,避免一直等待,可以使用exchange(V x, long timeout, TimeUnit unit)设置最大等待时长.
 *
 * Created by xiongcen on 16/11/1.
 */
public class ExchangerTest {

    private static final Exchanger<String> exgr = new Exchanger<>();

    static ExecutorService threadPool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String A = "银行流水A";
                    String B = exgr.exchange(A);
                    System.out.println(B);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String B = "银行流水B";
                    String A = exgr.exchange(B);
                    System.out.println("A数据和B数据是否一致:" + A.equals(B) + ",A录入的是:" + A + ",B录入的是:" + B);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        threadPool.shutdown();
    }

}
