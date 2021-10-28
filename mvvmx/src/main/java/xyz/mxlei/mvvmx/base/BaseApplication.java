package xyz.mxlei.mvvmx.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.style.MaterialStyle;

import xyz.mxlei.mvvmx.crash.CaocConfig;

/**
 * @author mxlei
 */

public class BaseApplication extends Application {
    private static Application sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        if (isMainProcess()) {
            setApplication(this);
        }
    }

    /**
     * 判断当前进程是否为主进程
     */
    public boolean isMainProcess() {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return getApplicationInfo().packageName.equals(appProcess.processName);
            }
        }
        return false;
    }

    /**
     * 是判断当前线程否为主线程
     */
    public boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 当主工程没有继承BaseApplication时，可以使用setApplication方法初始化BaseApplication
     *
     * @param application
     */
    public static synchronized void setApplication(@NonNull Application application) {
        sInstance = application;
        //注册监听每个activity的生命周期,便于堆栈式管理
        application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                AppManager.getAppManager().addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                AppManager.getAppManager().removeActivity(activity);
            }
        });
        //DialogX初始化
        DialogX.init(application);
        DialogX.DEBUGMODE = false;
        DialogX.globalStyle = MaterialStyle.style();
        DialogX.globalTheme = DialogX.THEME.AUTO;
        DialogX.dialogMaxWidth = application.getResources().getDisplayMetrics().widthPixels;
        DialogX.autoShowInputKeyboard = true;
        DialogX.onlyOnePopTip = false;
        DialogX.cancelable = true;
        DialogX.cancelableTipDialog = false;
        //崩溃检测
        CaocConfig.Builder.create()
                //背景模式,开启沉浸式
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT)
                //是否启动全局异常捕获
                .enabled(true)
                //是否显示错误详细信息
                .showErrorDetails(true)
                //是否显示重启按钮
                .showRestartButton(true)
                //是否跟踪Activity
                .trackActivities(true)
                //崩溃的间隔时间(毫秒)
                .minTimeBetweenCrashesMs(2000)
                .apply();
    }

    /**
     * 获得当前app运行的Application
     */
    public static Application getInstance() {
        if (sInstance == null) {
            throw new NullPointerException("please inherit BaseApplication or call setApplication.");
        }
        return sInstance;
    }
}
