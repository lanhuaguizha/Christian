package com.christian.view

import android.content.Context
import com.google.android.material.appbar.AppBarLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.christian.util.ChristianUtil

class TabLayoutBehavior(context: Context?, attrs: AttributeSet?) : androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior<View>(context, attrs) {
    override fun onLayoutChild(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: View, layoutDirection: Int): Boolean {
        (child.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams).topMargin = ChristianUtil.dpToPx(56)
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun layoutDependsOn(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: View, dependency: View): Boolean {
        val top = ((dependency.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams).behavior as AppBarLayout.Behavior).topAndBottomOffset
        Log.i("dd", top.toString())
        //因为TabLayout的滑动与ToolBar是正向的，所以取top值
        child.translationY = top.toFloat()
        return false
    }
}