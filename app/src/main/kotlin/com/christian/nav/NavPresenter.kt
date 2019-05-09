package com.christian.nav

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.annotation.NonNull
import com.christian.R
import com.christian.data.Gospel
import com.christian.navdetail.NavDetailActivity
import com.christian.navdetail.gospel.GospelDetailFragment
import com.christian.navdetail.gospel.GospelReviewFragment
import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur
import com.github.anzewei.parallaxbacklayout.ParallaxHelper
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import eightbitlab.com.blurview.BlurView
import kotlinx.android.synthetic.main.nav_activity.*
import kotlinx.android.synthetic.main.nav_fragment.*
import org.jetbrains.anko.debug
import org.jetbrains.anko.dip
import org.jetbrains.anko.info
import org.jetbrains.anko.singleLine
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * This contains all the NAV business logic, and the MVP control center. We'll write the code here first.
 * Hold references to View and Model, implementation of View is NavActivity, implementation of Model
 * is Gospel
 */
class NavPresenter(private var navId: Int, override var view: NavContract.INavActivity) : NavContract.IPresenter {

    var navFragmentList = ArrayList<NavFragment>()

    private var navActivity: NavActivity

    init {
        view.presenter = this
        navActivity = view as NavActivity
    }

    override fun init(whichActivity: Int?, navFragment: NavFragment?) {
        when (navFragment == null && whichActivity != null) {
            // called from a activity
            true -> {
                navFragmentList.clear()
                when (whichActivity) {
                    NAV_ACTIVITY -> {
                        val homeFragment = NavFragment()
                        homeFragment.navId = 0
                        navFragmentList.add(homeFragment)

                        val gospelFragment = NavFragment()
                        gospelFragment.navId = 1
                        navFragmentList.add(gospelFragment)

                        val discipleFragment = NavFragment()
                        discipleFragment.navId = 2
                        navFragmentList.add(discipleFragment)

                        val meFragment = NavFragment()
                        meFragment.navId = 3
                        navFragmentList.add(meFragment)
                    }
                    GOSPEL_DETAIL_ACTIVITY -> {
                        val gospelDetailFragment = GospelDetailFragment()
                        gospelDetailFragment.navId = 4
                        navFragmentList.add(gospelDetailFragment)

                        val gospelReviewFragment = GospelReviewFragment()
                        gospelReviewFragment.navId = 5
                        navFragmentList.add(gospelReviewFragment)
                    }
                }
                view.initView(navFragmentList)
            }
            // called from a fragment
            false -> {
            }
        }
    }

    override fun deleteNav(navId: String) {
    }

    override fun updateNav(gospels: List<Gospel>) {
    }

    override fun readNav() {
    }

    fun generateNavId(itemId: Int): Int {
        when (itemId) {
            R.id.navigation_home -> {
                navId = 0
            }
            R.id.navigation_gospel -> {
                navId = 1
            }
            R.id.navigation_chat -> {
                navId = 2
            }
            R.id.navigation_me -> {
                navId = 3
            }
        }
        return navId
    }

}

// navId values
const val VIEW_HOME = 0
const val VIEW_GOSPEL = 1
const val VIEW_DISCIPLE = 2
const val VIEW_ME = 3
const val VIEW_GOSPEL_DETAIL = 4
const val VIEW_GOSPEL_REVIEW = 5
const val VIEW_ME_DETAIL = 6
const val VIEW_ME_HISTORY = 7
const val VIEW_ME_STAR = 8
const val VIEW_ME_SETTING = 9
const val VIEW_ME_OPEN_SOURCE = 10
const val VIEW_ME_ABOUT_US = 11
const val VIEW_ME_MAILS_FROM = 12
const val VIEW_ME_MAILS_TO = 13

const val VIEW_TYPE_NORMAL = 0
const val VIEW_TYPE_PORTRAIT = 1
const val VIEW_TYPE_SMALL = 2
const val VIEW_TYPE_BUTTON = 3

const val MESSAGE_SET_TOOLBAR_EXPANDED = 20190429

const val HIDE_THRESHOLD = 0 //移动多少距离后显示隐藏
const val initFragmentIndex = 0
const val nullString = ""
const val toolbarTitle = "toolbarTitle"
const val NAV_ID = "navId"
const val NAV_FRAGMENT_LIST = "navFragmentList"

// whichActivity parameters
const val NAV_ACTIVITY = 0
const val GOSPEL_DETAIL_ACTIVITY = 1
const val ME_PORTRAIT_DETAIL_ACTIVITY = 2
const val ME_SETTING_DETAIL_ACTIVITY = 3

