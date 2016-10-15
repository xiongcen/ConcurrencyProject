package com.xiongcen.art;

/**
 * 扩展:{@link= "http://hapinwater.iteye.com/blog/310558"}
 * 如何保证一个线程调用interrupt()后会被中断
 * Created by xiongcen on 16/10/15.
 */
public class InterruptedTask {
    public static void main(String[] args) throws InterruptedException {
        //将任务交给一个线程执行
        Thread t = new Thread(new InterruptedRunner(), "InterruptedRunner");
        t.start();

        //运行一断时间中断线程
        Thread.sleep(50);
        System.out.println("****************************");
        System.out.println("Interrupted Thread!");
        System.out.println("****************************");
        t.interrupt();
    }

    static class InterruptedRunner implements Runnable {

        private double d = 0.0;

        @Override
        public void run() {
            //死循环执行打印"I am running!" 和做消耗时间的浮点计算
            try {
                while (!Thread.interrupted()) {
                    System.out.println("I am running!");

                    Thread.sleep(40);

                    System.out.println("Calculating");
                    for (int i = 0; i < 900000; i++) {
                        d = d + (Math.PI + Math.E) / d;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Exiting by Exception");
            }
            System.out.println("ATask.run() interrupted!");
        }
    }
}
/**
 * 如果在InterruptedRunner的休眠期间调用t.interrupt();执行结果为:
 * I am running!
 * ***************************
 * Interrupted Thread!
 * ***************************
 * Exiting by Exception
 * ATask.run() interrupted!
 * <p/>
 * 说明是抛出InterruptedException导致线程结束.
 * <p/>
 * 如果在InterruptedRunner的运行期间调用t.interrupt();执行结果为:
 * I am running!
 * Calculating
 * ***************************
 * Interrupted Thread!
 * ***************************
 * ATask.run() interrupted!
 * 说明是Thread.interrupted()判断起作用.
 */