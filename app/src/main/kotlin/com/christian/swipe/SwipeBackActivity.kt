package com.christian.swipe

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import com.afollestad.aesthetic.AestheticActivity
import com.christian.R
import com.github.anzewei.parallaxbacklayout.ParallaxBack
import com.github.anzewei.parallaxbacklayout.ParallaxHelper
import com.github.anzewei.parallaxbacklayout.widget.ParallaxBackLayout


/**
 * The activity base class, swipe back logic here.
 */
@ParallaxBack(edge = ParallaxBack.Edge.LEFT, layout = ParallaxBack.Layout.PARALLAX, edgeMode = ParallaxBack.EdgeMode.FULLSCREEN)
abstract class SwipeBackActivity : AestheticActivity() {

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
        parallaxBackLayout.setScrollThresHold(1/3f)
        parallaxBackLayout.setVelocity(Int.MAX_VALUE)
    }

//    override fun onBackPressed() {
//        val layout = ParallaxHelper.getParallaxBackLayout(this, false)
//        if (layout == null || !layout.scrollToFinishActivity(0))
//            super.onBackPressed()
//    }
}
