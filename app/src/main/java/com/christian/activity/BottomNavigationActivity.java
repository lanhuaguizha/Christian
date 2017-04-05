package com.christian.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.christian.Constant;
import com.christian.R;
import com.christian.fragment.HomeFragment;

public class BottomNavigationActivity extends BaseActivity {

    //    @ViewInject(R.id.navigation)
//    private BottomNavigationView navigation;
//    @ViewInject(R.id.toolbar)
//    private Toolbar toolbar;
    BottomNavigationView navigation;
    private Toolbar toolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentManager fm = getSupportFragmentManager();
                    Fragment fragment = null;
                    if (fm.findFragmentByTag(Constant.NAVIGATION_HOME) != null) {
                        fragment = fm.findFragmentByTag(Constant.NAVIGATION_HOME);
                    } else {
                        fm.beginTransaction().add(R.id.content_fl, HomeFragment.newInstance(), Constant.NAVIGATION_HOME).commit();
                    }
                    switchFragment(fragment);
                    return true;
//                case R.id.navigation_book:
//                    mTextMessage.setText(R.string.title_book);
//                    return true;
//                case R.id.navigation_music:
//                    mTextMessage.setText(R.string.title_music);
//                    return true;
//                case R.id.navigation_video:
//                    mTextMessage.setText(R.string.title_video);
//                    return true;
            }
            return false;
        }

    };

    public void switchFragment(Fragment fragment) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        initView();
        initListener();
    }

    private void initListener() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initView() {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
            toolbar.setLogo(getResources().getDrawable(R.mipmap.ic_launcher));
    }

}
