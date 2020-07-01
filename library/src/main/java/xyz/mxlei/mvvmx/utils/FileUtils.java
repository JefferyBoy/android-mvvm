package xyz.mxlei.mvvmx.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

/**
 * @author mxlei
 * @date 2020/2/27
 */
public class FileUtils {

    /**
     * File转Uri，支持Android7.0以上的contentProvider
     *
     * @param context
     * @param file
     * @return
     */
    public static Uri getUriFromFile(Context context, File file, String contentProviderAuthority) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), contentProviderAuthority, file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**
     * Uri转换为File
     * */
    public static File uriToFile(Uri uri){

        return null;
    }

    /**
     * 保存文件
     */
    public static void saveFile(byte[] data, File file) {

    }

    /**
     * 读取文件
     */
    public static byte[] readFile(File file) {

        return new byte[0];
    }


}
