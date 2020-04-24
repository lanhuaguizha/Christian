package com.christian.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class CustomChildViewPager extends ViewPager {

    private boolean flag = true;
    private float mLastMotionX;
    //父ViewPager的引用
    private ViewPager viewPager;

    public CustomChildViewPager(@NonNull Context context) {
        super(context);
    }

    public CustomChildViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final float x = ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 使父控件不处理任何触摸事件
                viewPager.requestDisallowInterceptTouchEvent(true);
                flag = true;
                mLastMotionX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                if (flag) {
                    if (x - mLastMotionX > 5 && getCurrentItem() == 0) {
                        flag = false;
                        viewPager.requestDisallowInterceptTouchEvent(false); //将事件交由父控件处理
                    }

                    if (x - mLastMotionX < -5 && getAdapter() != null && getCurrentItem() == getAdapter().getCount() - 1) {
                        flag = false;
                        viewPager.requestDisallowInterceptTouchEvent(false);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                viewPager.requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
