package com.christian.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class CustomViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs), AnkoLogger {

    private var mDisallowIntercept: Boolean = false

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (mDisallowIntercept) {
            info { "false" }
            false
        } else {
            info { "true" }
            super.onInterceptTouchEvent(ev)
        }
    }

    fun setDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        mDisallowIntercept = disallowIntercept
    }
}