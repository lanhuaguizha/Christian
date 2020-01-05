package com.christian.nav

import androidx.recyclerview.widget.RecyclerView

abstract class HidingScrollListener(private val rv_nav: RecyclerView) : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
    private var scrolledDistance = 0 //移动的中距离
    private var controlsVisible = true //显示或隐藏

    override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {//移动总距离大于规定距离 并且是显示状态就隐藏
            onHide()
            controlsVisible = false
            scrolledDistance = 0//归零
        } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
            onShow()
            controlsVisible = true
            scrolledDistance = 0
        }

        if (controlsVisible && dy > 0 || !controlsVisible && dy < 0) { //显示状态向上滑动 或 隐藏状态向下滑动 总距离增加
            scrolledDistance += dy
        }

        if (!rv_nav.canScrollVertically(-1) && dy < 0) { // 并且是向上滑动
            onTop()
        }

        if (!rv_nav.canScrollVertically(1) && dy > 0) { // 并且是向下滑动
            onBottom()
        }
    }

    abstract fun onHide()
    abstract fun onShow()
    abstract fun onTop()
    abstract fun onBottom()
}