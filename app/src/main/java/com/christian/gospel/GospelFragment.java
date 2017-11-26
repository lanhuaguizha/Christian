package com.christian.gospel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.christian.NavigationActivity;
import com.christian.R;
import com.christian.base.BaseFragment;
import com.christian.home.HomeFragment;
import com.christian.view.CustomSubViewPage;
import com.christian.view.SearchEditTextLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * author：Administrator on 2017/4/2 00:20
 * email：lanhuaguizha@gmail.com
 */

@ContentView(R.layout.gospel_frag)
public class GospelFragment extends BaseFragment {
    //    @ViewInject(R.id.swipe_refresh_layout)
//    SwipeRefreshLayout swipeRefreshLayout;
    private static final String TAG = "GospelFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @ViewInject(R.id.toolbar_actionbar)
    Toolbar mToolbar;
    private boolean isAdded;
    @ViewInject(R.id.tabs)
    private TabLayout mTabLayout;
    @ViewInject(R.id.vp_view)
    private CustomSubViewPage mViewPager;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<HomeFragment> mViewList = new ArrayList<>();//页卡视图集合
    @ViewInject(R.id.app_bar)
    AppBarLayout mAppBar;

    @ViewInject(R.id.search_view_container)
    private SearchEditTextLayout mSearchEditTextLayout;
    private HomeFragment homeFragment;

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

    public GospelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PlusOneFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static HomeFragment newInstance(String param1, String param2) {
//        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
    public static GospelFragment newInstance() {
        GospelFragment fragment = new GospelFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void scrollToTop() {
        if (homeFragment != null) {
            homeFragment.scrollToTop();
        }
        if (mAppBar != null) {
            mAppBar.setExpanded(true, true);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
        initData();
    }

    private void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                homeFragment = mViewList.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

//    private void initListener() {
//        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                switch (menuItem.getItemId()) {
//                    case R.id.menu_search:
//                        Log.i(TAG, "onMenuItemClick: menu_search is clicked");
//                        startActivity(new Intent(getActivity(), SearchActivity.class));
//                        break;
//                    default:
//                        break;
//                }
//                return true;
//            }
//        });
//    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initView();
        }
    }

    private void initView() {
        mToolbar.setTitle(getString(R.string.title_book));
        // Removing more of Book Fragment
//            if (!isAdded) {
//                mToolbar.inflateMenu(R.menu.menu_gospel);
//                isAdded = true;
//            }

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>copy

        //添加页卡视图
        for (int i = 0; i < 27; i++) {
            HomeFragment homeFragment = HomeFragment.newInstance(NavigationActivity.ChristianTab.NAVIGATION_BOOK.ordinal());
            mViewList.add(homeFragment);
        }
//        mViewList.add(HomeFragment.newInstance(NavigationActivity.ChristianTab.NAVIGATION_BOOK.ordinal()));
//        mViewList.add(HomeFragment.newInstance(NavigationActivity.ChristianTab.NAVIGATION_BOOK.ordinal()));
//        mViewList.add(HomeFragment.newInstance(NavigationActivity.ChristianTab.NAVIGATION_BOOK.ordinal()));
//        mViewList.add(HomeFragment.newInstance(NavigationActivity.ChristianTab.NAVIGATION_BOOK.ordinal()));
//        mViewList.add(HomeFragment.newInstance(NavigationActivity.ChristianTab.NAVIGATION_BOOK.ordinal()));
//        mViewList.add(HomeFragment.newInstance(NavigationActivity.ChristianTab.NAVIGATION_BOOK.ordinal()));
//        mViewList.add(HomeFragment.newInstance(NavigationActivity.ChristianTab.NAVIGATION_BOOK.ordinal()));

        //添加页卡标题
        mTitleList.add("马太福音");
        mTitleList.add("马可福音");
        mTitleList.add("路加福音");
        mTitleList.add("约翰福音");
        mTitleList.add("使徒行传");
        mTitleList.add("罗马书");
        mTitleList.add("哥林多前书");
        mTitleList.add("哥林多后书");
        mTitleList.add("加拉太书");
        mTitleList.add("以弗所书");
        mTitleList.add("腓立比书");
        mTitleList.add("歌罗西书");
        mTitleList.add("帖撒罗尼迦前书");
        mTitleList.add("帖撒罗尼迦后书");
        mTitleList.add("提摩太前书");
        mTitleList.add("提摩太后书");
        mTitleList.add("提多书");
        mTitleList.add("腓利门书");
        mTitleList.add("希伯来书");
        mTitleList.add("雅各书");
        mTitleList.add("彼得前书");
        mTitleList.add("彼得后书");
        mTitleList.add("约翰一书");
        mTitleList.add("约翰二书");
        mTitleList.add("约翰三书");
        mTitleList.add("犹太书");
        mTitleList.add("启示录");

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//设置tab模式，当前为系统默认模式
        for (int i = 0; i < 27; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(i)));//添加tab选项卡
        }
//        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
//        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(2)));
//        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(3)));
//        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(4)));
//        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(5)));
//        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(6)));
//        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(7)));


        MyPagerAdapter mAdapter = new MyPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mViewPager.setOffscreenPageLimit(0);
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mViewPager.setCurrentItem(1);

        mViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(0);
            }
        },100);
//        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器 //用setupWithViewPager就足够了
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>copy
    }

    private void initData() {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    //ViewPager适配器
    class MyPagerAdapter extends FragmentPagerAdapter {

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            mViewPager.setPosition(position);
            return mViewList.get(position);
        }


        @Override
        public int getCount() {
            return mViewList.size();//页卡数
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);//页卡标题
        }

    }

}
