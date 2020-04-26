package com.christian.view

import android.content.Context
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.util.AttributeSet
import android.view.View
import com.christian.R
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Christian on 2017/12/9
 */
@Suppress("DEPRECATION")
@androidx.coordinatorlayout.widget.CoordinatorLayout.DefaultBehavior(AnimationFloatingActionButton.Behavior2::class)
class AnimationFloatingActionButton(context: Context, attrs: AttributeSet?) : FloatingActionButton(context, attrs) {

    // true will always has animation
    override fun isLaidOut(): Boolean {
        return true
    }

    /**
     * In order to achieve the FloatingActionButton distance from the BottomNavigationView.
     */
    class Behavior : androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior<View>() {

        override fun layoutDependsOn(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: View, dependency: View): Boolean {
            return dependency is BottomNavigationView
        }

        override fun onDependentViewChanged(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: View, dependency: View): Boolean {
            val marginBottom = parent.context.resources.getDimension(R.dimen.fab_margin_bottom)
            marginBottom.let { child.translationY = it }
            return false
        }
    }


    class Behavior2 : com.google.android.material.floatingactionbutton.FloatingActionButton.Behavior() {

        override fun layoutDependsOn(parent: CoordinatorLayout, child: FloatingActionButton, dependency: View): Boolean {
            return dependency is BottomNavigationView

        }

        override fun onDependentViewChanged(parent: CoordinatorLayout, child: FloatingActionButton, dependency: View): Boolean {
            val marginBottom = parent.context.resources.getDimension(R.dimen.fab_margin_bottom)
            marginBottom.let { child.translationY = it }
            return false
        }
    }

}
