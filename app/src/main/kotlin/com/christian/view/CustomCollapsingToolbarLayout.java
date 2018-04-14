package com.christian.view;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.AttributeSet;

/**
 * Created by Christian on 2017/11/19.
 */

public class CustomCollapsingToolbarLayout extends CollapsingToolbarLayout implements AppBarLayout.OnOffsetChangedListener {
    private int mOldVerticalOffset;
    private boolean isDecrease;

    public CustomCollapsingToolbarLayout(Context context) {
        super(context);
    }

    public CustomCollapsingToolbarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setScrimsShown(boolean shown) {
//        if (isDecrease) {
//            super.setScrimsShown(false);
//        } else {
//            super.setScrimsShown(shown);
//        }
        super.setScrimsShown(false);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        isDecrease = verticalOffset - mOldVerticalOffset < 0;
        mOldVerticalOffset = verticalOffset;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ((AppBarLayout) getParent()).addOnOffsetChangedListener(this);
    }
}
