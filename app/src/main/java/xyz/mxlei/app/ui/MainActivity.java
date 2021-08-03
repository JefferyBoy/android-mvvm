package xyz.mxlei.app.ui;

import android.os.Bundle;

import xyz.mxlei.app.BR;
import xyz.mxlei.app.R;
import xyz.mxlei.app.databinding.ActivityMainBinding;
import xyz.mxlei.mvvmx.base.BaseActivity;

/**
 * @author mxlei
 * @date 2020/6/5
 */
public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewmodel;
    }
}
