package com.christian.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.christian.R;
import com.christian.fragment.AccountFragment;
import com.christian.fragment.GospelFragment;
import com.christian.fragment.HomeFragment;
import com.christian.fragment.PeotryFragment;
import com.christian.helper.BottomNavigationViewHelper;
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
    Fragment homeFragment, gospelFragment, poetryFragment, accountFragment;
    private ArrayList<Object> fragments;
    MenuItem prevMenuItem;

    private enum ChristianTab {
        NAVIGATION_HOME, NAVIGATION_BOOK, NAVIGATION_MUSIC,NAVIGATION_ACCOUNT;
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
            BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        }
    }

    private void initView() {
        fragments = new ArrayList<>();
        homeFragment = HomeFragment.newInstance();
        gospelFragment = GospelFragment.newInstance();
        poetryFragment = PeotryFragment.newInstance();
        accountFragment = AccountFragment.newInstance();
        fragments.add(homeFragment);
        fragments.add(gospelFragment);
        fragments.add(poetryFragment);
        fragments.add(accountFragment);
    }

    private void initListener() {
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
                    case R.id.navigation_peotry:
                        viewPager.setCurrentItem(ChristianTab.NAVIGATION_MUSIC.ordinal());
                        return true;
                    case R.id.navigation_account:
                        viewPager.setCurrentItem(ChristianTab.NAVIGATION_ACCOUNT.ordinal());
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
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return (Fragment) fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
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
}
