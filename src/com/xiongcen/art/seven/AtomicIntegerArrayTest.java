package com.xiongcen.art.seven;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Created by xiongcen on 16/10/31.
 */
public class AtomicIntegerArrayTest {

    static int[] value = new int[]{1, 2};

    // 数组value通过构造方法传递进去,然后AtomicIntegerArray会将当前数组复制一份,所以当
    // AtomicIntegerArray对内部的数组元素进行修改时,不会影响传入的数组.
    static AtomicIntegerArray array = new AtomicIntegerArray(value);

    public static void main(String[] args) {
        array.getAndSet(0, 3);
        System.out.println("array.get(0)=" + array.get(0));
        System.out.println("value[0]=" + value[0]);
    }
}
