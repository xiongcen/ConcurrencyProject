package com.xiongcen;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 另外一种方式重写MutexEvenGeneratort.java来保证同步.
 * 所有同步形式通过AtomicInteger得到根除.
 *
 * Created by xiongcen on 16/9/1.
 */
public class AtomicEvenGenerator extends IntGenerator {
    private AtomicInteger currentEvenValue =
            new AtomicInteger(0);
    public int next() {
        return currentEvenValue.addAndGet(2);
    }
    public static void main(String[] args) {
        EvenChecker.test(new AtomicEvenGenerator());
    }
}
