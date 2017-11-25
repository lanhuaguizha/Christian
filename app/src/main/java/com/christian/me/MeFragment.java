package com.christian.me;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.christian.R;
import com.christian.base.BaseFragment;
import com.christian.login.LoginActivity;
import com.christian.login.RegisterActivity;
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
    private static final String TAG = MeFragment.class.getSimpleName();
    @ViewInject(R.id.toolbar_actionbar)
    Toolbar mToolbar;
    @ViewInject(R.id.fabMe)
    private FloatingActionButton mFabMe;
    @ViewInject(R.id.app_bar)
    private AppBarLayout mAppBar;

    @Event(value = R.id.nsv_me, type = NestedScrollView.OnScrollChangeListener.class)
    private void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

        if (scrollY > oldScrollY) {
            Log.i(TAG, "Scroll DOWN");
            mFabMe.hide();
        } else {
//            mFabMe.show();
        }
//        if (scrollY < oldScrollY) {
//            Log.i(TAG, "Scroll UP");
//            mFabMe.hide();
//        } else {
//            mFabMe.show();
//        }
//
//        if (scrollY == 0) {
//            Log.i(TAG, "TOP SCROLL");
//            mFabMe.hide();
//        }
//
//        if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
//            Log.i(TAG, "BOTTOM SCROLL");
//            mFabMe.hide();
//        }
    }

    public void scrollToTop() {
//        mAppBar.setExpanded(true, true);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initListener(view);
    }

    private void initListener(View view) {
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i(TAG, "onMenuItemClick: ");
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

    @Event({R.id.sign_in, R.id.register, R.id.fabMe})
    private void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.sign_in:
                intent = new Intent(getActivity(), LoginActivity.class);
                break;
            case R.id.register:
                intent = new Intent(getActivity(), RegisterActivity.class);
                break;
            case R.id.fabMe:
                Snackbar snackbar = Snackbar.make(v, "写下你的问题", Snackbar.LENGTH_LONG);
//                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
                ((TextView) snackbar.getView().findViewById(R.id.snackbar_text)).setTextColor(getResources().getColor(R.color.white));
                snackbar.setActionTextColor(getResources().getColor(R.color.white));
                snackbar.setAction("好的", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "好的", Toast.LENGTH_SHORT).show();
                        ;
                    }
                });
                snackbar.show();
                break;
            default:
                break;
        }
        if (intent != null)
            getActivity().startActivity(intent);
    }
}
