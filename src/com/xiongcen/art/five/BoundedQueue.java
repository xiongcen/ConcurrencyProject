package com.xiongcen.art.five;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Java并发编程艺术 5.6.1Condition接口与实例 代码清单5-21
 * 有界队列:当队列为空时,队列的获取操作将会阻塞获取线程,直到队列中有新增元素;
 * 当队列已满时,队列的插入操作将会阻塞插入线程,直到队列出现"空位".
 * Created by xiongcen on 16/10/24.
 */
public class BoundedQueue<T> {
    private Object[] items;
    // 添加的下标,删除的下标和数组当前数量
    private int addIndex, removeIndex, count;
    private Lock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    public BoundedQueue(int size) {
        items = new Object[size];
    }

    // 添加一个元素,如果数组满,则添加线程进入等待状态,直到有"空位"
    public void add(T t) throws InterruptedException {
        // 获取锁,确保数组修改的可见性和排他性.
        lock.lock();
        try {
            // 当数组数量等于数组长度时,表示数组已满
            while (count == items.length) {
                // 当前线程随之释放锁并进入等待状态
                notFull.await();
            }
            items[addIndex] = t;
            if (++addIndex == items.length) {
                addIndex = 0;
            }
            ++count;
            // 通过等待在notEmpty上的线程,数组中已经有新元素可以获取
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    // 由头部删除一个元素,如果数组空,则删除线程进入等待状态,直到有新添加元素
    public T remove() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await();
            }
            Object item = items[removeIndex];
            if (++removeIndex == items.length) {
                removeIndex = 0;
            }
            --count;
            notFull.signal();
            return (T) item;
        } finally {
            lock.unlock();
        }
    }
}
