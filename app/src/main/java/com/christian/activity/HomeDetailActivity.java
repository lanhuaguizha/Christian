package com.christian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.christian.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
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
    @ViewInject(R.id.fab)
    FloatingActionButton floatingActionButton;
    private Toolbar toolbar;
    private ShareActionProvider mShareActionProvider;
    private boolean isScrollToBottom;
    private boolean hasHidePerformedOnce;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    private void initListener() {
        if (toolbar != null)
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        if (nestedScrollView != null)
            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    Log.i(TAG, "scrollX: " + scrollX);
                    Log.i(TAG, "scrollY: " + scrollY);
                    Log.i(TAG, "oldScrollX: " + oldScrollX);
                    Log.i(TAG, "oldScrollY: " + oldScrollY);

                    Log.i(TAG, "v.getChildAt(0).getHeight(): " + v.getChildAt(0).getHeight());
                    Log.i(TAG, "v.getHeight(): " + v.getHeight());
                    Log.i(TAG, "v.getScrollY(): " + v.getScrollY());
                    if (v.getChildAt(0).getHeight() == v.getHeight() + v.getScrollY()) {
                        isScrollToBottom = true;
                        scaleToShow();

                    } else if (v.getScrollY() == 0) {
                        isScrollToBottom = false;
                        scaleToShow();

                    } else {
                        if (!hasHidePerformedOnce) {
                            Log.i(TAG, "onScrollChange: perform scale to hide");
                            scaleToHide();
                        }
                    }
                }
            });
    }

    private void scaleToHide() {
        Animation scaleToHide = AnimationUtils.loadAnimation(HomeDetailActivity.this, R.anim.scale_to_hide);
        if (floatingActionButton.getVisibility() == View.VISIBLE) {
            floatingActionButton.startAnimation(scaleToHide);
            scaleToHide.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    hasHidePerformedOnce = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    floatingActionButton.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    private void scaleToShow() {
        Animation scaleToShow = AnimationUtils.loadAnimation(HomeDetailActivity.this, R.anim.scale_to_show);
        floatingActionButton.startAnimation(scaleToShow);
        scaleToShow.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                hasHidePerformedOnce = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                floatingActionButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Event(value = R.id.fab,
            type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                nestedScrollView.setSmoothScrollingEnabled(false);
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
                break;
            default:
                break;
        }
    }

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

        if (toolbar != null)
            toolbar.setNavigationContentDescription(R.string.go_back);

        restoreAndScrollToPosition();
    }

    private void restoreAndScrollToPosition() {
        appBarLayout.setExpanded(false, false);
        // Must using post.Runnable, this is so suck!
        nestedScrollView.post(new Runnable() {
            @Override
            public void run() {
                nestedScrollView.scrollTo(0, 5099);
            }
        });
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

//    @Event(value = R.id.nested_scroll_view, type = NestedScrollView.OnScrollChangeListener.class)
//    private void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//        floatingActionButton.animate().scaleY(Math.abs(scrollY));
//        floatingActionButton.animate().scaleX(Math.abs(scrollY));
//    }
}
