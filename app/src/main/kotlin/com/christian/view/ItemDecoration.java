package com.christian.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.christian.util.ChristianUtil;

/**
 * author：Administrator on 2017/6/13 22:07
 * email：lanhuaguizha@gmail.com
 */


public class ItemDecoration extends RecyclerView.ItemDecoration {
    int space;
    int top;

    public ItemDecoration(int space, int top) {
//    public ItemDecoration(int space, int left, int top, int right, int bottom) {
        this.space = space;
        this.top = top;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildPosition(view) == 0)
            outRect.top = top;
    }
}
