package xyz.mxlei.mvvmx.utils;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.UUID;

/**
 * @auther mxlei
 * @date 2019/12/2 13:53
 */
public class AppUtils {

    /**
     * 获取设备唯一ID
     * */
    public static String getUUID(Context context){
        String uuid;
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)androidId.hashCode() << 32));
        uuid = deviceUuid.toString();
        if(TextUtils.isEmpty(uuid)){
            uuid = SPUtils.getInstance().getString("UUID");
            if(TextUtils.isEmpty(uuid)){
                uuid = UUID.randomUUID().toString();
                SPUtils.getInstance().put("UUID",uuid);
            }
        }
        return uuid;
    }
}
