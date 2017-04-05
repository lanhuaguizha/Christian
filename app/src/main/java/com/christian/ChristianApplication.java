package com.christian;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2017/4/2.
 */

public class ChristianApplication extends Application {

    // 在application的onCreate中初始化
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
//        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
}
