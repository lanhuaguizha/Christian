package com.christian.detail;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.christian.R;
import com.christian.base.BaseActivity;

import org.xutils.view.annotation.ContentView;

/**
 * author：Administrator on 2017/4/17 01:36
 * email：lanhuaguizha@gmail.com
 */

@ContentView(R.layout.detail_act)
public class DetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        DetailFragment detailFragment = DetailFragment.newInstance(getIntent().getIntExtra("fromPage", -1));
//        ft.replace(R.id.detail_frag_container, detailFragment);
//        ft.commit();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ContentFragment contentFragment = ContentFragment.newInstance();
        ft.replace(R.id.detail_frag_container, contentFragment);
        ft.commit();
    }
}
