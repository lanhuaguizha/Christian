package com.christian.swipe

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.christian.swipe.SwipeBackHelper

/**
 * The activity base class, swipe back logic here.
 */
abstract class SwipeBackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SwipeBackHelper.onCreate(this)
        initSwipeBack()
    }

    private fun initSwipeBack() {
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
                .setSwipeSensitivity(0.5f)
                .setSwipeRelateEnable(false)
                .setSwipeRelateOffset(300)
                .setSwipeEdgePercent(1f)
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        SwipeBackHelper.onPostCreate(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        SwipeBackHelper.onDestroy(this)
    }
}
