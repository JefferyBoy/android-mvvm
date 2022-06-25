package xyz.mxlei.mvvmx.utils;

/**
 * @author lei
 * @date :2019/9/11 12:40
 */

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool {
    private static ExecutorService threadPool;
    private static Handler handler;
    private static final int POOL_SIZE = Integer.MAX_VALUE;

    public static boolean isMainLooper() {
        return Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId();
    }

    public static void onMainLooper(Runnable runnable) {
        if (Thread.currentThread().getId() != Looper.getMainLooper().getThread().getId()) {
            if (handler == null) {
                handler = new Handler(Looper.getMainLooper());
            }
            handler.post(runnable);
        } else {
            runnable.run();
        }
    }

    public static void onMainLooper(Runnable runnable, long delay) {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        if (delay > 0) {
            handler.postDelayed(runnable, delay);
        } else {
            handler.post(runnable);
        }
    }

    public static void onPool(Runnable runnable) {
        if (runnable != null) {
            if (threadPool == null) {
                synchronized (ThreadPool.class) {
                    if (threadPool == null) {
                        ThreadFactory threadFactory = new ThreadFactory() {
                            private final AtomicInteger threadNumber = new AtomicInteger(1);
                            private final ThreadGroup group = new ThreadGroup("ThreadPool");

                            @Override
                            public Thread newThread(Runnable r) {
                                Thread t = new Thread(group, r,
                                        String.valueOf(threadNumber.getAndIncrement()),
                                        0);
                                t.setDaemon(false);
                                t.setPriority(Thread.NORM_PRIORITY);
                                return t;
                            }
                        };
                        threadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                60L, TimeUnit.SECONDS,
                                new SynchronousQueue<Runnable>(), threadFactory);
                    }
                }
            }
            threadPool.execute(runnable);
        }
    }
}
