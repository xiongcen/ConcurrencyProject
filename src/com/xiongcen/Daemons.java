package com.xiongcen;//: concurrency/Daemons.java
// Daemon threads spawn other daemon threads.
import java.util.concurrent.*;

/**
 * Daemon线程被设置为后台模式,它派生出的子线程也默认是后台线程.
 * 第二个for循环也可能因为CPU被让出而中断
 */
class Daemon implements Runnable {
  private Thread[] t = new Thread[10];
  public void run() {
    for(int i = 0; i < t.length; i++) {
      t[i] = new Thread(new DaemonSpawn(i));
      t[i].start();
      System.out.println("DaemonSpawn " + i + " started, ");
    }
    for(int i = 0; i < t.length; i++)
      System.out.println("t[" + i + "].isDaemon() = " +
        t[i].isDaemon() + ", ");
    while(true)
      Thread.yield();
  }
}

class DaemonSpawn implements Runnable {

  private int i;
  public DaemonSpawn(int i) {
    this.i = i;
  }
  public void run() {
    System.out.println("DaemonSpawn " + i + " run, ");
    while(true)
      Thread.yield();
  }
}

public class Daemons {
  public static void main(String[] args) throws Exception {
    Thread d = new Thread(new Daemon());
    d.setDaemon(true);
    d.start();
    System.out.println("d.isDaemon() = " + d.isDaemon() + ", ");
    // Allow the daemon threads to
    // finish their startup processes:
    TimeUnit.SECONDS.sleep(1);
  }
} /* Output: (Sample)
DaemonSpawn 0 run,
d.isDaemon() = true,
DaemonSpawn 0 started,
DaemonSpawn 1 started,
DaemonSpawn 1 run,
DaemonSpawn 2 started,
DaemonSpawn 2 run,
DaemonSpawn 3 started,
DaemonSpawn 4 started,
DaemonSpawn 3 run,
DaemonSpawn 5 started,
DaemonSpawn 4 run,
DaemonSpawn 5 run,
DaemonSpawn 6 started,
DaemonSpawn 6 run,
DaemonSpawn 7 started,
DaemonSpawn 7 run,
DaemonSpawn 8 started,
DaemonSpawn 8 run,
DaemonSpawn 9 started,
t[0].isDaemon() = true,
DaemonSpawn 9 run,
t[1].isDaemon() = true,
t[2].isDaemon() = true,
t[3].isDaemon() = true,
t[4].isDaemon() = true,
t[5].isDaemon() = true,
t[6].isDaemon() = true,
t[7].isDaemon() = true,
t[8].isDaemon() = true,
t[9].isDaemon() = true,
*///:~
