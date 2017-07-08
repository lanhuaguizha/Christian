package com.christian.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.christian.R;

import java.util.List;

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

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.ViewHolder> {
    private final int NORMAL_TYPE = 0;
    private final int FOOT_TYPE = 11111;
    private List<String> mData;//数据
    private int mMaxCount = 15;//最大显示数
    private int mDataCount=0;
    private IListener mIListener;
    public SearchViewAdapter(List<String> data, IListener iListener) {
        mData = data;
        mIListener=iListener;
    }
    public interface IListener{
        void normalItemClick(String s);
        void clearItemClick();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View normal_views = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_search_item, parent, false);
        View foot_view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_search_item_clear_history, parent, false);

        if (viewType == FOOT_TYPE)
            return new ViewHolder(foot_view, FOOT_TYPE);
        return new ViewHolder(normal_views, NORMAL_TYPE);
    }
    @Override
    public int getItemViewType(int position) {
        if (position == mDataCount-1) {
            return FOOT_TYPE;
        }
        return NORMAL_TYPE;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //如果footview存在，并且当前位置ViewType是FOOT_TYPE
        if ( getItemViewType(position) != FOOT_TYPE) {
            holder.tvViewHolder.setText(mData.get(position));
        }else {
            Log.d("foot","填充底部容器");
        }
    }


    @Override
    public int getItemCount() {
        if (mData.size() < mMaxCount) {
            mDataCount=mData.size();
            return mData.size();
        }
        mDataCount=mMaxCount;
        return mMaxCount;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvViewHolder;

        public RelativeLayout tvFootView;//footView的TextView属于独自的一个layout

        //初始化viewHolder，此处绑定后在onBindViewHolder中可以直接使用
        public ViewHolder(final View itemView, int viewType) {
            super(itemView);
            if (viewType == NORMAL_TYPE) {
                tvViewHolder = (TextView) itemView.findViewById(R.id.tv_history_item);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIListener.normalItemClick(mData.get(ViewHolder.this.getAdapterPosition()));
                    }
                });
            } else if (viewType == FOOT_TYPE) {
                tvFootView = (RelativeLayout) itemView;
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIListener.clearItemClick();
                    }
                });
            }
        }
    }
}
