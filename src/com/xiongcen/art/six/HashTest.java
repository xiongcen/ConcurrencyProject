package com.xiongcen.art.six;

/**
 * 普通散列算法和ConcurrentHashMap默认值散列算法对比
 * Created by xiongcen on 16/10/29.
 */
public class HashTest {

    public static void main(String[] args) {
        System.out.println("普通散列算法");
        System.out.println(Integer.parseInt("0001111", 2) & 15);
        System.out.println(Integer.parseInt("0011111", 2) & 15);
        System.out.println(Integer.parseInt("0111111", 2) & 15);
        System.out.println(Integer.parseInt("1111111", 2) & 15);
        System.out.println();
        System.out.println("ConcurrentHashMap散列算法");
        System.out.println(segment(hash(Integer.parseInt("0001111", 2))));
        System.out.println(segment(hash(Integer.parseInt("0011111", 2))));
        System.out.println(segment(hash(Integer.parseInt("0111111", 2))));
        System.out.println(segment(hash(Integer.parseInt("1111111", 2))));
    }

    private static int hash(int h) {
        h += (h << 15) ^ 0xffffcd7d;
        h ^= (h >>> 10);
        h += (h << 3);
        h ^= (h >>> 6);
        h += (h << 2) + (h << 14);
        return h ^ (h >>> 16);
    }

    private static int segment(int hash) {
        return (hash >>> 28) & 15;
    }
}/**
 * 普通散列算法
 * 15
 * 15
 * 15
 * 15
 * <p/>
 * ConcurrentHashMap散列算法
 * 4
 * 15
 * 7
 * 8
 */
