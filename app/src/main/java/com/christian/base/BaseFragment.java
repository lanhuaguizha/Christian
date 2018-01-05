package com.christian.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.x;

/**
 * author：Administrator on 2017/4/5 00:19
 * email：lanhuaguizha@gmail.com
 */

public abstract class BaseFragment extends Fragment {

    // xUtils官方Copy
    private boolean mInjected;
    // Fragment的View加载完毕的标记
    private boolean isViewCreated;
    // Fragment对用户可见的标记
    private boolean isUIVisible;

//    @ViewInject(R.id.search_view_container)
//    private SearchEditTextLayout mSearchEditTextLayout;

//    @Event({R.id.search_view_container, R.id.search_magnifying_glass, R.id.search_box_start_search, R.id.search_back_button})
//    private void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.search_view_container:
//            case R.id.search_magnifying_glass:
//            case R.id.search_box_start_search:
//                if (!mSearchEditTextLayout.isExpanded()) {
//                    mSearchEditTextLayout.expand(true, true);
//                }
//                break;
//            case R.id.search_back_button:
//                if (mSearchEditTextLayout.isExpanded()) {
//                    mSearchEditTextLayout.collapse(true);
//                }
//                if (mSearchEditTextLayout.isFadedOut()) {
//                    mSearchEditTextLayout.fadeIn();
//                }
//            default:
//                break;
//        }
//    }

    public enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInjected = true;
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!mInjected) {
            x.view().inject(this, this.getView());
        }

        isViewCreated = true;
        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    private void lazyLoad() {
        if (isViewCreated && isUIVisible) {
            loadData();
            // 数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;
        }
    }

    protected abstract void loadData();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //页面销毁,恢复标记
        isViewCreated = false;
        isUIVisible = false;
    }
}
