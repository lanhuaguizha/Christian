/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.christian.data.source.remote

import android.content.Context
import android.os.Handler
import android.util.Log
import com.christian.data.Nav
import com.christian.data.source.NavsDataSource
import com.christian.util.HttpLoggingInterceptor
import com.christian.util.NetworkUtils
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * Implementation of the data source that adds a latency simulating network.
 */
object NavsRemoteDataSource : NavsDataSource {

    private const val TAG = "NavsRemoteDataSource"

    private const val SERVICE_LATENCY_IN_MILLIS = 5000L

    private var NAVS_SERVICE_DATA = LinkedHashMap<String, Nav>(2)

    private const val DEFAULT_HTTP_CACHE_SIZE = 10 * 1024 * 1024L

    private const val DEFAULT_TIMEOUT = 5L

    init {
        addNav("Build tower in Pisa", "Ground looks good, no foundation work required.")
        addNav("Finish bridge in Tacoma", "Found awesome girders at half the cost!")
    }

    private fun addNav(title: String, description: String) {
        val newNav = Nav(title, description)
        NAVS_SERVICE_DATA.put(newNav.id, newNav)
    }

    private var isFailure: Boolean = false

    /**
     * Note: [NavsDataSource.LoadNavsCallback.onDataNotAvailable] is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    override fun getNavs(ctx: Context, callback: NavsDataSource.LoadNavsCallback) {

        //设置缓存路径 内置存储
        val httpCacheDirectory = File(ctx.cacheDir, "responses")
        val cacheSize = DEFAULT_HTTP_CACHE_SIZE  //设置缓存10M


        val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.51.137:8080/")
//                .baseUrl("http://10.200.69.48:8080/")
                .client(getOkHttpClient(ctx, true, true))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val navService = retrofit.create(NavService::class.java)
        val call = navService.getNavs()

        call.enqueue(object : Callback<List<Nav>> {

            override fun onFailure(call: Call<List<Nav>>?, t: Throwable?) {
                callback.onDataNotAvailable()
                isFailure = true
                val newCall = call?.clone()
                newCall?.enqueue(object : Callback<List<Nav>> {

                    override fun onFailure(call: Call<List<Nav>>?, t: Throwable?) {
                        callback.onDataNotAvailable()
                    }

                    override fun onResponse(call: Call<List<Nav>>?, response: Response<List<Nav>>?) {

                        if (response != null) {

                            response.body()?.let {
                                callback.onNavsLoaded(it)
                                isFailure = false
                            }

                        } else {

                            // Todo this is no data

                        }

                    }

                })
            }

            override fun onResponse(call: Call<List<Nav>>?, response: Response<List<Nav>>?) {

                if (response != null) {

                    response.body()?.let {
                        callback.onNavsLoaded(it)
                        isFailure = false
                    }

                } else {

                    // Todo this is no data

                }

            }

        })

    }

    /**
     * Note: [NavsDataSource.GetNavCallback.onDataNotAvailable] is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    override fun getNav(navId: String, callback: NavsDataSource.GetNavCallback) {
        val Nav = NAVS_SERVICE_DATA[navId]

        // Simulate network by delaying the execution.
        with(Handler()) {
            if (Nav != null) {
                postDelayed({ callback.onNavLoaded(Nav) }, SERVICE_LATENCY_IN_MILLIS)
            } else {
                postDelayed({ callback.onDataNotAvailable() }, SERVICE_LATENCY_IN_MILLIS)
            }
        }
    }

    override fun saveNav(nav: Nav) {
        NAVS_SERVICE_DATA.put(nav.id, nav)
    }

    override fun completeNav(nav: Nav) {
        val completedNav = Nav(nav.title, nav.relation, nav.id).apply {
            isCompleted = true
        }
        NAVS_SERVICE_DATA.put(nav.id, completedNav)
    }

    override fun completeNav(navId: String) {
        // Not required for the remote data source because the {@link NavsRepository} handles
        // converting from a {@code navId} to a {@link Nav} using its cached data.
    }

    override fun activateNav(nav: Nav) {
        val activeNav = Nav(nav.title, nav.relation, nav.id)
        NAVS_SERVICE_DATA.put(nav.id, activeNav)
    }

    override fun activateNav(navId: String) {
        // Not required for the remote data source because the {@link NavsRepository} handles
        // converting from a {@code navId} to a {@link Nav} using its cached data.
    }

    override fun clearCompletedNavs() {
        NAVS_SERVICE_DATA = NAVS_SERVICE_DATA.filterValues {
            !it.isCompleted
        } as LinkedHashMap<String, Nav>
    }

    override fun refreshNavs() {
        // Not required because the {@link NavsRepository} handles the logic of refreshing the
        // Navs from all the available data sources.
    }

    override fun deleteAllNavs() {
        NAVS_SERVICE_DATA.clear()
    }

    override fun deleteNav(navId: String) {
        NAVS_SERVICE_DATA.remove(navId)
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
            if (isFailure || !NetworkUtils.isNetworkAvailable(context)) {
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