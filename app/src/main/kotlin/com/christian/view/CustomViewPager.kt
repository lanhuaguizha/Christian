package com.christian.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

class CustomViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs), AnkoLogger {

    private var mDisallowIntercept: Boolean = false

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (mDisallowIntercept) {
            debug { "false" }
            false
        } else {
            debug { "true" }
            super.onInterceptTouchEvent(ev)
        }
    }

    fun setDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        mDisallowIntercept = disallowIntercept
    }
}