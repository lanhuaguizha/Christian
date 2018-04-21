package com.christian

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Locate nav detail FAB
 */
class BottomNavigationViewBehaviorExt(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<View>(context, attrs) {

    override fun onLayoutChild(parent: CoordinatorLayout?, child: View?, layoutDirection: Int): Boolean {

        (child?.layoutParams as CoordinatorLayout.LayoutParams).topMargin = parent?.measuredHeight?.minus(child.measuredHeight) ?: 0
        return super.onLayoutChild(parent, child, layoutDirection)

    }

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {

        return dependency is AppBarLayout

    }

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {

        child?.translationY = 2000f
        return false

    }

}
