package com.christian.nav

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.christian.ChristianApplication
import com.christian.R
import com.christian.data.Bean
import com.christian.data.GospelBean
import com.christian.data.Gospels
import com.christian.data.MeBean
import com.christian.navdetail.NavDetailActivity
import com.christian.navitem.NavItemPresenter
import com.christian.view.ItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import kotlinx.android.synthetic.main.gospel_detail_fragment.*
import kotlinx.android.synthetic.main.nav_activity.*
import kotlinx.android.synthetic.main.nav_fragment.*
import kotlinx.android.synthetic.main.nav_fragment.view.*
import org.jetbrains.anko.debug


open class NavFragment : androidx.fragment.app.Fragment(), NavContract.INavFragment, NavItemPresenter.OnGospelSelectedListener {

    override fun onSaveInstanceState(outState: Bundle) {
    }

    override fun onGospelSelected(gospel: DocumentSnapshot) {
        // Go to the details page for the selected restaurant
        gospelId = gospel.id
        val intent = Intent(this@NavFragment.navActivity, NavDetailActivity::class.java)
        intent.putExtra(toolbarTitle, gospel.data?.get("subtitle").toString())
        startActivity(intent)
    }

    open lateinit var firestore: FirebaseFirestore
    open lateinit var query: Query
    private lateinit var navActivity: NavActivity
    private lateinit var ctx: Context
    private lateinit var navAdapter: NavItemPresenter<*>
    lateinit var v: View
    var navId = -1
    lateinit var gospelId: String

    init {
        debug { "look at init times" }
    }

