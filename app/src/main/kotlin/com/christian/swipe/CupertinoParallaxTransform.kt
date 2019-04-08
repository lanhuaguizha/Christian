package com.christian.swipe

import android.graphics.Canvas
import android.view.View
import com.github.anzewei.parallaxbacklayout.ViewDragHelper.*
import com.github.anzewei.parallaxbacklayout.transform.ITransform
import com.github.anzewei.parallaxbacklayout.widget.ParallaxBackLayout

/**
 * iOS Parallax Transform
 */

class CupertinoParallaxTransform : ITransform {
    override fun transform(canvas: Canvas, parallaxBackLayout: ParallaxBackLayout, child: View) {
        val mEdgeFlag = parallaxBackLayout.edgeFlag
        val width = parallaxBackLayout.width
        val height = parallaxBackLayout.height
        val leftBar = parallaxBackLayout.systemLeft
        val topBar = parallaxBackLayout.systemTop
        when (mEdgeFlag) {
            EDGE_LEFT -> {
                val left = (child.left - width) / 3
                canvas.translate(left.toFloat(), 0f)
                canvas.clipRect(0, 0, left + width, child.bottom)
            }
            EDGE_TOP -> {
                val top = (child.top - child.height) / 2
                canvas.translate(0f, top.toFloat())
                canvas.clipRect(0, 0, child.right, child.height + top + topBar)
            }
            EDGE_RIGHT -> {
                val left = (child.left + child.width - leftBar) / 2
                canvas.translate(left.toFloat(), 0f)
                canvas.clipRect(left + leftBar, 0, width, child.bottom)
            }
            EDGE_BOTTOM -> {
                val top = (child.bottom - topBar) / 2
                canvas.translate(0f, top.toFloat())
                canvas.clipRect(0, top + topBar, child.right, height)
            }
        }
    }
}
