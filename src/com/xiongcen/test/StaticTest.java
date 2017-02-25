package com.xiongcen.test;

/**
 * Created by xiongcen on 17/2/25.
 */
public class StaticTest {

    public static int X = 10;

    public static void main(String[] args) {
        System.out.println(Y); //输出60
    }

    static {
        X = 30;
    }

    public static int Y = X * 2;
}
