//package com.christian.home
//
//import android.os.Bundle
//import android.support.v4.content.ContextCompat
//import android.view.View
//import com.christian.NavigationActivity
//import com.christian.R
//import com.christian.adapter.DetailAdapter
//import com.christian.base.BaseFragment
//import com.christian.view.HomeItemDecoration
//import kotlinx.android.synthetic.main.home_frag.*
//import kotlinx.android.synthetic.main.search_edittext.*
//import kotlinx.android.synthetic.main.toolbar_searchbar.*
//import org.xutils.view.annotation.ContentView
//import org.xutils.view.annotation.Event
//
///**
// * Created by Christian on 2017/11/26
// */
//@ContentView(R.layout.home_frag)
//class HomeFrag : BaseFragment() {
//
//    private val mArgParam1 = "fromWho"
//
//    fun newInstance(fromWho: Int): HomeFragment {
//        val fragment = HomeFragment()
//        val args = Bundle()
//        args.putInt(mArgParam1, fromWho)
//        fragment.arguments = args
//        return fragment
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        initData()
//    }
//
//    /**
//     * Generates Strings for RecyclerView's homeAdapter. This data would usually come
//     * from a local content provider or remote server.
//     */
//    private fun initData() {
//
//        var mParam1 = arguments.getString(mArgParam1)
//
//        val dataSet = arrayOf<String>()
//        for (i in 0 until 20) {
//            dataSet[i] = getString(R.string.next_week)
//        }
//
//        initView(dataSet)
//    }
//
//    private fun initView(dataSet: Array<String>) {
//
//        if (arguments.getInt(mArgParam1) == NavigationActivity.ChristianTab.NAVIGATION_HOME.ordinal) {
//            app_bar.visibility = View.VISIBLE
//        } else if (arguments.getInt(mArgParam1) == NavigationActivity.ChristianTab.NAVIGATION_BOOK.ordinal) {
//            app_bar.visibility = View.GONE
//        }
//
//        //Recycler View set homeAdapter
//        val homeAdapter = DetailAdapter(dataSet)
//        recycler_view.adapter = homeAdapter
//        recycler_view.addItemDecoration(HomeItemDecoration(resources.getDimension(R.dimen.search_margin_horizontal).toInt()))
//        var currentLayoutManagerType = BaseFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER
////        setRecyclerViewLayoutManager(currentLayoutManagerType)
//
//        //        if (bottomNavigationActivity != null && bottomNavigationActivity.getSupportActionBar() != null) {
//        //            bottomNavigationActivity.getSupportActionBar().setTitle(getString(R.string.title_home));
//        //        }
//        toolbar_actionbar.title = getString(R.string.title_home)
//        //        if (!added && false) {
//        //            toolbar.inflateMenu(R.menu.menu_gospel);
//        //            added = true;
//        //        }
//
//        val toolbar = (activity as NavigationActivity).actionBarToolbar
//
//        swipe_refresh_layout.setColorSchemeColors(ContextCompat.getColor(context, R.color.colorAccent))
//    }
//
//    /**
//     * Set RecyclerView's LayoutManager to the one given.
//     *
//     * @param layoutManagerType Type of layout manager to switch to.
//     */
////    fun setRecyclerViewLayoutManager(layoutManagerType: BaseFragment.LayoutManagerType) {
////        var scrollPosition = 0
////
////        // If a layout manager has already been set, get current scroll position.
////        if (recycler_view.getLayoutManager() != null) {
////            scrollPosition = (recycler_view.getLayoutManager() as LinearLayoutManager)
////                    .findFirstCompletelyVisibleItemPosition()
////        }
////
////        when (layoutManagerType) {
////            BaseFragment.LayoutManagerType.GRID_LAYOUT_MANAGER -> {
////                layoutManager = GridLayoutManager(activity, SPAN_COUNT)
////                currentLayoutManagerType = BaseFragment.LayoutManagerType.GRID_LAYOUT_MANAGER
////            }
////            BaseFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER -> {
////                layoutManager = LinearLayoutManager(activity)
////                currentLayoutManagerType = BaseFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER
////            }
////            else -> {
////                layoutManager = LinearLayoutManager(activity)
////                currentLayoutManagerType = BaseFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER
////            }
////        }
////
////        recycler_view.setLayoutManager(layoutManager)
////        recycler_view.scrollToPosition(scrollPosition)
////    }
//
//    // For clicking the navigation menu to scroll the recycler view to the top when the menu is checked
//    fun scrollToTop() {
//        // 这里明明可能为Null，每次re-点击首页返回顶部都崩溃
//        recycler_view.smoothScrollToPosition(0)
//        app_bar.setExpanded(true, true)
//    }
//
//    @Event(R.id.search_view_container, R.id.search_magnifying_glass, R.id.search_box_start_search, R.id.search_back_button)
//    private fun onClick(v: View) {
//        when (v.id) {
//            R.id.search_view_container, R.id.search_magnifying_glass, R.id.search_box_start_search -> if (!search_view_container.isExpanded()) {
//                search_view_container.expand(true, true)
//            }
//            R.id.search_back_button -> {
//                if (search_view_container.isExpanded) {
//                    search_view_container.collapse(true)
//                }
//                if (search_view_container.isFadedOut) {
//                    search_view_container.fadeIn()
//                }
//            }
//            else -> {
//            }
//        }
//    }
//}