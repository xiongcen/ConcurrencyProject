package com.xiongcen;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用显示的Lock对象来创建临界区
 * <p/>
 * 该返回结果会抛出异常.检查异常来源为ExplicitPairManager2.
 * { @link:http://blog.nex3z.com/2016/07/03/%E6%B7%B7%E7%94%A8%E5%90%8C%E6%AD%A5%E5%9D%97%E5%92%8C%E5%90%8C%E6%AD%A5%E6%96%B9%E6%B3%95%E6%97%B6%E7%9A%84%E9%97%AE%E9%A2%98/ }
 * 解释:
 * 1.ExplicitPairManager2的increment()使用ReentrantLock进行手动加锁,确保x和y的自增不会被打断.
 * 2.问题应该出在读取x和y的时机.ExplicitPairManager2的getPair()直接继承自父类PairManager.
 * 3.getPair()使用了synchronized,也是同步的,但却没有与ExplicitPairManager2的increment()方法同步,在increment()运行期间执行了getPair(),得到了不同的x和y.
 * 结论:
 * 使用synchronized同步的方法是同步于对象自己的锁，而ExplicitPairManager2的increment()是同步于显式创建的ReentrantLock，
 * getPair()和increment()没有同步于同一个锁，导致二者实际上没有同步。
 * <p/>
 * 该例子为了说明:手动为代码块加锁能让对象更多地处于解锁状态,使共享资源能更充分地被其他任务使用.
 * Created by xiongcen on 16/9/8.
 */

class ExplicitPairManager1 extends PairManager {
    private Lock lock = new ReentrantLock();

    public synchronized void increment() {
        lock.lock();
        try {
            p.incrementX();
            p.incrementY();
            store(getPair());
        } finally {
            lock.unlock();
        }
    }
}

// Use a critical section:
class ExplicitPairManager2 extends PairManager {
    private Lock lock = new ReentrantLock();

    public void increment() {
        Pair temp;
        lock.lock();
        try {
            p.incrementX();
            p.incrementY();
            temp = getPair();
        } finally {
            lock.unlock();
        }
        store(temp);
    }
}

/**
 * 重写ExplicitPairManager2的getPair()方法,使其同步于increment()的锁,即可解决此问题.
 */
class ExplicitPairManager3 extends PairManager {

    private Lock lock = new ReentrantLock();

    @Override
    public void increment() {
        Pair temp;
        lock.lock();
        try {
            p.incrementX();
            p.incrementY();
            temp = getPair();
        } finally {
            lock.unlock();
        }
        store(temp);
    }

    @Override
    public Pair getPair() {
        lock.lock();
        try {
            return super.getPair();
        } finally {
            lock.unlock();
        }
    }
}

public class ExplicitCriticalSection {
    public static void main(String[] args) throws Exception {
        PairManager
                pman1 = new ExplicitPairManager1(),
                pman2 = new ExplicitPairManager3();
        CriticalSection.testApproaches(pman1, pman2);
    }
}/* Output: (Sample)
会抛异常:com.xiongcen.Pair$PairValuesNotEqualException: Pair cvalues not equal:x:2,y:1
pm1: Pair: x:259,y:259 checkCounter = 3
pm2: Pair: x:260,y:260 checkCounter = 1642170
*///:~
