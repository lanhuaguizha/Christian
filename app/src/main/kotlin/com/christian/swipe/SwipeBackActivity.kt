package com.christian.swipe

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.content.res.AppCompatResources
import com.christian.R
import com.github.anzewei.parallaxbacklayout.ParallaxBack
import com.github.anzewei.parallaxbacklayout.widget.ParallaxBackLayout
import com.github.anzewei.parallaxbacklayout.ParallaxHelper
import com.github.anzewei.parallaxbacklayout.ViewDragHelper
import android.view.ViewGroup
import org.jetbrains.anko.appcompat.v7.Appcompat
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
        initSbl()
    }

    private fun initSbl() {
        val parallaxBackLayout = ParallaxHelper.getParallaxBackLayout(this, true)
        parallaxBackLayout.setLayoutType(ParallaxBackLayout.LAYOUT_CUSTOM, CupertinoParallaxTransform())
        parallaxBackLayout.setShadowDrawable(AppCompatResources.getDrawable(this@SwipeBackActivity, R.drawable.bga_sbl_shadow))
        parallaxBackLayout.setScrollThresHold(0.5f)
        parallaxBackLayout.setVelocity(Int.MAX_VALUE)
    }

}
