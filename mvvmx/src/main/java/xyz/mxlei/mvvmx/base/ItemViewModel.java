package xyz.mxlei.mvvmx.base;


import androidx.annotation.NonNull;

/**
 * ItemViewModel
 *
 * @author mxlei
 */

public class ItemViewModel<VM extends BaseViewModel> {
    protected VM viewModel;

    public ItemViewModel(@NonNull VM viewModel) {
        this.viewModel = viewModel;
    }
}
