package com.xiongcen;

/**
 * Created by xiongcen on 16/8/25.
 */
public class JoinTest {
    public static void main(String[] args) throws Exception {
        final Thread b=new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("b start");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("b end");
            }
        });
        Thread a=new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("a start");
                try {
                    b.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("a end");
            }
        });
        a.start();
        b.start();
    }
}/* Output:
a start
b start
b end
a end
*///:~
