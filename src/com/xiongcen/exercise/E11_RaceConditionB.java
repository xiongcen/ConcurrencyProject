package com.xiongcen.exercise;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xiongcen on 16/8/27.
 */

class Tank {
    enum State {EMPTY, LOADED}

    private State state = State.EMPTY;
    private int current_load = 0;

    public void validate() {
        if ((state == State.EMPTY && current_load != 0) ||
                (state == State.LOADED && current_load == 0))
            throw new IllegalStateException();
    }

    public void fill() {
        state = State.LOADED;
        Thread.yield();     // Cause failure faster
        current_load = 10;  // Arbitrary value
    }

    public void drain() {
        state = State.EMPTY;
        Thread.yield();
        current_load = 0;
    }
}

class SafeTank extends Tank {
    public synchronized void validate() { super.validate(); }
    public synchronized void fill() { super.fill(); }
    public synchronized void drain() { super.drain(); }
}

class ConsistencyChecker implements Runnable {
    private static Random rnd = new Random();
    private Tank tank;

    ConsistencyChecker(Tank tank) {
        this.tank = tank;
    }

    public void run() {
        for (; ; ) {
            // Decide whether to fill or drain the tank
            if (rnd.nextBoolean())
                tank.fill();
            else
                tank.drain();
            tank.validate();
        }
    }
}

public class E11_RaceConditionB {
    public static void main(String[] args) {
        System.out.println("Press Control-C to exit");
        ExecutorService exec = Executors.newCachedThreadPool();
        Tank tank = new SafeTank();
        for (int i = 0; i < 10; i++)
            exec.execute(new ConsistencyChecker(tank));
        Thread.yield();
        exec.shutdown();
    }
}
