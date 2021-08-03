package xyz.mxlei.app.app;

import android.content.Context;
import android.os.Build;

import androidx.multidex.MultiDex;

import xyz.mxlei.mvvmx.base.BaseApplication;
import xyz.mxlei.mvvmx.utils.KLog;

/**
 * @author mxlei
 */
public class AppApplication extends BaseApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //安卓5.0以下需要处理多DEX，5.0及以上不需要
            MultiDex.install(this);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //是否开启打印日志
        KLog.init(true);
    }
}
