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

public class ThreadUtils {
    private static ExecutorService threadPool;
    private static Handler handler;
    private static final int POOL_SIZE = Integer.MAX_VALUE;

    public static boolean isUIThread() {
        return Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId();
    }

    public static void onUIThread(Runnable runnable) {
        if (Thread.currentThread().getId() != Looper.getMainLooper().getThread().getId()) {
            if (handler == null) {
                handler = new Handler(Looper.getMainLooper());
            }

            handler.post(runnable);
        } else {
            runnable.run();
        }
    }

    public static void onNewThread(Runnable runnable) {
        (new Thread(runnable)).start();
    }

    public static void onThreadPool(Runnable runnable) {
        if (runnable != null) {
            if (threadPool == null) {
                synchronized (ThreadUtils.class) {
                    if (threadPool == null) {
                        ThreadFactory threadFactory = new ThreadFactory() {
                            private final AtomicInteger threadNumber = new AtomicInteger(1);
                            private final ThreadGroup group = new ThreadGroup("MVVMThreadUtil");
                            private static final String NAME_PREFIX = "pool-";

                            @Override
                            public Thread newThread(Runnable r) {
                                Thread t = new Thread(group, r,
                                        NAME_PREFIX + threadNumber.getAndIncrement(),
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


    /**
     * 在非UI线程执行
     * 若当前线程为UI线程--在线程池执行
     * 若当前线程非UI线程--在当前线程执行
     */
    public static void onNotUIThread(Runnable runnable) {
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            onThreadPool(runnable);
        } else {
            runnable.run();
        }
    }
}
