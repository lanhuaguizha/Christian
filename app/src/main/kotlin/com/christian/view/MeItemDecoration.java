package com.christian.view;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * author：Administrator on 2017/6/13 22:07
 * email：lanhuaguizha@gmail.com
 */


public class MeItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public MeItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
//        outRect.left = space;
//        outRect.right = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildPosition(view) == 0)
            outRect.top = space;
    }
}
