package com.christian.util

import android.Manifest
import android.R.attr
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.MotionEvent
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.customview.widget.ViewDragHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.christian.R
import com.christian.nav.NavActivity
import com.christian.nav.nullString
import com.christian.nav.toolbarTitle
import com.christian.swipe.SwipeBackActivity
import com.christian.view.CustomViewPager
import com.firebase.ui.auth.AuthUI
import com.github.anzewei.parallaxbacklayout.ParallaxHelper
import com.google.android.material.appbar.AppBarLayout
import com.kotlinpermissions.KotlinPermissions
import com.vincent.blurdialog.BlurDialog
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
import java.util.regex.Pattern


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

private var lastPosition = 0//位置
private var lastOffset = 0//偏移量
fun recordScrolledPositionOfDetailPage(context: AppCompatActivity, recyclerView: RecyclerView) {
    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val topView = recyclerView.layoutManager?.getChildAt(0) //获取可视的第一个view
            lastOffset = topView?.top ?: 0//获取与该view的顶部的偏移量
            lastPosition = topView?.let { recyclerView.layoutManager?.getPosition(it) }
                    ?: 0  //得到该View的数组位置
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val topView = recyclerView.layoutManager?.getChildAt(0) //获取可视的第一个view
            lastOffset = topView?.top ?: 0//获取与该view的顶部的偏移量
            lastPosition = topView?.let { recyclerView.layoutManager?.getPosition(it) }
                    ?: 0  //得到该View的数组位置

            val sharedPreferences = context.getSharedPreferences(context.intent?.extras?.getString(toolbarTitle)
                    ?: nullString, Activity.MODE_PRIVATE)
            sharedPreferences.edit {
                putInt("lastPosition", lastPosition)
                putInt("lastOffset", lastOffset)
            }
        }
    })
}

fun restoreScrolledPositionOfDetailPage(context: AppCompatActivity, recyclerView: RecyclerView) {
    recyclerView.requestLayout()
    // 恢复位置
    recyclerView.postDelayed({
        val sharedPreferences = context.getSharedPreferences(context.intent?.extras?.getString(toolbarTitle)
                ?: nullString, Activity.MODE_PRIVATE)
        (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(sharedPreferences.getInt("lastPosition", 0), sharedPreferences.getInt("lastOffset", 0))
    }, 325)
}

fun filterImageUrlThroughDetailPageContent(gospelContent: String): String {
//    val pattern = "(https[^\\)]+\\))"
    val pattern = "https.+\\)"
    val p = Pattern.compile(pattern)

    val m = p.matcher(gospelContent)
    if (m.find()) {
        return m.group(0).replace(")", "")
    }
    return ""
}

lateinit var dialog: BlurDialog
fun showExitDialog(navActivity: NavActivity): BlurDialog {
//    dialog = BlurDialog.Builder()
//            .isCancelable(true)
//            .isOutsideCancelable(true)
//            .message(Html.fromHtml("<h2><font color=\"#FF8C00\">Exit</font></h2>Exit the app will have no access to write document"))
//            .positiveMsg(Html.fromHtml("<font color=\"#EC4E4F\">Yes</font>")) //You can change color by Html
//            .negativeMsg("No")
//            .positiveClick {
//                dialog.dismiss()
//                AuthUI.getInstance()
//                        .signOut(navActivity)
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                navActivity.snackbar("Sign out successful").show()
//                                navActivity.invalidateSignInUI()
//                            }
//                        }
//            }
//            .negativeClick { dialog.dismiss() }
//            .type(BlurDialog.TYPE_DOUBLE_OPTION)
//            .build(navActivity)
//    dialog.show()
//    return dialog


    dialog = BlurDialog()
    val builder = BlurDialog.Builder()
            .isCancelable(true)
            .isOutsideCancelable(true)
            .message("Exit the app will have no access to write document")
            .positiveMsg("Yes")
            .negativeMsg("No")
            .positiveClick {
                dialog.dismiss()
                AuthUI.getInstance()
                        .signOut(navActivity)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                navActivity.snackbar("Sign out successful").show()
                                navActivity.invalidateSignInUI()
                            }
                        }
            }
            .negativeClick {
                dialog.dismiss()
            }
            .dismissListener { }
            .type(BlurDialog.TYPE_DOUBLE_OPTION)
            .createBuilder(navActivity)
    dialog.setBuilder(builder)
    dialog.show()
    return dialog
}

private var lastX: Float = 0f
var isMovingRight: Boolean = true // true不会崩溃，进入nav detail左滑的时候
private var mActivePointerId: Int = ViewDragHelper.INVALID_POINTER
var pagePosition: Int = 0
var pagePositionOffset: Float = 0f
// used for enable back gesture
fun dispatchTouchEvent(editorActivity: EditorActivity, ev: MotionEvent) {
    when (ev.action) {
        MotionEvent.ACTION_DOWN -> {
            lastX = ev.x
            mActivePointerId = ev.getPointerId(0)
        }
        MotionEvent.ACTION_MOVE -> {
            try {
                isMovingRight = ev.getX(ev.findPointerIndex(mActivePointerId)) - lastX > 0
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        MotionEvent.ACTION_UP -> {
            mActivePointerId = ViewDragHelper.INVALID_POINTER
        }
    }
    enableSwipeBack(pagePosition, pagePositionOffset, editorActivity.mViewPager, editorActivity)
}

fun enableSwipeBack(position: Int, positionOffset: Float, vp_nav: CustomViewPager, activity: EditorActivity) {
    if (position == 0 && isMovingRight && positionOffset in 0f..0.3f) { // pagePosition从onPageSelected放到onPageScrolled之后就需要使用pagePositionOffset来限制在Review页面就可以返回的bug
        if (positionOffset > 0f) // 第一次进入positionOffset == 0f不能禁用viewPager
            vp_nav.setDisallowInterceptTouchEvent(true)
        else
            vp_nav.setDisallowInterceptTouchEvent(false)
        ParallaxHelper.getParallaxBackLayout(activity).setEnableGesture(true) // 滑动的过程当中，ParallaxBackLayout一直在接管手势
    } else {
        vp_nav.setDisallowInterceptTouchEvent(false)
        ParallaxHelper.getParallaxBackLayout(activity).setEnableGesture(false)
    }
}


