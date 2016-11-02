package com.xiongcen.art.night;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xiongcen on 16/11/2.
 */
public class ThreadPoolExecutorSource {

    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

    // COUNT_BITS = 32 - 3 = 29
    private static final int COUNT_BITS = Integer.SIZE - 3;

    // CAPACITY = (1 << 29) - 1 = 0001 | 1111 | 1111 | 1111 | 1111 | 1111 | 1111 | 1111
    private static final int CAPACITY = (1 << COUNT_BITS) - 1;

    // RUNNING = -1 << 29 = 1110 | 0000 | 0000 | 0000 | 0000 | 0000 | 0000 | 0000
    private static final int RUNNING = -1 << COUNT_BITS;

    // SHUTDOWN = 0 << 29 = 0000 | 0000 | 0000 | 0000 | 0000 | 0000 | 0000 | 0000
    private static final int SHUTDOWN = 0 << COUNT_BITS;

    // STOP = 1 << 29 = 0010 | 0000 | 0000 | 0000 | 0000 | 0000 | 0000 | 0000
    private static final int STOP = 1 << COUNT_BITS;

    // TIDYING = 2 << 29 = 0100 | 0000 | 0000 | 0000 | 0000 | 0000 | 0000 | 0000
    private static final int TIDYING = 2 << COUNT_BITS;

    // TERMINATED = 2 << 29 = 0110 | 0000 | 0000 | 0000 | 0000 | 0000 | 0000 | 0000
    private static final int TERMINATED = 3 << COUNT_BITS;

    /**
     * 该方法用于取出runState的值
     *  CAPACITY = 0001 | 1111 | 1111 | 1111 | 1111 | 1111 | 1111 | 1111
     * ~CAPACITY = 1110 | 0000 | 0000 | 0000 | 0000 | 0000 | 0000 | 0000
     * c & ~CAPACITY 将低29位置0,高3位还是保持原先的值,也就是runState的值
     */
    private static int runStateOf(int c) {
        return c & ~CAPACITY;
    }

    /**
     * 该方法用于取出workerCount的值
     * CAPACITY = 0001 | 1111 | 1111 | 1111 | 1111 | 1111 | 1111 | 1111
     * c & CAPACITY 将高3位置0,低29位还是保持原先的值,也就是workerCount的值
     */
    private static int workerCountOf(int c) {
        return c & CAPACITY;
    }

    /**
     * 将runState和workerCount存到同一个int中
     * @param rs runState移位过后的值，负责填充返回值的高3位
     * @param wc workerCount移位过后的值，负责填充返回值的低29位
     * @return 两者或运算过后的值
     */
    private static int ctlOf(int rs, int wc) {
        return rs | wc;
    }

