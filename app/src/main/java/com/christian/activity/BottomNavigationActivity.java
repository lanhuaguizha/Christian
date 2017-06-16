package com.christian.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.christian.R;
import com.christian.fragment.GospelFragment;
import com.christian.fragment.HomeFragment;
import com.christian.fragment.MeFragment;
import com.christian.swipebacksupport.SwipeBackHelper;
import com.christian.view.CustomViewPage;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

@ContentView(R.layout.activity_bottom_navigation)
public class BottomNavigationActivity extends BaseActivity {
    private static String TAG = BottomNavigationActivity.class.getSimpleName();
    @ViewInject(R.id.navigation)
    private BottomNavigationView bottomNavigationView;
    @ViewInject(R.id.content_view_page)
    private CustomViewPage viewPager;
    MenuItem prevMenuItem;
    private static final int DEFAULT_OFFSCREEN_PAGES = 2;
    private HomeFragment homeFragment;
    private GospelFragment gospelFragment;
    private MeFragment meFragment;

    private enum ChristianTab {
        NAVIGATION_HOME, NAVIGATION_BOOK, NAVIGATION_MUSIC, NAVIGATION_ME;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true);
        initView();
        initListener();
        // Initialize the load
        if (savedInstanceState == null) {
            viewPager.setCurrentItem(ChristianTab.NAVIGATION_HOME.ordinal());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // To remain 4 tabs fragments
        viewPager.setOffscreenPageLimit(DEFAULT_OFFSCREEN_PAGES);
//        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
    }

    private void initView() {
    }

    private void initListener() {
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch ((item.getItemId())) {
                    case R.id.navigation_home:
                        homeFragment.scrollToPosition();
                        break;
                    default:
                        break;
                }
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        viewPager.setCurrentItem(ChristianTab.NAVIGATION_HOME.ordinal());
                        return true;
                    case R.id.navigation_gospel:
                        viewPager.setCurrentItem(ChristianTab.NAVIGATION_BOOK.ordinal());
                        return true;
                    case R.id.navigation_account:
                        viewPager.setCurrentItem(ChristianTab.NAVIGATION_ME.ordinal());
                        return true;
                }
                return false;
            }

        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.i(TAG, "onPageScrolled: ");
            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(ChristianTab.NAVIGATION_HOME.ordinal()).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Log.i(TAG, "onPageScrollStateChanged: ");
            }
        });
        viewPager.setAdapter(new CustomFragmentPagerAdapter(getSupportFragmentManager()));
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
    private class CustomFragmentPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private int mCurrentPosition = -1;

        CustomFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            homeFragment = HomeFragment.newInstance();
            gospelFragment = GospelFragment.newInstance();
            meFragment = MeFragment.newInstance();
            fragments.add(homeFragment);
            fragments.add(gospelFragment);
            fragments.add(meFragment);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            if (position != mCurrentPosition) {
                Fragment fragment = (Fragment) object;
                CustomViewPage pager = (CustomViewPage) container;
                if (fragment != null && fragment.getView() != null) {
                    mCurrentPosition = position;
                    pager.measureFragment(fragment.getView());
                }
            }
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
}
