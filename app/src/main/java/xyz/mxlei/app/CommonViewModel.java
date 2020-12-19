package xyz.mxlei.app;

import android.app.Application;

import androidx.annotation.NonNull;

import xyz.mxlei.mvvmx.base.BaseModel;
import xyz.mxlei.mvvmx.base.BaseViewModel;

/**
 * @author mxlei
 * @date 2020/12/18
 */
public class CommonViewModel extends BaseViewModel<BaseModel> {

    public CommonViewModel(@NonNull Application application) {
        super(application);
    }
}
