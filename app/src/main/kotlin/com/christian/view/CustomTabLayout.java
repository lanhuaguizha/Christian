package com.christian.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.google.android.material.tabs.TabLayout;

public class CustomTabLayout extends TabLayout {
    private static final String TAG = CustomTabLayout.class.getSimpleName();
    private final VelocityTracker mVelocityTracker;
    private Scroller mScroller;
    private int mCurrentVelocity;
    private int mMinimumVelocity = 175;
    //    private int mMaximumVelocity = 0;
    private int mMaximumVelocity = 28000;
    private float mSpinner;
    private Spring mSpring;
    protected boolean mVerticalPermit = false;

    public CustomTabLayout(Context context) {
        this(context, null);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOverScrollMode(OVER_SCROLL_ALWAYS);
        mScroller = new Scroller(getContext());
        mVelocityTracker = VelocityTracker.obtain();
    }

    public boolean canScrollDown() {
        Log.i(TAG, "canScrollDown: getScrollX(), " + getScrollX() + " getWidth(), " + getWidth() + " sum, " + getChildAt(0).getWidth());
        return getScrollX() != getChildAt(0).getWidth() - getWidth();
    }

    public boolean canScrollUp() {
        return getScrollX() != 0;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(ev);
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
//                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                mCurrentVelocity = (int) mVelocityTracker.getXVelocity();
                Log.i(TAG, "onTouchEvent: mCurrentVelocity, " + mCurrentVelocity);
                startFlingIfNeed(0);
                break;
            case MotionEvent.ACTION_UP:
                if (getScrollX() != 0 && canScrollDown() || getScaleX() == 0 && canScrollUp()) // getScrollX()判断是不是在最左边
                    mVerticalPermit = true;//打开竖直通行证

                break;
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.clear();
                break;

        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int finalX = mScroller.getFinalX();
            int distance = 400;
            Log.i(TAG, "mVerticalPermit: !canScrollUp(), " + !canScrollUp());
            Log.i(TAG, "mVerticalPermit: !canScrollDown(), " + !canScrollDown());
            if (!canScrollUp() && mVerticalPermit) {
//                if(mVerticalPermit) {
                float velocity;
                velocity = finalX > 0 ? mScroller.getCurrVelocity() : mScroller.getCurrVelocity();
                animSpinnerBounce(distance);
                Log.i(TAG, "mVerticalPermit 111, " + velocity);
//                }
                mScroller.forceFinished(true);
                mVerticalPermit = false;
            } else if (!canScrollDown() && mVerticalPermit) {
                float velocity;
                velocity = -mScroller.getCurrVelocity();
                animSpinnerBounceReverse(-distance);
                Log.i(TAG, "mVerticalPermit 222" + velocity);
                mVerticalPermit = false;
            } else {
                final View thisView = this;
                thisView.invalidate();
                Log.i(TAG, String.valueOf(finalX));
                Log.i(TAG, "mVerticalPermit 333");
            }
        }
    }

    private void animSpinnerBounceReverse(int velocity) {
        SpringSystem mSpringSystem = SpringSystem.create();
        mSpring = mSpringSystem.createSpring();
        mSpring.setEndValue(1f);
        mSpring.addListener(new SpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                float scale = 1 - (value * 1f);
                setTranslationX(scale * velocity);
                Log.i(TAG, "onSpringUpdate: " + scale * 500);
            }

            @Override
            public void onSpringAtRest(Spring spring) {

            }

            @Override
            public void onSpringActivate(Spring spring) {

            }

            @Override
            public void onSpringEndStateChange(Spring spring) {
            }
        });
    }

    private void animSpinnerBounce(float velocity) {
        SpringSystem mSpringSystem = SpringSystem.create();
        mSpring = mSpringSystem.createSpring();
        mSpring.setEndValue(1f);
        mSpring.addListener(new SpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value * 1f);
                setTranslationX(scale * 500);
                Log.i(TAG, "onSpringUpdate: " + scale * 500);
            }

            @Override
            public void onSpringAtRest(Spring spring) {

            }

            @Override
            public void onSpringActivate(Spring spring) {

            }

            @Override
            public void onSpringEndStateChange(Spring spring) {
            }
        });
    }

    /**
     * 在必要的时候 开始 Fling 模式
     *
     * @param flingVelocity 速度
     * @return true 可以拦截 嵌套滚动的 Fling
     */
    protected boolean startFlingIfNeed(float flingVelocity) {
        float velocity = flingVelocity == 0 ? mCurrentVelocity : flingVelocity;
        if (Build.VERSION.SDK_INT > 27) {
            /*
             * 修复 API 27 以上【上下颠倒模式】没有回弹效果的bug
             */
            float scaleY = getScaleY();
            final View thisView = this;
            if (thisView.getScaleY() == -1) {
                velocity = -velocity;
            }
        }
        if (Math.abs(velocity) > mMinimumVelocity) {
//            if (velocity * mSpinner < 0) {
//                /*
//                 * 列表准备惯性滑行的时候，如果速度关系
//                 * velocity * mSpinner < 0 表示当前速度趋势，需要关闭 mSpinner 才合理
//                 * 但是在 mState.isOpening（不含二楼） 状态 和 noMoreData 状态 时 mSpinner 不会自动关闭
//                 * 需要使用 FlingRunnable 来关闭 mSpinner ，并在关闭结束后继续 fling 列表
//                 */
//                if (mState == RefreshState.Refreshing || mState == RefreshState.Loading || (mSpinner < 0 && mFooterNoMoreData)) {
//                    animationRunnable = new SmartRefreshLayout.FlingRunnable(velocity).start();
//                    return true;
//                } else if (mState.isReleaseToOpening) {
//                    return true;//拦截嵌套滚动时，即将刷新或者加载的 Fling
//                }
//            }
//            if ((velocity < 0 && ((mEnableOverScrollBounce && (mEnableLoadMore || mEnableOverScrollDrag)) || (mState == RefreshState.Loading && mSpinner >= 0) || (mEnableAutoLoadMore&&isEnableRefreshOrLoadMore(mEnableLoadMore))))
//                    || (velocity > 0 && ((mEnableOverScrollBounce && mEnableRefresh || mEnableOverScrollDrag) || (mState == RefreshState.Refreshing && mSpinner <= 0)))) {
            /*
             * 用于监听越界回弹、Refreshing、Loading、noMoreData 时自动拉出
             * 做法：使用 mScroller.fling 模拟一个惯性滚动，因为 AbsListView 和 ScrollView 等等各种滚动控件内部都是用 mScroller.fling。
             *      所以 mScroller.fling 的状态和 它们一样，可以试试判断它们的 fling 当前速度 和 是否结束。
             *      并再 computeScroll 方法中试试判读它们是否滚动到了边界，得到此时的 fling 速度
             *      如果 当前的速度还能继续 惯性滑行，自动拉出：越界回弹、Refreshing、Loading、noMoreData
             */
            mVerticalPermit = false;//关闭竖直通行证
            mScroller.fling(0, 0, 0, (int) -velocity, 0, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
            mScroller.computeScrollOffset();
            final View thisView = this;
            thisView.invalidate();
//            }
        }
        return false;
    }
}