    /**
     * It is the first place application code can run where the fragment is ready to be used
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        navActivity = context as NavActivity
        ctx = context
        debug { "onAttach" }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.nav_fragment, container, false)
        FirebaseFirestore.setLoggingEnabled(true)
        firestore = FirebaseFirestore.getInstance()
        query = firestore.collection("gospels")

        initView()

        // onCreateView and onDestroyView start and stop cause 27 child fragments needs it when destroy view at default limit
        if (navId != VIEW_ME) {
            navAdapter.startListening()
        }
        return v
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 统一detaiil页面，因为detail页面的解绑在其他页面做了
        try {
            navAdapter.stopListening()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun initView() {
        debug { "nav fragment is $this and navId is $navId --initView" }
        if (navId == VIEW_GOSPEL) {
            v.vp1_nav.visibility = View.VISIBLE
            v.rv_nav.visibility = View.GONE
            v.pb_nav.visibility = View.GONE
            initTl()
        } else {
            v.vp1_nav.visibility = View.GONE
            v.rv_nav.visibility = View.VISIBLE
            v.pb_nav.visibility = View.VISIBLE
        }
        initRv()
    }

    private lateinit var navFragment: NavFragment

    private var pageSelectedPosition: Int = -1

    open fun initTl() {
        val tabTitleList = arrayListOf(
                navActivity.getString(R.string._Mat),
                navActivity.getString(R.string._Mak),
                navActivity.getString(R.string._Luk),
                navActivity.getString(R.string._Jhn),
                navActivity.getString(R.string._Act),
                navActivity.getString(R.string._Rom),
                navActivity.getString(R.string._1Co),
                navActivity.getString(R.string._2Co),
                navActivity.getString(R.string._Gal),
                navActivity.getString(R.string._Eph),
                navActivity.getString(R.string._Phl),
                navActivity.getString(R.string._Col),
                navActivity.getString(R.string._1Ts),
                navActivity.getString(R.string._2Ts),
                navActivity.getString(R.string._1Ti),
                navActivity.getString(R.string._2Ti),
                navActivity.getString(R.string._Tit),
                navActivity.getString(R.string._Mon),
                navActivity.getString(R.string._Heb),
                navActivity.getString(R.string._Jas),
                navActivity.getString(R.string._1Pe),
                navActivity.getString(R.string._2Pe),
                navActivity.getString(R.string._1Jn),
                navActivity.getString(R.string._2Jn),
                navActivity.getString(R.string._3Jn),
                navActivity.getString(R.string._Jud),
                navActivity.getString(R.string._Rev)
        )

        for (tabTitle in tabTitleList) {
            navActivity.tl_nav.newTab().setText(tabTitle).let { navActivity.tl_nav.addTab(it) }
        }

        val navChildFragmentPagerAdapter = NavChildFragmentPagerAdapter(childFragmentManager, tabTitleList)
        v.vp1_nav.adapter = navChildFragmentPagerAdapter
        navActivity.tl_nav.setupWithViewPager(v.vp1_nav)//将TabLayout和ViewPager关联起来

        v.vp1_nav.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                pageSelectedPosition = position
                navFragment = navChildFragmentPagerAdapter.currentFragment
//                navActivity.srl_nav.setTargetView(navFragment.rv_nav)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

    }

    var isPageTop: Boolean = true

    var isPageBottom: Boolean = false
    lateinit var bean: Bean

    private fun initRv() {
        this@NavFragment.bean = GospelBean(listOf(Gospels()))

        when (navId) {
            VIEW_HOME -> {
            }
            VIEW_GOSPEL -> {
            }
            VIEW_DISCIPLE -> {
            }
            VIEW_ME -> {
                this@NavFragment.bean = Gson().fromJson<MeBean>(getJson("me.json", ctx), MeBean::class.java)
                unregisterForContextMenu(v.rv_nav)
            }
        }
        navAdapter = object : NavItemPresenter<Bean>(query, this@NavFragment, bean = this@NavFragment.bean, navId = navId) {
            override fun onDataChanged() {
                if (itemCount == 0) {
                    v.rv_nav.visibility = View.GONE
                    v.pb_nav.visibility = View.VISIBLE
                } else {
                    v.rv_nav.visibility = View.VISIBLE
                    v.pb_nav.visibility = View.GONE
                }
            }

            override fun onError(e: FirebaseFirestoreException) {
                // Show a snackbar on errors
                Snackbar.make(cl_gospel_detail,
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show()
            }

        }

        // set Query
        if (navId != VIEW_ME) // 增加了复杂性，需要想办法统一Me
            navAdapter.setQuery(query) // onStop的时候注销了snapshotListener，onResume的时候一定要开启
        v.rv_nav.adapter = navAdapter
        v.rv_nav.addItemDecoration(ItemDecoration(resources.getDimension(R.dimen.search_margin_horizontal).toInt()))
        v.rv_nav.addOnScrollListener(object : HidingScrollListener(this) {

            override fun onHide() {
                hideFab()
                isPageTop = false
                isPageBottom = false
                controlOverScroll(navActivity, navActivity.abl_nav, navActivity.verticalOffset)
            }

            override fun onShow() {
                showFab()
                isPageTop = false
                isPageBottom = false
                controlOverScroll(navActivity, navActivity.abl_nav, navActivity.verticalOffset)
            }

            override fun onTop() {
                isPageTop = true
                controlOverScroll(navActivity, navActivity.abl_nav, navActivity.verticalOffset)
            }

            override fun onBottom() {
                isPageBottom = true
                controlOverScroll(navActivity, navActivity.abl_nav, navActivity.verticalOffset)
            }
        })

//        val indexScrollListener = IndexScrollListener()
//        indexScrollListener.register(v.fs_nav)
//        v.rv_nav.addOnScrollListener(indexScrollListener)
    }

    override fun showFab() {
//        navActivity.showFAB()
//        if (navId == 1 && cv_nav_frag.visibility == View.GONE) {
//            cv_nav_frag.visibility = View.VISIBLE
//            val fadeIn = AnimationUtils.loadAnimation(context, R.anim.abc_fade_in)
//            cv_nav_frag.startAnimation(fadeIn)
//        }
    }

    override fun hideFab() {
//        navActivity.fab_nav.hide()
//        if (navId == 1 && cv_nav_frag.visibility == View.VISIBLE) {
//            val fadeOut = AnimationUtils.loadAnimation(context, R.anim.abc_fade_out)
//            cv_nav_frag.startAnimation(fadeOut)
//            cv_nav_frag.visibility = View.GONE
//        }
    }

    override fun invalidateRv(bean: Bean) {
        navAdapter.bean = bean
        runLayoutAnimation(v.rv_nav)
        navActivity.showFAB()
    }

    private fun runLayoutAnimation(recyclerView: androidx.recyclerview.widget.RecyclerView) {
        val animation = AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.layout_animation_from_right)
        recyclerView.layoutAnimation = animation
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        // handling memory leaks
        val refWatcher = ChristianApplication.getRefWatcher(ctx)
        refWatcher.watch(this)
    }

    class NavChildFragmentPagerAdapter(fm: androidx.fragment.app.FragmentManager, private val tabTitleList: ArrayList<String>) : androidx.fragment.app.FragmentStatePagerAdapter(fm) {

        lateinit var currentFragment: NavFragment

        override fun getItem(position: Int): androidx.fragment.app.Fragment {
            val navFragment = NavFragment()
            navFragment.navId = position + 4
            return navFragment
        }

        override fun getCount(): Int {
            return tabTitleList.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return tabTitleList[position]
        }

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            currentFragment = `object` as NavFragment
            super.setPrimaryItem(container, position, `object`)
        }
    }

    abstract class HidingScrollListener(private val navFragment: NavFragment) : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
        private var scrolledDistance = 0 //移动的中距离
        private var controlsVisible = true //显示或隐藏

        override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            Log.i("rv", "-1 " + navFragment.rv_nav.canScrollVertically(-1))
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

            if (!navFragment.rv_nav.canScrollVertically(-1) && dy < 0) { // 并且是向上滑动
                onTop()
            }

            if (!navFragment.rv_nav.canScrollVertically(1) && dy > 0) { // 并且是向下滑动
                onBottom()
            }
        }

        abstract fun onHide()
        abstract fun onShow()
        abstract fun onTop()
        abstract fun onBottom()
    }
}

