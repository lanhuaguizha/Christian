package com.christian.view;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.christian.util.ChristianUtil;

/**
 * author：Administrator on 2017/6/13 22:07
 * email：lanhuaguizha@gmail.com
 */


public class GospelItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public GospelItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildPosition(view) == 0)
            outRect.top = ChristianUtil.dpToPx(64);
    }
}
