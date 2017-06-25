package com.christian.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.christian.R;
import com.christian.activity.LoginActivity;
import com.christian.activity.SettingsActivity;
import com.christian.adapter.MeAdapter;
import com.christian.view.MeItemDecoration;

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
    private OnClickListener mOnClickListener;

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
        view.findViewById(R.id.sign_in).setOnClickListener(mOnClickListener);
    }

    private void initView() {
        toolbar.inflateMenu(R.menu.menu_me);
        mOnClickListener = new OnClickListener();
    }

    private class OnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.sign_in:
                    intent = new Intent(getActivity(), LoginActivity.class);
                    break;
                default:
                    break;
            }
            if (intent != null)
                getActivity().startActivity(intent);
        }
    }

//    @Event({R.id.sign_in})
//    private void onClick(View v) {
//        Intent intent = null;
//        switch (v.getId()) {
//            case R.id.sign_in:
//                intent = new Intent(getActivity(), LoginActivity.class);
//                break;
//            default:
//                break;
//        }
//        if (intent != null)
//            getActivity().startActivity(intent);
//    }
}
