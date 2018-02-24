package com.christian.nav

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.LinearLayoutManager
import com.christian.Injection
import com.christian.R
import com.christian.adapter.ContentItemViewAdapter
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
class ActNav : ActBase(), NavContract.View {

    /**
     * presenter will be initialized when the NavPresenter is initialized
     */
    override lateinit var presenter: NavContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_nav)

        NavPresenter("", Injection.provideNavsRepository(applicationContext), this)
        presenter.start()
    }

    override fun initView() {

        initSwipe()

        initRv()

        initBnv()
    }

    override fun setupToolbar(title: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setupSearchbar(searchHint: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startSwipeRefreshLayout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgressBar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgressBar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showRecyclerView(nav: Nav) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideRecyclerView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showFloatingActionButton() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideFloatingActionButton() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun activeFloatingActionButton() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun scrollToPosition() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initSwipe() {

        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true)
    }

    private fun initRv() {

        rv_nav.addItemDecoration(ItemDecoration(resources.getDimension(R.dimen.search_margin_horizontal).toInt()))
        rv_nav.layoutManager = LinearLayoutManager(this)
    }

    private fun initBnv() {

        BottomNavigationViewHelper.disableShiftMode(bnv_nav)

        setBnvListener()
    }

    private fun setBnvListener() {

        bnv_nav.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    showFab(false)
                    presenter.getData(item.itemId) // ToDo? So wordy
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_gospel -> {
                    showFab(false)
                    presenter.getData(item.itemId)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_chat -> {
                    showFab(false)
                    presenter.getData(item.itemId)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_me -> {
                    showFab(true)
                    presenter.getData(item.itemId)
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

    private fun loadView(dataSet: Array<String>) {

        val adapter = ContentItemViewAdapter(dataSet)
        rv_nav.adapter = adapter
    }
}
