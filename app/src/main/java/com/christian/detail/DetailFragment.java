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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.christian.NavigationActivity;
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
    private static final String FROM_PAGE = "fromPage";
    @ViewInject(R.id.toolbar_actionbar)
    private Toolbar mToolbar;
    @ViewInject(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;
    @ViewInject(R.id.detail_frag_app_bar)
    AppBarLayout mDetailFragAppBarLayout;
    @ViewInject(R.id.detail_frag_fab)
    FloatingActionButton mDetailFragFab;
    @ViewInject(R.id.gospel_detail)
    TextView gospelDetail;
    private ShareActionProvider mShareActionProvider;
    private boolean hasHidePerformedOnce;
    private int mFromPage;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);//添加菜单不调用该方法是没有用的
        initData();
        initView();
        initListener();
    }

    private void initData() {
        if (getArguments() != null) {
            mFromPage = getArguments().getInt(FROM_PAGE);
        }
    }

    @Event(value = {R.id.detail_frag_fab}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_frag_fab:
                nestedScrollView.scrollTo(0, 0);
                Log.i(TAG, "onClick: " + nestedScrollView.toString());
                mDetailFragAppBarLayout.setExpanded(true, true);
                Log.i(TAG, "onClick: " + mDetailFragAppBarLayout.toString());
                break;
            default:
                break;
        }
    }

    @Event(value = R.id.nested_scroll_view, type = NestedScrollView.OnScrollChangeListener.class)
    private void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (v.getChildAt(0).getHeight() == v.getHeight() + v.getScrollY()) {
            mDetailFragFab.show();
        } else {
            mDetailFragFab.hide();
        }
    }

    public static DetailFragment newInstance(Integer fromPage) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        Bundle args = new Bundle();
        args.putInt(FROM_PAGE, fromPage);
        fragment.setArguments(args);
        return fragment;
    }

    private void initView() {
        nestedScrollView.setSmoothScrollingEnabled(true);
        setUpButton();
        setGospelDetail();
    }

    private void setCardViewAnimation() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_card_show);
        nestedScrollView.startAnimation(animation);
    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        // Get a support ActionBar corresponding to this mToolbar
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        // Enable the Up button
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (supportActionBar != null) {
            if (mFromPage == NavigationActivity.ChristianTab.NAVIGATION_HOME.ordinal()) {
                supportActionBar.setTitle(R.string.title_home);
            } else if (mFromPage == NavigationActivity.ChristianTab.NAVIGATION_BOOK.ordinal()) {
                supportActionBar.setTitle(R.string.title_book);
            }
        }
        mToolbar.setNavigationContentDescription(R.string.go_back);
    }

    private void restoreScrollPosition() {
        mDetailFragAppBarLayout.setExpanded(false, false);
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
        mDetailFragFab.startAnimation(scaleToShow);
        scaleToShow.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                hasHidePerformedOnce = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDetailFragFab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void animateFabButtonToHide() {
        Animation scaleToHide = AnimationUtils.loadAnimation(getContext(), R.anim.scale_to_hide);
        if (mDetailFragFab.getVisibility() == View.VISIBLE) {
            mDetailFragFab.startAnimation(scaleToHide);
            scaleToHide.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    hasHidePerformedOnce = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mDetailFragFab.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }
}
