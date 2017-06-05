package com.christian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.christian.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * author：Administrator on 2017/4/17 01:36
 * email：lanhuaguizha@gmail.com
 */

@ContentView(R.layout.activity_home_detial)
public class HomeDetailActivity extends BaseActivity {
    private static String TAG = HomeDetailActivity.class.getSimpleName();
    @ViewInject(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;
    @ViewInject(R.id.app_bar)
    AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private ShareActionProvider mShareActionProvider;
    private boolean isScrollToBottom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: ");
                if (!isScrollToBottom) {
                    if (nestedScrollView != null)
                        nestedScrollView.fullScroll(View.FOCUS_DOWN);
                    if (appBarLayout != null)
                        appBarLayout.setExpanded(false, false);
                    isScrollToBottom = true;
                } else {
                    if (nestedScrollView != null)
                        nestedScrollView.fullScroll(View.FOCUS_UP);
                    if (appBarLayout != null)
                        appBarLayout.setExpanded(true, false);
                    isScrollToBottom = false;
                }
            }
        });
    }

//    @Event(value = R.id.fab,
//            type = View.OnClickListener.class)
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.fab:
//                Log.i(TAG, "onClick: ");
//                break;
//            default:
//                break;
//        }
//    }

    private void initView() {
        // my_child_toolbar is defined in the layout file
        toolbar = getActionBarToolbar();

        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationContentDescription(R.string.go_back);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);

        MenuItem shareItem = menu.findItem(R.id.menu_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        setShareIntent(createShareIntent());

        // Configure the search info and add any event listeners...
//        shareItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.menu_share:
//                        Log.i("", "onMenuItemClick: ");
//                        return true;
//                }
//                return false;
//            }
//        });

        return super.onCreateOptionsMenu(menu);
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "http://www.baidu.com");
        return shareIntent;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}
