package com.christian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.christian.Constant;
import com.christian.R;
import com.christian.fragment.BookFragment;
import com.christian.fragment.HomeFragment;
import com.christian.fragment.MusicFragment;
import com.christian.view.CustomViewPage;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_bottom_navigation)
public class BottomNavigationActivity extends BaseActivity {
    private static String TAG = BottomNavigationActivity.class.getSimpleName();
    @ViewInject(R.id.navigation)
    private BottomNavigationView navigation;
    @ViewInject(R.id.content_view_page)
    private CustomViewPage viewPager;
    Fragment homeFragment, bookFragment, musicFragment;
    private ArrayList<Object> fragments;

    private enum ChristianTab {
        NAVIGATION_HOME, NAVIGATION_BOOK, NAVIGATION_MUSIC
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        // Initialize the load
        if (savedInstanceState == null) {
            viewPager.setCurrentItem(ChristianTab.NAVIGATION_HOME.ordinal());
        }
    }

    private void initView() {
        fragments = new ArrayList<>();
        homeFragment = HomeFragment.newInstance();
        bookFragment = BookFragment.newInstance();
        musicFragment = MusicFragment.newInstance();
        fragments.add(homeFragment);
        fragments.add(bookFragment);
        fragments.add(musicFragment);

    }

    private void initListener() {
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        viewPager.setCurrentItem(ChristianTab.NAVIGATION_HOME.ordinal());
                        return true;
                    case R.id.navigation_book:
                        viewPager.setCurrentItem(ChristianTab.NAVIGATION_BOOK.ordinal());
                        return true;
                    case R.id.navigation_music:
                        viewPager.setCurrentItem(ChristianTab.NAVIGATION_MUSIC.ordinal());
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
                navigation.setSelectedItemId(position);
                Log.i(TAG, "onPageSelected: " + position);
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
