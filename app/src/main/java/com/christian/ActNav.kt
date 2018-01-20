package com.christian

import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.christian.adapter.ContentItemViewAdapter
import com.christian.base.ActBase
import com.christian.helper.BottomNavigationViewHelper
import com.christian.swipe.SwipeBackHelper
import com.christian.view.ItemDecoration
import kotlinx.android.synthetic.main.act_nav.*
import kotlinx.android.synthetic.main.content_fragment.*

/**
 * 主页、福音、交流、我的4个tab的主入口activity.
 */
class ActNav : ActBase() {


    override fun getLayoutId(): Int {

        return R.layout.act_nav
    }


    override fun initView() {

        super.initView()

        initSwipe()

        initCl()

        initAbl()

        initTb()

        initRv()

        initFab()

        initBnv()
    }

    private fun initSwipe() {

        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true)
    }

    private fun initCl() {

    }

    private fun initAbl() {

    }

    private fun initTb() {

    }

    private fun initRv() {

        rv_nav.addItemDecoration(ItemDecoration(resources.getDimension(R.dimen.search_margin_horizontal).toInt()))
        rv_nav.layoutManager = LinearLayoutManager(this)
    }

    private fun initFab() {

        fab_nav.setOnClickListener({ scrollRvToTop() })
    }

    private fun initBnv() {

        BottomNavigationViewHelper.disableShiftMode(bnv_nav)
    }

    override fun initListener() {

        super.initListener()

        setBnvListener()
    }

    private fun setBnvListener() {

        bnv_nav.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    showFab(false)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_gospel -> {
                    showFab(false)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_chat -> {
                    showFab(false)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_me -> {
                    showFab(true)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })

        bnv_nav.setOnNavigationItemReselectedListener { scrollRvToTop() }
    }

    private fun showFab(b: Boolean) {

        if (b) fab_nav.show() else fab_nav.hide()
    }

    private fun scrollRvToTop() {

        rv_nav.smoothScrollToPosition(-100) // 为了滚到顶
        abl_nav.setExpanded(true, true)
    }

    override fun loadData() {

        val dataSet: Array<String> = arrayOf("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
        for (i in 0 until 30) {
            dataSet[i] = getString(R.string.next_week) + i
        }

        loadView(dataSet)
    }

    private fun loadView(dataSet: Array<String>) {

        val adapter = ContentItemViewAdapter(dataSet)
        rv_nav.adapter = adapter
    }

}
