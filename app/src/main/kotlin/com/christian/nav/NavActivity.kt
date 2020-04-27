package com.christian.nav

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.AttributeSet
import android.util.Log
import android.view.*
import androidx.appcompat.widget.TooltipCompat
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.christian.R
import com.christian.SettingsActivity
import com.christian.nav.disciple.DiscipleFragment
import com.christian.swipe.SwipeBackActivity
import com.christian.view.UITools
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.kotlinpermissions.KotlinPermissions
import kotlinx.android.synthetic.main.nav_activity.*
import kotlinx.android.synthetic.main.nav_activity.view.*
import kotlinx.android.synthetic.main.nav_fragment.*
import kotlinx.android.synthetic.main.nav_item_me_portrait.*
import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator
import me.everything.android.ui.overscroll.adapters.IOverScrollDecoratorAdapter
import org.jetbrains.anko.debug
import org.jetbrains.anko.dip
import org.jetbrains.anko.info
import ren.qinc.markdowneditors.view.EditorActivity
import java.lang.ref.WeakReference
import java.util.*
import kotlin.math.abs

/**
 * Home, Gospel, Communication, Me 4 TAB main entrance activity.
 * implementation of NavContract.View.
 */
open class NavActivity : SwipeBackActivity(), NavContract.INavActivity {

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menuItemSearch = menu.findItem(R.id.menu_search)
        menuItemSetting = menu.findItem(R.id.menu_setting)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_nav, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_setting -> {
                val i = Intent(this, SettingsActivity::class.java)
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    lateinit var menuItemSetting: MenuItem
    private lateinit var menuItemSearch: MenuItem
    private var customTime = 0L
    val SHOTRER_DURATION = 225L

    val LONGER_DURATION = 375L

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
    var pageSelectedPosition = 0

    /**
     * presenter will be initialized when the NavPresenter is initialized
     */
    override lateinit var presenter: NavContract.IPresenter
    var verticalOffset = -1
    lateinit var navFragmentPagerAdapter: NavFragmentPagerAdapter

    open val viewPagerOnPageChangeListener = object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
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

            pageSelectedPosition = position

            showFab(position)

        }

        override fun onPageScrollStateChanged(state: Int) {
            if (state == 2) {
//                fab_nav.hide()
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

    override fun initView(navFragmentList: ArrayList<Fragment>) {
        initAbl()
        initTb()
        initPortrait()
        initVp(navFragmentList)
        initFab()
        initBv()
        initBnv()
        // 自动夜间模式
//        sunriseSunset()
    }

    private fun sunriseSunset() {
        getLatitudeLongitudePermissions()
    }

    private fun getLatitudeLongitudePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            KotlinPermissions.with(this) // where this is an FragmentActivity instance
                    .permissions(INTERNET, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
                    .onAccepted { permissions ->
                        //List of accepted permissions
                        if (permissions.contains(ACCESS_FINE_LOCATION))
                            bindNavService()
                    }
                    .onDenied { permissions ->
                        //List of denied permissions
                    }
                    .onForeverDenied { permissions ->
                        //List of forever denied permissions
                    }
                    .ask()
        } else {
            bindNavService()
        }
    }

    private fun initAbl() {
        tb_nav.setOnClickListener(object : DoubleClickListener() {
            override fun onDoubleClick(v: View) {
                scrollRvToTop(this@NavActivity)
            }
        })
    }

    internal lateinit var tabTitleList: ArrayList<String>
    open fun initTb() {
        setSupportActionBar(tb_nav)
        sbl_nav.visibility = View.GONE

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

        UITools.elasticPadding(tl_nav, 500);
//        OverScrollDecoratorHelper.setUpStaticOverScroll(tl_nav, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)
        HorizontalOverScrollBounceEffectDecorator(object : IOverScrollDecoratorAdapter {
            override fun isInAbsoluteEnd(): Boolean {
                return !tl_nav.canScrollDown()
            }

            override fun isInAbsoluteStart(): Boolean {
                return !tl_nav.canScrollUp()
            }

            override fun getView(): View {
                return tl_nav
            }

        })
        info { "圣经初始化" }
    }

    private fun initPortrait() {
        applyMarqueeEffect(intro)
        sign_in.setOnClickListener {
            signIn()
        }
        sign_out.setOnClickListener {

        }
    }

    open fun initVp(navFragmentList: ArrayList<Fragment>) {
        navFragmentPagerAdapter = NavFragmentPagerAdapter(supportFragmentManager)
        vp_nav.offscreenPageLimit = 3
        vp_nav.adapter = navFragmentPagerAdapter
        vp_nav.addOnPageChangeListener(viewPagerOnPageChangeListener)
    }

    open fun initFab() {
//        fab_nav.visibility = View.GONE

//        fab_nav.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_edit_black_24dp, theme))
//        fab_nav.setOnClickListener {
//            ViewModelProviders.of(this).get(NavViewModel::class.java).loadImage()
//        }
    }

    open fun initBv() {

        makeViewBlur(bv_nav, cl_nav, window)

        val params = CoordinatorLayout.LayoutParams(bv_nav.layoutParams)
        params.gravity = Gravity.BOTTOM
        params.behavior = BottomNavigationViewBehavior(this, null)
        bv_nav.layoutParams = params
    }

    open fun showFAB() {
        fab_nav.show()
    }

    open fun initBnv() {
//        disableShiftMode(bnv_nav)
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

    override fun showFab(pos: Int) {
        if (!fab_nav.isGone) {
            Log.i("fab", "hide")
            fab_nav.visibility = View.GONE
        }

        when (pos) {
            0 -> {
                tb_nav.title = getString(R.string.title_home)

                if (::menuItemSearch.isInitialized) menuItemSearch.isVisible = true
                if (::menuItemSetting.isInitialized) menuItemSetting.isVisible = false

                fab_nav.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_edit_black_24dp, theme))
//                fab_nav.backgroundTintList = ColorStateList.valueOf(ResourcesCompat.getColor(resources, R.color.colorAccent,theme))
                if (verticalOffset != -tb_nav.height)
                    fab_nav.hide()
                TooltipCompat.setTooltipText(fab_nav, "Edit")

                fab_nav.setOnClickListener {
                    startActivity(Intent(this@NavActivity, EditorActivity::class.java))
                }
            }
            1 -> {
                tb_nav.title = getString(R.string.title_book)

                if (::menuItemSearch.isInitialized) menuItemSearch.isVisible = true
                if (::menuItemSetting.isInitialized) menuItemSetting.isVisible = false

                fab_nav.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_filter_list_black_24dp, theme))
//                fab_nav.backgroundTintList = ColorStateList.valueOf(ResourcesCompat.getColor(resources, R.color.colorAccent,theme))
                if (verticalOffset > -tb_nav.height)
                    fab_nav.hide()
                TooltipCompat.setTooltipText(fab_nav, "Filter")

                fab_nav.setOnClickListener {
                    startActivity(Intent(this@NavActivity, ren.qinc.markdowneditors.view.MainActivity::class.java))
                }
            }
            2 -> {
                tb_nav.title = getString(R.string.title_chat)

                if (::menuItemSearch.isInitialized) menuItemSearch.isVisible = true
                if (::menuItemSetting.isInitialized) menuItemSetting.isVisible = false

                fab_nav.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_downward_black_24dp, theme))
