package xyz.mxlei.mvvmx.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

import xyz.mxlei.mvvmx.R;

/**
 * @author mxlei
 * @date 2021/10/7
 */
public abstract class BaseDialog<VB extends ViewDataBinding, VM extends BaseViewModel> extends DialogFragment {

    protected VB binding;
    protected VM viewModel;
    private Dialog dialog;

    public BaseDialog() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViewDataBinding(inflater, container, savedInstanceState);
        registorUIChangeLiveDataCallBack();
        initData();
        initViewObservable();
        viewModel.registerRxBus();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.viewModel != null) {
            this.viewModel.removeRxBus();
        }
        if (this.binding != null) {
            this.binding.unbind();
        }
    }

    protected void initData() {

    }

    protected void initViewObservable() {

    }

    protected abstract int initVariableId();

    protected VM initViewModel() {
        return null;
    }

    private void initViewDataBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class bindingClass = (Class) type.getActualTypeArguments()[0];
        Class viewModelClass = (Class) type.getActualTypeArguments()[1];
        try {
            Method inflateMethod = bindingClass.getMethod("inflate", LayoutInflater.class);
            this.binding = (VB) inflateMethod.invoke(null, inflater);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        this.viewModel = this.initViewModel();
        if (this.viewModel == null) {
            if (viewModelClass == null) {
                viewModelClass = BaseViewModel.class;
            }
            viewModel = (VM) new ViewModelProvider(this).get(viewModelClass);
        }
        this.binding.setVariable(initVariableId(), this.viewModel);
        this.binding.setLifecycleOwner(this);
        this.getLifecycle().addObserver(this.viewModel);
    }

    private void registorUIChangeLiveDataCallBack() {
        this.viewModel.getUC().getShowLoadingDialogEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String title) {
                BaseDialog.this.showDialog(title);
            }
        });
        this.viewModel.getUC().getDismissLoadingDialogEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                BaseDialog.this.dismissDialog();
            }
        });
        this.viewModel.getUC().getStartActivityEvent().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> params) {
                Class<?> clz = (Class) params.get(BaseViewModel.ParameterField.CLASS);
                Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
                BaseDialog.this.startActivity(clz, bundle);
            }
        });
        this.viewModel.getUc().getStartActivityForResultEvent().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> params) {
                Class<?> clz = (Class) params.get(BaseViewModel.ParameterField.CLASS);
                Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
                int requestCode = (Integer) params.get(BaseViewModel.ParameterField.REQUEST_CODE);
                Intent intent = new Intent(requireContext(), clz);
                if (bundle != null) {
                    intent.putExtras(bundle);
                }

                BaseDialog.this.startActivityForResult(intent, requestCode);
            }
        });
        this.viewModel.getUC().getFinishEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                BaseDialog.this.dismiss();
            }
        });
        this.viewModel.getUC().getOnBackPressedEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                BaseDialog.this.dismiss();
            }
        });
    }

    protected void showDialog(String title) {
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

    protected void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    protected void startActivity(Class<?> clz) {
        this.startActivity(new Intent(requireContext(), clz));
    }

    protected void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(requireContext(), clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        this.startActivity(intent);
    }
}
