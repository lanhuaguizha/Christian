package com.christian.navdetail

import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.christian.R
import com.christian.data.Nav
import com.christian.nav.NavActivity
import com.christian.navitem.NavItemPresenter
import com.christian.view.ItemDecoration
import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur
import kotlinx.android.synthetic.main.nav_activity.*
import kotlinx.android.synthetic.main.sb_nav.*
import kotlinx.android.synthetic.main.tb_nav.*
import org.jetbrains.anko.info

private fun NavActivity.initTbE(title: String) {
    sl_nav.visibility = View.GONE

    /**
     * set up button
     */
    setSupportActionBar(toolbar_actionbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.title = title
    toolbar_actionbar.setNavigationOnClickListener { finish() }
}

private fun NavActivity.initSrlForbidden() {
    srl_nav.isEnabled = false
}

// 不需要添加背景色的同时不需要有elevation
private fun NavActivity.initFlE() {
    srl_nav.background = ResourcesCompat.getDrawable(resources, R.color.default_background_nav, theme)
}

private fun NavActivity.initBvE() {
    //set background, if your root layout doesn't have one
    val windowBackground = window.decorView.background
    val radius = 25f
    bv_nav.setupWith(cl_nav)
            .windowBackground(windowBackground)
            .blurAlgorithm(SupportRenderScriptBlur(this))
            .blurRadius(radius)
            .setHasFixedTransformationMatrix(true)
    // set behavior
    val params = CoordinatorLayout.LayoutParams(bv_nav.layoutParams)
    params.gravity = Gravity.BOTTOM
    params.behavior = NavActivity.BottomNavigationViewBehaviorExt(this, null)
    bv_nav.layoutParams = params
}

private fun NavActivity.initFABE() {
    fab_nav.visibility = View.INVISIBLE

    // set FAB image
    fab_nav.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_upward_black_24dp, theme))
    fab_nav.show()

    // set FAB animate to hide's behavior
    // set listener
    fab_nav.setOnClickListener { scrollRvToTop() }
}

private fun NavActivity.startNavE(navId: String) {
    presenter.insertNav(navId)
}

private fun runLayoutAnimationE(recyclerView: RecyclerView) {
    recyclerView.adapter.notifyDataSetChanged()
}

/**
 * The nav details page contains all the logic of the nav page.
 */
class NavDetailActivity : NavActivity(), NavDetailContract.View {

    override fun showCv() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideCv() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initView(navs: List<Nav>) {
        info { "navs$navs" }
        initAbl()
        initTbE(intent.extras.getString("title"))
        initSrlForbidden()
        initFlE()
        initRv(navs)
        initBvE()
        startNavE("0")
    }

    override fun initRv(navs: List<Nav>) {
        adapter = NavItemPresenter(navs, false)
        rv_nav.addItemDecoration(ItemDecoration(resources.getDimension(R.dimen.activity_horizontal_margin_0).toInt()))
        rv_nav.layoutManager = LinearLayoutManager(this)
        rv_nav.adapter = adapter
        rv_nav.addOnScrollListener(object : NavActivity.HidingScrollListener() {
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

    override fun invalidateRv(navs: List<Nav>) {
        adapter.navs = navs
        runLayoutAnimationE(rv_nav)
        fab_nav.post {
            initFABE()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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
}