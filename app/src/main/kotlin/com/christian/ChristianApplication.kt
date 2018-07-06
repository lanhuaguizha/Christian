package com.christian

import android.app.Application
import android.content.Context
import com.christian.util.CrashHandler

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

    }

    override fun onCreate() {
        super.onCreate()
        val crashHandler = CrashHandler.getInstance()
        crashHandler.get()?.init(this)
    }

}
