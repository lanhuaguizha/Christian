package com.christian.view

import android.content.Context
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import com.christian.R

/**
 * Created by Christian on 2017/12/9
 */
@Suppress("DEPRECATION")
@CoordinatorLayout.DefaultBehavior(AnimatedFloatingActionButton.Behavior::class)
class AnimatedFloatingActionButton(context: Context?, attrs: AttributeSet?) : FloatingActionButton(context, attrs) {

    // true will always has animation
    override fun isLaidOut(): Boolean {
        return true
    }

    /**
     * In order to achieve the FloatingActionButton distance from the BottomNavigationView.
     */
    class Behavior : CoordinatorLayout.Behavior<View>() {

        override fun layoutDependsOn(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
            return dependency is BottomNavigationView
        }

        override fun onDependentViewChanged(parent: CoordinatorLayout?, child: View, dependency: View?): Boolean {
            val marginBottom = parent?.context?.resources?.getDimension(R.dimen.fab_margin_bottom)
            marginBottom?.let { child.translationY = it }
            return false
        }
    }

}
