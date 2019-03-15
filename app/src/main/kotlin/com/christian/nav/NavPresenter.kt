package com.christian.nav

import android.content.Context
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.TextView
import com.christian.R
import com.christian.data.NavBean
import com.christian.data.source.NavsRepository
import com.christian.disciple.DiscipleFragment
import com.christian.http.SdHelper
import com.christian.gospeldetail.ui.main.GospelDetailFragment
import com.christian.gospeldetail.ui.main.GospelReviewFragment
import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur
import eightbitlab.com.blurview.BlurView
import okhttp3.Cache
import org.jetbrains.anko.singleLine
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

/**
 * This contains all the NAV business logic, and the MVP control center. We'll write the code here first.
 * Hold references to View and Model, implementation of View is NavActivity, implementation of Model
 * is NavBean
 */
class NavPresenter(
        private var navId: Int,
        private val navsRepository: NavsRepository,
        override var view: NavContract.INavActivity) : NavContract.IPresenter {

    val tabTitleList = listOf(
            "马太福音",
            "马可福音",
            "路加福音",
            "约翰福音",
            "使徒行传",
            "罗马书",
            "哥林多前书",
            "哥林多后书",
            "加拉太书",
            "以弗所书",
            "腓立比书",
            "歌罗西书",
            "帖撒罗尼迦前书",
            "帖撒罗尼迦后书",
            "提摩太前书",
            "提摩太后书",
            "提多书",
            "腓利门书",
            "希伯来书",
            "雅各书",
            "彼得前书",
            "彼得后书",
            "约翰一书",
            "约翰二书",
            "约翰三书",
            "犹太书",
            "启示录"
    )

    private val navList = listOf(NavBean())
    var navFragmentList = ArrayList<NavFragment>()

    init {
        view.presenter = this

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

                        val discipleFragment = DiscipleFragment()
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
                navFragment?.initView(navList)
            }
        }
    }

    override fun deleteNav(navId: String) {
    }

    override fun updateNav(navBeans: List<NavBean>) {
    }

    override fun readNav() {
    }

    private fun getCache(): Cache {
        val httpCacheDirectory = File(SdHelper.getDiskCacheDir(), "responses")
        val cacheSize = 10 * 1024 * 1024//确定10M大小的缓存
        return Cache(httpCacheDirectory, cacheSize.toLong())
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

// nav contains views
const val VIEW_HOME = 0
const val VIEW_GOSPEL = 1
const val VIEW_DISCIPLE = 2
const val VIEW_ME = 3
const val VIEW_GOSPEL_DETAIL = 4
const val VIEW_GOSPEL_REVIEW = 5

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
 * utils to load json files from /assets folder
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
fun makeViewBlur(view: BlurView, parent: ViewGroup) {
//    val windowBackground = window.decorView.background
    val radius = 25f
    view.setupWith(parent)
//            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(SupportRenderScriptBlur(parent.context))
            .setBlurRadius(radius)
            .setHasFixedTransformationMatrix(false)
}
