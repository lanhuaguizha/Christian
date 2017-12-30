package com.christian.content;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.christian.R;
import com.christian.adapter.ContentItemViewAdapter;
import com.christian.adapter.ContentViewAdapter;
import com.christian.data.ContentData;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 福音信息内容页
 */
@ContentView(R.layout.content_activity)
public class ContentActivity extends AppCompatActivity {

    @ViewInject(R.id.content_activity_coordinator_layout)
    private SwipeRefreshLayout mContentActivityCoordinatorLayout;

    @ViewInject(R.id.content_activity_app_bar_layout)
    private AppBarLayout mContentActivityAppBarLayout;

    @ViewInject(R.id.content_activity_swipe_refresh_layout)
    private SwipeRefreshLayout mContentActivitySwipeRefreshLayout;

    @ViewInject(R.id.content_activity_recycler_view)
    private RecyclerView mContentActivityRecyclerView;

    @ViewInject(R.id.content_activity_floating_action_button)
    private SwipeRefreshLayout mContentActivityFloatingActionButton;

    private RecyclerView.Adapter<ContentViewAdapter.ViewHolder> mRecyclerViewAdapter;

    // 后台返回的本页面的数据结构
    private ContentData mContentData;

    public ContentActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
        // toDo
        if (mContentData != null) {
            loadView(mContentData);
        } else {
            showEmptyView();
        }
    }

    private void loadView(ContentData contentData) {
        showView();
    }

    private void showEmptyView() {

    }

    private void showView() {

    }



}
