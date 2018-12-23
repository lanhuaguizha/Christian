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

import android.os.Handler
import android.util.Log
import com.christian.data.NavBean
import com.christian.data.source.NavsDataSource
import org.jetbrains.anko.info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Implementation of the data source that adds a latency simulating network.
 */
object NavsRemoteDataSource : NavsDataSource {

    private const val SERVICE_LATENCY_IN_MILLIS = 5000L

    private var NAVS_SERVICE_DATA = LinkedHashMap<String, NavBean>(2)

    init {
        addNav("Build tower in Pisa", "Ground looks good, no foundation work required.")
        addNav("Finish bridge in Tacoma", "Found awesome girders at half the cost!")
    }

    private fun addNav(title: String, description: String) {
        val newNav = NavBean(title, description)
        NAVS_SERVICE_DATA.put(newNav.id, newNav)
    }

    /**
     * Note: [NavsDataSource.LoadNavsCallback.onDataNotAvailable] is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    override fun getNavs(call: Call<List<NavBean>>, callback: NavsDataSource.LoadNavsCallback) {

        val newCall = call.clone()
        newCall.enqueue(object : Callback<List<NavBean>> {

            override fun onFailure(call: Call<List<NavBean>>?, t: Throwable?) {
                info { "onFailure${t.toString()}" }
                Log.d("cache_log", "onFailure")
                callback.onDataNotAvailable()
            }

            override fun onResponse(call: Call<List<NavBean>>?, response: Response<List<NavBean>>) {
                Log.d("cache_log", response.code().toString())
                when (response.code()) {
                    504 -> callback.onDataNotAvailable()
                }
                info { "onResponse:${response.toString()}" }

                response.body()?.let {
                    callback.onNavsLoaded(it)
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
        val nav = NAVS_SERVICE_DATA[navId]

        // Simulate network by delaying the execution.
        with(Handler()) {
            if (nav != null) {
                postDelayed({ callback.onNavLoaded(nav) }, SERVICE_LATENCY_IN_MILLIS)
            } else {
                postDelayed({ callback.onDataNotAvailable() }, SERVICE_LATENCY_IN_MILLIS)
            }
        }
    }

    override fun saveNav(navBean: NavBean) {
        NAVS_SERVICE_DATA.put(navBean.id, navBean)
    }

    override fun completeNav(navBean: NavBean) {
        val completedNav = NavBean(navBean.title, navBean.relation, navBean.id).apply {
            isCompleted = true
        }
        NAVS_SERVICE_DATA.put(navBean.id, completedNav)
    }

    override fun completeNav(navId: String) {
        // Not required for the remote data source because the {@link NavsRepository} handles
        // converting from a {@code navId} to a {@link NavBean} using its cached data.
    }

    override fun activateNav(navBean: NavBean) {
        val activeNav = NavBean(navBean.title, navBean.relation, navBean.id)
        NAVS_SERVICE_DATA.put(navBean.id, activeNav)
    }

    override fun activateNav(navId: String) {
        // Not required for the remote data source because the {@link NavsRepository} handles
        // converting from a {@code navId} to a {@link NavBean} using its cached data.
    }

    override fun clearCompletedNavs() {
        NAVS_SERVICE_DATA = NAVS_SERVICE_DATA.filterValues {
            !it.isCompleted
        } as LinkedHashMap<String, NavBean>
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


}