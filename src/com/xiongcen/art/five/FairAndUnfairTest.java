package com.xiongcen.art.five;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Java并发编程艺术 5.3重入锁->展示公平和非公平 代码清单5-15
 * Created by xiongcen on 16/10/20.
 */
public class FairAndUnfairTest {

    private static Lock fairLock = new ReentrantLock2(true);
    private static Lock unfairLock = new ReentrantLock2(false);

    public static void main(String[] args) {
        FairAndUnfairTest test = new FairAndUnfairTest();
//        test.testLock(fairLock);
        test.testLock(unfairLock);
    }

    private void testLock(Lock lock) {
        for (int i = 0; i < 5; i++) {
            Job job = new Job(lock);
            job.start();
        }
    }

    static class ReentrantLock2 extends ReentrantLock {
        ReentrantLock2(boolean fair) {
            super(fair);
        }

        public Collection<Thread> getQueuedThreads() {
            List<Thread> arrayList = new ArrayList<>(super.getQueuedThreads());
            Collections.reverse(arrayList);
            return arrayList;
        }
    }

    static class Job extends Thread {

        private Lock lock;

        public Job(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            if (lock instanceof ReentrantLock2) {
                lock.lock();
                System.out.print("Lock by[" + Thread.currentThread().getName() + "],Waiting by [");
                Collection<Thread> queuedThreads1 = ((ReentrantLock2) lock).getQueuedThreads();
                Iterator<Thread> iterator1 = queuedThreads1.iterator();
                StringBuilder sb1 = new StringBuilder();
                while (iterator1.hasNext()) {
                    sb1.append(iterator1.next().getName() + ",");
                }
                System.out.print(sb1.toString() + "]");
                System.out.println();
                lock.unlock();
                lock.lock();
                System.out.print("Lock by[" + Thread.currentThread().getName() + "],Waiting by [");
                Collection<Thread> queuedThreads = ((ReentrantLock2) lock).getQueuedThreads();
                Iterator<Thread> iterator = queuedThreads.iterator();
                StringBuilder sb = new StringBuilder();
                while (iterator.hasNext()) {
                    sb.append(iterator.next().getName() + ",");
                }
                System.out.print(sb.toString() + "]");
                System.out.println();
                lock.unlock();
            }
        }
    }
}/**
 * fair:
 * Lock by[Thread-0],Waiting by [Thread-1,]
 * Lock by[Thread-1],Waiting by [Thread-2,Thread-3,Thread-0,Thread-4,]
 * Lock by[Thread-2],Waiting by [Thread-3,Thread-0,Thread-4,Thread-1,]
 * Lock by[Thread-3],Waiting by [Thread-0,Thread-4,Thread-1,Thread-2,]
 * Lock by[Thread-0],Waiting by [Thread-4,Thread-1,Thread-2,Thread-3,]
 * Lock by[Thread-4],Waiting by [Thread-1,Thread-2,Thread-3,]
 * Lock by[Thread-1],Waiting by [Thread-2,Thread-3,Thread-4,]
 * Lock by[Thread-2],Waiting by [Thread-3,Thread-4,]
 * Lock by[Thread-3],Waiting by [Thread-4,]
 * Lock by[Thread-4],Waiting by []
 * <p/>
 * unfair:
 * Lock by[Thread-0],Waiting by [Thread-1,]
 * Lock by[Thread-0],Waiting by [Thread-1,Thread-2,]
 * Lock by[Thread-1],Waiting by [Thread-2,Thread-3,]
 * Lock by[Thread-1],Waiting by [Thread-2,Thread-3,]
 * Lock by[Thread-2],Waiting by [Thread-3,Thread-4,]
 * Lock by[Thread-2],Waiting by [Thread-3,Thread-4,]
 * Lock by[Thread-3],Waiting by [Thread-4,]
 * Lock by[Thread-3],Waiting by [Thread-4,]
 * Lock by[Thread-4],Waiting by []
 * Lock by[Thread-4],Waiting by []
 */
