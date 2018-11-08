package com.christian.view

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class GridItemDecoration(space: Int) : ItemDecoration(space) {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        super.getItemOffsets(outRect, view, parent, state)

        if (parent.getChildAdapterPosition(view) % 2 != 0)
            outRect.left = 0

        if (parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) == 1)
            outRect.top = space
    }
}