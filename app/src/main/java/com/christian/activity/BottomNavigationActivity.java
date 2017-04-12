package com.christian.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.christian.Constant;
import com.christian.R;
import com.christian.customview.LaunchScreen;
import com.christian.fragment.BookFragment;
import com.christian.fragment.HomeFragment;
import com.christian.fragment.MusicFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_bottom_navigation)
public class BottomNavigationActivity extends BaseActivity {

    @ViewInject(R.id.navigation)
    private BottomNavigationView navigation;

    @ViewInject(R.id.launch_screen)
    private LaunchScreen launchScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
//        LaunchScreen.
    }

    private void initListener() {
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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

        });
    }

}
