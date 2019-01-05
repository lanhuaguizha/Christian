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
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import com.christian.ChristianApplication
import com.christian.Injection
import com.christian.R
import com.christian.swipe.SwipeBackActivity
import com.christian.swipe.SwipeBackHelper
import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.nav_activity.*
import kotlinx.android.synthetic.main.nav_activity.view.*
import kotlinx.android.synthetic.main.nav_fragment.*
import kotlinx.android.synthetic.main.sb_nav.*
import kotlinx.android.synthetic.main.search_bar_expanded.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.info
import java.util.*
import kotlin.math.abs


/**
 * Home, Gospel, Communication, Me 4 TAB main entrance activity.
 * implementation of NavContract.View.
 */
open class NavActivity : SwipeBackActivity(), NavContract.INavActivity {

    override fun onSaveInstanceState(outState: Bundle?) {
//        super.onSaveInstanceState(outState)
//        outState?.putParcelableArrayList(NAV_FRAGMENT_LIST, (presenter as NavPresenter).navFragmentList)
    }

    companion object {
        const val RC_SIGN_IN = 0
    }

    internal var mPosition: Int = 0
    /**
     * presenter will be initialized when the NavPresenter is initialized
     */
    override lateinit var presenter: NavContract.IPresenter
    private lateinit var navFragment: NavFragment
    private val providers = Arrays.asList(
//                    AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
//                    AuthUI.IdpConfig.GoogleBuilder().build(),
//                    AuthUI.IdpConfig.FacebookBuilder().build(),
//                    AuthUI.IdpConfig.TwitterBuilder().build())
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_activity)
        NavPresenter(initFragmentIndex, Injection.provideNavsRepository(applicationContext), this)
        presenter.init(navFragmentSize = 4, savedInstanceState = savedInstanceState)
        info { "look at init times" }
    }

    override fun initView(navFragments: List<NavFragment>) {
        initSbl()
        initAbl()
        initTb()
        initSb()
        initVp(navFragments)
        initBv()
        initBnv()
    }

    override fun deinitView() {
    }

    open fun initSbl() {
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(false)
                .setDisallowInterceptTouchEvent(true)
    }

    fun initAbl() {
        abl_nav.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout: AppBarLayout, verticalOffset: Int ->
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
        })
    }

    open fun initTb() {
        sb_nav.visibility = View.VISIBLE
    }

    private fun initSb() {
        sb_nav.setOnClickListener { slExpand() }
        search_magnifying_glass.setOnClickListener { slExpand() }
        search_box_start_search.setOnClickListener { slExpand() }
        search_back_button.setOnClickListener { slCollapse() }
    }

    open fun initVp(navFragments: List<NavFragment>) {
        val navFragmentPagerAdapter = NavFragmentPagerAdapter(navFragments, supportFragmentManager)

        vp_nav.offscreenPageLimit = 3
        vp_nav.adapter = navFragmentPagerAdapter

        vp_nav.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                info { "onPageScrolled, position$position, positionOffset$positionOffset, positionOffsetPixels$positionOffsetPixels" }
            }

            override fun onPageSelected(position: Int) {
                info { "onPageSelected$position" }
                mPosition = position
                info { "position$position" }
                bnv_nav.menu.getItem(position).isChecked = true
            }

            override fun onPageScrollStateChanged(state: Int) {
                info { "onPageScrollStateChanged, state$state" }
                if (state == 2) {
                    fab_nav.hide()
                } else if (state == 0) {
//                    fab_nav.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_edit_black_24dp, theme))
                    showFAB()
                }
            }

        })
    }

    open fun initBv() {
        //set background, if your root layout doesn't have one
        val windowBackground = window.decorView.background
        val radius = 25f
        bv_nav.setupWith(cl_nav)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(SupportRenderScriptBlur(this))
                .setBlurRadius(radius)
                .setHasFixedTransformationMatrix(false)
        // set behavior
        val params = CoordinatorLayout.LayoutParams(bv_nav.layoutParams)
        params.gravity = Gravity.BOTTOM
        params.behavior = BottomNavigationViewBehavior(this, null)
        bv_nav.layoutParams = params
    }

    @SuppressLint("RestrictedApi")
    fun showFAB() {
//        fab_nav.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_edit_black_24dp, theme))
        when (mPosition) {
            3 -> fab_nav.show()
            else -> fab_nav.hide()
        }
        fab_nav.setOnClickListener {

            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN)
        }
//        if (fab_nav.visibility != View.VISIBLE) {
//            info { "initFAB View.VISIBLE" }
//            fab_nav.visibility = View.VISIBLE
//        }
    }

    private fun initBnv() {
        disableShiftMode(bnv_nav)

//        vp_nav.currentItem = initFragmentIndex

        bnv_nav.bnv_nav.setOnNavigationItemSelectedListener {
            info { "generateNavId${(presenter as NavPresenter).generateNavId(it.itemId)}" }
            vp_nav.currentItem = (presenter as NavPresenter).generateNavId(it.itemId)
            true
        }

        bnv_nav.setOnNavigationItemReselectedListener {
            (presenter as NavPresenter).navFragmentList[mPosition].rv_nav.smoothScrollToPosition(dip(0)) // 为了滚到顶
            abl_nav.setExpanded(true, true)
        }
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
        if (!sb_nav.isExpanded) {
            sb_nav.expand(true, true)
        }
    }

    private fun slCollapse() {
        if (sb_nav.isExpanded) {
            sb_nav.collapse(true)
        }
        if (sb_nav.isFadedOut) {
            sb_nav.fadeIn()
        }
    }

    /**
     * Immersive reading, swipe hidden.
     */
    class BottomNavigationViewBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<View>(context, attrs) {
        override fun onLayoutChild(parent: CoordinatorLayout, child: View, layoutDirection: Int): Boolean {
            (child.layoutParams as CoordinatorLayout.LayoutParams).topMargin = parent.measuredHeight.minus(child.measuredHeight)
            return super.onLayoutChild(parent, child, layoutDirection)
        }

        override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
            return dependency is AppBarLayout
        }

        override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
            val top = ((dependency.layoutParams as CoordinatorLayout.LayoutParams).behavior as AppBarLayout.Behavior).topAndBottomOffset
            Log.i("dd", top.toString())
            //因为BottomNavigation的滑动与ToolBar是反向的，所以取-top值
            child.translationY = (-top).toFloat()
            return false
        }
    }

    /**
     * Locate nav detail FAB
     */
    class BottomNavigationViewBehaviorDetail(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<View>(context, attrs) {
        override fun onLayoutChild(parent: CoordinatorLayout, child: View, layoutDirection: Int): Boolean {
            (child.layoutParams as CoordinatorLayout.LayoutParams).topMargin = parent.measuredHeight.minus(child.measuredHeight)
            return super.onLayoutChild(parent, child, layoutDirection)
        }

        override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
            return dependency is AppBarLayout
        }

        override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
            child.translationY = 2000f
            return false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val refWatcher = ChristianApplication.getRefWatcher(this)
        refWatcher.watch(this)
    }
}

@SuppressLint("RestrictedApi")
fun disableShiftMode(view: BottomNavigationView) {
    val menuView = view.getChildAt(0) as BottomNavigationMenuView
    menuView.labelVisibilityMode = 1
    for (i in 0 until menuView.childCount) {
        val item = menuView.getChildAt(i) as BottomNavigationItemView
        item.setShifting(false)
        item.setChecked(item.itemData.isChecked)
    }

}
