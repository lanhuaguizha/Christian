package com.christian.http;

import android.os.Environment;
import com.christian.ChristianApplication;

/**
 * Created by Administrator on 2016/10/17.
 */
public class SdHelper {

    public static String getDiskCacheDir() {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = ChristianApplication.context.getExternalCacheDir().getPath();
        } else {
            cachePath = ChristianApplication.context.getCacheDir().getPath();
        }
        return cachePath;
    }
}
