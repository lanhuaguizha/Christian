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
package com.christian.data.source.local

import android.support.annotation.VisibleForTesting
import com.christian.data.Nav
import com.christian.data.source.NavsDataSource
import com.christian.util.AppExecutors


/**
 * Concrete implementation of a data source as a db.
 */
class NavsLocalDataSource private constructor(
        val appExecutors: AppExecutors,
        val navsDao: NavsDao
) : NavsDataSource {

    /**
     * Note: [NavsDataSource.LoadNavsCallback.onDataNotAvailable] is fired if the database doesn't exist
     * or the table is empty.
     */
    override fun getNavs(callback: NavsDataSource.LoadNavsCallback) {
        appExecutors.diskIO.execute {
            val navs = navsDao.getNavs()
            appExecutors.mainThread.execute {
                if (navs.isEmpty()) {
                    // This will be called if the table is new or just empty.
                    callback.onDataNotAvailable()
                } else {
                    callback.onNavsLoaded(navs)
                }
            }
        }
    }

    /**
     * Note: [NavsDataSource.GetNavCallback.onDataNotAvailable] is fired if the [Nav] isn't
     * found.
     */
    override fun getNav(navId: String, callback: NavsDataSource.GetNavCallback) {
        appExecutors.diskIO.execute {
            val nav = navsDao.getNavById(navId)
            appExecutors.mainThread.execute {
                if (nav != null) {
                    callback.onNavLoaded(nav)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    override fun saveNav(nav: Nav) {
        appExecutors.diskIO.execute { navsDao.insertNav(nav) }
    }

    override fun completeNav(nav: Nav) {
        appExecutors.diskIO.execute { navsDao.updateCompleted(nav.id, true) }
    }

    override fun completeNav(navId: String) {
        // Not required for the local data source because the {@link NavsRepository} handles
        // converting from a {@code navId} to a {@link Nav} using its cached data.
    }

    override fun activateNav(nav: Nav) {
        appExecutors.diskIO.execute { navsDao.updateCompleted(nav.id, false) }
    }

    override fun activateNav(navId: String) {
        // Not required for the local data source because the {@link NavsRepository} handles
        // converting from a {@code navId} to a {@link Nav} using its cached data.
    }

    override fun clearCompletedNavs() {
        appExecutors.diskIO.execute { navsDao.deleteCompletedNavs() }
    }

    override fun refreshNavs() {
        // Not required because the {@link NavsRepository} handles the logic of refreshing the
        // Navs from all the available data sources.
    }

    override fun deleteAllNavs() {
        appExecutors.diskIO.execute { navsDao.deleteNavs() }
    }

    override fun deleteNav(navId: String) {
        appExecutors.diskIO.execute { navsDao.deleteNavById(navId) }
    }

    companion object {
        private var INSTANCE: NavsLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, NavsDao: NavsDao): NavsLocalDataSource {
            if (INSTANCE == null) {
                synchronized(NavsLocalDataSource::javaClass) {
                    INSTANCE = NavsLocalDataSource(appExecutors, NavsDao)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }
}
