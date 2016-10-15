package com.xiongcen.art;

import java.util.concurrent.TimeUnit;

/**
 * Created by xiongcen on 16/10/15.
 */
public class SleepUtils {

    public static final void second(long second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
