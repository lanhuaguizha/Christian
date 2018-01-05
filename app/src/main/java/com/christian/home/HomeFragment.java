package com.christian.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.christian.BottomNavigationActivity;
import com.christian.R;
import com.christian.adapter.ContentItemViewAdapter;
import com.christian.base.BaseFragment;
import com.christian.view.HomeItemDecoration;
import com.christian.view.SearchEditTextLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * author：Administrator on 2017/4/2 00:19
 * email：lanhuaguizha@gmail.com
 */

@ContentView(R.layout.home_frag)
public class HomeFragment extends BaseFragment {

    private static final String FROM_PAGE = "fromPage";
    private int mFromPage;
    protected ContentItemViewAdapter homeAdapter;
    protected RecyclerView.LayoutManager layoutManager;
    protected LayoutManagerType currentLayoutManagerType;
    private String[] dataSet;
    @ViewInject(R.id.home_frag_swipe_refresh_layout)
    private SwipeRefreshLayout mHomeFragSwipeRefreshLayout;
    @ViewInject(R.id.toolbar_actionbar)
    private Toolbar mToolbarActionbar;
    private static final int SPAN_COUNT = 2;
    private static final int DATA_SET_COUNT = 20;
    @ViewInject(R.id.home_frag_recycler_view)
    private RecyclerView mHomeFragRecyclerView;
    @ViewInject(R.id.home_frag_app_bar_layout)
    private AppBarLayout mHomeFragAppBarLayout;
    public static final int TOP = 0;
    @ViewInject(R.id.search_view_container)
    private SearchEditTextLayout mSearchEditTextLayout;

    @Event({R.id.search_view_container, R.id.search_magnifying_glass, R.id.search_box_start_search, R.id.search_back_button})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_view_container:
            case R.id.search_magnifying_glass:
            case R.id.search_box_start_search:
                if (!mSearchEditTextLayout.isExpanded()) {
                    mSearchEditTextLayout.expand(true, true);
                }
                break;
            case R.id.search_back_button:
                if (mSearchEditTextLayout.isExpanded()) {
                    mSearchEditTextLayout.collapse(true);
                }
                if (mSearchEditTextLayout.isFadedOut()) {
                    mSearchEditTextLayout.fadeIn();
                }
            default:
                break;
        }
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(Integer fromPage) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(FROM_PAGE, fromPage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadData() {
        initDataSet();
        initData();
        initView();
        initListener();
    }

    private void initListener() {
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (navigation.getVisibility() != View.VISIBLE) {
////                    SnackbarHelper.dismissSnackbar();
//                    navigation.setVisibility(View.VISIBLE);
//                } else {
////                    SnackbarHelper.showSnackbar(v, getString(R.string.version));
//                    navigation.setVisibility(View.GONE);
//                }
//            }
//        });

        mToolbarActionbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_share:
                        break;
                    case R.id.menu_more:
                        break;
                }
                return true;
            }
        });

        mHomeFragSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        List<String> newDatas = new ArrayList<String>();
//                        for (int i = 0; i <5; i++) {
//                            int index = i + 1;
//                            newDatas.add("new item" + index);
//                        }
//                        homeAdapter.addItem(newDatas);
                        mHomeFragSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 700);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initView();
        }
    }

    private void initView() {

        if (mFromPage == BottomNavigationActivity.ChristianTab.NAVIGATION_HOME.ordinal()) {
            mHomeFragAppBarLayout.setVisibility(View.VISIBLE);
        } else if (mFromPage == BottomNavigationActivity.ChristianTab.NAVIGATION_BOOK.ordinal()) {
            mHomeFragAppBarLayout.setVisibility(View.GONE);
        }

        homeAdapter = new ContentItemViewAdapter(dataSet);
        mHomeFragRecyclerView.setAdapter(homeAdapter);
        mHomeFragRecyclerView.addItemDecoration(new HomeItemDecoration((int) getResources().getDimension(R.dimen.search_margin_horizontal)));
        currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        setRecyclerViewLayoutManager(currentLayoutManagerType);

//        if (bottomNavigationActivity != null && bottomNavigationActivity.getSupportActionBar() != null) {
//            bottomNavigationActivity.getSupportActionBar().setTitle(getString(R.string.title_home));
//        }
        mToolbarActionbar.setTitle(getString(R.string.title_home));
//        if (!added && false) {
//            mToolbarActionbar.inflateMenu(R.menu.menu_gospel);
//            added = true;
//        }

        Toolbar toolbar = ((BottomNavigationActivity) getActivity()).getActionBarToolbar();

        mHomeFragSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorAccent));
    }

    private void initData() {
        if (getArguments() != null) {
            mFromPage = getArguments().getInt(FROM_PAGE);
        }
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mHomeFragRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mHomeFragRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                layoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                currentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                layoutManager = new LinearLayoutManager(getActivity());
                currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                layoutManager = new LinearLayoutManager(getActivity());
                currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mHomeFragRecyclerView.setLayoutManager(layoutManager);
        mHomeFragRecyclerView.scrollToPosition(scrollPosition);
    }

    /**
     * Generates Strings for RecyclerView's homeAdapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataSet() {
        dataSet = new String[DATA_SET_COUNT];
        for (int i = 0; i < DATA_SET_COUNT; i++) {
            dataSet[i] = getString(R.string.next_week) + i;
        }
    }

    // For clicking the navigation menu to scroll the recycler view to the top
    public void scrollToTop() {
        mHomeFragRecyclerView.scrollToPosition(TOP);
        mHomeFragAppBarLayout.setExpanded(true, true);
    }
}
