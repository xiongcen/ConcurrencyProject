package com.xiongcen;

/**
 * 错误理解:院子操作不需要进行同步控制.
 * 原子操作:不能被线程调度机制中断的操作;
 * 一旦操作开始,那么它一定在可能发生的"上下文切换"之前(切换到其他线程执行)
 * 执行完毕.
 * 原子性可以应用于除long和double之外所有基本类型之上的"简单操作".
 * 因为JVM可以将64位(long和double变量)的读取和写入当做两个分离的32位操作来执行,
 * 这就产生了再一个读取和写入操作中间发生上下文切换,从而导致不同的任务可以看到不正确结果的可能性.
 *
 * !!!---volatile关键字---!!!
 * volatile确保应用可视性.如果将一个域声明为volatile的,那么只要对这个域产生了写操作,
 * 那么所有的读操作就可以看到这个修改.
 * 即便使用了本地缓存,情况也确实如此,volatile域会被立即写入到主存中,而读取操作就发生在主存中.
 *
 * 如果多个任务在同时访问某个域,那么这个域就应该是volatile的,否则这个域就应该只能经由同步来访问.(synchronized或lock等)
 *
 * volatile无法工作的情况:
 * 1.当一个域的值依赖于它之前的值时(例如递增一个计数器);
 * 2.如果某个域的值收到其他域的值的限制(例如Range类的lower和upper便捷就必须能遵循lower<=upper的限制);
 *
 * 使用volatile而不是synchronized的唯一安全的情况是类中只有一个可变的域.第一选择应该是synchronized关键字.
 *
 * Created by xiongcen on 16/9/1.
 */
public class Atomicity {
    int i;
    void f1() { i++; }
    void f2() { i += 3; }
}/* Output: (Sample)
每一条指令都会产生一个get和put,它们之间还有一些其他的指令.
因此在获取和放置之间,另一个任务可能会修改这个域.所以这些操作都不是原子性的.

...
void f1();
  Code:
   0:        aload_0
   1:        dup
   2:        getfield        #2; //Field i:I
   5:        iconst_1
   6:        iadd
   7:        putfield        #2; //Field i:I
   10:        return

void f2();
  Code:
   0:        aload_0
   1:        dup
   2:        getfield        #2; //Field i:I
   5:        iconst_3
   6:        iadd
   7:        putfield        #2; //Field i:I
   10:        return
*///:~
