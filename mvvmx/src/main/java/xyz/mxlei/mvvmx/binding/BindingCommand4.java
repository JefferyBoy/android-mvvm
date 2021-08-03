package xyz.mxlei.mvvmx.binding;

import android.view.View;

/**
 * @author mxlei
 * @date 2021/8/3
 */
public interface BindingCommand4<T> {

    T call(View view, Object... item);
}
