package com.christian.navdetail

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.christian.BottomNavigationViewBehaviorExt
import com.christian.R
import com.christian.data.Nav
import com.christian.nav.NavActivity
import com.christian.navitem.NavItemPresenter
import com.christian.view.ItemDecoration
import kotlinx.android.synthetic.main.nav_activity.*
import kotlinx.android.synthetic.main.sb_nav.*
import kotlinx.android.synthetic.main.tb_nav.*

/**
 * The nav details page contains all the logic of the nav page.
 */
class NavDetailActivity : NavActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        initTb(intent.extras.getString("title"))

    }

    // 不需要禁用滑动
    override fun initSbl() {
    }

    // 不需要添加背景色的同时不需要有elevation
    override fun initFl() {
    }

    override fun initRv(navs: List<Nav>) {

        rv_nav.addItemDecoration(ItemDecoration(resources.getDimension(R.dimen.activity_horizontal_margin_0).toInt()))

        rv_nav.layoutManager = LinearLayoutManager(this)

        adapter = NavItemPresenter(navs, false)
        rv_nav.adapter = adapter

        rv_nav.addOnScrollListener(object : HidingScrollListener() {

            override fun onHide() {

                fab_nav.hide()

            }

            override fun onShow() {

                fab_nav.show()

            }

            override fun onTop() {
            }

            override fun onBottom() {
            }
        })

    }

    override fun initFAB(drawableId: Int) {

        fab_nav.visibility = View.INVISIBLE

        // set FAB image
        fab_nav.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_keyboard_arrow_up_black_24dp, theme))
        fab_nav.show()

        // set FAB animate to hide's behavior

        // set listener
        fab_nav.setOnClickListener { scrollRvToTop() }

    }

    override fun initBv() {

        val params = CoordinatorLayout.LayoutParams(bv_nav.layoutParams)

        params.gravity = Gravity.BOTTOM

        params.behavior = BottomNavigationViewBehaviorExt(this, null)

        bv_nav.layoutParams = params

    }

    override fun initTb(title: String) {


        sl_nav.visibility = View.GONE

        /**
         * set up button
         */
        setSupportActionBar(toolbar_actionbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = title
        toolbar_actionbar.setNavigationOnClickListener { finish() }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_share -> {
                true
            }
            R.id.menu_download -> {
                true
            }
            R.id.menu_collection -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun runLayoutAnimation(recyclerView: RecyclerView) {
        recyclerView.adapter.notifyDataSetChanged()
    }

}