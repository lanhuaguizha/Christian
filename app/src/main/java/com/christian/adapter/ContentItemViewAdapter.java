/*
* Copyright (C) 2014 The Android Open Source Project
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

package com.christian.adapter;

import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.christian.R;
import com.christian.content.ActCon;
import com.christian.detail.DetailActivity;
import com.christian.util.ChristianUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class ContentItemViewAdapter extends RecyclerView.Adapter<ContentItemViewAdapter.ViewHolder> {
    private int mLastPosition;
    private static final String TAG = "DetailAdapter";
    private String[] mDataSet;
    private boolean mAnimateItems = false;
    private static final int ANIMATED_ITEMS_COUNT = 4;
    private int mLastAnimatedPosition = 0;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.content_subtitle)
        private TextView mTvHomeTitle;
        @ViewInject(R.id.more_action)
        private AppCompatImageButton btnHomeMore;

        @Event(value = {R.id.more_action, R.id.cl_item_container})
        private void onClick(View v) {
            switch (v.getId()) {
                case R.id.more_action:
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v.findViewById(R.id.more_action));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        popupMenu.setGravity(Gravity.CENTER_HORIZONTAL);
                    }
                    popupMenu.getMenuInflater().inflate(R.menu.menu_home, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            return false;
                        }
                    });
                    popupMenu.show();
                    break;
                case R.id.cl_item_container:
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                    Intent intent = new Intent(v.getContext(), DetailActivity.class);
                    v.getContext().startActivity(intent);
                    break;
                default:
                    break;
            }
        }

        ViewHolder(View v) {
            super(v);
            x.view().inject(this, v);
        }

        TextView getTvHomeTitle() {
            return mTvHomeTitle;
        }

        AppCompatImageButton getBtnHomeMore() {
            return btnHomeMore;
        }

    }

    public ContentItemViewAdapter(String[] dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.detail_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    private void runEnterAnimation(View view, int position) {
//        if (!mAnimateItems || position >= ANIMATED_ITEMS_COUNT - 1) {
//            return;
//        }

        if (position >= mLastAnimatedPosition) {
            mLastAnimatedPosition = position;
            view.setTranslationY(ChristianUtil.getScreenHeight(view.getContext()));
            view.animate()
                    .translationY(0)
                    .setStartDelay(400 * position)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(400)
                    .start();
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        int adapterPosition = viewHolder.getAdapterPosition();
        viewHolder.itemView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int screenItems = ChristianUtil.getScreenHeight(viewHolder.itemView.getContext()) / viewHolder.itemView.getMeasuredHeight();
        Animation animation = null;
        // 3 conditions no animation: 1. last position < adapter position 2. screen items in front 3. destroy view
        if (adapterPosition >= mLastPosition && adapterPosition >= screenItems) {
            Log.i(TAG, "onBindViewHolder: " +mLastPosition + adapterPosition);
//            if (adapterPosition >= screenItems) {
//                if (adapterPosition >= mLastPosition) {
                // Load animation form xml
                animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.up_from_bottom);
//                } else {
//                    animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.stay);
//                }

                // Load animator form xml
                //                ObjectAnimator animator = (ObjectAnimator) AnimatorInflater.loadAnimator(viewHolder.itemView.getContext(), R.animator.up_from_bottom);
                //                animator.setTarget(viewHolder.itemView);
                //                animator.start();

                //                viewHolder.itemView.setTranslationY(viewHolder.itemView.getMeasuredHeight());
                //                viewHolder.itemView.animate()
                //                        .translationY(0)
                //                        .setInterpolator(new DecelerateInterpolator(3.f))
                //                        .setDuration(200)
                //                        .start();
//            } else {
//                runEnterAnimation(viewHolder.itemView, adapterPosition);
//            }
        } else {
            Log.i(TAG, "onBindViewHolder stay: " + mLastPosition + adapterPosition);
            animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.stay);
        }
        if (animation != null)
            viewHolder.itemView.startAnimation(animation);
        mLastPosition = adapterPosition;
        // Get element from your dataset at this adapterPosition and replace the contents of the view
        // with that element
//        viewHolder.getTvHomeTitle().setText(mDataSet[adapterPosition]);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}
