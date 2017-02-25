package com.xiongcen.test;

/**
 * Created by xiongcen on 17/1/23.
 */
public class ConstructorTest {

    class A {
        A() {
            System.out.println("A()");
        }

        A(int n) {
            System.out.println("A(int n)");
        }
    }

    class B extends A {
        B() {
            System.out.println("B()");
        }

        B(int n) {
            System.out.println("B(int n)");
        }
    }

    public void test() {
        B b = new B();
        System.out.println("------");
        B b1 = new B(2);
        System.out.println("------");
    }

    public static void main(String[] args) {
        ConstructorTest test = new ConstructorTest();
        test.test();
    }
}/**
 * A()
 * B()
 * ------
 * A()
 * B(int n)
 * ------
 */
