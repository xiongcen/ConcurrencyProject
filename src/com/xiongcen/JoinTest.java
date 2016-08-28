package com.xiongcen;

/**
 * { @link http://blog.csdn.net/axman/article/details/3986918 }
 * 跟进b.join()方法里,发现最终调用的是wait()方法,wait()是Object的方法.
 * while (isAlive()) {
 *  wait(0);
 * }
 * isAlive()指线程b,因为是调用线程b的join()方法.从b线程看就是this.isAlive(),从a线程看就是b.isAlive()
 * wait(0)方法是指当前线程即线程a的等待,等待什么?当然是等待被唤醒.这里的等待条件:
 * 当线程b is Not Alive时，一定会唤醒调用join()方法所在线程即线程a.
 *
 * 线程退出时会调用本地方法,除了做收尾工作,还会唤醒在这个线程对象上wait的所有线程(例子里就是:在线程对象b上wait的线程a).
 *
 * wait方法除了wait到期被线程调度唤醒外,始终等待wait的反条件.
 * 同时,也把唤醒责任交给了改变那个条件的线程,所以如果要看一个wait钟的线程被谁唤醒,就要看谁在控制这个条件,如果控制这个条件的线程始终没有唤醒调用wait的线程,
 * 那么设计就是一个致命错误!!!
 *
 * 真正唤醒join/wait中的线程的责任者是能够使等待条件不成立的线程。而wait中的线程真正等待的是反wait条件。
 *
 * Created by xiongcen on 16/8/25.
 */
public class JoinTest {
    public static void main(String[] args) throws Exception {
        final Thread b = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("b start");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("b end");
            }
        });
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("a start");
                try {
                    b.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("a end");
            }
        });
        a.start();
        b.start();
    }
}/* Output:
a start
b start
b end
a end
*///:~
