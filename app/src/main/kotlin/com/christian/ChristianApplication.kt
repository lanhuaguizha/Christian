package com.christian

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.christian.util.CrashHandler
import com.github.anzewei.parallaxbacklayout.ParallaxHelper
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

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
        registerActivityLifecycleCallbacks(ParallaxHelper.getInstance())
        // Dark Mode
//        SkinCompatManager.withoutActivity(this)                         // 基础控件换肤初始化
//                .addInflater(SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
//                .addInflater(SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
//                .addInflater(SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
////                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
////                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
//                .loadSkin()
    }

    lateinit var refWatcher: RefWatcher

}
