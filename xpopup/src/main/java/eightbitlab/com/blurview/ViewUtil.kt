package eightbitlab.com.blurview

import android.view.ViewGroup
import android.view.Window
import androidx.cardview.widget.CardView
import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur
import eightbitlab.com.blurview.BlurViewExtendsVerticalRecyclerView

fun makeViewBlur(viewVertical: BlurViewExtendsVerticalRecyclerView, parent: ViewGroup, window: Window, boolean: Boolean = false) {
    val windowBackground = window.decorView.background
    val radius = 25f
    viewVertical.setupWith(parent)
        .setFrameClearDrawable(windowBackground)
        .setBlurAlgorithm(SupportRenderScriptBlur(parent.context))
        .setBlurRadius(radius)
        .setHasFixedTransformationMatrix(boolean)
}