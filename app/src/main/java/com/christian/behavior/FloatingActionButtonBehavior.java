package com.christian.behavior;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

/**
 * author：Administrator on 2017/6/6 17:29
 * email：lanhuaguizha@gmail.com
 */

public class FloatingActionButtonBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    private Rect mTmpRect;

    public FloatingActionButtonBehavior() {
    }

    // 注意一下，带有参数的这个构造必须要重载，因为在CoordinatorLayout里利用反射去获取这个Behavior的时候就是拿的这个构造
    public FloatingActionButtonBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if (dependency instanceof NestedScrollView) {
            // If we're depending on an AppBarLayout we will show/hide it automatically
            // if the FAB is anchored to the AppBarLayout
//            updateFabVisibilityForAppBarLayout(parent, (NestedScrollView) dependency, child);
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }

//    private boolean updateFabVisibilityForAppBarLayout(CoordinatorLayout parent, NestedScrollView nestedScrollView, FloatingActionButton child) {
//
//        if (mTmpRect == null) {
//            mTmpRect = new Rect();
//        }
//
//        // First, let's get the visible rect of the dependency
//        final Rect rect = mTmpRect;
//        ViewGroupUtils.getDescendantRect(parent, nestedScrollView, rect);
//
//        if (rect.bottom <= nestedScrollView.getMinimumHeightForVisibleOverlappingContent()) {
//            // If the anchor's bottom is below the seam, we'll animate our FAB out
////            child.hide(mInternalAutoHideListener, false);
//            child.setVisibility(View.GONE);
//        } else {
//            // Else, we'll animate our FAB back in
////            child.show(mInternalAutoHideListener, false);
//            child.setVisibility(View.VISIBLE);
//        }
//        return true;
//    }

}
