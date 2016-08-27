package com.xiongcen;

/**
 * Created by xiongcen on 16/8/27.
 */
public abstract class IntGenerator {

    private volatile boolean canceled = false;
    public abstract int next();
    // Allow this to be canceled:
    public void cancel() { canceled = true; }
    public boolean isCanceled() { return canceled; }
}
