package xyz.mxlei.mvvmx.binding;

import android.view.View;

/**
 * @author mxlei
 * @date 2020/12/19
 */
public interface BindingCommand<T> {

    void call(View view, T item);
}
