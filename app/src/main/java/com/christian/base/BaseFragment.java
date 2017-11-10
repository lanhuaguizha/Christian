package com.christian.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christian.R;
import com.christian.home.HomeFragment;
import com.christian.view.SearchEditTextLayout;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * author：Administrator on 2017/4/5 00:19
 * email：lanhuaguizha@gmail.com
 */

public class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    @ViewInject(R.id.search_view_container)
    private SearchEditTextLayout mSearchEditTextLayout;

    @Event({R.id.search_view_container, R.id.search_magnifying_glass, R.id.search_box_start_search, R.id.search_back_button})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_view_container:
            case R.id.search_magnifying_glass:
            case R.id.search_box_start_search:
                if (!mSearchEditTextLayout.isExpanded()) {
                    mSearchEditTextLayout.expand(true, true);
                }
                break;
            case R.id.search_back_button:
                if (mSearchEditTextLayout.isExpanded()) {
                    mSearchEditTextLayout.collapse(true);
                }
                if (mSearchEditTextLayout.isFadedOut()) {
                    mSearchEditTextLayout.fadeIn();
                }
            default:
                break;
        }
    }

    public enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    private boolean injected;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        injected = true;
        Log.i(TAG, "onCreateView: >>>>>>>");
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onCreateView: >>>>>>>!injected");
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