const val RC_SIGN_IN = 0

// 布局设置竟然不生效？网上解决方案：https://www.cnblogs.com/yuqf/p/5808236.html
fun applyMarqueeEffect(textView: TextView) {
    textView.ellipsize = TextUtils.TruncateAt.MARQUEE
    textView.isHorizontalFadingEdgeEnabled = true
    textView.singleLine = true
    textView.isSelected = true
    textView.isFocusable = true
    textView.isFocusableInTouchMode = true
}

/**
 * utils to load json files from assets folder
 */
fun getJson(fileName: String, context: Context): String {
    //将json数据变成字符串
    val stringBuilder = StringBuilder()
    try {
        //获取assets资源管理器
        val assetManager = context.assets
        //通过管理器打开文件并读取
        val bf = BufferedReader(InputStreamReader(
                assetManager.open(fileName)))
        var line: String
        while (true) {
            try {
                line = bf.readLine()
            } catch (e: Exception) {
                break
            }
            stringBuilder.append(line)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return stringBuilder.toString()
}

/**
 * utils to blur a view
 */
fun makeViewBlur(view: BlurView, parent: ViewGroup, window: Window) {
    val windowBackground = window.decorView.background
    val radius = 25f
    view.setupWith(parent)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(SupportRenderScriptBlur(parent.context))
            .setBlurRadius(radius)
            .setHasFixedTransformationMatrix(false)
}

/**
 * utils to expand a toolbar
 */
fun setTabLayoutExpanded(context: Context, position: Int) {
    val navActivity = context as NavActivity
    navActivity.info {
        "setTabLayoutExpanded$position"
    }
    when (position) {
        VIEW_HOME -> {
            setToolbarExpanded(navActivity, true)
            setTabLayoutExpanded(context, false)
            setPortraitExpanded(context, false)
        }
        VIEW_GOSPEL -> {
            setToolbarExpanded(navActivity, true)
            setPortraitExpanded(context, false)
            var time = 0L
            if (isPortraitExpanded(context)) {
                time = 300
            }
            navActivity.tl_nav.postDelayed({
                setTabLayoutExpanded(context, true)
            }, time)
        }
        VIEW_DISCIPLE -> {
            setToolbarExpanded(navActivity, true)
            setTabLayoutExpanded(context, false)
            setPortraitExpanded(context, false)
        }
        VIEW_ME -> {
            setToolbarExpanded(navActivity, false)
            setTabLayoutExpanded(context, false)
            var time = 0L
            if (isTabLayoutExpanded(context)) {
                time = 100
            }
            navActivity.tl_nav.postDelayed({
                setPortraitExpanded(context, true)
            }, time)
        }
    }
}

private fun setTabLayoutExpanded(context: Context, expanded: Boolean) {
    val navActivity = context as NavActivity
    when (expanded) {
        true -> {
            if (!isTabLayoutExpanded(context))
                expandedAnimation(navActivity, true)
        }
        false -> {
            if (isTabLayoutExpanded(context))
                expandedAnimation(navActivity, false)
        }
    }

}

private fun setPortraitExpanded(context: Context, expanded: Boolean) {
    val navActivity = context as NavActivity
    when (expanded) {
        true -> {
            if (!isPortraitExpanded(context))
                expandedAnimationPortrait(navActivity, true)
        }
        false -> {
            if (isPortraitExpanded(context))
                expandedAnimationPortrait(navActivity, false)
        }
    }

}

fun isTabLayoutExpanded(context: Context): Boolean {
    val navActivity = context as NavActivity
    return navActivity.tl_nav.visibility == View.VISIBLE
}

fun isPortraitExpanded(context: Context): Boolean {
    val navActivity = context as NavActivity
    return navActivity.portrait_nav.visibility == View.VISIBLE
}

private fun expandedAnimation(navActivity: NavActivity, expanded: Boolean) {
    val animator: ValueAnimator = if (expanded) {
        ValueAnimator.ofFloat(navActivity.abl_nav.bottom.toFloat(), navActivity.abl_nav.bottom.toFloat() + navActivity.dip(56))
    } else {
        ValueAnimator.ofFloat(navActivity.abl_nav.bottom.toFloat(), navActivity.abl_nav.bottom.toFloat() - navActivity.dip(56))
    }
    animator.duration = 100
    animator.interpolator = LinearInterpolator()
    animator.addUpdateListener {
        val floatValue = it.animatedValue as Float
        navActivity.abl_nav.bottom = floatValue.toInt()
    }
    animator.start()
    animator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator) {
        }

        override fun onAnimationEnd(animation: Animator) {
            if (expanded) {
                navActivity.tl_nav.visibility = View.VISIBLE
            } else {
                navActivity.tl_nav.visibility = View.GONE
            }
        }

        override fun onAnimationCancel(animation: Animator) {
        }

        override fun onAnimationStart(animation: Animator) {
        }

    })
}

