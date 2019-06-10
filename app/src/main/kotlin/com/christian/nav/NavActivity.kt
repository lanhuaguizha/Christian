package com.christian.nav

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.edit
import com.afollestad.aesthetic.Aesthetic
import com.bumptech.glide.Glide
import com.christian.R
import com.christian.swipe.SwipeBackActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.kotlinpermissions.KotlinPermissions
import kotlinx.android.synthetic.main.nav_activity.*
import kotlinx.android.synthetic.main.nav_activity.view.*
import kotlinx.android.synthetic.main.nav_fragment.*
import kotlinx.android.synthetic.main.nav_item_me_portrait.*
import kotlinx.android.synthetic.main.sb_nav.*
import kotlinx.android.synthetic.main.search_bar_expanded.*
import org.jetbrains.anko.debug
import org.jetbrains.anko.dip
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

    override fun initView(navFragmentList: ArrayList<NavFragment>) {
        initAbl()
        initTb()
        initPortrait()
        initSb()
        initVp(navFragmentList)
        initFab()
        initBv()
        initBnv()
        // SunriseSunset sets DarkMode
        sunriseSunset()
    }

    private fun sunriseSunset() {
        getLatitudeLongitudePermissions()
    }

    // start--
    private fun getLatitudeLongitudePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            KotlinPermissions.with(this) // where this is an FragmentActivity instance
                    .permissions(Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                    .onAccepted { permissions ->
                        //List of accepted permissions
                        sunriseSunsetApply()
                    }
                    .onDenied { permissions ->
                        //List of denied permissions
                        snackbar(getString(R.string.tips_location_permission_request)).show()
                    }
                    .onForeverDenied { permissions ->
                        //List of forever denied permissions
                        snackbar(getString(R.string.tips_location_permission_request)).show()
                    }
                    .ask()
        } else {
            sunriseSunsetApply()
        }
    }

    private fun sunriseSunsetApply() {
        val ll = getLngAndLat(this@NavActivity)
        info { ll[0] }
        info { ll[1] }
        val sunriseSunset = ca.rmen.sunrisesunset.SunriseSunset.getSunriseSunset(Calendar.getInstance(), ll[0], ll[1])
        val sharedPreferences = getSharedPreferences("christian", Activity.MODE_PRIVATE)
        sharedPreferences.edit {
            putString("sunrise", sunriseSunset[0].time.toString())
            putString("sunset", sunriseSunset[1].time.toString())
        }
        info { "Sunrise at: " + sunriseSunset[0].time }
        info { "Sunset at: " + sunriseSunset[1].time }
        info { "currentTimeMillis: ${Date(System.currentTimeMillis())}" }
        info { "-----------------" }
        info { "Sunrise at: " + sunriseSunset[0].timeInMillis }
        info { "Sunset at: " + sunriseSunset[1].timeInMillis }
        info { "currentTimeMillis: ${System.currentTimeMillis()}" }
        if (System.currentTimeMillis() in sunriseSunset[0].timeInMillis..sunriseSunset[1].timeInMillis) {
            // 恢复应用默认皮肤
            info { "恢复应用默认皮肤" }
            Aesthetic.config {
                activityTheme(R.style.Christian)
                isDark(false)
                textColorPrimary(res = R.color.text_color_primary)
                textColorSecondary(res = R.color.text_color_secondary)
                attribute(R.attr.my_custom_attr, res = R.color.default_background_nav)
                attribute(R.attr.my_custom_attr2, res = R.color.white)
                attribute(R.attr.my_custom_attr3, res = R.color.colorOverlay)
            }
        } else {
            // 夜间模式
            info { "夜间模式" }
            Aesthetic.config {
                activityTheme(R.style.ChristianDark)
                isDark(true)
                textColorPrimary(res = android.R.color.primary_text_dark)
                textColorSecondary(res = android.R.color.secondary_text_dark)
                attribute(R.attr.my_custom_attr, res = R.color.text_color_primary)
                attribute(R.attr.my_custom_attr2, res = R.color.background_material_dark)
                attribute(R.attr.my_custom_attr3, res = R.color.fui_transparent)
            }
        }
    }

    private lateinit var locationManager: LocationManager

    /**
     * 获取经纬度
     */
    private fun getLngAndLat(context: Context): Array<Double> {

        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {  //从gps获取经纬度
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return arrayOf(latitude, longitude)
                }
            }
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude
            } else {//当GPS信号弱没获取到位置的时候又从网络获取
                return getLngAndLatWithNetwork()
            }
        } else {    //从网络获取经纬度
            return getLngAndLatWithNetwork()
        }
        return arrayOf(latitude, longitude)
    }

    //从网络获取经纬度
    private fun getLngAndLatWithNetwork(): Array<Double> {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return arrayOf(latitude, longitude)
            }
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0f, locationListener)
        } catch (e: Exception) {
            return arrayOf(latitude, longitude)
        }
        val location: Location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        latitude = location.latitude
        longitude = location.longitude
        return arrayOf(latitude, longitude)
    }

    private var locationListener: LocationListener = object : LocationListener {

        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

        }

        // Provider被enable时触发此函数，比如GPS被打开
        override fun onProviderEnabled(provider: String) {

        }

        // Provider被disable时触发此函数，比如GPS被关闭
        override fun onProviderDisabled(provider: String) {

        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        override fun onLocationChanged(location: Location) {}
    }
    // end--

    private fun initAbl() {
        tb_nav.setOnClickListener(object : DoubleClickListener() {
            override fun onDoubleClick(v: View) {
                scrollRvToTop(this@NavActivity)
            }
        })
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
            signIn()
        }
        sign_out.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            user?.delete()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    snackbar("User account deleted")
                }
            }
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

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            info { "response: ${response?.idpToken}" }

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                info { "user: $user" }
                if (user != null) {
                    sign_in.visibility = View.GONE
                    sign_out.visibility = View.VISIBLE
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
                    sign_in.visibility = View.VISIBLE
                    sign_out.visibility = View.GONE
                    portrait.visibility = View.GONE
                    name.visibility = View.GONE
                    intro.visibility = View.GONE

                    portrait_nav.isClickable = false
                    portrait_nav.isFocusable = false
                    portrait_nav.isFocusableInTouchMode = false
                }
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                val snackbar = snackbar("sign in filed.")
                snackbar.show()
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

    /**
     * Me Page登录方法，会在onActivityResult返回结果
     */
    private fun signIn() {
        // Choose authentication providers
        val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.PhoneBuilder().build(),
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
        val snackbar = Snackbar.make(cl_nav, s, Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            snackbarView.elevation = dip(3).toFloat()
        }

        val params = snackbarView.layoutParams as CoordinatorLayout.LayoutParams
        params.anchorId = R.id.bnv_nav
        params.width = CoordinatorLayout.LayoutParams.MATCH_PARENT
        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL// 相对锚点的位置
        params.anchorGravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL // 锚点的位置
        snackbarView.layoutParams = params

        return snackbar
    }

    open fun scrollRvToTop(navActivity: NavActivity) {
        if (::navFragmentPagerAdapter.isInitialized)
            navActivity.navFragmentPagerAdapter.currentFragment.rv_nav.smoothScrollToPosition(0) // 为了滚到顶
        navActivity.abl_nav.setExpanded(true, true)
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

    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(locationListener)
    }
}
