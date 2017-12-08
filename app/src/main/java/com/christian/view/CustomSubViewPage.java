package com.christian.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Christian on 2017/11/11.
 */

public class CustomSubViewPage extends ViewPager {

    private int mPosition;

    public CustomSubViewPage(Context context) {
        super(context);
    }

    public CustomSubViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPosition(int position) {
        mPosition = position;
    }

}
