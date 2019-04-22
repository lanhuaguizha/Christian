package com.christian.navdetail

import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.customview.widget.ViewDragHelper.INVALID_POINTER
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.christian.ChristianApplication
import com.christian.R
import com.christian.nav.*
import kotlinx.android.synthetic.main.nav_activity.*
import kotlinx.android.synthetic.main.nav_fragment.*
import kotlinx.android.synthetic.main.sb_nav.*
import org.jetbrains.anko.debug
import java.util.*

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
//    (presenter as NavPresenter).navFragmentList[mPosition].srl_nav.isEnabled = false
}

//// 不需要添加背景色的同时不需要有elevation
//private fun NavActivity.initFlE() {
//    srl_nav.background = ResourcesCompat.getDrawable(resources, R.color.default_background_nav, theme)
//}

private fun NavActivity.initFABGospelDetail(navDetailActivity: NavDetailActivity, rv_nav: RecyclerView) {
    fab_nav.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_upward_black_24dp, theme))
    fab_nav.show()
    fab_nav.setOnClickListener { scrollRvToTop(navDetailActivity, rv_nav) }
}

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

    companion object {
        const val KEY_GOSPEL_ID = "key_gospel_id" // gospels集合ID
    }

//    override fun initView(bean: List<Gospels>) {
//        debug { "bean$bean" }
//        initAbl()
//        initTbWithTitle(intent.extras.getString("title"))
//        initSrlForbidden()
//        initFlE()
//        initRv(bean)
//        initBvWithReviews()
//        startNavE("0")
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // determine size of view pager
        presenter.init(whichActivity = GOSPEL_DETAIL_ACTIVITY)
    }

    override fun initView(navFragmentList: ArrayList<NavFragment>) {
        super.initView(navFragmentList)
        initVp(navFragmentList)
    }

    override fun initTb() {
        initTbWithTitle(intent?.extras?.getString(toolbarTitle) ?: nullString)
    }

    override fun initBv() {
        super.initBv()
        val params = androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(bv_nav.layoutParams)
        params.gravity = Gravity.BOTTOM
        params.behavior = BottomNavigationViewBehaviorDetail(this, null)
        bv_nav.layoutParams = params
    }

    private var pagePosition: Int = 0

    private var pagePositionOffset: Float = 0f

    override fun initVp(navFragmentList: ArrayList<NavFragment>) {
        super.initVp(navFragmentList)
        vp_nav.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                debug { "initVp: onPageScrolled, position$position, positionOffset$positionOffset, positionOffsetPixels$positionOffsetPixels" }
                pagePosition = position
                pagePositionOffset = positionOffset
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageSelected(position: Int) {
                debug { "initVp: onPageSelected$position" }
            }

        })
    }

    private var lastX: Float = 0f
    var isMovingRight: Boolean = true // true不会崩溃，进入nav detail左滑的时候

    private var mActivePointerId: Int = INVALID_POINTER

    // used for enable back gesture
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = ev.x
                mActivePointerId = ev.getPointerId(0)
            }
            MotionEvent.ACTION_MOVE -> {
                try {
                    isMovingRight = ev.getX(ev.findPointerIndex(mActivePointerId)) - lastX > 0
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                debug { "ACTION_MOVE" }
            }
            MotionEvent.ACTION_UP -> {
                mActivePointerId = INVALID_POINTER
                debug { "ACTION_UP" }
            }
        }
        debug { "dispatchTouchEvent: isMovingRight$isMovingRight, pagePosition$pagePosition, pagePositionOffset$pagePositionOffset" }
        enableSwipeBack(pagePosition, pagePositionOffset, this@NavDetailActivity)
        return super.dispatchTouchEvent(ev)
    }

    //    override fun initRv(bean: List<Gospels>) {
//        adapter = NavItemPresenter(bean, false)
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

    //    override fun invalidateRv(bean: List<Gospels>) {
//        adapter.bean = bean
//        runLayoutAnimationE(rv_nav)
//        fab_nav.post {
//            initFABGospelDetail()
//        }
//    }
    override fun initFab() {
        initFABGospelDetail(this@NavDetailActivity, navFragment.rv_nav)
    }

    var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
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

    override fun onDestroy() {
        super.onDestroy()
        // Handling memory leaks
        val navPresenter = presenter as NavPresenter
        navPresenter.navFragmentList.clear()

        menu?.close()

        val refWatcher = ChristianApplication.getRefWatcher(this)
        refWatcher.watch(this)
    }

}