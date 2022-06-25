package xyz.mxlei.app.ui;

import android.annotation.SuppressLint;
import android.app.Application;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.functions.Consumer;
import xyz.mxlei.app.data.DataRepository;
import xyz.mxlei.app.data.model.User;
import xyz.mxlei.mvvmx.base.BaseViewModel;
import xyz.mxlei.mvvmx.binding.BindingCommand;
import xyz.mxlei.mvvmx.binding.BindingCommand3;
import xyz.mxlei.mvvmx.utils.KLog;
import xyz.mxlei.mvvmx.utils.RxUtils;
import xyz.mxlei.mvvmx.utils.ToastUtils;

/**
 * @author mxlei
 * @date 2020/7/12
 */
@SuppressLint("CheckResult")
public class MainViewModel extends BaseViewModel {

    public MutableLiveData<User> user;
    public ObservableField<String> username = new ObservableField<>("guo");
    public ObservableField<Float> imageTranslateX = new ObservableField<>(0f);
    public ObservableField<Float> imageTranslateY = new ObservableField<>(0f);

    public MainViewModel(@NonNull Application application) {
        super(application);
        user = new MutableLiveData<>();
        user.setValue(DataRepository.sp().getLoginUser());
    }

    public void login() {
        KLog.d("login " + user.getValue().getName() + "\t" + user.getValue().getPassword());
        DataRepository.sp().setLoginUser(user.getValue());
        DataRepository.http()
                .login(user.getValue().getName(), user.getValue().getPassword())
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.bindToLifecycle(getLifecycleProvider()))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        ToastUtils.showShort(aBoolean ? "登录成功" : "登录失败");
                    }
                });
    }

    public BindingCommand clickLogin = new BindingCommand() {
        @Override
        public void call(View view, Object item) {
            KLog.d("clickLogin");
        }
    };

    public BindingCommand<Boolean> switchCheckedChange = new BindingCommand<Boolean>() {
        @Override
        public void call(View view, Boolean item) {
            KLog.d("switchCheckedChange " + item);
        }
    };

    public BindingCommand3<MotionEvent, Boolean> onImageTouch = new BindingCommand3<MotionEvent, Boolean>() {
        private float x, y;
        private float downTransX, downTransY;

        @Override
        public Boolean call(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                x = motionEvent.getRawX();
                y = motionEvent.getRawY();
                downTransX = view.getTranslationX();
                downTransY = view.getTranslationY();
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                KLog.d("X = " + motionEvent.getX() + "\tY = " + motionEvent.getY());
                imageTranslateX.set(downTransX + motionEvent.getRawX() - x);
                imageTranslateY.set(downTransY + motionEvent.getRawY() - y);
            }
            return true;
        }
    };
}