//                fab_nav.backgroundTintList = ColorStateList.valueOf(ResourcesCompat.getColor(resources, R.color.colorAccent,theme))
                if (verticalOffset > -tb_nav.height)
                    fab_nav.hide()
                TooltipCompat.setTooltipText(fab_nav, "Down")

                fab_nav.setOnClickListener {
                }
            }
            3 -> {
                tb_nav.title = getString(R.string.me)

                if (::menuItemSearch.isInitialized) menuItemSearch.isVisible = false
                if (::menuItemSetting.isInitialized) menuItemSetting.isVisible = true
                invalidateSignInUI()

                fab_nav.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_exit_to_app_black_24dp, theme))
//                fab_nav.backgroundTintList = ColorStateList.valueOf(ResourcesCompat.getColor(resources, R.color.colorAccentRed,theme))
                if (auth.currentUser != null) {
                    if (verticalOffset != -tb_nav.height)
                        fab_nav.show()
                    TooltipCompat.setTooltipText(fab_nav, "Exit")
                } else {
                    fab_nav.hide()
                }

                fab_nav.setOnClickListener {
                    AuthUI.getInstance()
                            .signOut(this)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    snackbar("Sign out successful").show()
                                    invalidateSignInUI()
                                }
                            }
//            val user = auth.currentUser
//            user?.delete()?.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    snackbar("User account deleted").show()
//                }
//            }
                }
            }
        }
    }

    override fun hideFab() {
        fab_nav.hide()
    }

    private fun slExpand() {
        if (!sbl_nav.isExpanded) {
            sbl_nav.expand(true, true)
        }
    }

    private fun slCollapse() {
        if (sbl_nav.isExpanded) {
            sbl_nav.collapse(true)
        }
        if (sbl_nav.isFadedOut) {
            sbl_nav.fadeIn()
        }
    }

    /**
     * Immersive reading, swipe hidden.
     */
    class BottomNavigationViewBehavior(context: Context?, attrs: AttributeSet?) : androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior<View>(context, attrs) {
        override fun onLayoutChild(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: View, layoutDirection: Int): Boolean {
            (child.layoutParams as CoordinatorLayout.LayoutParams).topMargin = parent.measuredHeight.minus(child.measuredHeight)
            return super.onLayoutChild(parent, child, layoutDirection)
        }

        override fun layoutDependsOn(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: View, dependency: View): Boolean {
            return dependency is AppBarLayout
        }

        override fun onDependentViewChanged(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: View, dependency: View): Boolean {
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
    class BottomNavigationViewBehaviorDetail(context: Context?, attrs: AttributeSet?) : androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior<View>(context, attrs) {
        override fun onLayoutChild(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: View, layoutDirection: Int): Boolean {
            (child.layoutParams as CoordinatorLayout.LayoutParams).topMargin = parent.measuredHeight.minus(child.measuredHeight)
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

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            info { "response: ${response?.idpToken}" }

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                invalidateSignInUI()
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                val snackbar = snackbar(getString(R.string.sign_in_failed))
                snackbar.show()
            }
        }
    }

    fun invalidateSignInUI() {
        val user = auth.currentUser
        info { "user: $user" }
        if (user != null) {
            tb_nav.title = ""
            sign_in.visibility = View.GONE
            sign_out.visibility = View.GONE
            fab_nav.show()
            portrait.visibility = View.VISIBLE
            name.visibility = View.VISIBLE
            intro.visibility = View.VISIBLE
            name.text = user.displayName
            intro.text = user.email
            Glide.with(this).load(user.photoUrl).into(iv_nav_item_small)
            info { "user.photoUrl: ${user.photoUrl}, user.displayName: ${user.displayName}, user.email: ${user.email}" }

            portrait_nav.isClickable = true
            portrait_nav.isFocusable = true
            portrait_nav.isFocusableInTouchMode = true
        } else {
            tb_nav.title = getString(R.string.title_me)
            sign_in.visibility = View.VISIBLE
            sign_out.visibility = View.GONE
            fab_nav.hide()
            portrait.visibility = View.GONE
            name.visibility = View.GONE
            intro.visibility = View.GONE

            portrait_nav.isClickable = false
            portrait_nav.isFocusable = false
            portrait_nav.isFocusableInTouchMode = false
        }
        // ...
    }

    open class NavFragmentPagerAdapter(fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {

        lateinit var currentFragment: NavFragment

        override fun getItem(position: Int): androidx.fragment.app.Fragment {
            when (position) {
                2 -> {
                    return DiscipleFragment()
                }
            }
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

    /**
     * Me Page登录方法，会在onActivityResult返回结果
     */
    private fun signIn() {
        // Choose authentication providers
        val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
//                AuthUI.IdpConfig.PhoneBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
//                AuthUI.IdpConfig.FacebookBuilder().build(),
//                AuthUI.IdpConfig.TwitterBuilder().build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN)
    }

    fun snackbar(s: String): Snackbar {
        val snackbar = Snackbar.make(cl_nav_2, s, Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            snackbarView.elevation = dip(3).toFloat()
        }
        // Snackbar
//        val params = snackbarView.layoutParams as CoordinatorLayout.LayoutParams
//        params.anchorId = R.id.bnv_nav
//        params.width = CoordinatorLayout.LayoutParams.MATCH_PARENT
//        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL// 相对锚点的位置
//        params.anchorGravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL // 锚点的位置
//        snackbarView.layoutParams = params
        return snackbar
    }

    open fun scrollRvToTop(navActivity: NavActivity) {
        try {
            if (::navFragmentPagerAdapter.isInitialized) {
                navActivity.navFragmentPagerAdapter.currentFragment.rv_nav.smoothScrollToPosition(0) // 为了滚到顶

                navActivity.navFragmentPagerAdapter.currentFragment.scrollChildRVToTop()
            }
            navActivity.abl_nav.setExpanded(true, true)
        } catch (e: Exception) {
        }
    }


    abstract class DoubleClickListener : View.OnClickListener {

        companion object {
            const val DOUBLE_TIME = 1000
            var lastClickTime = 0L
        }

        override fun onClick(v: View) {
            val currentTimeMillis = System.currentTimeMillis()
            if (currentTimeMillis - lastClickTime < DOUBLE_TIME) {
                onDoubleClick(v)
            }
            lastClickTime = currentTimeMillis
        }

        abstract fun onDoubleClick(v: View)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindNavService()
    }

    private lateinit var navService: NavService
    private var isBind: Boolean = false

    /**
     * 用于获取NavService
     */
    val navServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val navBinder = service as NavService.NavBinder
            navService = navBinder.navService
            isBind = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBind = false
        }
    }


    fun bindNavService() {
        val intent = Intent(this, NavService::class.java)
        bindService(intent, navServiceConnection, Context.BIND_AUTO_CREATE)
    }

    fun unbindNavService() {
        if (isBind) {
            unbindService(navServiceConnection);
            isBind = false;
        }
    }


    override fun onBackPressed() {
        //没有东西可以返回了，剩下软件退出逻辑
        //没有东西可以返回了，剩下软件退出逻辑
        if (abs(customTime - System.currentTimeMillis()) < 2000) {
            finish()
        } else { // 提示用户退出
            customTime = System.currentTimeMillis()
            snackbar(getString(R.string.double_click_exit)).show()
        }
    }
}
