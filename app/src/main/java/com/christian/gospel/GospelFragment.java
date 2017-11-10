package com.christian.gospel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.christian.NavigationActivity;
import com.christian.R;
import com.christian.base.BaseFragment;
import com.christian.home.HomeFragment;

import org.xutils.view.annotation.ContentView;
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
    private ViewPager mViewPager;
    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
//        initListener();
        initData();
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

        mInflater = LayoutInflater.from(getContext());

        //添加页卡视图
        mViewList.add(HomeFragment.newInstance(NavigationActivity.ChristianTab.NAVIGATION_BOOK.ordinal()));
        mViewList.add(HomeFragment.newInstance(NavigationActivity.ChristianTab.NAVIGATION_BOOK.ordinal()));
        mViewList.add(HomeFragment.newInstance(NavigationActivity.ChristianTab.NAVIGATION_BOOK.ordinal()));
        mViewList.add(HomeFragment.newInstance(NavigationActivity.ChristianTab.NAVIGATION_BOOK.ordinal()));
        mViewList.add(HomeFragment.newInstance(NavigationActivity.ChristianTab.NAVIGATION_BOOK.ordinal()));
        mViewList.add(HomeFragment.newInstance(NavigationActivity.ChristianTab.NAVIGATION_BOOK.ordinal()));
        mViewList.add(HomeFragment.newInstance(NavigationActivity.ChristianTab.NAVIGATION_BOOK.ordinal()));
        mViewList.add(HomeFragment.newInstance(NavigationActivity.ChristianTab.NAVIGATION_BOOK.ordinal()));

        //添加页卡标题
        mTitleList.add("马太福音");
        mTitleList.add("马可福音");
        mTitleList.add("路加福音");
        mTitleList.add("约翰福音");
        mTitleList.add("使徒行传");
        mTitleList.add("罗马书");
        mTitleList.add("哥林多前书");
        mTitleList.add("哥林多后书");

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(2)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(3)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(4)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(5)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(6)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(7)));


        MyPagerAdapter mAdapter = new MyPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mViewPager.setOffscreenPageLimit(66);
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
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
