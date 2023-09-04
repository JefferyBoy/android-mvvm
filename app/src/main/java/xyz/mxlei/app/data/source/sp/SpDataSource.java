package xyz.mxlei.app.data.source.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import xyz.mxlei.app.data.model.User;
import xyz.mxlei.mvvmx.utils.Mvvm;

/**
 * SharePreference数据源
 *
 * @author mxlei
 * @date 2019/03/07
 */
public class SpDataSource {
    private volatile static SpDataSource INSTANCE = null;
    private final SharedPreferences sp;
    private final Gson gson;

    private SpDataSource() {
        sp = Mvvm.getContext().getSharedPreferences("SharedPreferencesDefault.db", Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static SpDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (SpDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SpDataSource();
                }
            }
        }
        return INSTANCE;
    }

    public User getLoginUser() {
        String str = sp.getString("LoginUser", null);
        User user = null;
        try {
            if (str != null && str.length() > 0) {
                user = gson.fromJson(str, User.class);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } finally {
            if (user == null) {
                user = new User();
            }
        }
        return user;
    }

    public void setLoginUser(User user) {
        sp.edit().putString("LoginUser", gson.toJson(user)).apply();
    }
}
