package com.christian;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.christian.base.BaseActivity;
import com.christian.gospel.GospelFragment;
import com.christian.home.HomeFragment;
import com.christian.me.MeFragment;
import com.christian.swipeback.SwipeBackHelper;
import com.christian.view.CustomViewPage;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

@ContentView(R.layout.navigation_act)
public class NavigationActivity extends BaseActivity {

    @ViewInject(R.id.navigation)
    private BottomNavigationView mBottomNavigationView;
    @ViewInject(R.id.content_view_page)
    private CustomViewPage mCustomViewPager;
    private long exitTime = 0;
    MenuItem mPrevMenuItem;
    private static final int DEFAULT_OFFSCREEN_PAGES = 2;
    private HomeFragment mHomeFragment;
    private GospelFragment mGospelFragment;
    private MeFragment mMeFragment;
    private ArrayList<Fragment> fragments;
    private CustomFragmentPagerAdapter adapter;
    @ViewInject(R.id.navigation_act_fab)
    private FloatingActionButton mNavigationActFab;

    public enum ChristianTab {
        NAVIGATION_HOME, NAVIGATION_BOOK, NAVIGATION_MUSIC, NAVIGATION_ME;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true);

        if (savedInstanceState != null && savedInstanceState.size() != 0) {
        } else {
            mHomeFragment = HomeFragment.newInstance(ChristianTab.NAVIGATION_HOME.ordinal());
            mGospelFragment = GospelFragment.newInstance();
            mMeFragment = MeFragment.newInstance();
        }
        initView();
        initListener();
    }

    private void initView() {
        fragments = new ArrayList<>();

        fragments.add(mHomeFragment);
        fragments.add(mGospelFragment);
        fragments.add(mMeFragment);

        // To remain 4 tabs fragments
        mCustomViewPager.setOffscreenPageLimit(DEFAULT_OFFSCREEN_PAGES);
        adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        mCustomViewPager.setAdapter(adapter);
        mCustomViewPager.setCurrentItem(ChristianTab.NAVIGATION_HOME.ordinal());
//        NavigationViewHelper.disableShiftMode(mBottomNavigationView);
    }

    private void initListener() {
        mBottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch ((item.getItemId())) {
                    case R.id.navigation_home:
                        ((HomeFragment) fragments.get(0)).scrollToTop();
                        break;
                    case R.id.navigation_gospel:
                        ((GospelFragment) fragments.get(1)).scrollToTop();
                        break;
                    case R.id.navigation_me:
                        ((MeFragment) fragments.get(2)).scrollToTop();
                        break;
                    default:
                        break;
                }
            }
        });
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        mCustomViewPager.setCurrentItem(ChristianTab.NAVIGATION_HOME.ordinal());
                        return true;
                    case R.id.navigation_gospel:
                        mCustomViewPager.setCurrentItem(ChristianTab.NAVIGATION_BOOK.ordinal());
                        return true;
                    case R.id.navigation_me:
                        mCustomViewPager.setCurrentItem(ChristianTab.NAVIGATION_ME.ordinal());
                        return true;
                }
                return false;
            }

        });
        mCustomViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.i(TAG, "onPageScrolled: ");
            }

            @Override
            public void onPageSelected(int position) {
                // 底部栏颜色变化
                if (mPrevMenuItem != null) {
                    mPrevMenuItem.setChecked(false);
                } else {
                    mBottomNavigationView.getMenu().getItem(ChristianTab.NAVIGATION_HOME.ordinal()).setChecked(false);
                }
                mBottomNavigationView.getMenu().getItem(position).setChecked(true);
                mPrevMenuItem = mBottomNavigationView.getMenu().getItem(position);

                //Fab显示隐藏
                if (position == mCustomViewPager.getChildCount() - 1) {
                    mNavigationActFab.show();
                } else {
                    mNavigationActFab.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Log.i(TAG, "onPageScrollStateChanged: ");
            }
        });
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
////        Toolbar toolbar = getActionBarToolbar();
//        toolbar.inflateMenu(R.menu.menu_share_and_more);
//        toolbar.setOnMenuItemClickListener(this);
//        return true;
//    }
//
//    @Override
//    public boolean onMenuItemClick(MenuItem menuItem) {
//        switch (menuItem.getItemId()) {
//            case R.id.menu_share:
//                return true;
//            case R.id.menu_more:
////                startActivity(new Intent(this, SearchActivity.class));
//                return true;
//        }
//        return false;
//    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - exitTime) < 2000) {
            super.onBackPressed();
        } else {
            Snackbar snackbar = Snackbar.make(mCustomViewPager, R.string.double_click_exit, Snackbar.LENGTH_SHORT);
            ((TextView) snackbar.getView().findViewById(R.id.snackbar_text)).setTextColor(getResources().getColor(R.color.white));
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
            snackbar.setAction("确定", null).show();
            exitTime = currentTime;
        }
    }

    private class CustomFragmentPagerAdapter extends FragmentPagerAdapter {

        CustomFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }
}
