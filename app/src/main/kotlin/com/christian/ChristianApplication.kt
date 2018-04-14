package com.christian

import android.app.Application
import com.christian.util.CrashHandler

class ChristianApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val crashHandler = CrashHandler.getInstance()
        crashHandler.get()?.init(this)
    }

}
