package com.christian.nav

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import com.christian.Injection
import com.christian.R
import com.christian.data.Nav
import com.christian.swipe.SwipeBackActivity
import com.christian.swipe.SwipeBackHelper
import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur
import kotlinx.android.synthetic.main.nav_activity.*
import kotlinx.android.synthetic.main.nav_activity.view.*
import kotlinx.android.synthetic.main.nav_fragment.*
import kotlinx.android.synthetic.main.sb_nav.*
import kotlinx.android.synthetic.main.search_bar_expanded.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.info
import kotlin.math.abs

/**
 * Home, Gospel, Communication, Me 4 TAB main entrance activity.
 * implementation of NavContract.View.
 */
open class NavActivity : SwipeBackActivity(), NavContract.INavActivity {

    /**
     * presenter will be initialized when the NavPresenter is initialized
     */
    override lateinit var presenter: NavContract.IPresenter
    private lateinit var navFragment: NavFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_activity)
        NavPresenter("0", Injection.provideNavsRepository(applicationContext), this)
        presenter.init()
        info { "look at init times" }
    }

    override fun initView(navFragments: List<NavFragment>, navs: List<Nav>) {
        initSbl()
        initAbl()
        initTb()
        initSb()
        initVp(navFragments)
        initBv()
        initBnv(navFragments)
    }

    override fun deinitView() {
    }

    private fun initSbl() {
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true)
    }

    private fun initAbl() {
        abl_nav.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            info { "verticalOffset$verticalOffset" }
            if (-verticalOffset == appBarLayout.height) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    abl_nav.elevation = 0f
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    abl_nav.elevation = dip(4).toFloat()
                }
            }

            when (abs(verticalOffset) - appBarLayout.height / 2 > 0) {
                true -> (presenter as NavPresenter).showAblAndScrollRv()
                false -> (presenter as NavPresenter).hideAblAndScrollRv()
            }
        }
    }

    private fun initTb() {
        sl_nav.visibility = View.VISIBLE
    }

    private fun initSb() {
        sl_nav.setOnClickListener { slExpand() }
        search_magnifying_glass.setOnClickListener { slExpand() }
        search_box_start_search.setOnClickListener { slExpand() }
        search_back_button.setOnClickListener { slCollapse() }
    }

    private fun initVp(navFragments: List<NavFragment>) {
        val navFragmentPagerAdapter = NavFragmentPagerAdapter(navFragments, supportFragmentManager)

        vp_nav.adapter = navFragmentPagerAdapter

        vp_nav.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                selectNavFragment(navFragments, position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }

    // ToDo 不应该每次滑动都请求数据去创建数据库
    private fun selectNavFragment(navFragments: List<NavFragment>, position: Int) {
        navFragment = navFragments[position]
        navFragment.navId = position
        info { "onPageSelected" }
        if (bnv_nav.menu.getItem(position).isCheckable) {
            info { "bnv_nav.menu.getItem(position).isCheckable${bnv_nav.menu.getItem(position).isCheckable}" }
            bnv_nav.menu.getItem(position).isChecked = true
        }
        presenter.createNav(position.toString(), navFragment = navFragment)
    }

    private fun initBv() {
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
        params.behavior = BottomNavigationViewBehavior(this, null)
        bv_nav.layoutParams = params
    }

    fun initFAB() {
        fab_nav.visibility = View.GONE
        fab_nav.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_edit_black_24dp, theme))
        fab_nav.show()
        fab_nav.setOnClickListener(null)
        if (fab_nav.visibility != View.VISIBLE) {
            info { "initFAB View.VISIBLE" }
            fab_nav.visibility = View.VISIBLE
        }
    }

    private fun initBnv(navFragments: List<NavFragment>) {
        disableShiftMode(bnv_nav)

        bnv_nav.postDelayed({ selectNavFragment(navFragments, initFragmentIndex) }, 2000)

        bnv_nav.bnv_nav.setOnNavigationItemSelectedListener {
            vp_nav.currentItem = (presenter as NavPresenter).generateNavId(it.itemId).toInt()
            true
        }

        bnv_nav.setOnNavigationItemReselectedListener {
            navFragment.rv_nav.smoothScrollToPosition(dip(0)) // 为了滚到顶
            abl_nav.setExpanded(true, true)
        }
    }

    override fun initSb(searchHint: String) {
    }


    override fun showPb() {
        pb_nav.visibility = View.VISIBLE
    }

    override fun hidePb() {
        pb_nav.visibility = View.GONE
    }


    override fun showFab(drawableId: Int) {

//        if (!fab_nav.isGone) {
//            Log.i("fab", "hide")
//            fab_nav.hide()
//        }

//        fab_nav.postDelayed({
//            fab_nav.setImageDrawable(ResourcesCompat.getDrawable(resources, drawableId, theme))
//            fab_nav.show()
//            Log.i("fab", "show")
//        }, SHORTER_DURATION)

//        when (drawableId) {
//
//            R.drawable.ic_edit_black_24dp -> {
//
//                fab_nav.setOnClickListener(null)
//
//            }
//
//            R.drawable.ic_keyboard_arrow_up_black_24dp -> {
//
//                fab_nav.setOnClickListener { scrollRvToTop() }
//
//            }
//
//        }

    }

    override fun hideFab() {
    }

    private fun slExpand() {
        if (!sl_nav.isExpanded) {
            sl_nav.expand(true, true)
        }
    }

    private fun slCollapse() {
        if (sl_nav.isExpanded) {
            sl_nav.collapse(true)
        }
        if (sl_nav.isFadedOut) {
            sl_nav.fadeIn()
        }
    }

    /**
     * Immersive reading, swipe hidden.
     */
    class BottomNavigationViewBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<View>(context, attrs) {
        override fun onLayoutChild(parent: CoordinatorLayout?, child: View?, layoutDirection: Int): Boolean {
            (child?.layoutParams as CoordinatorLayout.LayoutParams).topMargin = parent?.measuredHeight?.minus(child.measuredHeight) ?: 0
            return super.onLayoutChild(parent, child, layoutDirection)
        }

        override fun layoutDependsOn(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
            return dependency is AppBarLayout
        }

        override fun onDependentViewChanged(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
            val top = ((dependency?.layoutParams as CoordinatorLayout.LayoutParams).behavior as AppBarLayout.Behavior).topAndBottomOffset
            Log.i("dd", top.toString())
            //因为BottomNavigation的滑动与ToolBar是反向的，所以取-top值
            child?.translationY = (-top).toFloat()
            return false
        }
    }

    /**
     * Locate nav detail FAB
     */
    class BottomNavigationViewBehaviorExt(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<View>(context, attrs) {
        override fun onLayoutChild(parent: CoordinatorLayout?, child: View?, layoutDirection: Int): Boolean {
            (child?.layoutParams as CoordinatorLayout.LayoutParams).topMargin = parent?.measuredHeight?.minus(child.measuredHeight) ?: 0
            return super.onLayoutChild(parent, child, layoutDirection)
        }

        override fun layoutDependsOn(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
            return dependency is AppBarLayout
        }

        override fun onDependentViewChanged(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
            child?.translationY = 2000f
            return false
        }
    }

}

@SuppressLint("RestrictedApi")
fun disableShiftMode(view: BottomNavigationView) {
    val menuView = view.getChildAt(0) as BottomNavigationMenuView
    try {
        val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
        shiftingMode.isAccessible = true
        shiftingMode.setBoolean(menuView, false)
        shiftingMode.isAccessible = false
        for (i in 0 until menuView.childCount) {
            val item = menuView.getChildAt(i) as BottomNavigationItemView
            item.setShiftingMode(false)
            item.setChecked(item.itemData.isChecked)
        }
    } catch (e: NoSuchFieldException) {
        error(e)
    } catch (e: IllegalAccessException) {
        error(e)
    }
}