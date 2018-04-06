package com.christian.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.christian.R;
import com.christian.base.BaseFragment;
import com.christian.setting.SettingsActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * author：Administrator on 2017/4/2 00:20
 * email：lanhuaguizha@gmail.com
 */
@ContentView(R.layout.me_frag)
public class MeFragment extends BaseFragment {
    @ViewInject(R.id.toolbar_actionbar)
    Toolbar mToolbar;
    @ViewInject(R.id.app_bar)
    private AppBarLayout mAppBar;

    @Override
    protected void loadData() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initListener(view);
    }

    @Event(value = R.id.nsv_me, type = NestedScrollView.OnScrollChangeListener.class)
    private void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

//        if (scrollY > oldScrollY) {
////            Log.i(TAG, "Scroll DOWN");
////            mMeFragFab.hide();
//        } else {
////            mMeFragFab.show();
//        }
//        if (scrollY < oldScrollY) {
//            Log.i(TAG, "Scroll UP");
//            mMeFragFab.hide();
//        } else {
//            mMeFragFab.show();
//        }
//
//        if (scrollY == 0) {
//            Log.i(TAG, "TOP SCROLL");
//            mMeFragFab.hide();
//        }
//
//        if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
//            Log.i(TAG, "BOTTOM SCROLL");
//            mMeFragFab.hide();
//        }
    }

    public void scrollToTop() {
        mAppBar.setExpanded(true, true);
    }

    public MeFragment() {
        // Required empty public constructor
    }

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void initListener(View view) {
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intentSetting = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intentSetting);
                return false;
            }
        });
    }

    private void initView(View view) {
        mToolbar.inflateMenu(R.menu.menu_me);
        ((TextView) view.findViewById(R.id.favorite).findViewById(R.id.tv)).setText("阅读");
        ((TextView) view.findViewById(R.id.read).findViewById(R.id.tv)).setText("收藏");
    }

    @Event({R.id.sign_in, R.id.register})
    private void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.sign_in:
                break;
            case R.id.register:
                break;
            default:
                break;
        }
        if (intent != null)
            getActivity().startActivity(intent);
    }
}
