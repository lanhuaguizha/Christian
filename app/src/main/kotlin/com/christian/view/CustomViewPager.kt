package com.christian.view

import android.content.Context
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

class CustomViewPager(context: Context, attrs: AttributeSet?) : androidx.viewpager.widget.ViewPager(context, attrs), AnkoLogger {

    // Remove switching animation
//    override fun setCurrentItem(item: Int) {
//        super.setCurrentItem(item, false)
//    }

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