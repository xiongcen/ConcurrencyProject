package com.xiongcen;

/**
 * Created by xiongcen on 16/8/20.
 */
//: concurrency/MoreBasicThreads.java
// Adding more threads.

public class MoreBasicThreads {
    public static void main(String[] args) {
        for(int i = 0; i < 5; i++)
            new Thread(new LiftOff()).start();
        System.out.println("Waiting for LiftOff");
    }
} /* Output: (Sample)
#1(9), #4(9), #1(8), #4(8), #3(9), Waiting for LiftOff
#2(9), #0(9), #3(8), #4(7), #1(7),
#3(7), #0(8), #2(8), #0(7), #1(6), #3(6), #4(6), #3(5), #1(5), #0(6),
#2(7), #0(5), #1(4), #3(4), #4(5), #3(3), #1(3), #0(4), #2(6), #0(3),
#1(2), #3(2), #4(4), #3(1), #1(1), #0(2), #2(5), #0(1), #1(Liftoff!), #3(Liftoff!),
#4(3), #0(Liftoff!), #2(4), #4(2), #2(3), #4(1), #2(2), #4(Liftoff!), #2(1), #2(Liftoff!),
*///:~
