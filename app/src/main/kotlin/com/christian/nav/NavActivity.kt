package com.christian.nav

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.christian.R
import com.christian.swipe.SwipeBackActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.nav_activity.*
import kotlinx.android.synthetic.main.nav_activity.view.*
import kotlinx.android.synthetic.main.nav_item_me_portrait.*
import kotlinx.android.synthetic.main.sb_nav.*
import kotlinx.android.synthetic.main.search_bar_expanded.*
import org.jetbrains.anko.debug
import org.jetbrains.anko.info
import java.lang.ref.WeakReference
import java.util.*

/**
 * Home, Gospel, Communication, Me 4 TAB main entrance activity.
 * implementation of NavContract.View.
 */
open class NavActivity : SwipeBackActivity(), NavContract.INavActivity {

    companion object {
        class StaticHandler(navActivity: NavActivity) : Handler() {
            private val navActivityWeakReference = WeakReference<NavActivity>(navActivity)
            override fun handleMessage(msg: Message?) {
                when (msg?.what) {
                    MESSAGE_SET_TOOLBAR_EXPANDED -> {
                        navActivityWeakReference.get()?.info { "setTabLayoutExpanded--${msg.arg1}" }
                        navActivityWeakReference.get()?.let { setTabLayoutExpanded(it, msg.arg1) }
                    }
                }
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
    }

    private lateinit var mStaticHandler: StaticHandler
    /**
     * presenter will be initialized when the NavPresenter is initialized
     */
    override lateinit var presenter: NavContract.IPresenter
    var verticalOffset = -1
    lateinit var navFragmentPagerAdapter: NavFragmentPagerAdapter

    private val providers = Arrays.asList(
//                    AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
//                    AuthUI.IdpConfig.GoogleBuilder().build(),
//                    AuthUI.IdpConfig.FacebookBuilder().build(),
//                    AuthUI.IdpConfig.TwitterBuilder().build())
    )
    private val viewPagerOnPageChangeListener = object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            bnv_nav.menu.getItem(position).isChecked = true

            mStaticHandler.removeMessages(MESSAGE_SET_TOOLBAR_EXPANDED)
            val msg = Message()
            msg.what = MESSAGE_SET_TOOLBAR_EXPANDED
            msg.arg1 = position
            info { "setTabLayoutExpanded---$position" }
            mStaticHandler.sendMessageDelayed(msg, 0)
//            setTabLayoutExpanded(this@NavActivity, position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            if (state == 2) {
                fab_nav.hide()
            } else if (state == 0) {
//                    fab_nav.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_edit_black_24dp, theme))
//                    if (showOrHideLogicExecute) {
//                        showFAB()
//                    }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mStaticHandler = StaticHandler(this@NavActivity)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_activity)
        NavPresenter(initFragmentIndex, this)
        presenter.init(whichActivity = NAV_ACTIVITY)
    }

    override fun initView(navFragmentList: ArrayList<NavFragment>) {
        initTb()
        initPortrait()
        initSb()
        initVp(navFragmentList)
        initFab()
        initBv()
        initBnv()
    }

    override fun deinitView() {
    }

    internal lateinit var tabTitleList: ArrayList<String>
    open fun initTb() {
        sb_nav.visibility = View.VISIBLE

        tabTitleList = arrayListOf(
                getString(R.string._Gen),
                getString(R.string._Exo),
                getString(R.string._Lev),
                getString(R.string._Num),
                getString(R.string._Deu),
                getString(R.string._Jos),
                getString(R.string._Jug),
                getString(R.string._Rut),
                getString(R.string._1Sa),
                getString(R.string._2Sa),
                getString(R.string._1Ki),
                getString(R.string._2Ki),
                getString(R.string._1Ch),
                getString(R.string._2Ch),
                getString(R.string._Ezr),
                getString(R.string._Neh),
                getString(R.string._Est),
                getString(R.string._Job),
                getString(R.string._Psm),
                getString(R.string._Pro),
                getString(R.string._Ecc),
                getString(R.string._Son),
                getString(R.string._Isa),
                getString(R.string._Jer),
                getString(R.string._Lam),
                getString(R.string._Eze),
                getString(R.string._Dan),
                getString(R.string._Hos),
                getString(R.string._Joe),
                getString(R.string._Amo),
                getString(R.string._Oba),
                getString(R.string._Jon),
                getString(R.string._Mic),
                getString(R.string._Nah),
                getString(R.string._Hab),
                getString(R.string._Zep),
                getString(R.string._Hag),
                getString(R.string._Zec),
                getString(R.string._Mal),

                getString(R.string._Mat),
                getString(R.string._Mak),
                getString(R.string._Luk),
                getString(R.string._Jhn),
                getString(R.string._Act),
                getString(R.string._Rom),
                getString(R.string._1Co),
                getString(R.string._2Co),
                getString(R.string._Gal),
                getString(R.string._Eph),
                getString(R.string._Phl),
                getString(R.string._Col),
                getString(R.string._1Ts),
                getString(R.string._2Ts),
                getString(R.string._1Ti),
                getString(R.string._2Ti),
                getString(R.string._Tit),
                getString(R.string._Mon),
                getString(R.string._Heb),
                getString(R.string._Jas),
                getString(R.string._1Pe),
                getString(R.string._2Pe),
                getString(R.string._1Jn),
                getString(R.string._2Jn),
                getString(R.string._3Jn),
                getString(R.string._Jud),
                getString(R.string._Rev)
        )
        for (tabTitle in tabTitleList) {
            tl_nav.newTab().setText(tabTitle).let { tl_nav.addTab(it) }
        }
        info { "圣经初始化" }
    }

    private fun initPortrait() {
        applyMarqueeEffect(intro)
        sign_in.setOnClickListener {
            info { "sign_in.setOnClickListener" }
        }
    }

    private fun initSb() {
        sb_nav.setOnClickListener { slExpand() }
        search_magnifying_glass.setOnClickListener { slExpand() }
        search_box_start_search.setOnClickListener { slExpand() }
        search_back_button.setOnClickListener { slCollapse() }
    }

    open fun initVp(navFragmentList: ArrayList<NavFragment>) {
        navFragmentPagerAdapter = NavFragmentPagerAdapter(supportFragmentManager)
        vp_nav.offscreenPageLimit = 3
        vp_nav.adapter = navFragmentPagerAdapter
        vp_nav.addOnPageChangeListener(viewPagerOnPageChangeListener)
    }

    open fun initFab() {

    }

    open fun initBv() {

        makeViewBlur(bv_nav, cl_nav, window)

        val params = androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(bv_nav.layoutParams)
        params.gravity = Gravity.BOTTOM
        params.behavior = BottomNavigationViewBehavior(this, null)
        bv_nav.layoutParams = params
    }

    @SuppressLint("RestrictedApi")
    fun showFAB() {
//        fab_nav.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_edit_black_24dp, theme))
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
//            debug { "initFAB View.VISIBLE" }
//            fab_nav.visibility = View.VISIBLE
//        }
    }

    open fun initBnv() {
        disableShiftMode(bnv_nav)
//        vp_nav.post {
//            vp_nav.currentItem = VIEW_DISCIPLE
//            vp_nav.currentItem = VIEW_HOME
//        }
        viewPagerOnPageChangeListener.onPageSelected(initFragmentIndex)
        bnv_nav.bnv_nav.setOnNavigationItemSelectedListener {
            val itemPosition = (presenter as NavPresenter).generateNavId(it.itemId)
            debug { "generateNavId$itemPosition" }
            vp_nav.currentItem = itemPosition
            true
        }
        bnv_nav.setOnNavigationItemReselectedListener {
            scrollRvToTop(this@NavActivity)
//            scrollRvToTop(this@NavActivity, navFragment.rv_nav)
        }
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
    class BottomNavigationViewBehavior(context: Context?, attrs: AttributeSet?) : androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior<View>(context, attrs) {
        override fun onLayoutChild(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: View, layoutDirection: Int): Boolean {
            (child.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams).topMargin = parent.measuredHeight.minus(child.measuredHeight)
            return super.onLayoutChild(parent, child, layoutDirection)
        }

        override fun layoutDependsOn(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: View, dependency: View): Boolean {
            return dependency is AppBarLayout
        }

        override fun onDependentViewChanged(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: View, dependency: View): Boolean {
            val top = ((dependency.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams).behavior as AppBarLayout.Behavior).topAndBottomOffset
            Log.i("dd", top.toString())
            //因为BottomNavigation的滑动与ToolBar是反向的，所以取-top值
            child.translationY = (-top).toFloat()
            return false
        }
    }

    /**
     * Locate nav detail FAB
     */
    class BottomNavigationViewBehaviorDetail(context: Context?, attrs: AttributeSet?) : androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior<View>(context, attrs) {
        override fun onLayoutChild(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: View, layoutDirection: Int): Boolean {
            (child.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams).topMargin = parent.measuredHeight.minus(child.measuredHeight)
            return super.onLayoutChild(parent, child, layoutDirection)
        }

        override fun layoutDependsOn(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: View, dependency: View): Boolean {
            return dependency is AppBarLayout
        }

        override fun onDependentViewChanged(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: View, dependency: View): Boolean {
            child.translationY = 2000f
            return false
        }
    }

    // presenter solve login in and login out logic
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        presenter.createUser()
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    sign_in.visibility = View.GONE
                    portrait.visibility = View.VISIBLE
                    name.visibility = View.VISIBLE
                    intro.visibility = View.VISIBLE

                    portrait_layout.isClickable = true
                    portrait_layout.isFocusable = true
                    portrait_layout.isFocusableInTouchMode = true
                } else {
                    sign_in.visibility = View.VISIBLE
                    portrait.visibility = View.GONE
                    name.visibility = View.GONE
                    intro.visibility = View.GONE

                    portrait_layout.isClickable = false
                    portrait_layout.isFocusable = false
                    portrait_layout.isFocusableInTouchMode = false
                }
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    open class NavFragmentPagerAdapter(fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {

        lateinit var currentFragment: NavFragment

        override fun getItem(position: Int): androidx.fragment.app.Fragment {
            val navFragment = NavFragment()
            navFragment.navId = position
            return navFragment
        }

        override fun getCount(): Int {
            return 4
        }

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            currentFragment = `object` as NavFragment
            super.setPrimaryItem(container, position, `object`)
        }
    }

    val appBarLayoutOnOffsetChangedListener = AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
        appBarLayoutOnOffsetChangedListener(this@NavActivity, appBarLayout, verticalOffset)
        this@NavActivity.verticalOffset = verticalOffset
    }
}
