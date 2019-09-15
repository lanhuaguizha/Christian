package com.christian.nav

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.paging.PagedList
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.christian.R
import com.christian.data.Gospel
import com.christian.data.Setting
import com.christian.view.ItemDecoration
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.nav_activity.*
import kotlinx.android.synthetic.main.nav_fragment.*
import kotlinx.android.synthetic.main.nav_fragment.view.*
import kotlinx.android.synthetic.main.nav_item_me_portrait.*
import org.jetbrains.anko.debug

open class NavFragment : androidx.fragment.app.Fragment(), NavContract.INavFragment {

    override fun onSaveInstanceState(outState: Bundle) {
    }
//
//    override fun onGospelSelected(gospel: DocumentSnapshot) {
//        // Go to the details page for the selected restaurant
//        gospelId = gospel.id
//        val intent = Intent(this@NavFragment.navActivity, NavDetailActivity::class.java)
//        intent.putExtra(toolbarTitle, gospel.data?.get("subtitle").toString())
//        startActivity(intent)
//    }

    lateinit var navActivity: NavActivity
    private lateinit var ctx: Context
    //    private lateinit var navAdapter: NavItemPresenter<*>
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

    private var isInitView = false
    private var isVisibled = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.nav_fragment, container, false)
        isInitView = true
        isCanLoadData()

        return v
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见，获取该标志记录下来
        if (isVisibleToUser) {
            isVisibled = true
            isCanLoadData()
        } else {
            isVisibled = false
        }
    }

    private fun isCanLoadData() {
        //所以条件是view初始化完成并且对用户可见
        if (isInitView && isVisibled) {
            initView()

//            lazyLoad()

            //防止重复加载数据
            isInitView = false
            isVisibled = false
        }
    }

    override fun initView() {
        debug { "nav fragment is $this and navId is $navId --initView" }
        when (navId) {
            VIEW_ME -> {
                initPortrait()
            }
        }
        if (navId == VIEW_GOSPEL) {
            v.vp1_nav.visibility = View.VISIBLE
            v.srl_nav.visibility = View.GONE
            v.pb_nav.visibility = View.GONE
            initVp(navActivity.tabTitleList)
        } else {
            v.vp1_nav.visibility = View.GONE
            v.srl_nav.visibility = View.VISIBLE
            initSrl()
        }
        initRv()
    }

    private fun initSrl() {
    }

    private fun initPortrait() {
        Glide.with(navActivity).load(R.drawable.me).into(navActivity.iv_nav_item_small)
    }

    private lateinit var navFragment: NavFragment

    private var pageSelectedPosition: Int = -1

    private fun initVp(tabTitleList: ArrayList<String>) {
        val navChildFragmentPagerAdapter = NavChildFragmentPagerAdapter(childFragmentManager, tabTitleList)
        v.vp1_nav.adapter = navChildFragmentPagerAdapter
        navActivity.tl_nav.setupWithViewPager(v.vp1_nav)//将TabLayout和ViewPager关联起来

        v.vp1_nav.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                pageSelectedPosition = position
                navFragment = navChildFragmentPagerAdapter.currentFragment
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }

    var isPageTop: Boolean = true

    var isPageBottom: Boolean = false

    private lateinit var gospelAdapter: FirestorePagingAdapter<Gospel, NavItemView>
    private lateinit var meAdapter: FirestoreRecyclerAdapter<Setting, NavItemView>

    override fun onDestroyView() {
        super.onDestroyView()
        if (navId == VIEW_HOME)
            gospelAdapter.stopListening()
        if (navId == VIEW_ME && ::meAdapter.isInitialized)
            meAdapter.stopListening()
    }

    private fun initRv() {
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build()

        when (navId) {
            VIEW_HOME, VIEW_GOSPEL -> {
                val query = navActivity.firestore.collection("gospels")
                query.orderBy("subtitle", Query.Direction.ASCENDING)
                val options = FirestorePagingOptions.Builder<Gospel>()
//                        .setLifecycleOwner(this@NavFragment)
                        .setQuery(query, config, Gospel::class.java)
                        .build()
                gospelAdapter = object : FirestorePagingAdapter<Gospel, NavItemView>(options) {
                    @NonNull
                    override fun onCreateViewHolder(@NonNull parent: ViewGroup,
                                                    viewType: Int): NavItemView {
                        val view = LayoutInflater.from(parent.context)
                                .inflate(R.layout.nav_item_gospel, parent, false)
                        return NavItemView(view)
                    }

                    override fun onBindViewHolder(@NonNull holder: NavItemView,
                                                  position: Int,
                                                  @NonNull model: Gospel) {
                        applyViewHolderAnimation(holder)
                        holder.bind(model)
                    }

                    override fun onLoadingStateChanged(@NonNull state: LoadingState) {
                        when (state) {
                            LoadingState.LOADING_INITIAL, LoadingState.LOADING_MORE -> pb_nav.visibility = View.VISIBLE
                            LoadingState.LOADED -> {
                                v.pb_nav.visibility = View.GONE
                                rv_nav.scheduleLayoutAnimation()
                            }
                            LoadingState.FINISHED -> {
                                pb_nav.visibility = View.GONE
                                navActivity.snackbar(getString(R.string.finished)).show()
//                                showToast()
                            }
                            LoadingState.ERROR -> {
                                navActivity.snackbar(getString(R.string.error)).show()
//                                showToast()
                                retry()
                            }
                        }
                    }
                }
                gospelAdapter.startListening()
                v.rv_nav.adapter = gospelAdapter
            }
            VIEW_GOSPEL -> {
            }
            VIEW_DISCIPLE -> {
            }
            VIEW_ME -> {
                meAdapter = firestoreRecyclerAdapter()
                meAdapter.startListening()
                v.rv_nav.adapter = meAdapter
            }
        }
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
//                hideFab()
                controlOverScroll(navActivity, navActivity.abl_nav, navActivity.verticalOffset)
            }

            override fun onBottom() {
                isPageBottom = true
                controlOverScroll(navActivity, navActivity.abl_nav, navActivity.verticalOffset)
            }
        })

        val controller =
                AnimationUtils.loadLayoutAnimation(navActivity, R.anim.layout_animation_from_right)
        v.rv_nav.layoutAnimation = controller
    }

    private fun firestoreRecyclerAdapter(): FirestoreRecyclerAdapter<Setting, NavItemView> {
        val query = navActivity.firestore.collection("mes").orderBy("id", Query.Direction.ASCENDING)
        val options = FirestoreRecyclerOptions.Builder<Setting>()
                .setQuery(query, Setting::class.java)
//                .setLifecycleOwner(navActivity)
                .build()

        return object : FirestoreRecyclerAdapter<Setting, NavItemView>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavItemView {
                return NavItemView(LayoutInflater.from(parent.context)
                        .inflate(R.layout.nav_item_me, parent, false))
            }

            override fun onBindViewHolder(holder: NavItemView, position: Int, model: Setting) {
                holder.bind(model)
            }

            override fun onDataChanged() {
                // If there are no chat messages, show a view that invites the user to add a message.
//                mEmptyListMessage.setVisibility(if (itemCount == 0) View.VISIBLE else View.GONE)
                if (itemCount == 0) {
                } else {
                    rv_nav.scheduleLayoutAnimation()
                    pb_nav.visibility = View.GONE
                }
            }
        }
    }

    fun showToast(message: String) {
        Toast.makeText(navActivity, message, Toast.LENGTH_SHORT).show();
    }

    override fun showFab() {
        navActivity.showFAB()
//        if (navId == 1 && cv_nav_frag.visibility == View.GONE) {
//            cv_nav_frag.visibility = View.VISIBLE
//            val fadeIn = AnimationUtils.loadAnimation(context, R.anim.abc_fade_in)
//            cv_nav_frag.startAnimation(fadeIn)
//        }
    }

    override fun hideFab() {
        navActivity.hideFab()
//        if (navId == 1 && cv_nav_frag.visibility == View.VISIBLE) {
//            val fadeOut = AnimationUtils.loadAnimation(context, R.anim.abc_fade_out)
//            cv_nav_frag.startAnimation(fadeOut)
//            cv_nav_frag.visibility = View.GONE
//        }
    }

//
//    private fun runLayoutAnimation(recyclerView: androidx.recyclerview.widget.RecyclerView) {
//        val animation = AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.layout_animation_from_right)
//        recyclerView.layoutAnimation = animation
//        recyclerView.gospelAdapter?.notifyDataSetChanged()
//        recyclerView.scheduleLayoutAnimation()
//    }

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



