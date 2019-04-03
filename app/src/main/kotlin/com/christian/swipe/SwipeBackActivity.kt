package com.christian.swipe

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import com.christian.R
import com.github.anzewei.parallaxbacklayout.ParallaxBack
import com.github.anzewei.parallaxbacklayout.widget.ParallaxBackLayout
import com.github.anzewei.parallaxbacklayout.ParallaxHelper
import com.github.anzewei.parallaxbacklayout.ViewDragHelper
import android.view.ViewGroup
import org.jetbrains.anko.dip


/**
 * The activity base class, swipe back logic here.
 */
@ParallaxBack(edge = ParallaxBack.Edge.LEFT, layout = ParallaxBack.Layout.PARALLAX, edgeMode = ParallaxBack.EdgeMode.FULLSCREEN)
abstract class SwipeBackActivity : AppCompatActivity() {

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        initSbl()
    }

    private fun initSbl() {
        val parallaxBackLayout = ParallaxHelper.getParallaxBackLayout(this, true)
        parallaxBackLayout.setLayoutType(ParallaxBackLayout.LAYOUT_CUSTOM, CupertinoParallaxTransform())
    }

}
