package com.xiongcen;

/**
 *
 * 所有对象都自动含有单一的锁(也称为监视器).当在对象上调用其任意synchronized方法的时候,此对象都被加锁,
 * 这时该对象上的其他synchronized方法只有等到前一个方法调用完毕并释放了锁后才能被调用.
 * 所以,
 * 对于某个特定对象来说,其所有synchronized方法共享同一个锁,这可以被用来防止多个任务同时访问被编码为对象内存.
 * 注意:
 * 在使用并发时,将域设置为private非常重要,否则synchronized关键字就不能防止其他任务直接访问域,这样会产生冲突.
 *
 * 一个任务可以多次获得对象的锁.比如--->如果一个方法在同一个对象上调用了第二个方法,后者又调用了同一对象上的另一个方法,就会发生这种情况.
 * JVM负责跟踪对象被加锁的次数.只有首先获得了锁的任务才允许继续获取多个锁.
 *
 * 针对每个类,也有一个锁(作为类的Class对象的一部分);所以synchronized static方法可以在类的范围内防止对static数据的并发访问.
 *
 * 什么时候同步?
 * 如果正在写一个变量,它可能接下来将被另一个线程读取,或者你正在读取一个上一次已经被另一个线程写过的变量,那么必须使用同步,并且,读写线程都必须用相同的监视器锁同步.
 * !!!每个访问临界共享资源的方法都必须被同步!!!
 *
 * Created by xiongcen on 16/8/27.
 */
public class SynchronizedEvenGenerator extends IntGenerator {
    private int currentEvenValue = 0;
    public synchronized int next() {
        ++currentEvenValue;
        Thread.yield(); // Cause failure faster
        ++currentEvenValue;
        return currentEvenValue;
    }
    public static void main(String[] args) {
        EvenChecker.test(new SynchronizedEvenGenerator());
    }
}/* Output:
*一直不会停止*
*///:~