package com.christian.swipe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import com.christian.R
import com.github.anzewei.parallaxbacklayout.ParallaxBack
import com.github.anzewei.parallaxbacklayout.ParallaxHelper
import com.github.anzewei.parallaxbacklayout.widget.ParallaxBackLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings


/**
 * The activity base class, swipe back logic here.
 */
@ParallaxBack(edge = ParallaxBack.Edge.LEFT, layout = ParallaxBack.Layout.PARALLAX, edgeMode = ParallaxBack.EdgeMode.FULLSCREEN)
abstract class SwipeBackActivity : AppCompatActivity() {

    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        firestore.firestoreSettings = settings
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSbl()
    }

    private fun initSbl() {
        val parallaxBackLayout = ParallaxHelper.getParallaxBackLayout(this, true)
        parallaxBackLayout.setLayoutType(ParallaxBackLayout.LAYOUT_CUSTOM, CupertinoParallaxTransform())
        parallaxBackLayout.setShadowDrawable(AppCompatResources.getDrawable(this@SwipeBackActivity, R.drawable.bga_sbl_shadow))
        parallaxBackLayout.setScrollThresHold(0.2f)
    }

    override fun onBackPressed() {
        val layout = ParallaxHelper.getParallaxBackLayout(this, false)
        if (layout == null || !layout.scrollToFinishActivity(0))
            super.onBackPressed()
    }
}

