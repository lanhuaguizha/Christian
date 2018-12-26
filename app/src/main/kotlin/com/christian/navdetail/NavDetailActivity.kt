package com.christian.navdetail

import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.christian.R
import com.christian.nav.*
import com.christian.swipe.SwipeBackHelper
import kotlinx.android.synthetic.main.nav_activity.*
import kotlinx.android.synthetic.main.nav_fragment.*
import kotlinx.android.synthetic.main.sb_nav.*

private fun NavActivity.initTbWithTitle(title: String) {
    sb_nav.visibility = View.GONE

    /**
     * set up button
     */
    setSupportActionBar(tb_nav)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.title = title
    tb_nav.setNavigationOnClickListener { finish() }
}

private fun NavActivity.initSrlForbidden() {
    (presenter as NavPresenter).navFragmentList[mPosition].srl_nav.isEnabled = false
}

//// 不需要添加背景色的同时不需要有elevation
//private fun NavActivity.initFlE() {
//    srl_nav.background = ResourcesCompat.getDrawable(resources, R.color.default_background_nav, theme)
//}

//private fun NavActivity.initFABE() {
//    fab_nav.visibility = View.INVISIBLE
//
//    // set FAB image
//    fab_nav.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_upward_black_24dp, theme))
//    fab_nav.show()
//
//    // set FAB animate to hide's behavior
//    // set listener
//    fab_nav.setOnClickListener { scrollRvToTop() }
//}

//private fun NavActivity.startNavE(navId: String) {
//    presenter.createNav(navId)
//}

//private fun runLayoutAnimationE(recyclerView: RecyclerView) {
//    recyclerView.adapter.notifyDataSetChanged()
//}

/**
 * The nav details page contains all the logic of the nav page.
 */
class NavDetailActivity : NavActivity() {

//    override fun initView(navs: List<NavBean>) {
//        info { "navs$navs" }
//        initAbl()
//        initTbWithTitle(intent.extras.getString("title"))
//        initSrlForbidden()
//        initFlE()
//        initRv(navs)
//        initBvWithReviews()
//        startNavE("0")
//    }

    override fun initSbl() {
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
                .setDisallowInterceptTouchEvent(false)
    }

    override fun initTb() {
        initTbWithTitle(intent?.extras?.getString(toolbarTitle) ?: nullString)
    }

    override fun initBv() {
        super.initBv()
        val params = CoordinatorLayout.LayoutParams(bv_nav.layoutParams)
        params.gravity = Gravity.BOTTOM
        params.behavior = BottomNavigationViewBehaviorDetail(this, null)
        bv_nav.layoutParams = params
    }

    override fun initVp(navFragments: List<NavFragment>) {
        super.initVp(navFragments)
        vp_nav.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    SwipeBackHelper.getCurrentPage(this@NavDetailActivity)
                            .setDisallowInterceptTouchEvent(false)
                } else {
                    SwipeBackHelper.getCurrentPage(this@NavDetailActivity)
                            .setDisallowInterceptTouchEvent(true)
                }
            }

        })
    }


    //    override fun initRv(navs: List<NavBean>) {
//        adapter = NavItemPresenter(navs, false)
//        rv_nav.addItemDecoration(ItemDecoration(resources.getDimension(R.dimen.activity_horizontal_margin_0).toInt()))
//        rv_nav.layoutManager = LinearLayoutManager(this)
//        rv_nav.adapter = adapter
//        rv_nav.addOnScrollListener(object : NavActivity.HidingScrollListener() {
//            override fun onHide() {
//                fab_nav.hide()
//            }
//
//            override fun onShow() {
//                fab_nav.show()
//            }
//
//            override fun onTop() {
//            }
//
//            override fun onBottom() {
//            }
//        })
//    }

//    override fun invalidateRv(navs: List<NavBean>) {
//        adapter.navs = navs
//        runLayoutAnimationE(rv_nav)
//        fab_nav.post {
//            initFABE()
//        }
//    }

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
