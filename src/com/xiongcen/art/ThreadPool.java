package com.xiongcen.art;

/**
 * Java并发编程艺术 4.4.3线程池技术及其示例 代码清单4-19
 * Created by xiongcen on 16/10/16.
 */
public interface ThreadPool<Job extends Runnable> {
    // 执行一个Job,这个Job需要实现Runnable
    void execute(Job job);
    // 关闭线程池
    void shutdown();
    // 增加工作者线程
    void addWorkers(int num);
    // 减少工作者线程
    void removeWorker(int num);
    // 得到正在等待执行的任务数量
    int getJobSize();
}
