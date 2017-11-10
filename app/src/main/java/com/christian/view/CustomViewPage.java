package com.christian.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * author：Administrator on 2017/5/31 12:02
 * email：lanhuaguizha@gmail.com
 */

public class CustomViewPage extends ViewPager {
    private View mCurrentView;

    public CustomViewPage(Context context) {
        super(context);
    }

    public CustomViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //Removing the slide page effect of page switching
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }

//    @Override
//    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        if (mCurrentView == null) {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//            return;
//        }
//        int height = 0;
//        mCurrentView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//        int h = mCurrentView.getMeasuredHeight();
//        if (h > height) height = h;
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }

    public void measureCurrentView(View currentView) {
        mCurrentView = currentView;
        requestLayout();
    }

    public int measureFragment(View view) {
        if (view == null)
            return 0;

        view.measure(0, 0);
        return view.getMeasuredHeight();
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return false;
//    }
}
