package xyz.mxlei.mvvmx.base;


import androidx.annotation.NonNull;

/**
 * @author mxlei
 * Description：RecycleView多布局ItemViewModel是封装
 */

public class MultiItemViewModel<VM extends BaseViewModel> extends ItemViewModel<VM> {
    protected Object multiType;

    public Object getItemType() {
        return multiType;
    }

    public void multiItemType(@NonNull Object multiType) {
        this.multiType = multiType;
    }

    public MultiItemViewModel(@NonNull VM viewModel) {
        super(viewModel);
    }
}
