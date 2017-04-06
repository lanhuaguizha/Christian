package com.christian.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.christian.Constant;
import com.christian.R;
import com.christian.fragment.BookFragment;
import com.christian.fragment.HomeFragment;
import com.christian.fragment.MusicFragment;
import com.christian.fragment.VideoFragment;

public class BottomNavigationActivity extends BaseActivity {

    //    @ViewInject(R.id.navigation)
//    private BottomNavigationView navigation;
//    @ViewInject(R.id.toolbar)
//    private Toolbar toolbar;
    BottomNavigationView navigation;
    private Toolbar toolbar;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment homeFragment = fm.findFragmentByTag(Constant.NAVIGATION_HOME);
            Fragment bookFragment = fm.findFragmentByTag(Constant.NAVIGATION_BOOK);
            Fragment musicFragment = fm.findFragmentByTag(Constant.NAVIGATION_MUSIC);
            Fragment videoFragment = fm.findFragmentByTag(Constant.NAVIGATION_VIDEO);
            if (homeFragment != null) {
                ft.hide(homeFragment);
            }
            if (bookFragment != null) {
                ft.hide(bookFragment);
            }
            if (musicFragment != null) {
                ft.hide(musicFragment);
            }
            if (videoFragment != null) {
                ft.hide(videoFragment);
            }
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (homeFragment == null) {
                        homeFragment = HomeFragment.newInstance();
                        ft.add(R.id.content_fl, homeFragment, Constant.NAVIGATION_HOME);
                    } else {
                        ft.show(homeFragment);
                    }
                    ft.commit();
                    return true;
                case R.id.navigation_book:
                    if (bookFragment == null) {
                        bookFragment = BookFragment.newInstance();
                        ft.add(R.id.content_fl, bookFragment, Constant.NAVIGATION_BOOK);
                    } else {
                        ft.show(bookFragment);
                    }
                    ft.commit();
                    return true;
                case R.id.navigation_music:
                    if (musicFragment == null) {
                        musicFragment = MusicFragment.newInstance();
                        ft.add(R.id.content_fl, musicFragment, Constant.NAVIGATION_MUSIC);
                    } else {
                        ft.show(musicFragment);
                    }
                    ft.commit();
                    return true;
//                case R.id.navigation_video:
//                    if (videoFragment == null) {
//                        videoFragment = VideoFragment.newInstance();
//                        ft.add(R.id.content_fl, videoFragment, Constant.NAVIGATION_VIDEO);
//                    } else {
//                        ft.show(videoFragment);
//                    }
//                    ft.commit();
//                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        initView();
        initListener();
        // Initialize the load
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment homeFragment = HomeFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.content_fl, homeFragment, Constant.NAVIGATION_HOME).commit();
        }
    }

    private void initView() {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.app_name));
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
            toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        }
    }

    private void initListener() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BottomNavigationActivity.this, "被点击", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}
