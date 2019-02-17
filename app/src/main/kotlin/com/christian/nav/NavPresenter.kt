package com.christian.nav

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.christian.R
import com.christian.data.NavBean
import com.christian.data.source.NavsDataSource
import com.christian.data.source.NavsRepository
import com.christian.data.source.remote.NavService
import com.christian.http.CacheInterceptor
import com.christian.http.SdHelper
import com.christian.http.cache.CacheStrategy
import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur
import eightbitlab.com.blurview.BlurView
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.anko.info
import org.jetbrains.anko.singleLine
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * This contains all the NAV business logic, and the MVP control center. We'll write the code here first.
 * Hold references to View and Model, implementation of View is NavActivity, implementation of Model
 * is NavBean
 */
class NavPresenter(
        private var navId: Int,
        private val navsRepository: NavsRepository,
        override var view: NavContract.INavActivity) : NavContract.IPresenter {

    companion object {
        const val DEFAULT_HTTP_CACHE_SIZE = 10 * 1024 * 1024L
        const val DEFAULT_TIMEOUT = 5L
    }

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
    private val call: Call<List<NavBean>>

    init {
        view.presenter = this

        val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.193:8080/")
//                .baseUrl("http://10.200.11.209:8080/")
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val navService = retrofit.create(NavService::class.java)
        call = navService.getNavs()
    }

    override fun init(navFragmentSize: Int?, navFragment: NavFragment?, savedInstanceState: Bundle?) {
        when (navFragment == null && navFragmentSize != null) {
            // represent called from a Activity
            true -> {
                navFragmentList.clear()
                for (i in 0 until navFragmentSize) {
                    val mNavFragment = NavFragment()
//                            mNavFragment.retainInstance = true

                    mNavFragment.navId = i
                    navFragmentList.add(mNavFragment)
                    info { "nav fragment is $mNavFragment and navId is ${mNavFragment.navId} ---initial" }
                }
                view.initView(navFragmentList)
            }
            // represent called from a Fragment
            false -> {
                navFragment?.initView(navList)
            }
        }
    }

    override fun deleteNav(navId: String) {
    }

    override fun createNav(navId: Int, isSrl: Boolean, navFragment: NavFragment): Boolean {

        info { "nav fragment is $navFragment and navId is $navId ---createNav" }

        if (isSrl) {
            view.hidePb()
        } else {
            view.showPb()
            navFragment.hideSrl()
        }

        navsRepository.getNavs(call, object : NavsDataSource.LoadNavsCallback {

            override fun onNavsLoaded(navBeans: List<NavBean>) {

                navFragment.invalidateRv(navBeans)

                view.hidePb()
                navFragment.hideSrl()

            }

            override fun onDataNotAvailable() {
                view.hidePb()
                navFragment.hideSrl()
            }

        })
        info { "insert nav id $navId" }
        return true
    }

    override fun updateNav(navBeans: List<NavBean>) {
    }

    override fun readNav() {
    }

    /**
     * Cache strategy
     */
    private fun getOkHttpClient(): OkHttpClient {

        val logInterceptor = HttpLoggingInterceptor(CacheStrategy())
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
        builder.cache(getCache())
        builder.addInterceptor(CacheInterceptor()) //为了在没有网络的情况下也能设置request和response
        builder.addNetworkInterceptor(CacheInterceptor())
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        return builder.build()
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

    fun showAblAndScrollRv() {
    }

    fun hideAblAndScrollRv() {
    }

}

const val VIEW_HOME = 0
const val VIEW_GOSPEL = 1
const val VIEW_DISCIPLE = 2
const val VIEW_ME = 3
const val HIDE_THRESHOLD = 0 //移动多少距离后显示隐藏
const val initFragmentIndex = 0
const val nullString = ""
const val toolbarTitle = "toolbarTitle"
const val NAV_ID = "navId"
const val NAV_FRAGMENT_LIST = "navFragmentList"

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
fun makeViewBlur(view: BlurView, parent: ViewGroup, window: Window) {
    val windowBackground = window.decorView.background
    val radius = 7f
    view.setupWith(parent)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(SupportRenderScriptBlur(parent.context))
            .setBlurRadius(radius)
            .setHasFixedTransformationMatrix(false)
}
