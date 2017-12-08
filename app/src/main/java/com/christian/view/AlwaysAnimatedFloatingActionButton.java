package com.christian.view;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

/**
 * Created by Christian on 2017/12/9
 */

public class AlwaysAnimatedFloatingActionButton extends FloatingActionButton {

    public AlwaysAnimatedFloatingActionButton(Context context) {
        super(context);
    }

    public AlwaysAnimatedFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlwaysAnimatedFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // true will always has animation
    @Override
    public boolean isLaidOut() {
        return true;
    }
}
