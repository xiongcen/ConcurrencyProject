package com.xiongcen.art.eight;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Java并发编程艺术 8.3控制并发线程数的Semaphore 代码清单8-7
 * Semaphore(信号量)是用来控制同时访问特定资源的线程数量,它通过协调各个线程,以保证合理的使用公共资源.
 * 可以把Semaphore比作是控制流量的红绿灯:
 * 比如xx马路要限制流量,只允许同时有100辆车在这条路上行驶,其他的都必须在路口等待,所以前100辆车会看到绿灯,可以开进这条马路,后面的车会看到红灯,不能驶入xx马路.
 * 但是如果前100辆中有5辆已经离开了xx马路,那么后面就允许有5辆车驶入马路.
 * 这个例子里说的车就是线程,驶入马路就表示线程在执行,离开马路就表示线程执行完成,看见红灯就表示线程被阻塞,不能执行.
 * <p/>
 * 应用场景:
 * 加入有一个需求,要读取几万个文件的数据,因为都是IO密集型任务,我们可以启动几十个线程并发地读取,但是如果读到内存后,还需要存储到数据库中,而数据库的连接数只能是10个.
 * 这时我们必须控制只有10个线程同时获取数据库连接保存数据,否则会报错无法获取数据库连接.
 * 这个时候可以用Semaphore来做流程控制.
 * Created by xiongcen on 16/11/1.
 */
public class SemaphoreTest {

    private static final int THREAD_COUNT = 30;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);

    private static Semaphore s = new Semaphore(10);

    public static void main(String[] args) {
        for (int i = 0; i < THREAD_COUNT; i++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 获得许可证
                        s.acquire();
                        System.out.println("[" + System.currentTimeMillis() + "] save data");
                        // 归还许可证
                        s.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        threadPool.shutdown();
    }
}
