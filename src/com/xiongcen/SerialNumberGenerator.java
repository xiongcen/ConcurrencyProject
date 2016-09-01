package com.xiongcen;

/**
 * 产生序列数字
 *
 * 如果一个域可能会被多个任务同时访问,或者这些任务中至少有一个是写入操作,
 * 那么就应该将这个域设置为volatile的.
 *
 * 如果将一个域定义为volatile,那么它就会告诉编译器不要执行任何移除读取和写入操作的优化,
 * 这些操作的目的是用线程中的局部变量维护对这个域的精确同步.
 * 实际上,volatile的读取和写入都是直接针对内存的,没有缓存.
 *
 * !!!---但是,volatile并不能对递增不是原子性操作这一事实产生影响---!!!
 * Created by xiongcen on 16/9/1.
 */
public class SerialNumberGenerator {
    private static volatile int serialNumber = 0;
    public static int nextSerialNumber() {
        return serialNumber++; // 线程不安全
    }
}
