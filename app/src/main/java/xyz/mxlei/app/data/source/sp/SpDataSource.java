package xyz.mxlei.app.data.source.sp;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import xyz.mxlei.app.data.model.User;
import xyz.mxlei.mvvmx.utils.SPUtils;

/**
 * SharePreference数据源
 *
 * @author mxlei
 * @date 2019/03/07
 */
public class SpDataSource {
    private volatile static SpDataSource INSTANCE = null;
    private SPUtils sp;
    private Gson gson;

    private SpDataSource() {
        sp = SPUtils.getInstance();
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
        String str = sp.getString("LoginUser");
        User user = null;
        try {
            if (str != null && str.length() > 0) {
                user = gson.fromJson(str, User.class);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } finally {
            if (user == null) user = new User();
        }
        return user;
    }

    public void setLoginUser(User user) {
        sp.putRealTime("LoginUser", gson.toJson(user));
    }
}
