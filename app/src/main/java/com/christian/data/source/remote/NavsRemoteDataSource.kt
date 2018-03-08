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
import com.christian.data.Nav
import com.christian.data.source.NavsDataSource
import com.google.common.collect.Lists
import retrofit2.Retrofit



/**
 * Implementation of the data source that adds a latency simulating network.
 */
object NavsRemoteDataSource : NavsDataSource {

    private const val SERVICE_LATENCY_IN_MILLIS = 5000L

    private var NAVS_SERVICE_DATA = LinkedHashMap<String, Nav>(2)

    init {
        addNav("Build tower in Pisa", "Ground looks good, no foundation work required.")
        addNav("Finish bridge in Tacoma", "Found awesome girders at half the cost!")
    }

    private fun addNav(title: String, description: String) {
        val newNav = Nav(title, description)
        NAVS_SERVICE_DATA.put(newNav.id, newNav)
    }

    /**
     * Note: [NavsDataSource.LoadNavsCallback.onDataNotAvailable] is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    override fun getNavs(callback: NavsDataSource.LoadNavsCallback) {
        // Simulate network by delaying the execution.
        val Navs = Lists.newArrayList(NAVS_SERVICE_DATA.values)
        Handler().postDelayed({
            callback.onNavsLoaded(Navs)
        }, SERVICE_LATENCY_IN_MILLIS)

        val retrofit = Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .build()
        retrofit.
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
        val completedNav = Nav(nav.title, nav.description, nav.id).apply {
            isCompleted = true
        }
        NAVS_SERVICE_DATA.put(nav.id, completedNav)
    }

    override fun completeNav(navId: String) {
        // Not required for the remote data source because the {@link NavsRepository} handles
        // converting from a {@code navId} to a {@link Nav} using its cached data.
    }

    override fun activateNav(nav: Nav) {
        val activeNav = Nav(nav.title, nav.description, nav.id)
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
}