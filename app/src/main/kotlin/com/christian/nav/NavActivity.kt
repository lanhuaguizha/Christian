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
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.get
import com.christian.Injection
import com.christian.R
import com.christian.data.Nav
import com.christian.navitem.NavItemPresenter
import com.christian.swipe.SwipeBackActivity
import com.christian.swipe.SwipeBackHelper
import com.christian.view.ItemDecoration
import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur
import kotlinx.android.synthetic.main.nav_activity.*
import kotlinx.android.synthetic.main.sb_nav.*
import kotlinx.android.synthetic.main.search_bar_expanded.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.px2dip

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

/**
 * Home, Gospel, Communication, Me 4 TAB main entrance activity.
 * implementation of NavContract.View.
 */
open class NavActivity : SwipeBackActivity(), NavContract.View {

    override fun deinitView() {
    }

    companion object {
        const val SHORTER_DURATION = 225L
        const val HIDE_THRESHOLD = 0 //移动多少距离后显示隐藏
    }

    /**
     * presenter will be initialized when the NavPresenter is initialized
     */
    override lateinit var presenter: NavContract.Presenter

    lateinit var adapter: NavItemPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_activity)
        createPresenter()
    }

    private fun createPresenter() {
        NavPresenter("", Injection.provideNavsRepository(applicationContext), this)
        presenter.start()
    }

    override fun initView(navs: List<Nav>) {
        initSbl()
        initAbl()
        initTb()
        initSl()
        initSrl()
        initFs()
        initFl()
        initRv(navs)
        initBv()
        initBnv()
        startNav(0)
    }

    private fun initSbl() {
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true)
    }

    protected fun initAbl() {
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

    private fun initSl() {
        sl_nav.setOnClickListener { slExpand() }
        search_magnifying_glass.setOnClickListener { slExpand() }
        search_box_start_search.setOnClickListener { slExpand() }
        search_back_button.setOnClickListener { slCollapse() }
    }

    private fun initSrl() {
        srl_nav.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent))
        srl_nav.setOnRefreshListener { presenter.insertNav(0, true) }
    }

    private fun initFs() {
        fs_nav.setRecyclerView(rv_nav)
    }

    private fun initFl() {
        srl_nav.background = ResourcesCompat.getDrawable(resources, R.color.default_background_nav, theme)
    }

    open fun initRv(navs: List<Nav>) {
        adapter = NavItemPresenter(navs)
        rv_nav.addItemDecoration(ItemDecoration(resources.getDimension(R.dimen.search_margin_horizontal).toInt()))
        rv_nav.layoutManager = LinearLayoutManager(this)
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
                bv_nav.requestFocus()
                Log.i("bottom", "onBottom")
            }
        })
    }

    private fun initBv() {
        comment_nav.visibility = View.GONE
        bnv_nav.visibility = View.VISIBLE
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
    }

    private fun initBnv() {
        disableShiftMode(bnv_nav)
        bnv_nav.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startNav(navId = 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_gospel -> {
                    startNav(navId = 1)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_chat -> {
                    startNav(navId = 2)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_me -> {
                    startNav(navId = 3)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
        bnv_nav.setOnNavigationItemReselectedListener { scrollRvToTop() }
    }

    override fun setupSb(searchHint: String) {
    }

    override fun startSwipeRefreshLayout() {
    }

    override fun stopSrl() {
        srl_nav.isRefreshing = false
    }

    override fun startPb() {
        pb_nav.visibility = View.VISIBLE
    }

    override fun stopPb() {
        pb_nav.visibility = View.GONE
    }

    override fun invalidateRv(navs: List<Nav>) {
        adapter.navs = navs
        runLayoutAnimation(rv_nav)
        fab_nav.postDelayed({
            initFAB()
        }, SHORTER_DURATION)
    }

    override fun restoreRvPos() {
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

    /**
     * @param navId quest parameter of nav page to get navs lists
     */
    private fun startNav(navId: Int) {
//        if (navId == 0) {
        presenter.insertNav(navId)
//        }
    }

    fun scrollRvToTop() {
        rv_nav.smoothScrollToPosition(dip(-1)) // 为了滚到顶
        abl_nav.setExpanded(true, true)
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val animation = AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.layout_animation_from_right)
        recyclerView.layoutAnimation = animation
        recyclerView.adapter.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
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

    abstract inner class HidingScrollListener : RecyclerView.OnScrollListener() {
        private var scrolledDistance = 0 //移动的中距离
        private var controlsVisible = true //显示或隐藏

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            Log.i("rv", "-1 " + rv_nav.canScrollVertically(-1))
            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {//移动总距离大于规定距离 并且是显示状态就隐藏
                onHide()
                controlsVisible = false
                scrolledDistance = 0//归零
            } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                onShow()
                controlsVisible = true
                scrolledDistance = 0
            }

            if (controlsVisible && dy > 0 || !controlsVisible && dy < 0) { //显示状态向上滑动 或 隐藏状态向下滑动 总距离增加
                scrolledDistance += dy
            }

            if (!rv_nav.canScrollVertically(-1) && dy < 0) { // 并且是向上滑动
                onTop()
            }

            Log.i("bottom", (!rv_nav.canScrollVertically(1)).toString())
            Log.i("bottom dy > 0", (dy > 0).toString())
            if (!rv_nav.canScrollVertically(1)) { // 并且是向下滑动
                onBottom()
            }
        }

        abstract fun onHide()
        abstract fun onShow()
        abstract fun onTop()
        abstract fun onBottom()
    }
}
