package com.christian

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper
import com.christian.util.CrashHandler
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import skin.support.SkinCompatManager
import skin.support.app.SkinCardViewInflater
import skin.support.constraint.app.SkinConstraintViewInflater
import skin.support.design.app.SkinMaterialViewInflater

class ChristianApplication : Application() {

    init {
        context = this
    }

    /**
     * Gets the application context.
     *
     * @return the application context
     */
    companion object {

        lateinit var context: ChristianApplication

        fun getRefWatcher(context: Context): RefWatcher {
            val application = context.applicationContext as ChristianApplication
            return application.refWatcher
        }
    }

    override fun onCreate() {
        super.onCreate()
        // leak canary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        refWatcher = LeakCanary.install(this);
        MultiDex.install(this)
        // Normal app init code...
        val crashHandler = CrashHandler.getInstance()
        crashHandler.get()?.init(this)
        /**
         * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         */
        BGASwipeBackHelper.init(this, null)
        // Dark Mode
        SkinCompatManager.withoutActivity(this)                         // 基础控件换肤初始化
                .addInflater(SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
//                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
//                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin()
    }

    lateinit var refWatcher: RefWatcher

}
