package com.xiongcen;//: concurrency/DaemonsDontRunFinally.java
// Daemon threads don't run the finally clause
import java.util.concurrent.*;

class ADaemon implements Runnable {
  public void run() {
    try {
      System.out.println("Starting ADaemon");
      TimeUnit.SECONDS.sleep(1);
    } catch(InterruptedException e) {
      System.out.println("Exiting via InterruptedException");
    } finally {
      System.out.println("This should always run?");
    }
  }
}

/**
 * 当最后一个非线程终止时,后台线程会突然终止.不够优雅,不是好的实现方式!!!
 */
public class DaemonsDontRunFinally {
  public static void main(String[] args) throws Exception {
    Thread t = new Thread(new ADaemon());
    t.setDaemon(true);
    t.start();
  }
} /* Output:
Starting ADaemon
*///:~
