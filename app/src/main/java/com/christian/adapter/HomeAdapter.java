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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.christian.R;
import com.christian.activity.GospelHomeDetailActivity;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private int lastPosition;
    private static final String TAG = "HomeAdapter";
    private String[] mDataSet;


    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                    Intent intent = new Intent(v.getContext(), GospelHomeDetailActivity.class);
                    v.getContext().startActivity(intent);
                }
            });
            textView = (TextView) v.findViewById(R.id.gospel_title);
        }

        TextView getTextView() {
            return textView;
        }
    }

    public HomeAdapter(String[] dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_home_gospel, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        int adapterPosition = viewHolder.getAdapterPosition();
        Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(),
                (adapterPosition > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        viewHolder.itemView.startAnimation(animation);
        lastPosition = adapterPosition;
        // Get element from your dataset at this adapterPosition and replace the contents of the view
        // with that element
        viewHolder.getTextView().setText(mDataSet[adapterPosition]);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}