private fun expandedAnimationPortrait(navActivity: NavActivity, expanded: Boolean) {
    val animator: ValueAnimator = if (expanded) {
        ValueAnimator.ofFloat(navActivity.abl_nav.bottom.toFloat(), navActivity.abl_nav.bottom.toFloat() + navActivity.dip(168))
    } else {
        ValueAnimator.ofFloat(navActivity.abl_nav.bottom.toFloat(), navActivity.abl_nav.bottom.toFloat() - navActivity.dip(168))
    }
    animator.duration = 300
    animator.interpolator = LinearInterpolator()
    animator.addUpdateListener {
        val floatValue = it.animatedValue as Float
        navActivity.abl_nav.bottom = floatValue.toInt()
    }
    animator.start()
    animator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator) {
        }

        override fun onAnimationEnd(animation: Animator) {
            if (expanded) {
                navActivity.portrait_nav.visibility = View.VISIBLE
            } else {
                navActivity.portrait_nav.visibility = View.GONE
            }
        }

        override fun onAnimationCancel(animation: Animator) {
        }

        override fun onAnimationStart(animation: Animator) {
        }

    })
}

fun enableSwipeBack(position: Int, positionOffset: Float, activity: NavDetailActivity) {
    if (position == 0 && activity.isMovingRight && positionOffset in 0f..0.3f) { // pagePosition从onPageSelected放到onPageScrolled之后就需要使用pagePositionOffset来限制在Review页面就可以返回的bug
        activity.debug { "enableSwipeBack: enable back gesture" }
        if (positionOffset > 0f) // 第一次进入positionOffset == 0f不能禁用viewPager
            activity.vp_nav.setDisallowInterceptTouchEvent(true)
        else
            activity.vp_nav.setDisallowInterceptTouchEvent(false)
        ParallaxHelper.getParallaxBackLayout(activity).setEnableGesture(true) // 滑动的过程当中，ParallaxBackLayout一直在接管手势
    } else {
        activity.debug { "enableSwipeBack: disable back gesture" }
        activity.vp_nav.setDisallowInterceptTouchEvent(false)
        ParallaxHelper.getParallaxBackLayout(activity).setEnableGesture(false)
    }
}

fun appBarLayoutOnOffsetChangedListener(navActivity: NavActivity, appBarLayout: AppBarLayout, verticalOffset: Int) {
    if (verticalOffset == -appBarLayout.height) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            navActivity.abl_nav.elevation = 0f
        }
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            navActivity.abl_nav.elevation = navActivity.dip(4).toFloat()
        }
    }

    controlOverScroll(navActivity, appBarLayout, verticalOffset)
    // TwinklingRefreshLayout
//    if (navActivity.srl_nav != null) {
//        navActivity.srl_nav.setEnableRefresh(false)
//    }
}

fun controlOverScroll(navActivity: NavActivity, appBarLayout: AppBarLayout, verticalOffset: Int) {
//    val navFragment = navActivity.navFragment
//    if (verticalOffset == 0 && navFragment.isPageBottom) {
//        navActivity.srl_nav.setEnableOverScroll(false)
//        navActivity.srl_nav.setEnableLoadmore(false)
//    } else if (verticalOffset == -appBarLayout.height && navFragment.isPageTop) {
//        navActivity.srl_nav.setEnableOverScroll(false)
//    } else if (verticalOffset == 0 || verticalOffset == -appBarLayout.height) {
//        navActivity.srl_nav.setEnableOverScroll(true)
//    }
}

fun scrollRvToTop(navActivity: NavActivity) {
    navActivity.navFragmentPagerAdapter.currentFragment.rv_nav.smoothScrollToPosition(0) // 为了滚到顶
    navActivity.abl_nav.setExpanded(true, true)
}

@SuppressLint("RestrictedApi")
fun disableShiftMode(view: BottomNavigationView) {
    val menuView = view.getChildAt(0) as BottomNavigationMenuView
    menuView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
    for (i in 0 until menuView.childCount) {
        val item = menuView.getChildAt(i) as BottomNavigationItemView
        item.setShifting(false)
        item.setChecked(item.itemData.isChecked)
    }

}

