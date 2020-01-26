package com.christian.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.TextView

class CustomTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    // ripple will show when this view does not response touch event
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return false
    }
}