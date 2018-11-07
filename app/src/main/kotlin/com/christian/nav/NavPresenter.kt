package com.christian.nav

import com.christian.R
import com.christian.data.Nav
import com.christian.data.source.NavsDataSource
import com.christian.data.source.NavsRepository
import com.christian.data.source.remote.NavService
import com.christian.http.CacheInterceptor
import com.christian.http.SdHelper
import com.christian.http.cache.CacheStrategy
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.anko.info
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * This contains all the NAV business logic, and the MVP control center. We'll write the code here first.
 * Hold references to View and Model, implementation of View is NavActivity, implementation of Model
 * is Nav
 */
class NavPresenter(
        private var navId: String,
        private val navsRepository: NavsRepository,
        private val navActivity: NavContract.INavActivity) : NavContract.IPresenter {

    companion object {
        const val DEFAULT_HTTP_CACHE_SIZE = 10 * 1024 * 1024L
        const val DEFAULT_TIMEOUT = 5L
    }

    private var call: Call<List<Nav>>

    init {
        navActivity.presenter = this

        val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.193:8080/")
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val navService = retrofit.create(NavService::class.java)
        call = navService.getNavs()
    }

    override fun init(navFragment: NavFragment?) {
        val navList = listOf(Nav())
        val navFragmentList = listOf(NavFragment(this), NavFragment(this), NavFragment(this), NavFragment(this))

        when (navFragment == null) {
            true -> navActivity.initView(navFragmentList, navList)
            false -> {
                info { "initView Fragment" }
                navFragment.initView(navFragmentList, navList)
            }
        }
    }

    override fun deinit() {
    }

    override fun deleteNav(navId: String) {
    }

    override fun createNav(navId: String, isSrl: Boolean, navFragment: NavFragment): Boolean {
        if (isSrl) {
            navActivity.hidePb()
        } else {
            navActivity.showPb()
            navFragment.hideSrl()
        }

        navsRepository.getNavs(call, object : NavsDataSource.LoadNavsCallback {

            override fun onNavsLoaded(navs: List<Nav>) {

                navFragment.invalidateRv(navs)

                navActivity.hidePb()
                navFragment.hideSrl()

            }

            override fun onDataNotAvailable() {
                navActivity.hidePb()
                navFragment.hideSrl()
            }

        })
        info { "insert nav id $navId" }
        return true
    }

    override fun updateNav(navs: List<Nav>) {
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

    fun generateNavId(itemId: Int): String {
        when (itemId) {
            R.id.navigation_home -> {
                navId = "0"
            }
            R.id.navigation_gospel -> {
                navId = "1"
            }
            R.id.navigation_chat -> {
                navId = "2"
            }
            R.id.navigation_me -> {
                navId = "3"
            }
        }
        return navId
    }

    fun showAblAndScrollRv() {
    }

    fun hideAblAndScrollRv() {
    }

}

const val HIDE_THRESHOLD = 0 //移动多少距离后显示隐藏
const val initFragmentIndex = 0