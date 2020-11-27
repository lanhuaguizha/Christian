package com.lxj.xpopup.util

import android.view.ViewGroup
import android.view.Window
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