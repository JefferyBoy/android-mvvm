package xyz.mxlei.mvvmx.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

import xyz.mxlei.mvvmx.R;
import xyz.mxlei.mvvmx.base.BaseViewModel.ParameterField;

/**
 * @author mxlei
 * @date 2020/12/16
 */
public abstract class BaseLazyFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends Fragment implements IBaseView {
    protected V binding;
    protected VM viewModel;
    private int viewModelId;
    private boolean isLoaded = false;

    private LayoutInflater mLayoutInflater;
    private Bundle mSavedInstanceState;
    private ViewGroup mViewGroup;
    private FrameLayout mFragmentRoot;
    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mSavedInstanceState = savedInstanceState;
        this.mLayoutInflater = inflater;
        this.mViewGroup = container;

        View view = inflater.inflate(initLoadingLayout(), container, false);
        mFragmentRoot = view.findViewById(R.id.root_container);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (viewModel != null) {
            viewModel.removeRxBus();
        }
        if (binding != null) {
            binding.unbind();
        }
        isLoaded = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isLoaded && !isHidden()) {
            lazyInit(mSavedInstanceState);
            isLoaded = true;
            onLazyLoaded();
        }
    }

    protected int initLoadingLayout() {
        return R.layout.mvvmx_fragment_loading;
    }

    private void lazyInit(@Nullable Bundle savedInstanceState) {
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding();
        //私有的ViewModel与View的契约事件回调逻辑
        registorUIChangeLiveDataCallBack();
        //页面数据初始化方法
        initData();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable();
        //注册RxBus
        viewModel.registerRxBus();
        mFragmentRoot.removeAllViews();
        mFragmentRoot.addView(binding.getRoot());
    }

    protected void onLazyLoaded() {
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class bindingClass = (Class) type.getActualTypeArguments()[0];
        Class viewModelClass = (Class) type.getActualTypeArguments()[1];
        try {
            Method inflateMethod = bindingClass.getMethod("inflate", LayoutInflater.class);
            binding = (V) inflateMethod.invoke(null, getLayoutInflater());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        viewModelId = initVariableId();
        viewModel = initViewModel();
        if (viewModel == null) {
            if (viewModelClass == null) {
                viewModelClass = BaseViewModel.class;
            }
            viewModel = (VM) createViewModel(this, viewModelClass);
        }
        binding.setVariable(viewModelId, viewModel);
        binding.setLifecycleOwner(this);
        getLifecycle().addObserver(viewModel);
    }

    /**
     * =====================================================================
     **/
    //注册ViewModel与View的契约UI回调事件
    protected void registorUIChangeLiveDataCallBack() {
        //加载对话框显示
        viewModel.getUC().getShowLoadingDialogEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String title) {
                showLoadingDialog(title);
            }
        });
        //加载对话框消失
        viewModel.getUC().getDismissLoadingDialogEvent().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                dismissLoadingDialog();
            }
        });
        //跳入新页面
        viewModel.getUC().getStartActivityEvent().observe(getViewLifecycleOwner(), new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> params) {
                Class<?> clz = (Class<?>) params.get(ParameterField.CLASS);
                Bundle bundle = (Bundle) params.get(ParameterField.BUNDLE);
                startActivity(clz, bundle);
            }
        });
        viewModel.getUC().getStartActivityForResultEvent().observe(getViewLifecycleOwner(), new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> params) {
                Class<?> clz = (Class<?>) params.get(ParameterField.CLASS);
                Bundle bundle = (Bundle) params.get(ParameterField.BUNDLE);
                int requestCode = (int) params.get(ParameterField.REQUEST_CODE);
                Intent intent = new Intent(getContext(), clz);
                if (bundle != null) {
                    intent.putExtras(bundle);
                }
                startActivityForResult(intent, requestCode);
            }
        });
        //关闭界面
        viewModel.getUC().getFinishEvent().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                getActivity().finish();
            }
        });
        //关闭上一层
        viewModel.getUC().getOnBackPressedEvent().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                getActivity().onBackPressed();
            }
        });
    }

    protected void showLoadingDialog(String title) {
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(R.layout.mvvmx_dialog_loading);
            if (TextUtils.isEmpty(title)) {
                builder.setTitle(title);
            }
            dialog = builder.create();
        }
        dialog.show();
    }

    protected void dismissLoadingDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    protected void startActivity(Class<?> clz) {
        startActivity(new Intent(getContext(), clz));
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    protected void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(getContext(), clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * =====================================================================
     **/

    //刷新布局
    protected void refreshLayout() {
        if (viewModel != null) {
            binding.setVariable(viewModelId, viewModel);
        }
    }

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    protected abstract int initVariableId();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    protected VM initViewModel() {
        return null;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {

    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    protected <T extends ViewModel> T createViewModel(Fragment fragment, Class<T> cls) {
        return new ViewModelProvider(fragment).get(cls);
    }
}
