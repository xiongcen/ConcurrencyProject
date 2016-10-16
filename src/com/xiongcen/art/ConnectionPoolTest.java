package com.xiongcen.art;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Java并发编程艺术 4.4.2一个简单的数据库连接池实例 代码清单4-18
 * 设定场景是10个线程同时开始运行获取连接池(10个连接)中的连接,
 * 通过调节线程数量来观测未获取到连接的情况.
 *
 * 结论:随着客户端线程的逐步增加,客户端出现超市无法获取连接的比率不断升高.
 * 虽然客户端线程在这种超时获取的模式下会出现连接无法获取的情况,
 * 但是它能够保障客户端线程不会一直挂在连接获取的操作上,而是"按时"返回,
 * 并告知客户端连接获取出现问题,是系统的一种自我保护机制.
 * Created by xiongcen on 16/10/16.
 */
public class ConnectionPoolTest {

    static ConnectionPool pool = new ConnectionPool(10);

    //保证所有ConnectionRunner能够同时开始
    static CountDownLatch start = new CountDownLatch(1);

    //main线程将会等待所有ConnectionRunner结束后才能继续执行
    static CountDownLatch end;

    public static void main(String[] args) throws InterruptedException {
        // 线程数量,可以修改线程数量进行观察
        int threadCount = 30;
        end = new CountDownLatch(threadCount);
        int count = 20;
        AtomicInteger got = new AtomicInteger();
        AtomicInteger notGot = new AtomicInteger();
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new ConnectionRunner(count, got, notGot), "ConnectionRunnerThread");
            thread.start();
        }
        start.countDown();
        end.await();
        System.out.println("total invoke: " + (threadCount * count));
        System.out.println("got connection: " + got);
        System.out.println("not got connection: " + notGot);
    }

    static class ConnectionRunner implements Runnable {
        int count;
        AtomicInteger got;
        AtomicInteger notGot;

        public ConnectionRunner(int count, AtomicInteger got, AtomicInteger notGot) {
            this.count = count;
            this.got = got;
            this.notGot = notGot;
        }

        @Override
        public void run() {
            try {
                start.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (count > 0) {
                try {
                    //从线程池中获取连接,如果100ms内无法获取到,将会返回null
                    //分别统计连接获取的数量got和未获取到的数量notGot
                    Connection connection = pool.fetchConnection(100);
                    if (connection != null) {
                        try {
                            connection.createStatement();
                            connection.commit();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            pool.releaseConnection(connection);
                            got.incrementAndGet();
                        }
                    } else {
                        notGot.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    count--;
                }
            }
            end.countDown();
        }
    }
}
