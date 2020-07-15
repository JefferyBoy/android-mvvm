package xyz.mxlei.app.ui;

import android.annotation.SuppressLint;
import android.app.Application;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import xyz.mxlei.app.data.DataRepository;
import xyz.mxlei.app.data.model.User;
import xyz.mxlei.mvvmx.binding.command.BindingAction;
import xyz.mxlei.mvvmx.binding.command.BindingCommand;
import xyz.mxlei.mvvmx.binding.command.BindingConsumer;
import xyz.mxlei.mvvmx.binding.command.ResponseCommand;
import xyz.mxlei.mvvmx.utils.KLog;
import xyz.mxlei.mvvmx.utils.ToastUtils;

/**
 * @author mxlei
 * @date 2020/7/12
 */
public class MainViewModel extends AndroidViewModel {

    public MutableLiveData<User> user;
    public ObservableField<Float> imageTranslateX;
    public ObservableField<Float> imageTranslateY;

    public MainViewModel(@NonNull Application application) {
        super(application);
        user = new MutableLiveData<>();
        user.setValue(DataRepository.sp().getLoginUser());
        imageTranslateX = new ObservableField<>();
        imageTranslateY = new ObservableField<>();
    }

    @SuppressLint("CheckResult")
    public void login() {
        KLog.d("login " + user.getValue().getName() + "\t" + user.getValue().getPassword());
        DataRepository.sp().setLoginUser(user.getValue());
        DataRepository.http()
                .login(user.getValue().getName(), user.getValue().getPassword())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        ToastUtils.showShort(aBoolean ? "登录成功" : "登录失败");
                    }
                });
    }

    public BindingCommand clickLogin = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            KLog.d("clickLogin");
        }
    });

    public BindingCommand switchCheckedChange = new BindingCommand<>(new BindingConsumer<Boolean>() {
        @Override
        public void call(Boolean o) {
            KLog.d("switchCheckedChange " + o);
        }
    });

    public ResponseCommand<MotionEvent, Boolean> onImageTouch = new ResponseCommand<>(new Function<MotionEvent, Boolean>() {
        private float x, y;

        @Override
        public Boolean apply(MotionEvent motionEvent) throws Exception {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                x = motionEvent.getRawX();
                y = motionEvent.getRawY();
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                KLog.d("X = " + motionEvent.getX() + "\tY = " + motionEvent.getY());
                imageTranslateX.set(motionEvent.getRawX() - x);
                imageTranslateY.set(motionEvent.getRawY() - y);
            }
            return false;
        }
    });

    public BindingCommand clickImage = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            KLog.d("clickImage");
        }
    });
}
