package com.christian.swipe

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.christian.ChristianApplication


/**
 * The activity base class, swipe back logic here.
 */
abstract class SwipeBackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SwipeBackHelper.onCreate(this)
        initSwipeBack()

        val leakThread = LeakThread()
        leakThread.start()
    }

    class LeakThread : Thread() {
        override fun run() {
            super.run()
            try {
                Thread.sleep(6 * 60 * 1000)
            } catch (e: Exception) {
            }
        }
    }

    private fun initSwipeBack() {
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
                .setSwipeSensitivity(0.5f)
                .setSwipeRelateEnable(false)
                .setSwipeRelateOffset(300)
                .setSwipeEdgePercent(1f)
                .setClosePercent(0.2f)
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        SwipeBackHelper.onPostCreate(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        SwipeBackHelper.onDestroy(this)

        val refWatcher = ChristianApplication.getRefWatcher(this)
        refWatcher.watch(this)
    }

}
