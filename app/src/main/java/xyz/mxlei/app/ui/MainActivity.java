package xyz.mxlei.app.ui;

import xyz.mxlei.app.BR;
import xyz.mxlei.app.databinding.ActivityMainBinding;
import xyz.mxlei.mvvmx.base.BaseActivity;

/**
 * @author mxlei
 * @date 2020/6/5
 */
public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {


    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    @Override
    public int initVariableId() {
        return BR.viewmodel;
    }

}
