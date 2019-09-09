package com.christian.multitype

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CustomRecyclerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    override fun requestChildFocus(child: View?, focused: View?) {
    }
}