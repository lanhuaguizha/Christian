package com.christian.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.christian.R;
import com.christian.activity.LoginActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * author：Administrator on 2017/4/2 00:20
 * email：lanhuaguizha@gmail.com
 */
@ContentView(R.layout.fragment_me)
public class MeFragment extends BaseFragment {
    private static final String TAG = MeFragment.class.getSimpleName();
    @ViewInject(R.id.toolbar_actionbar)
    Toolbar toolbar;
    @ViewInject(R.id.nsv_me)
    private NestedScrollView nsvMe;

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
        initView();
        initListener(view);
    }

    private void initListener(View view) {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i(TAG, "onMenuItemClick: ");
//                Intent intentSetting = new Intent(getActivity(), SettingsActivity.class);
//                startActivity(intentSetting);
                return false;
            }
        });
    }

    private void initView() {
        toolbar.inflateMenu(R.menu.menu_me);
    }

    @Event({R.id.sign_in, R.id.fab})
    private void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.sign_in:
                intent = new Intent(getActivity(), LoginActivity.class);
                break;
            case R.id.fab:
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                break;
            default:
                break;
        }
        if (intent != null)
            getActivity().startActivity(intent);
    }
}
