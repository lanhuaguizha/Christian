package com.christian.nav

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.christian.Injection
import com.christian.R
import com.christian.adapter.DetailAdapter
import com.christian.base.ActBase
import com.christian.data.Nav
import com.christian.helper.BottomNavigationViewHelper
import com.christian.swipe.SwipeBackHelper
import com.christian.view.ItemDecoration
import kotlinx.android.synthetic.main.act_nav.*

/**
 * Home, Gospel, Communication, Me 4 TAB main entrance activity.
 * implementation of NavContract.View.
 */
class NavActivity : ActBase(), NavContract.View {


    /**
     * presenter will be initialized when the NavPresenter is initialized
     */
    override lateinit var presenter: NavContract.Presenter

    private lateinit var adapter: DetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.act_nav)

        NavPresenter("", Injection.provideNavsRepository(applicationContext), this)
        presenter.start()

    }

    private fun initSwipe() {

        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true)

    }

    private fun initPb() {

        showProgressBar()

    }

    private fun initRv(navs: List<Nav>) {

        rv_nav.addItemDecoration(ItemDecoration(resources.getDimension(R.dimen.search_margin_horizontal).toInt()))

        rv_nav.layoutManager = LinearLayoutManager(this)


        adapter = DetailAdapter(navs)
        rv_nav.adapter = adapter

    }

    private fun initBnv() {

        BottomNavigationViewHelper.disableShiftMode(bnv_nav)

        setBnvListener()
    }

    override fun initView(navs: List<Nav>) {

        initSwipe()

        initPb()

        initRv(navs)

        initBnv()

        startNav(false, 0)

    }

    override fun setupToolbar(title: String) {
    }

    override fun setupSearchbar(searchHint: String) {
    }

    override fun startSwipeRefreshLayout() {
    }

    override fun showProgressBar() {

        pb_nav.visibility = View.VISIBLE

    }

    override fun hideProgressBar() {

        pb_nav.visibility = View.GONE

    }

    override fun invalidateRv(navs: List<Nav>) {

        adapter.setmDataSet(navs)

        adapter.notifyDataSetChanged()

    }

    override fun restoreRvPos() {
    }

    override fun showFloatingActionButton() {
    }

    override fun hideFloatingActionButton() {
    }

    override fun activeFloatingActionButton() {
    }

    private fun setBnvListener() {

        bnv_nav.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.navigation_home -> {

                    startNav(false, 0)

                    return@OnNavigationItemSelectedListener true

                }

                R.id.navigation_gospel -> {

                    startNav(false, 1)

                    return@OnNavigationItemSelectedListener true

                }

                R.id.navigation_chat -> {

                    startNav(false, 2)

                    return@OnNavigationItemSelectedListener true

                }

                R.id.navigation_me -> {

                    startNav(true, 3)

                    return@OnNavigationItemSelectedListener true

                }
            }

            false

        })

        bnv_nav.setOnNavigationItemReselectedListener { scrollRvToTop() }

    }

    /**
     * @param fabVisibility true presents showing floating action button, false otherwise.
     * @param navId quest parameter of nav page to get navs lists
     */
    private fun startNav(fabVisibility: Boolean, navId: Int) {

        if (fabVisibility) fab_nav.show() else fab_nav.hide()

        if (navId == 0) {
            presenter.insertNav(navId)
        }

    }

    private fun scrollRvToTop() {

        rv_nav.smoothScrollToPosition(-100) // 为了滚到顶

        abl_nav.setExpanded(true, true)

    }

}
