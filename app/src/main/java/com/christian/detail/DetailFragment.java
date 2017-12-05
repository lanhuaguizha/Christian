package com.christian.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.christian.R;
import com.christian.base.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/*
 * Copyright 2015 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@ContentView(R.layout.detail_frag)
public class DetailFragment extends BaseFragment {
    private static final String TAG = DetailFragment.class.getSimpleName();
    @ViewInject(R.id.toolbar_actionbar)
    private Toolbar toolbar;
    @ViewInject(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;
    @ViewInject(R.id.app_bar)
    AppBarLayout appBarLayout;
    @ViewInject(R.id.fabMe)
    FloatingActionButton floatingActionButton;
    @ViewInject(R.id.gospel_detail)
    TextView gospelDetail;
    private ShareActionProvider mShareActionProvider;
    private boolean isScrollToBottom;
    private boolean hasHidePerformedOnce;

    @Event(value = {R.id.fabMe}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabMe:
                nestedScrollView.setSmoothScrollingEnabled(true);
//                if (!isScrollToBottom) {
//                    nestedScrollView.fullScroll(View.FOCUS_DOWN);
//                    appBarLayout.setExpanded(false, true);
//                } else {
                nestedScrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nestedScrollView.smoothScrollTo(0, 0);//解决间接性不能到顶部
                    }
                }, 10);
//                    nestedScrollView.fullScroll(View.FOCUS_UP);
                appBarLayout.setExpanded(true, true);
//                }
                break;
            default:
                break;
        }
    }

    @Event(value = R.id.nested_scroll_view, type = NestedScrollView.OnScrollChangeListener.class)
    private void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                    Log.i(TAG, "scrollY: " + scrollY);
//                    Log.i(TAG, "oldScrollX: " + oldScrollX);
//                    Log.i(TAG, "oldScrollY: " + oldScrollY);
//
//                    Log.i(TAG, "v.getChildAt(0).getHeight(): " + v.getChildAt(0).getHeight());
//                    Log.i(TAG, "v.getHeight(): " + v.getHeight());
//                    Log.i(TAG, "v.getScrollY(): " + v.getScrollY());
        if (v.getChildAt(0).getHeight() == v.getHeight() + v.getScrollY()) {
            isScrollToBottom = true;
//            animateFabButtonToShow();
            floatingActionButton.show();
        } else {
            floatingActionButton.hide();
//            if (!hasHidePerformedOnce) {
//                Log.i(TAG, "onScrollChange: perform scale to hide");
//                animateFabButtonToHide();
//            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);//添加菜单不调用该方法是没有用的
        initView();
        initListener();
    }

    private void initView() {
        setUpButton();
        setGospelDetail();
    }

    private void setCardViewAnimation() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_card_show);
        nestedScrollView.startAnimation(animation);
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void setGospelDetail() {
        gospelDetail.postDelayed(new Runnable() {
            @Override
            public void run() {
                gospelDetail.setText(getString(R.string.large_text));
                nestedScrollView.setVisibility(View.VISIBLE);
                setCardViewAnimation();
            }
        }, 200);
//        restoreScrollPosition();
    }

    private void setUpButton() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this mToolbar
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationContentDescription(R.string.go_back);
    }

    private void restoreScrollPosition() {
        appBarLayout.setExpanded(false, false);
        // Must using post.Runnable, this is so suck!
        nestedScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                nestedScrollView.scrollTo(0, 1000);
            }
        }, 200);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_share, menu);
        MenuItem shareItem = menu.findItem(R.id.menu_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        setShareIntent(createShareIntent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                getActivity().finish();
                return true;
            case R.id.menu_download:
                getActivity().finish();
                return true;
            case R.id.menu_collection:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void animateFabButtonToShow() {
        Animation scaleToShow = AnimationUtils.loadAnimation(getContext(), R.anim.scale_to_show);
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

    private void animateFabButtonToHide() {
        Animation scaleToHide = AnimationUtils.loadAnimation(getContext(), R.anim.scale_to_hide);
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
}