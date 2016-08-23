package com.xiongcen;

/**
 * Created by xiongcen on 16/8/20.
 */
//: concurrency/MainThread.java

public class MainThread {
    public static void main(String[] args) {
        LiftOff launch = new LiftOff();
        launch.run();
        System.out.println("Waiting for LiftOff");
    }
} /* Output:
#0(9), #0(8), #0(7), #0(6), #0(5), #0(4), #0(3), #0(2), #0(1), #0(Liftoff!), Waiting for LiftOff
*///:~