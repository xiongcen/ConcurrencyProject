package com.xiongcen.exercise;

import java.util.Arrays;

/**
 * Created by xiongcen on 16/8/20.
 */
public class E02_Fibonacci {
    public static void main(String[] args) {
        for(int i = 1; i <= 5; i++)
            new Thread(new Fibonacci(i)).start();
    }
}

class Fibonacci implements Generator<Integer>, Runnable {

    private int count;
    private final int n;
    public Fibonacci(int n) { this.n = n; }
    private int fib(int n) {
        if (n < 2) return 1;
        return fib(n - 2) + fib(n - 1);
    }

    @Override
    public void run() {
        System.out.println("Fibonacci.run n:"+n);
        Integer[] sequence = new Integer[n];
        for (int i = 0; i < n; i++) {
            sequence[i] = next();
            System.out.println("Fibonacci.for i:"+i);
        }
        System.out.println("Fibonacci.yield before n:"+n);
//        Thread.yield();
        System.out.println(
                "Seq. of " + n + ": " + Arrays.toString(sequence));
    }

    @Override
    public Integer next() {
        return fib(count++);
    }
}
