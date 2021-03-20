package com.yiqizuoye.library.page.api;


import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

/**
 * Author: jiao
 * Date: 2021/3/19
 * Description:
 * 线程调度类
 */
class PageThreadManager {
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    /**
     * 运行在主线程
     *
     * @param runnable
     */
    public static void runMain(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            mainHandler.post(runnable);
        }
    }

    /**
     * 运行在主线程，异步执行
     *
     * @param runnable
     */
    public static void runMainAsync(Runnable runnable) {
        mainHandler.post(runnable);
    }

    /**
     * 运行在工作线程
     *
     * @param runnable
     */
    public static void runWork(Runnable runnable) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(runnable);
    }
}
