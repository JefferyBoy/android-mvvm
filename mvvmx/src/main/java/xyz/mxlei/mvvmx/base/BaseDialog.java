package xyz.mxlei.mvvmx.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.kongzue.dialogx.dialogs.WaitDialog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author mxlei
 * @date 2021/10/7
 */
public abstract class BaseDialog<VB extends ViewDataBinding, VM extends BaseViewModel> extends DialogFragment {

    protected VB binding;
    protected VM viewModel;

    public BaseDialog() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initParam(getArguments());
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

    protected void initParam(Bundle arg) {

    }

    protected void initData() {

    }

    protected void initViewObservable() {

    }

    protected abstract int initContentView();

    protected abstract int initVariableId();

    protected VM initViewModel() {
        return null;
    }

    private void initViewDataBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = DataBindingUtil.inflate(inflater, initContentView(), container, true);
        this.viewModel = this.initViewModel();
        if (this.viewModel == null) {
            Type type = this.getClass().getGenericSuperclass();
            Class modelClass;
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                modelClass = BaseViewModel.class;
            }
            viewModel = (VM) new ViewModelProvider(this).get(modelClass);
        }
        this.binding.setVariable(initVariableId(), this.viewModel);
        this.binding.setLifecycleOwner(this);
        this.getLifecycle().addObserver(this.viewModel);
        this.viewModel.injectLifecycleOwner(this);
    }

    private void registorUIChangeLiveDataCallBack() {
        this.viewModel.getUC().getShowDialogEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String title) {
                BaseDialog.this.showDialog(title);
            }
        });
        this.viewModel.getUC().getDismissDialogEvent().observe(this, new Observer<Void>() {
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
        this.viewModel.getUc().getstartActivityForResultEvent().observe(this, new Observer<Map<String, Object>>() {
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
        this.viewModel.getUC().getStartContainerActivityEvent().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> params) {
                String canonicalName = (String) params.get(BaseViewModel.ParameterField.CANONICAL_NAME);
                Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
                BaseDialog.this.startContainerActivity(canonicalName, bundle);
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
        WaitDialog.show(title);
    }

    protected void dismissDialog() {
        WaitDialog.dismiss();
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

    protected void startContainerActivity(String canonicalName) {
        this.startContainerActivity(canonicalName, null);
    }

    protected void startContainerActivity(String canonicalName, Bundle bundle) {
        Intent intent = new Intent(requireContext(), ContainerActivity.class);
        intent.putExtra("fragment", canonicalName);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        this.startActivity(intent);
    }
}
