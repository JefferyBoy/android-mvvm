package xyz.mxlei.app.ui;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import xyz.mxlei.app.data.DataRepository;
import xyz.mxlei.app.data.model.User;
import xyz.mxlei.mvvmx.base.BaseViewModel;

/**
 * @author mxlei
 * @date 2020/7/12
 */
public class MainViewModel extends BaseViewModel {

    public MutableLiveData<User> user;
    public ObservableField<Float> imageTranslateX;
    public ObservableField<Float> imageTranslateY;

    public MainViewModel(Application app) {
        super(app);
        user = new MutableLiveData<>();
        user.setValue(DataRepository.sp().getLoginUser());
        imageTranslateX = new ObservableField<>();
        imageTranslateY = new ObservableField<>();
    }

    @SuppressLint("CheckResult")
    public void login() {
        Log.d("demo", "login " + user.getValue().getName() + "\t" + user.getValue().getPassword());
        DataRepository.sp().setLoginUser(user.getValue());
        DataRepository.http()
            .login(user.getValue().getName(), user.getValue().getPassword())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    Toast.makeText(getApplication(), aBoolean ? "登录成功" : "登录失败", Toast.LENGTH_SHORT).show();
                }
            });
    }

    public void clickLogin(View v) {
        Log.d("demo", "clickLogin");
    }

    public void clickImage(View v) {
        Log.d("demo", "clickImage");
    }
}
