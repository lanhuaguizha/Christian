package com.christian.util

import android.Manifest
import android.R.attr
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.widget.TextView
import androidx.annotation.NonNull
import com.bumptech.glide.Glide
import com.christian.R
import com.christian.swipe.SwipeBackActivity
import com.google.android.material.appbar.AppBarLayout
import com.kotlinpermissions.KotlinPermissions
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.ImageSize
import io.noties.markwon.image.ImageSizeResolverDef
import io.noties.markwon.image.glide.GlideImagesPlugin
import io.noties.markwon.linkify.LinkifyPlugin
import org.jetbrains.anko.dip
import ren.qinc.markdowneditors.AppContext
import ren.qinc.markdowneditors.view.EditorActivity


fun requestStoragePermission(editorActivity: EditorActivity, path: String) {
    KotlinPermissions.with(editorActivity) // where this is an FragmentActivity instance
            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .onAccepted { permissions ->
                //List of accepted permissions
                editorActivity.requestStoragePermissionAccepted(path)
            }
            .onDenied { permissions ->
                //List of denied permissions
                AppContext.showSnackbar(editorActivity.mViewPager, editorActivity.getString(R.string.no_acccess_to_read))
                requestStoragePermission(editorActivity, path)
            }
            .onForeverDenied { permissions ->
                //List of forever denied permissions
                AppContext.showSnackbar(editorActivity.mViewPager, R.string.no_acccess_to_read_image_to_display)
            }
            .ask()
}

fun fixToolbarElevation(appBarLayout: AppBarLayout) {
    appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
        if (verticalOffset == -appBarLayout.height) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                appBarLayout.elevation = 0f
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                appBarLayout.elevation = appBarLayout.dip(4).toFloat()
            }
        }
    })
}

fun setToolbarAsUp(activity: SwipeBackActivity, toolbar: androidx.appcompat.widget.Toolbar, title: String) {
    activity.setSupportActionBar(toolbar)
    activity.supportActionBar?.title = title
    activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
}

fun setMarkdownToTextView(context: Context, textView: TextView, gospelContent: String) {
    val markdownView = Markwon.builder(context) // automatically create Glide instance
            .usePlugin(GlideImagesPlugin.create(Glide.with(context)))
            .usePlugin(LinkifyPlugin.create())
            .usePlugin(HtmlPlugin.create()) //                // if you need more control
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
                    builder.imageSizeResolver(object : ImageSizeResolverDef() {
                        override fun resolveImageSize(imageSize: ImageSize?, imageBounds: Rect, canvasWidth: Int, textSize: Float): Rect {
                            return if (imageSize == null) fitWidth(imageBounds, canvasWidth) else super.resolveImageSize(imageSize, imageBounds, canvasWidth, attr.textSize.toFloat())
                        }

                        @NonNull
                        fun fitWidth(@NonNull imageBounds: Rect, canvasWidth: Int): Rect {
                            val ratio = imageBounds.width().toFloat() / imageBounds.height()
                            val height = canvasWidth / ratio + .5F
                            return Rect(0, 0, canvasWidth, height.toInt())
                        }
                    })
                }
            })
            .build()
    markdownView.setMarkdown(textView, gospelContent)
}
