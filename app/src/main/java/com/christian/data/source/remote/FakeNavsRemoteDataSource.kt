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

import android.support.annotation.VisibleForTesting
import com.christian.data.source.NavsDataSource
import com.google.common.collect.Lists
import com.christian.data.Nav

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
class FakeNavsRemoteDataSource private constructor() : NavsDataSource {

    private val NAVS_SERVICE_DATA = LinkedHashMap<String, Nav>()

    override fun getNavs(callback: NavsDataSource.LoadNavsCallback) {
        callback.onNavsLoaded(Lists.newArrayList(NAVS_SERVICE_DATA.values))
    }

    override fun getNav(navId: String, callback: NavsDataSource.GetNavCallback) {
        val nav = NAVS_SERVICE_DATA[navId]
        if (nav != null) {
            callback.onNavLoaded(nav)
        } else {
            callback.onDataNotAvailable()
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
        // Not required for the remote data source.
    }

    override fun activateNav(nav: Nav) {
        val activeNav = Nav(nav.title, nav.description, nav.id)
        NAVS_SERVICE_DATA.put(nav.id, activeNav)
    }

    override fun activateNav(navId: String) {
        // Not required for the remote data source.
    }

    override fun clearCompletedNavs() {
        with(NAVS_SERVICE_DATA.entries.iterator()) {
            while (hasNext()) {
                if (next().value.isCompleted) {
                    remove()
                }
            }
        }
    }

    override fun refreshNavs() {
        // Not required because the {@link NavsRepository} handles the logic of refreshing the
        // Navs from all the available data sources.
    }

    override fun deleteNav(navId: String) {
        NAVS_SERVICE_DATA.remove(navId)
    }

    override fun deleteAllNavs() {
        NAVS_SERVICE_DATA.clear()
    }

    @VisibleForTesting
    fun addNavs(vararg navs: Nav) {
        for (Nav in navs) {
            NAVS_SERVICE_DATA.put(Nav.id, Nav)
        }
    }

    companion object {

        private lateinit var INSTANCE: FakeNavsRemoteDataSource
        private var needsNewInstance = true

        @JvmStatic
        fun getInstance(): FakeNavsRemoteDataSource {
            if (needsNewInstance) {
                INSTANCE = FakeNavsRemoteDataSource()
                needsNewInstance = false
            }
            return INSTANCE
        }
    }
}
