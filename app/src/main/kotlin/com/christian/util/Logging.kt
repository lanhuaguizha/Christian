package com.christian.util

import android.util.Log

/**
 * 日志记录方法
 * Created by itbird on 2016/6/6
 */
object Logging {

    var isDebugLogging = true

    fun v(tag: String, msg: String) {
        if (isDebugLogging) {
            Log.v(tag, msg)
        }
    }

    fun v(tag: String, msg: String, tr: Throwable) {
        if (isDebugLogging) {
            Log.v(tag, msg, tr)
        }
    }

    fun d(tag: String, msg: String) {
        if (isDebugLogging) {
            Log.d(tag, msg)
        }
    }

    fun d(tag: String, msg: String, tr: Throwable) {
        if (isDebugLogging) {
            Log.d(tag, msg, tr)
        }
    }

    fun i(tag: String, msg: String) {
        if (isDebugLogging) {
            Log.i(tag, msg)
        }
    }

    fun i(tag: String, msg: String, tr: Throwable) {
        if (isDebugLogging) {
            Log.i(tag, msg, tr)
        }
    }

    fun w(tag: String, msg: String) {
        if (isDebugLogging) {
            Log.w(tag, msg)
        }
    }

    fun w(tag: String, msg: String, tr: Throwable) {
        if (isDebugLogging) {
            Log.w(tag, msg, tr)
        }
    }

    fun w(tag: String, tr: Throwable) {
        if (isDebugLogging) {
            Log.w(tag, tr)
        }
    }

    fun e(tag: String, msg: String) {
        if (isDebugLogging) {
            Log.e(tag, msg)
        }
    }

    fun e(tag: String, msg: String, tr: Throwable) {
        if (isDebugLogging) {
            Log.e(tag, msg, tr)
        }
    }
}