package com.christian.nav

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
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
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.isVisible
import com.christian.Injection
import com.christian.R
import com.christian.data.Nav
import com.christian.swipe.SwipeBackActivity
import com.christian.swipe.SwipeBackHelper
import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur
import kotlinx.android.synthetic.main.nav_activity.*
import kotlinx.android.synthetic.main.sb_nav.*
import kotlinx.android.synthetic.main.search_bar_expanded.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.info
import org.jetbrains.anko.px2dip

/**
 * Home, Gospel, Communication, Me 4 TAB main entrance activity.
 * implementation of NavContract.View.
 */
open class NavActivity : SwipeBackActivity(), NavContract.INavActivity {

    companion object {
        var HIDE_THRESHOLD = 0 //移动多少距离后显示隐藏
    }

    /**
     * presenter will be initialized when the NavPresenter is initialized
     */
    override lateinit var presenter: NavContract.IPresenter
    private lateinit var navFragment: NavFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_activity)
        NavPresenter("0", Injection.provideNavsRepository(applicationContext), this)
        presenter.init(position = 0)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            abl_nav.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                Log.i("systemui", "dd" + verticalOffset + "cc" + appBarLayout.height + "ee" + px2dip(abl_nav.elevation.toInt()) + "ff" + abl_nav.elevation)
                if (-verticalOffset == appBarLayout.height) {
                    abl_nav.elevation = 0f
                } else {
                    abl_nav.elevation = dip(4).toFloat()
                    Log.i("systemui", "ee" + px2dip(abl_nav.elevation.toInt()) + "ff" + abl_nav.elevation)
                }
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
        vp_nav.offscreenPageLimit = 1

        vp_nav.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                navFragment = navFragments[position]
//                presenter.init(false, position)
                info { "onPageSelected" }
                if (bnv_nav.menu.getItem(position).isCheckable) {
                    info { "bnv_nav.menu.getItem(position).isCheckable${bnv_nav.menu.getItem(position).isCheckable}" }
                    bnv_nav.menu.getItem(position).isChecked = true
                }
                presenter.insertNav(position.toString(), navFragment = navFragment)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
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

    private fun initFAB() {
        fab_nav.visibility = View.GONE
        fab_nav.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_edit_black_24dp, theme))
        fab_nav.show()
        fab_nav.setOnClickListener(null)
        if (!fab_nav.isVisible) {
            info { "initFAB View.VISIBLE" }
            fab_nav.visibility = View.VISIBLE
        }
    }

    private fun initBnv(navFragments: List<NavFragment>) {
        disableShiftMode(bnv_nav)

//        bnv_nav.menu.getItem(0).isChecked = true //no effect, why?
        presenter.insertNav("0", navFragment = navFragments[0])

        bnv_nav.setOnNavigationItemSelectedListener {
//            vp_nav.currentItem = presenter.generateNavId(it.itemId).toInt()
            val position = presenter.generateNavId(it.itemId).toInt()
            vp_nav.currentItem = position
            true
        }
//        bnv_nav.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.navigation_home -> {
//                    vp_nav.currentItem = 0
//                    return@OnNavigationItemSelectedListener true
//                }
//                R.id.navigation_gospel -> {
//                    vp_nav.currentItem = 1
//                    return@OnNavigationItemSelectedListener true
//                }
//                R.id.navigation_chat -> {
//                    vp_nav.currentItem = 2
//                    return@OnNavigationItemSelectedListener true
//                }
//                R.id.navigation_me -> {
//                    vp_nav.currentItem = 3
//                    return@OnNavigationItemSelectedListener true
//                }
//            }
//            presenter.insertNav(presenter.generateNavId(item.itemId))
//            false
//        })
        bnv_nav.setOnNavigationItemReselectedListener { scrollRvToTop() }
    }

    override fun initSb(searchHint: String) {
    }


    override fun startPb() {
        pb_nav.visibility = View.VISIBLE
    }

    override fun stopPb() {
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

    override fun hideFloatingActionButton() {
    }

    override fun activeFloatingActionButton() {
    }

    fun scrollRvToTop() {
//        rv_nav.smoothScrollToPosition(dip(-1)) // 为了滚到顶
        abl_nav.setExpanded(true, true)
    }

    // --Start android tv
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        Log.i("keyCode", "keyCode$keyCode")
        val view = (bnv_nav[0] as ViewGroup).getChildAt(0)
        Log.i("keyCode", "view.isFocused${view.isFocused}")
        val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        if (uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    return if (!view.isFocused) {
                        view.requestFocus()
                        Log.i("keyCode", "view.hasFocus${view.hasFocus()}")
                        true
                    } else {
                        super.onKeyDown(keyCode, event)
                    }
                }
            }
        }

        return super.onKeyDown(keyCode, event)
    }
    // --End

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