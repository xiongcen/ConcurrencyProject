package com.xiongcen.interview;

import java.lang.reflect.Field;

/**
 * 演示如何获取java对象的相对地址偏移量及使用Unsafe完成CAS操作.
 * { link="https://www.kancloud.cn/seaboat/java-concurrent/117871" }
 * Created by xiongcen on 16/10/26.
 */
public class UnsafeTest {

    private int flag = 100;

    private static long offset;

    private static sun.misc.Unsafe unsafe = null;

    static {
        try {
            unsafe = getUnsafeInstance();
            offset = unsafe.objectFieldOffset(UnsafeTest.class.getDeclaredField("flag"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        int expect = 100;
        int update = 101;
        UnsafeTest unsafeTest = new UnsafeTest();
        System.out.println("unsafeTest对象的flag字段的地址偏移量为：" + offset);
        unsafeTest.doSwap(offset, expect, update);
        System.out.println("CAS操作后的flag值为：" + unsafeTest.getFlag());
    }

    private boolean doSwap(long offset, int expect, int update) {
        return unsafe.compareAndSwapInt(this, offset, expect, update);

    }

    public int getFlag() {
        return flag;

    }

    private static sun.misc.Unsafe getUnsafeInstance() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field theUnsafeInstance = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafeInstance.setAccessible(true);
        return (sun.misc.Unsafe) theUnsafeInstance.get(sun.misc.Unsafe.class);
    }
}/**
 * unsafeTest对象的flag字段的地址偏移量为：12
 * CAS操作后的flag值为：101
 */
