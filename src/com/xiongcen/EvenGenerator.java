package com.xiongcen;

/**
 * Created by xiongcen on 16/8/27.
 */
public class EvenGenerator extends IntGenerator {
    private int currentEvenValue = 0;

    /**
     * 一个任务有可能在另一个任务执行第一个对currentEvenValue的递增操作之后,但是没有执行第二个操作之前,调用next()方法.
     * !!!递增不是原子性操作!!!
     * 递增过程中任务可能会被线程机制挂起.因此如果不保护,单一的递增也不安全.
     * @return
     */
    public int next() {
        ++currentEvenValue; // Danger point here!
        ++currentEvenValue;
        return currentEvenValue;
    }
    public static void main(String[] args) {
        EvenChecker.test(new EvenGenerator());
    }
}
