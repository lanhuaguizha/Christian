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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.christian.BottomNavigationActivity;
import com.christian.R;
import com.christian.base.BaseFragment;
import com.christian.home.EmptyFragment;
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
    public static final int OFFSCREEN_PAGE_LIMIT = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @ViewInject(R.id.toolbar_actionbar)
    Toolbar mToolbar;
    private boolean isAdded;
    @ViewInject(R.id.tabs)
    private TabLayout mTabLayout;
    @ViewInject(R.id.vp_view)
    private ViewPager mViewPager;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    //    private List<String> mTitleList = new ArrayList<>(Arrays.asList("创世纪", "出埃及记", "利未记", "民数记", "申命记", "约书亚记"));//页卡标题集合
    private List<EmptyFragment> mViewList = new ArrayList<>();//页卡视图集合
    @ViewInject(R.id.app_bar)
    AppBarLayout mAppBar;
    @ViewInject(R.id.search_view_container)
    private SearchEditTextLayout mSearchEditTextLayout;
    private EmptyFragment homeFragment;
    private static final String MFRAGMENTNAMELIST = "mViewList";

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
        return new GospelFragment();
    }

    @Override
    protected void loadData() {
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
        loadView();
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
            loadView();
        }
    }

    private void loadView() {
        //新建页卡视图
        for (int i = 0; i < mTitleList.size(); i++) {
            EmptyFragment homeFragment = EmptyFragment.newInstance(getContext());
//            EmptyFragment homeFragment = EmptyFragment.newInstance(BottomNavigationActivity.ChristianTab.NAVIGATION_BOOK.ordinal());
            mViewList.add(homeFragment);
        }
        mToolbar.setTitle(getString(R.string.title_book));
        homeFragment = mViewList.get(0);

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>copy





        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//设置tab模式，当前为系统默认模式
        for (int i = 0; i < 27; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(i)));//添加tab选项卡
        }

        MyPagerAdapter mAdapter = new MyPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mViewPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。

        mTabLayout.setVisibility(View.VISIBLE);

        // Below are views' listeners
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

    public void scrollToTop() {
//        homeFragment.scrollToTop();
        mAppBar.setExpanded(true, true);
    }

    //ViewPager适配器
    class MyPagerAdapter extends FragmentPagerAdapter {

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }
}