fun setToolbarVisible(navActivity: NavActivity, expanded: Boolean) {
    when (expanded) {
        true -> {
//            navActivity.tb_nav.visibility = View.VISIBLE
            navActivity.abl_nav.addOnOffsetChangedListener(navActivity.appBarLayoutOnOffsetChangedListener)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                navActivity.abl_nav.elevation = navActivity.dip(4).toFloat()
            }
            val anim = AnimationUtils.loadAnimation(navActivity, R.anim.abc_slide_in_top)
            if (navActivity.tb_nav.visibility == View.GONE)
                navActivity.tb_nav.startAnimation(anim)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    navActivity.tb_nav.visibility = View.VISIBLE
                }

                override fun onAnimationStart(animation: Animation?) {
                    navActivity.tb_nav.visibility = View.VISIBLE
                }

            })
        }
        false -> {
            navActivity.tb_nav.visibility = View.GONE
            navActivity.abl_nav.removeOnOffsetChangedListener(navActivity.appBarLayoutOnOffsetChangedListener)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                navActivity.abl_nav.elevation = navActivity.dip(0).toFloat()
            }
//            val anim = AnimationUtils.loadAnimation(navActivity, R.anim.abc_slide_out_top)
//            if (navActivity.tb_nav.visibility == View.VISIBLE)
//                navActivity.tb_nav.startAnimation(anim)
//            anim.setAnimationListener(object : Animation.AnimationListener {
//                override fun onAnimationRepeat(animation: Animation?) {
//                }
//
//                override fun onAnimationEnd(animation: Animation?) {
//                    navActivity.tb_nav.visibility = View.GONE
//                }
//
//                override fun onAnimationStart(animation: Animation?) {
//                }
//
//            })
        }
    }
}

private fun setToolbarExpanded(context: Context, expanded: Boolean) {
    val navActivity = context as NavActivity
    when (expanded) {
        true -> {
            if (!isToolbarExpanded(context))
                expandedAnimationToolbar(navActivity, true)
        }
        false -> {
            if (isToolbarExpanded(context))
                expandedAnimationToolbar(navActivity, false)
        }
    }

}

fun isToolbarExpanded(context: Context): Boolean {
    val navActivity = context as NavActivity
    return navActivity.tb_nav.visibility == View.VISIBLE
}

private fun expandedAnimationToolbar(navActivity: NavActivity, expanded: Boolean) {
    val animator: ValueAnimator = if (expanded) {
        ValueAnimator.ofFloat(navActivity.abl_nav.bottom.toFloat(), navActivity.abl_nav.bottom.toFloat() + navActivity.dip(56))
    } else {
        ValueAnimator.ofFloat(navActivity.abl_nav.bottom.toFloat(), navActivity.abl_nav.bottom.toFloat() - navActivity.dip(56))
    }
    animator.duration = 0
    animator.interpolator = LinearInterpolator()
    animator.addUpdateListener {
        val floatValue = it.animatedValue as Float
        navActivity.abl_nav.bottom = floatValue.toInt()
    }
    animator.start()
    animator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator) {
        }

        override fun onAnimationEnd(animation: Animator) {
            if (expanded) {
                navActivity.abl_nav.addOnOffsetChangedListener(navActivity.appBarLayoutOnOffsetChangedListener)
                navActivity.tb_nav.visibility = View.VISIBLE
                navActivity.tb_nav.postDelayed({
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        navActivity.abl_nav.elevation = navActivity.dip(4).toFloat()
                    }
                }, 0)
            } else {
                navActivity.abl_nav.removeOnOffsetChangedListener(navActivity.appBarLayoutOnOffsetChangedListener)
                navActivity.tb_nav.visibility = View.GONE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    navActivity.abl_nav.elevation = navActivity.dip(0).toFloat()
                }
            }
        }

        override fun onAnimationCancel(animation: Animator) {
        }

        override fun onAnimationStart(animation: Animator) {
        }

    })
}

fun getDocumentReference(@NonNull collectionPath: String, @NonNull documentPath: String): DocumentReference {
    val firestore = FirebaseFirestore.getInstance()
    return firestore.collection(collectionPath).document(documentPath)
}

fun getQuery(@NonNull collectionPath: String, @NonNull field: String, @NonNull direction: Query.Direction): Query {
    val firestore = FirebaseFirestore.getInstance()
    return firestore.collection(collectionPath).orderBy(field, direction)
}
