package com.xiongcen;

/**
 * Created by xiongcen on 16/8/25.
 */
class Sleeper extends Thread {
    private int duration;

    public Sleeper(String name, int sleepTime) {
        super(name);
        duration = sleepTime;
        start();
    }

    public void run() {
        try {
            sleep(duration);
        } catch (InterruptedException e) {
            System.out.println(System.currentTimeMillis()+ " "+getName() + " was interrupted. " +
                    "isInterrupted(): " + isInterrupted());
            return;
        }
        System.out.println(System.currentTimeMillis()+ " "+getName() + " has awakened");
    }
}

class Joiner extends Thread {
    private Sleeper sleeper;

    public Joiner(String name, Sleeper sleeper) {
        super(name);
        this.sleeper = sleeper;
        start();
    }

    public void run() {
        try {
            sleeper.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        System.out.println(System.currentTimeMillis()+ " "+getName() + " join completed");
    }
}

public class Joining {
    public static void main(String[] args) {
        Sleeper sleepy = new Sleeper("Sleepy", 1500);
        Sleeper grumpy = new Sleeper("Grumpy", 1500);
        // (sleepy)线程在(dopey)其他线程之上调用join()方法,其效果是(dopey)其他线程等待一段时间直到(sleepy)线程结束才继续执行.
        // (Joiner)dopey挂起,直到(Sleeper)sleepy结束才恢复
        Joiner dopey = new Joiner("Dopey", sleepy);
        Joiner doc = new Joiner("Doc", grumpy);
        // join()方法可以被中断
        // 当在main线程上调用grumpy线程的interrupt()方法时,将给grumpy线程设定一个标志,表明该线程已经被中断.
        // 然而异常捕获时将清理这个标志,所以在catch语句中,在异常捕获时该标志总是为false.
        grumpy.interrupt();
        // 如果Sleeper被中断或者是正常结束,Joiner将和Sleeper一同结束.
    }
}/* Output:
1472120388808 Grumpy was interrupted. isInterrupted(): false
1472120388809 Doc join completed
1472120390310 Sleepy has awakened
1472120390310 Dopey join completed
*///:~