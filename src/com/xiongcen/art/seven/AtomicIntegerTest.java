package com.xiongcen.art.seven;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xiongcen on 16/10/31.
 */
public class AtomicIntegerTest {
    public static void main(String[] args) {
        int a = 1;
        AtomicInteger ai = new AtomicInteger(a);
        System.out.println("ai.getAndIncrement()=" + ai.getAndIncrement());
        System.out.println("ai.get()=" + ai.get());
        System.out.println("a=" + a);
    }
}
