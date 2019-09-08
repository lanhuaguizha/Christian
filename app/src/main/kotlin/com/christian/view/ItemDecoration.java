package com.christian.view;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.christian.util.ChristianUtil;

import java.util.Objects;

/**
 * author：Administrator on 2017/6/13 22:07
 * email：lanhuaguizha@gmail.com
 */


public class ItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public ItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) == 0)
            outRect.top = space;
//        if (parent.getChildAdapterPosition(view) == Objects.requireNonNull(parent.getAdapter()).getItemCount() - 1)
//            outRect.bottom = ChristianUtil.dpToPx(168);
    }
}
