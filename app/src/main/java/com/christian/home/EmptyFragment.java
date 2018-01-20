package com.christian.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.christian.R;
import com.christian.adapter.ContentItemViewAdapter;
import com.christian.base.BaseFragment;
import com.christian.view.ItemDecoration;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 用在Gospel底部栏目，是创世纪...马太福音、马可福音圣经等等66卷的Fragment。
 * 起这个名字可能有些误会，但本质是EmptyFragment，因为在ViewPager滑动的时候我们呈现的
 * 的确是带有一个ProgressBar的空Fragment，后续网络请求成功我们才进行了改变，把数据加进空Fragment
 * 变成非空的信息Fragment
 */
@ContentView(R.layout.empty_frag)
public class EmptyFragment extends BaseFragment {

    @ViewInject(R.id.cl_gospel)
    private FrameLayout mClGospel;

    @ViewInject(R.id.pb_gospel)
    private ProgressBar mPbGospel;

    private RecyclerView mRvGospel;

    public static final int INT = 12;

    public static EmptyFragment newInstance(Context context) {

        return new EmptyFragment();
    }

//    public EmptyFragment(Context context) {
//        mRvGospel = new RecyclerView(context);
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mRvGospel.getParent() == null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mClGospel.addView(mRvGospel, params);
        }

        // addView已经有parent的标准做法 但其实addView一次就够了，下一次只需要显示出来
//        if (mRvGospel.getParent() == null) {
//            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            mClGospel.addView(mRvGospel, params);
//        } else {
//            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            ((FrameLayout) mRvGospel.getParent()).removeView(mRvGospel);
////            ((FrameLayout) mRvGospel.getParent()).addView(mRvGospel, params);
//            mClGospel.addView(mRvGospel, params);
//        }
    }

    @Override
    protected void loadData() {

        initView();

        requestData();
    }

    // 初始化View的参数
    private void initView() {

        visible(true);

        mRvGospel.addItemDecoration(new ItemDecoration((int) getResources().getDimension(R.dimen.search_margin_horizontal)));
        mRvGospel.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void visible(boolean b) {
        if (b) {
            mPbGospel.setVisibility(View.VISIBLE);
            mRvGospel.setVisibility(View.GONE);
        } else {
            mPbGospel.setVisibility(View.GONE);
            mRvGospel.setVisibility(View.VISIBLE);
        }
    }

    // 网络请求数据
    private void requestData() {
        String[] dataSet;
        dataSet = new String[INT];
        for (int i = 0; i < INT; i++) {
            dataSet[i] = getString(R.string.next_week) + i;
        }

        // http request success
        loadView(dataSet);
    }

    private void loadView(String[] dataSet) {

        visible(false);

        ContentItemViewAdapter adapter;
        adapter = new ContentItemViewAdapter(dataSet);
        mRvGospel.setAdapter(adapter);
        setScrollPosition();
    }

    public void setScrollPosition() {
        int scrollPosition = 0;
        // If a layout manager has already been set, get current scroll position.
        if (mRvGospel.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRvGospel.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        mRvGospel.scrollToPosition(scrollPosition);
    }

}
