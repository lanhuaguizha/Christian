package com.christian.nav

import android.content.Context
import android.util.Log
import com.christian.data.Nav
import com.christian.data.source.NavsDataSource
import com.christian.data.source.NavsRepository
import com.christian.data.source.remote.NavService
import com.christian.data.source.remote.NavsRemoteDataSource
import com.christian.util.HttpLoggingInterceptor
import com.christian.util.NetworkUtils
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
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
        private val navId: String,
        private val navsRepository: NavsRepository,
        private val navView: NavContract.View) : NavContract.Presenter {

    private var call: Call<List<Nav>>

    companion object {

        const val DEFAULT_HTTP_CACHE_SIZE = 10 * 1024 * 1024L

        const val DEFAULT_TIMEOUT = 5L

        const val TAG = "NavPresenter"
    }

    init {

        navView.presenter = this

        val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.193:8080/")
//                .baseUrl("http://10.200.69.48:8080/")
                .client(getOkHttpClient(navView as NavActivity, true, true))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val navService = retrofit.create(NavService::class.java)
        call = navService.getNavs()

    }

    override fun start() {

        navView.initView(navs = listOf(Nav()))

    }

    override fun insertNav(itemId: Int) {

        insertNav(itemId, false)

    }

    override fun insertNav(itemId: Int, isSrl: Boolean) {

        if (isSrl) navView.stopPb() else navView.startPb()

        navsRepository.getNavs(call, object : NavsDataSource.LoadNavsCallback {

            override fun onNavsLoaded(navs: List<Nav>) {

                navView.invalidateRv(navs)

                navView.stopPb()
                navView.stopSrl()

            }

            override fun onDataNotAvailable() {

                navView.stopPb()
                navView.stopSrl()

            }

        })

    }

    override fun updateNav(navs: List<Nav>) {
    }

    override fun queryNav() {
    }

    /**
     * 缓存策略
     *
     * @param context
     * @param isAllowCache 是否允许使用缓存策略
     * @param cacheMethod  false:有网和没有网都是先读缓存 true:离线可以缓存，在线就获取最新数据 default=false
     * @return
     */
    private fun getOkHttpClient(context: Context, isAllowCache: Boolean, cacheMethod: Boolean): OkHttpClient {

        /**
         * 获取缓存
         */
        val baseInterceptor = Interceptor {
            var request = it.request()
            if (NavsRemoteDataSource.isFailure || !NetworkUtils.isNetworkAvailable(context)) {
                /**
                 * 离线缓存控制  总的缓存时间=在线缓存时间+设置离线缓存时间
                 */
                val maxStale = 60 * 60 * 24 * 28 // 离线时缓存保存4周,单位:秒
                val tempCacheControl = CacheControl.Builder()
                        .onlyIfCached()
                        .maxStale(maxStale, TimeUnit.SECONDS)
                        .build()
                request = request.newBuilder()
                        .cacheControl(tempCacheControl)
                        .build()
                Log.i(TAG, "intercept:no network ")
            }
            it.proceed(request)
        }
        //只有 网络拦截器环节 才会写入缓存写入缓存,在有网络的时候 设置缓存时间
        val rewriteCacheControlInterceptor = HttpLoggingInterceptor(isAllowCache)
        //设置缓存路径 内置存储
        //File httpCacheDirectory = new File(context.getCacheDir(), "responses");
        //外部存储
        val httpCacheDirectory = File(context.externalCacheDir, "responses")
        //设置缓存 10M
        val cacheSize = DEFAULT_HTTP_CACHE_SIZE
        val cache = Cache(httpCacheDirectory, cacheSize)
        val builder = OkHttpClient.Builder()
        builder.cache(cache)
        if (isAllowCache && cacheMethod) {
            builder.addInterceptor(baseInterceptor)
        }
        builder.addNetworkInterceptor(rewriteCacheControlInterceptor)
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        return builder.build()
    }

}