    /**
     * 只有Running状态小于0
     */
    private static boolean isRunning(int c) {
        return c < SHUTDOWN;
    }


//    private final class Worker
//            extends AbstractQueuedSynchronizer
//            implements Runnable
//    {
//        /**
//         * This class will never be serialized, but we provide a
//         * serialVersionUID to suppress a javac warning.
//         */
//        private static final long serialVersionUID = 6138294804551838833L;
//
//        /** Thread this worker is running in.  Null if factory fails. */
//        final Thread thread;
//        /** Initial task to run.  Possibly null. */
//        Runnable firstTask;
//        /** Per-thread task counter */
//        volatile long completedTasks;
//
//        /**
//         * Creates with given first task and thread from ThreadFactory.
//         * @param firstTask the first task (null if none)
//         */
//        Worker(Runnable firstTask) {
//            setState(-1); // inhibit interrupts until runWorker
//            this.firstTask = firstTask;
//            this.thread = getThreadFactory().newThread(this);
//        }
//
//        /** Delegates main run loop to outer runWorker  */
//        public void run() {
//            runWorker(this);
//        }
//
//        // Lock methods
//        //
//        // The value 0 represents the unlocked state.
//        // The value 1 represents the locked state.
//
//        protected boolean isHeldExclusively() {
//            return getState() != 0;
//        }
//
//        protected boolean tryAcquire(int unused) {
//            if (compareAndSetState(0, 1)) {
//                setExclusiveOwnerThread(Thread.currentThread());
//                return true;
//            }
//            return false;
//        }
//
//        protected boolean tryRelease(int unused) {
//            setExclusiveOwnerThread(null);
//            setState(0);
//            return true;
//        }
//
//        public void lock()        { acquire(1); }
//        public boolean tryLock()  { return tryAcquire(1); }
//        public void unlock()      { release(1); }
//        public boolean isLocked() { return isHeldExclusively(); }
//
//        void interruptIfStarted() {
//            Thread t;
//            if (getState() >= 0 && (t = thread) != null && !t.isInterrupted()) {
//                try {
//                    t.interrupt();
//                } catch (SecurityException ignore) {
//                }
//            }
//        }
//    }
//
//    //任务缓存队列，用来存放等待执行的任务
//    private final BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(10);
//
//    //线程池的主要状态锁，对线程池状态（比如线程池大小、runState等）的改变都要使用这个锁
//    private final ReentrantLock mainLock = new ReentrantLock();
//
//    //用来存放工作集
//    private final HashSet<Worker> workers = new HashSet<Worker>();
//
//    //线程存活时间   
//    private volatile long keepAliveTime;
//
//    //是否允许为核心线程设置存活时间
//    private volatile boolean allowCoreThreadTimeOut;
//
//    //核心池的大小（即线程池中的线程数目大于这个参数时，提交的任务会被放进任务缓存队列）
//    private volatile int corePoolSize;
//
//    //线程池最大能容忍的线程数
//    private volatile int maximumPoolSize;
//
//    //线程池中当前的线程数
//    private volatile int poolSize;
//
//    //任务拒绝策略
//    private volatile RejectedExecutionHandler handler;
//
//    //线程工厂，用来创建线程
//    private volatile ThreadFactory threadFactory;
//
//    //用来记录线程池中曾经出现过的最大线程数
//    private int largestPoolSize;
//
//    //用来记录已经执行完毕的任务个数
//    private long completedTaskCount;
//
//    public void execute(Runnable command) {
//        if (command == null)
//            throw new NullPointerException();
//
//        int c = ctl.get();
//        // 活动线程数 < corePoolSize
//        if (workerCountOf(c) < corePoolSize) {
//            // 直接启动新的线程。第二个参数true:addWorker中会重新检查workerCount是否小于corePoolSize
//            if (addWorker(command, true))
//                // 添加成功返回
//                return;
//            c = ctl.get();
//        }
//        // 活动线程数 >= corePoolSize
//        // runState为RUNNING && 队列未满
//        if (isRunning(c) && workQueue.offer(command)) {
//            int recheck = ctl.get();
//            // double check
//            // 非RUNNING状态 则从workQueue中移除任务并拒绝
//            if (!isRunning(recheck) && remove(command))
//                reject(command);// 采用线程池指定的策略拒绝任务
//                // 线程池处于RUNNING状态 || 线程池处于非RUNNING状态但是任务移除失败
//            else if (workerCountOf(recheck) == 0)
//                // 这行代码是为了SHUTDOWN状态下没有活动线程了，但是队列里还有任务没执行这种特殊情况。
//                // 添加一个null任务是因为SHUTDOWN状态下，线程池不再接受新任务
//                addWorker(null, false);
//
//            // 两种情况：
//            // 1.非RUNNING状态拒绝新的任务
//            // 2.队列满了启动新的线程失败（workCount > maximumPoolSize）
//        } else if (!addWorker(command, false))
//            reject(command);
//    }
//
//    private boolean addWorker(Runnable firstTask, boolean core) {
//        retry: for (;;) {
//            int c = ctl.get();
//            int rs = runStateOf(c);// 当前线程池状态
//
//            // Check if queue empty only if necessary.
//            // 这条语句等价：rs >= SHUTDOWN && (rs != SHUTDOWN || firstTask != null || workQueue.isEmpty())
//            // 满足下列条件则直接返回false，线程创建失败:
//            // rs > SHUTDOWN:STOP || TIDYING || TERMINATED 此时不再接受新的任务，且所有任务执行结束
//            // rs = SHUTDOWN:firtTask != null 此时不再接受任务，但是仍然会执行队列中的任务
//            // rs = SHUTDOWN:firtTask == null见execute方法的addWorker(null,false)，任务为null && 队列为空
//            // 最后一种情况也就是说SHUTDONW状态下，如果队列不为空还得接着往下执行，为什么？add一个null任务目的到底是什么？
//            // 看execute方法只有workCount==0的时候firstTask才会为null结合这里的条件就是线程池SHUTDOWN了不再接受新任务
//            // 但是此时队列不为空，那么还得创建线程把任务给执行完才行。
//            if (rs >= SHUTDOWN && !(rs == SHUTDOWN && firstTask == null && !workQueue.isEmpty()))
//                return false;
//
//            // 走到这的情形：
//            // 1.线程池状态为RUNNING
//            // 2.SHUTDOWN状态，firstTask!=null,但队列中还有任务需要执行
//            for (;;) {
//                int wc = workerCountOf(c);
//                if (wc >= CAPACITY || wc >= (core ? corePoolSize : maximumPoolSize))
//                    return false;
//                if (compareAndIncrementWorkerCount(c))// 原子操作递增workCount
//                    break retry;// 操作成功跳出的重试的循环
//                c = ctl.get(); // Re-read ctl
//                if (runStateOf(c) != rs)// 如果线程池的状态发生变化则重试
//                    continue retry;
//                // else CAS failed due to workerCount change; retry inner loop
//            }
//        }
//
//        // wokerCount递增成功
//        boolean workerStarted = false;
//        boolean workerAdded = false;
//        Worker w = null;
//        try {
//            final ReentrantLock mainLock = this.mainLock;
//            w = new Worker(firstTask);
//            final Thread t = w.thread;
//            if (t != null) {
//                // 并发的访问线程池workers对象必须加锁
//                mainLock.lock();
//                try {
//                    // Recheck while holding lock.
//                    // Back out on ThreadFactory failure or if
//                    // shut down before lock acquired.
//                    int c = ctl.get();
//                    int rs = runStateOf(c);
//
//                    // RUNNING状态 || SHUTDONW状态下清理队列中剩余的任务
//                    if (rs < SHUTDOWN || (rs == SHUTDOWN && firstTask == null)) {
//                        if (t.isAlive()) // precheck that t is startable
//                            throw new IllegalThreadStateException();
//                        // 将新启动的线程添加到线程池中
//                        workers.add(w);
//                        // 更新largestPoolSize
//                        int s = workers.size();
//                        if (s > largestPoolSize)
//                            largestPoolSize = s;
//                        workerAdded = true;
//                    }
//                } finally {
//                    mainLock.unlock();
//                }
//                // 启动新添加的线程，这个线程首先执行firstTask，然后不停的从队列中取任务执行
//                // 当等待keepAlieTime还没有任务执行则该线程结束。见runWoker和getTask方法的代码。
//                if (workerAdded) {
//                    t.start();// 最终执行的是ThreadPoolExecutor的runWoker方法
//                    workerStarted = true;
//                }
//            }
//        } finally {
//            // 线程启动失败，则从wokers中移除w并递减wokerCount
//            if (!workerStarted)
//                // 递减wokerCount会触发tryTerminate方法
//                addWorkerFailed(w);
//        }
//        return workerStarted;
//    }
//
//    final void runWorker(Worker w) {
//        Thread wt = Thread.currentThread();
//        Runnable task = w.firstTask;
//        w.firstTask = null;
//        // Worker的构造函数中抑制了线程中断setState(-1)，所以这里需要unlock从而允许中断
//        w.unlock();
//        // 用于标识是否异常终止，finally中processWorkerExit的方法会有不同逻辑
//        // 为true的情况：1.执行任务抛出异常；2.被中断。
//        boolean completedAbruptly = true;
//        try {
//            // 如果getTask返回null那么getTask中会将workerCount递减，如果异常了这个递减操作会在processWorkerExit中处理
//            while (task != null || (task = getTask()) != null) {
//                w.lock();
//                // If pool is stopping, ensure thread is interrupted;
//                // if not, ensure thread is not interrupted. This
//                // requires a recheck in second case to deal with
//                // shutdownNow race while clearing interrupt
//                // 如果wt被中断过,wt.isInterrupted()==true,则直接调用wt.interrupt();
//                // 如果wt未被中断过,wt.isInterrupted()==false,则判断当前线程池状态:
//                // 如果runState>=STOP,调用wt.interrupt();
//                // 如果runState<STOP,Thread.interrupted()中断状态复位成功 && runState>=STOP,调用wt.interrupt();
//                // 所以runState<STOP并且wt没有被中断过,才继续执行用户任务
//                if ((runStateAtLeast(ctl.get(), STOP) || (Thread.interrupted() && runStateAtLeast(ctl.get(), STOP)))
//                        && !wt.isInterrupted())
//                    wt.interrupt();
//                try {
//                    // 任务执行前可以插入一些处理，子类重载该方法
//                    beforeExecute(wt, task);
//                    Throwable thrown = null;
//                    try {
//                        task.run();// 执行用户任务
//                    } catch (RuntimeException x) {
//                        thrown = x;
//                        throw x;
//                    } catch (Error x) {
//                        thrown = x;
//                        throw x;
//                    } catch (Throwable x) {
//                        thrown = x;
//                        throw new Error(x);
//                    } finally {
//                        // 和beforeExecute一样，留给子类去重载
//                        afterExecute(task, thrown);
//                    }
//                } finally {
//                    task = null;
//                    w.completedTasks++;
//                    w.unlock();
//                }
//            }
//
//            completedAbruptly = false;
//        } finally {
//            // 结束线程的一些清理工作
//            processWorkerExit(w, completedAbruptly);
//        }
//    }
//
//    private Runnable getTask() {
//        boolean timedOut = false; // Did the last poll() time out?
//        retry: for (;;) {
//            int c = ctl.get();
//            int rs = runStateOf(c);
//
//            // Check if queue empty only if necessary.
//            // 1.rs > SHUTDOWN 所以rs至少等于STOP,这时不再处理队列中的任务
//            // 2.rs = SHUTDOWN 所以rs>=STOP肯定不成立，这时还需要处理队列中的任务除非队列为空
//            // 这两种情况都会返回null让runWoker退出while循环也就是当前线程结束了，所以必须要decrement
//            // wokerCount
//            if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
//                // 递减workerCount值
//                decrementWorkerCount();
//                return null;
//            }
//            // 标记从队列中取任务时是否设置超时时间
//            boolean timed; // Are workers subject to culling?
//
//            // 1.RUNING状态
//            // 2.SHUTDOWN状态，但队列中还有任务需要执行
//            for (;;) {
//                int wc = workerCountOf(c);
//
//                // 1.core thread允许被超时，那么超过corePoolSize的的线程必定有超时
//                // 2.allowCoreThreadTimeOut == false && wc >
//                // corePoolSize时，一般都是这种情况，core thread即使空闲也不会被回收，只要超过的线程才会
//                timed = allowCoreThreadTimeOut || wc > corePoolSize;
//
//                // 从addWorker可以看到一般wc不会大于maximumPoolSize，所以更关心后面半句的情形：
//                // 1. timedOut == false 第一次执行循环， 从队列中取出任务不为null方法返回 或者
//                // poll出异常了重试
//                // 2.timeOut == true && timed ==
//                // false:看后面的代码workerQueue.poll超时时timeOut才为true，
//                // 并且timed要为false，这两个条件相悖不可能同时成立（既然有超时那么timed肯定为true）
//                // 所以超时不会继续执行而是return null结束线程。（重点：线程是如何超时的？？？）
//                if (wc <= maximumPoolSize && !(timedOut && timed))
//                    break;
//
//                // workerCount递减，结束当前thread
//                if (compareAndDecrementWorkerCount(c))
//                    return null;
//                c = ctl.get(); // Re-read ctl
//                // 需要重新检查线程池状态，因为上述操作过程中线程池可能被SHUTDOWN
//                if (runStateOf(c) != rs)
//                    continue retry;
//                // else CAS failed due to workerCount change; retry inner loop
//            }
//            try {
//                // 1.以指定的超时时间从队列中取任务
//                // 2.core thread没有超时
//                Runnable r = timed ? workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) : workQueue.take();
//                if (r != null)
//                    return r;
//                timedOut = true;// 超时
//            } catch (InterruptedException retry) {
//                timedOut = false;// 线程被中断重试
//            }
//        }
//    }
//
//    private void processWorkerExit(Worker w, boolean completedAbruptly) {
//        // 正常的话再runWorker的getTask方法workerCount已经被减一了
//        if (completedAbruptly)
//            decrementWorkerCount();
//
//        final ReentrantLock mainLock = this.mainLock;
//        mainLock.lock();
//        try {
//            // 累加线程的completedTasks
//            completedTaskCount += w.completedTasks;
//            // 从线程池中移除超时或者出现异常的线程
//            workers.remove(w);
//        } finally {
//            mainLock.unlock();
//        }
//
//        // 尝试停止线程池
//        tryTerminate();
//
//        int c = ctl.get();
//        // runState为RUNNING或SHUTDOWN
//        if (runStateLessThan(c, STOP)) {
//            // 线程不是异常结束
//            if (!completedAbruptly) {
//                // 线程池最小空闲数，允许core thread超时就是0，否则就是corePoolSize
//                int min = allowCoreThreadTimeOut ? 0 : corePoolSize;
//                // 如果min == 0但是队列不为空要保证有1个线程来执行队列中的任务
//                if (min == 0 && !workQueue.isEmpty())
//                    min = 1;
//                // 线程池还不为空那就不用担心了
//                if (workerCountOf(c) >= min)
//                    return; // replacement not needed
//            }
//            // 1.线程异常退出
//            // 2.线程池为空，但是队列中还有任务没执行，看addWoker方法对这种情况的处理
//            addWorker(null, false);
//        }
//    }
//
//    final void tryTerminate() {
//        for (;;) {
//            int c = ctl.get();
//            // 以下状态直接返回：
//            // 1.线程池还处于RUNNING状态
//            // 2.SHUTDOWN状态但是任务队列非空
//            // 3.runState >= TIDYING 线程池已经停止了或在停止了
//            if (isRunning(c) || runStateAtLeast(c, TIDYING) || (runStateOf(c) == SHUTDOWN && !workQueue.isEmpty()))
//                return;
//
//            // 只能是以下情形会继续下面的逻辑：结束线程池。
//            // 1.SHUTDOWN状态，这时不再接受新任务而且任务队列也空了
//            // 2.STOP状态，当调用了shutdownNow方法
//
//            // workerCount不为0则还不能停止线程池,而且这时线程都处于空闲等待的状态
//            // 需要中断让线程“醒”过来，醒过来的线程才能继续处理shutdown的信号。
//            if (workerCountOf(c) != 0) { // Eligible to terminate
//                // runWoker方法中w.unlock就是为了可以被中断,getTask方法也处理了中断。
//                // ONLY_ONE:这里只需要中断1个线程去处理shutdown信号就可以了。
//                interruptIdleWorkers(ONLY_ONE);
//                return;
//            }
//
//            final ReentrantLock mainLock = this.mainLock;
//            mainLock.lock();
//            try {
//                // 进入TIDYING状态
//                if (ctl.compareAndSet(c, ctlOf(TIDYING, 0))) {
//                    try {
//                        // 子类重载：一些资源清理工作
//                        terminated();
//                    } finally {
//                        // TERMINATED状态
//                        ctl.set(ctlOf(TERMINATED, 0));
//                        // 继续awaitTermination
//                        termination.signalAll();
//                    }
//                    return;
//                }
//            } finally {
//                mainLock.unlock();
//            }
//            // else retry on failed CAS
//        }
//    }
//
//    public void shutdown() {
//        final ReentrantLock mainLock = this.mainLock;
//        mainLock.lock();
//        try {
//            checkShutdownAccess();
//            // 线程池状态设为SHUTDOWN，如果已经至少是这个状态那么则直接返回
//            advanceRunState(SHUTDOWN);
//            // 注意这里是中断所有空闲的线程：runWorker中等待的线程被中断 → 进入processWorkerExit →
//            // tryTerminate方法中会保证队列中剩余的任务得到执行。
//            interruptIdleWorkers();
//            onShutdown(); // hook for ScheduledThreadPoolExecutor
//        } finally {
//            mainLock.unlock();
//        }
//        tryTerminate();
//    }
//
//    public List<Runnable> shutdownNow() {
//        List<Runnable> tasks;
//        final ReentrantLock mainLock = this.mainLock;
//        mainLock.lock();
//        try {
//            checkShutdownAccess();
//            // STOP状态：不再接受新任务且不再执行队列中的任务。
//            advanceRunState(STOP);
//            // 中断所有线程
//            interruptWorkers();
//            // 返回队列中还没有被执行的任务。
//            tasks = drainQueue();
//        }
//        finally {
//            mainLock.unlock();
//        }
//        tryTerminate();
//        return tasks;
//    }
//
//    private void interruptIdleWorkers(boolean onlyOne) {
//        final ReentrantLock mainLock = this.mainLock;
//        mainLock.lock();
//        try {
//            for (Worker w : workers) {
//                Thread t = w.thread;
//                // w.tryLock能获取到锁，说明该线程没有在运行，因为runWorker中执行任务会先lock，
//                // 因此保证了中断的肯定是空闲的线程。
//                if (!t.isInterrupted() && w.tryLock()) {
//                    try {
//                        t.interrupt();
//                    } catch (SecurityException ignore) {
//                    } finally {
//                        w.unlock();
//                    }
//                }
//                if (onlyOne)
//                    break;
//            }
//        }
//        finally {
//            mainLock.unlock();
//        }
//    }
//
//    void interruptIfStarted() {
//        Thread t;
//        // 初始化时state == -1
//        if (getState() >= 0 && (t = thread) != null && !t.isInterrupted()) {
//            try {
//                t.interrupt();
//            } catch (SecurityException ignore) {
//            }
//        }
//    }

}
