package com.christian.navdetail

import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.customview.widget.ViewDragHelper.INVALID_POINTER
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.christian.R
import com.christian.nav.*
import com.google.android.material.tabs.TabLayout.MODE_FIXED
import kotlinx.android.synthetic.main.gospel_detail_fragment.*
import kotlinx.android.synthetic.main.nav_activity.*
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
    fab_nav.setOnClickListener { scrollRvToTop(navDetailActivity) }
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

//    override fun initView(bean: List<Gospel>) {
//        debug { "bean$bean" }
//        initAbl()
//        initTbWithTitle(intent.extras.getString("title"))
//        initSrlForbidden()
//        initFlE()
//        initRv(bean)
//        initBvWithReviews()
//        startNavE("0")
//    }

//    override fun initView(navFragmentList: ArrayList<NavFragment>) {
//        super.initView(navFragmentList)
//        initVp(navFragmentList)
//    }

    override fun initTb() {
        initTbWithTitle(intent?.extras?.getString(toolbarTitle) ?: nullString)
        val tabTitleList = arrayListOf(
                getString(R.string._Hot_Review),
                getString(R.string._Newest_Review)
        )
        for (tabTitle in tabTitleList) {
            tl_nav.newTab().setText(tabTitle).let { tl_nav.addTab(it) }
        }
        tl_nav.tabMode = MODE_FIXED
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

    private lateinit var navDetailFragmentPagerAdapter: NavDetailFragmentPagerAdapter

    override fun initVp(navFragmentList: ArrayList<NavFragment>) {
        navDetailFragmentPagerAdapter = NavDetailFragmentPagerAdapter(supportFragmentManager)
        vp_nav.adapter = navDetailFragmentPagerAdapter
        vp_nav.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                debug { "initVp: onPageScrolled, position$position, positionOffset$positionOffset, positionOffsetPixels$positionOffsetPixels" }
                pagePosition = position
                pagePositionOffset = positionOffset
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageSelected(position: Int) {
                setTabLayoutExpanded(this@NavDetailActivity, position)
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

    //    override fun initRv(bean: List<Gospel>) {
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

    //    override fun invalidateRv(bean: List<Gospel>) {
//        adapter.bean = bean
//        runLayoutAnimationE(rv_nav)
//        fab_nav.post {
//            initFABGospelDetail()
//        }
//    }
    override fun initFab() {
//        initFABGospelDetail(this@NavDetailActivity, navFragment.rv_nav)
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
//            R.id.menu_collection -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Handling memory leaks
        val navPresenter = presenter as NavPresenter
        navPresenter.navFragmentList.clear()

        menu?.close()
    }

    class NavDetailFragmentPagerAdapter(fm: FragmentManager) : NavActivity.NavFragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> {
                    val gospelDetailFragment = NavDetailFragment()
                    gospelDetailFragment.navId = 31
                    return gospelDetailFragment
                }
//                1 -> {
//                    val gospelReviewFragment = GospelReviewFragment()
//                    gospelReviewFragment.navId = position + 31
//                    return gospelReviewFragment
//                }
            }
            return super.getItem(position)
        }

        override fun getCount(): Int {
            return 1
        }

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            when (position) {
                0 -> {
                    currentFragment = `object` as NavDetailFragment
                }
//                1 -> {
//                    currentFragment = `object` as GospelReviewFragment
//                }
            }
            super.setPrimaryItem(container, position, `object`)
        }
    }

    override fun scrollRvToTop(navActivity: NavActivity) {
        if (::navDetailFragmentPagerAdapter.isInitialized)
            navDetailFragmentPagerAdapter.currentFragment.rv_gospel_detail.smoothScrollToPosition(0) // 为了滚到顶
        navActivity.abl_nav.setExpanded(true, true)
    }
}
