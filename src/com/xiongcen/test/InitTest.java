package com.xiongcen.test;

/**
 * Created by xiongcen on 17/2/25.
 */
class B {

    B() {
        System.out.println("B.B");
    }

    static {
        System.out.println("Class B is initialized.");
    }

    static void print() {
        System.out.println("A.print");
    }
}

class A extends B {
    static int value = 100;

    A() {
        System.out.println("A.A");
    }


    static {
        System.out.println("Class A is initialized.");
    }
}

public class InitTest {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        new A();
    }
}
