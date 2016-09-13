package com.xiongcen;

/**
 * 演示:两个任务可以同时进入同一个对象,只要这个对象上的方法是在不同锁上同步的即可.
 *
 * DualSynch.f()(通过同步整个方法)在this同步,而g()有一个在syncObject上同步的synchronized块.因此这两个同步是互相独立的!!!
 * 例子中展示:在新的线程调用f(),在main线程调用g(),从输出可以看到,这两个方式在同时运行,因此任何一个方法都没有因为对另一个方法的同步而被阻塞.
 * Created by xiongcen on 16/9/12.
 */
class DualSynch {
    private Object syncObject = new Object();
    public synchronized void f() {
        for(int i = 0; i < 5; i++) {
            System.out.println("f()");
            Thread.yield();
        }
    }
    public void g() {
        synchronized(syncObject) {
            for(int i = 0; i < 5; i++) {
                System.out.println("g()");
                Thread.yield();
            }
        }
    }
}

public class SyncObject {
    public static void main(String[] args) {
        final DualSynch ds = new DualSynch();
        new Thread() {
            public void run() {
                ds.f();
            }
        }.start();
        ds.g();
    }
}/* Output: (Sample)
g()
f()
f()
g()
f()
f()
g()
g()
g()
f()
*///:~
