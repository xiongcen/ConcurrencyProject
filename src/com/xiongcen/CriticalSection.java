package com.xiongcen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Critical:临界的
 *
 * Pair: Pair类为非线程安全,自增加操作不是线程安全,并且因为没有任何方法被标记为synchronized,
 * 所以不能保证一个Pair对象在多线程程序中不会被破坏.
 *
 * PairManager: 如果给一个非线程安全的Pair类需要在一个线程环境中使用它,通过创建PairManager类就可以实现这一点,
 * PairManager类持有一个Pair对象并控制对它的一切访问.注意唯一的public方法是getPair(),它是synchronized的.
 * 对于抽象方法increment(),对increment()的同步控制将在实现的时候进行处理.
 *
 * PairManipulator被创建用来测试两种不同类型的PairManager,其方法是在某个任务中调用increment(),
 * 而PairChecker则在另一个任务中执行.为了跟踪可以运行测试的频度,PairChecker在每次成功时都递增checkCounter.
 *
 * 尽管每次运行的结果可能会非常不同,但一般来说,对于PairChecker的检查频率,
 * PairManager1.increment()不允许有PairManger2.increment()那样多.
 * 因为PairManger2.increment()采用同步控制块进行同步,所以对象不加锁的时间更长.
 *
 * !!!所以使用同步控制块而不是对整个方法进行同步控制的原因:使得其他线程能更多地访问(在安全的情况下尽可能多)!!!
 *
 * Created by xiongcen on 16/9/5.
 */
class Pair {
    private int x, y;

    public Pair() {
        this(0, 0);
    }

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void incrementX() {
        x++;
    }

    public void incrementY() {
        y++;
    }

    @Override
    public String toString() {
        return "x:" + x + ",y:" + y;
    }

    public class PairValuesNotEqualException extends RuntimeException {
        public PairValuesNotEqualException() {
            super("Pair cvalues not equal:" + Pair.this);
        }
    }

    public void checkState() {
        if (x != y) {
            throw new PairValuesNotEqualException();
        }
    }
}

abstract class PairManager {
    AtomicInteger checkCounter = new AtomicInteger(0);
    protected Pair p = new Pair();
    private List<Pair> storage = Collections.synchronizedList(new ArrayList<Pair>());

    public synchronized Pair getPair() {
        return new Pair(p.getX(), p.getY());
    }

    protected void store(Pair p) {
        storage.add(p);

        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract void increment();
}

class PairManager1 extends PairManager {

    /** synchronized关键字不属于方法特征签名的组成部分,所以可以在覆盖方法的时候加上去 */
    @Override
    public synchronized void increment() {
        p.incrementX();
        p.incrementY();
        store(getPair());
    }
}

class PairManager2 extends PairManager {

    @Override
    public void increment() {
        Pair temp;
        synchronized (this) {
            p.incrementX();
            p.incrementY();
            temp = getPair();
        }
        /** store()方法将一个Pair对象添加到synchronized ArrayList中,所以操作线程安全 */
        store(temp);
    }
}

/**
 * Manipulator:调度器
 */
class PairManipulator implements Runnable {
    private PairManager pm;

    public PairManipulator(PairManager pm) {
        this.pm = pm;
    }

    public void run() {
        while (true)
            pm.increment();
    }

    public String toString() {
        return "Pair: " + pm.getPair() +
                " checkCounter = " + pm.checkCounter.get();
    }
}

class PairChecker implements Runnable {
    private PairManager pm;
    public PairChecker(PairManager pm) {
        this.pm = pm;
    }
    public void run() {
        while(true) {
            pm.checkCounter.incrementAndGet();
            pm.getPair().checkState();
        }
    }
}

public class CriticalSection {
    static void
    testApproaches(PairManager pman1, PairManager pman2) {
        ExecutorService exec = Executors.newCachedThreadPool();
        PairManipulator
                pm1 = new PairManipulator(pman1),
                pm2 = new PairManipulator(pman2);
        PairChecker
                pcheck1 = new PairChecker(pman1),
                pcheck2 = new PairChecker(pman2);
        exec.execute(pm1);
        exec.execute(pm2);
        exec.execute(pcheck1);
        exec.execute(pcheck2);
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch(InterruptedException e) {
            System.out.println("Sleep interrupted");
        }
        System.out.println("pm1: " + pm1 + "\npm2: " + pm2);
        System.exit(0);
    }
    public static void main(String[] args) {
        PairManager
                pman1 = new PairManager1(),
                pman2 = new PairManager2();
        testApproaches(pman1, pman2);
    }
}/* Output:
pm1: Pair: x:41,y:41 checkCounter = 3
pm2: Pair: x:41,y:41 checkCounter = 102456413
*///:~
