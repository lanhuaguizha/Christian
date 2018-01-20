package com.christian.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.christian.swipe.SwipeBackHelper

/**
 * activity基类，封装initView、initListener、loadData、loadView..方法，暂时能想到的只有那么多。
 */
abstract class ActBase : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        SwipeBackHelper.onCreate(this)

        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initView()
    }

    abstract fun getLayoutId(): Int

    open fun initView() {

        initSwipeBack()

        initListener()

        loadData()
    }

    private fun initSwipeBack() {

        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
                .setSwipeSensitivity(0.5f)
                .setSwipeRelateEnable(false)
                .setSwipeRelateOffset(300)
                .setSwipeEdgePercent(1f)
    }

    open fun initListener() {

    }

    open fun loadData() {

        loadView()
    }

    open fun loadView() {

        dismissLoading()

    }

    private fun dismissLoading() {


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
