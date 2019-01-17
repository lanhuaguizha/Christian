package com.christian

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.christian.util.CrashHandler
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
    }

    lateinit var refWatcher: RefWatcher

}
