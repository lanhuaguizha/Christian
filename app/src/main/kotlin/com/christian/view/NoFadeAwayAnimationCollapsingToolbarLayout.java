package com.christian.view;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.AttributeSet;

/**
 * Created by Christian on 2017/12/14.
 */

public class NoFadeAwayAnimationCollapsingToolbarLayout extends CollapsingToolbarLayout {
    public NoFadeAwayAnimationCollapsingToolbarLayout(Context context) {
        super(context);
    }

    public NoFadeAwayAnimationCollapsingToolbarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoFadeAwayAnimationCollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setScrimsShown(boolean shown, boolean animate) {
//        super.setScrimsShown(shown, animate);
    }
}
