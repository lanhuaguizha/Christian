package com.christian.view;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.tabs.TabLayout;

public class CustomTabLayout extends TabLayout {
    public CustomTabLayout(Context context) {
        this(context, null);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean canScrollDown() {
        return getChildAt(0).getWidth() != getScrollX() + getWidth();
    }

    public boolean canScrollUp() {
        return getScrollX() != 0;
    }
}
