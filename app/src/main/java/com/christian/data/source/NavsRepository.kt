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
package com.christian.data.source

import com.christian.data.Nav
import java.util.*

/**
 * Concrete implementation to load Navs from the data sources into a cache.
 *
 *
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */
class NavsRepository(
        val navsRemoteDataSource: NavsDataSource

) : NavsDataSource {

    /**
     * This variable has public visibility so it can be accessed from tests.
     */
    var cachedNavs: LinkedHashMap<String, Nav> = LinkedHashMap()

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    var cacheIsDirty = false

    /**
     * Gets Navs from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     *
     *
     * Note: [NavsDataSource.LoadNavsCallback.onDataNotAvailable] is fired if all data sources fail to
     * get the data.
     */
    override fun getNavs(callback: NavsDataSource.LoadNavsCallback) {
        // Respond immediately with cache if available and not dirty
        if (cachedNavs.isNotEmpty() && !cacheIsDirty) {
            callback.onNavsLoaded(ArrayList(cachedNavs.values))
            return
        }

        getNavsFromRemoteDataSource(callback)
//        if (cacheIsDirty) {
//            // If the cache is dirty we need to fetch new data from the network.
//            getNavsFromRemoteDataSource(callback)
//        } else {
//            // Query the local storage if available. If not, query the network.
//            navsLocalDataSource.getNavs(object : NavsDataSource.LoadNavsCallback {
//                override fun onNavsLoaded(navs: List<Nav>) {
//                    refreshCache(navs)
//                    callback.onNavsLoaded(ArrayList(cachedNavs.values))
//                }
//
//                override fun onDataNotAvailable() {
//                    getNavsFromRemoteDataSource(callback)
//                }
//            })
//        }
    }

    override fun saveNav(nav: Nav) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(nav) {
            navsRemoteDataSource.saveNav(it)
//            navsLocalDataSource.saveNav(it)
        }
    }

    override fun completeNav(nav: Nav) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(nav) {
            it.isCompleted = true
            navsRemoteDataSource.completeNav(it)
//            navsLocalDataSource.completeNav(it)
        }
    }

    override fun completeNav(navId: String) {
        getNavWithId(navId)?.let {
            completeNav(it)
        }
    }

    override fun activateNav(nav: Nav) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(nav) {
            it.isCompleted = false
            navsRemoteDataSource.activateNav(it)
//            navsLocalDataSource.activateNav(it)
        }
    }

    override fun activateNav(navId: String) {
        getNavWithId(navId)?.let {
            activateNav(it)
        }
    }

    override fun clearCompletedNavs() {
        navsRemoteDataSource.clearCompletedNavs()
//        navsLocalDataSource.clearCompletedNavs()

        cachedNavs = cachedNavs.filterValues {
            !it.isCompleted
        } as LinkedHashMap<String, Nav>
    }

    /**
     * Gets Navs from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     *
     *
     * Note: [NavsDataSource.GetNavCallback.onDataNotAvailable] is fired if both data sources fail to
     * get the data.
     */
    override fun getNav(navId: String, callback: NavsDataSource.GetNavCallback) {
        val navInCache = getNavWithId(navId)

        // Respond immediately with cache if available
        if (navInCache != null) {
            callback.onNavLoaded(navInCache)
            return
        }

        // Load from server/persisted if needed.

        // Is the Nav in the local data source? If not, query the network.
//        navsLocalDataSource.getNav(navId, object : NavsDataSource.GetNavCallback {
//            override fun onNavLoaded(nav: Nav) {
//                // Do in memory cache update to keep the app UI up to date
//                cacheAndPerform(nav) {
//                    callback.onNavLoaded(it)
//                }
//            }
//
//            override fun onDataNotAvailable() {
//                navsRemoteDataSource.getNav(navId, object : NavsDataSource.GetNavCallback {
//                    override fun onNavLoaded(nav: Nav) {
//                        // Do in memory cache update to keep the app UI up to date
//                        cacheAndPerform(nav) {
//                            callback.onNavLoaded(it)
//                        }
//                    }
//
//                    override fun onDataNotAvailable() {
//                        callback.onDataNotAvailable()
//                    }
//                })
//            }
//        })
    }

    override fun refreshNavs() {
        cacheIsDirty = true
    }

    override fun deleteAllNavs() {
        navsRemoteDataSource.deleteAllNavs()
//        navsLocalDataSource.deleteAllNavs()
        cachedNavs.clear()
    }

    override fun deleteNav(navId: String) {
        navsRemoteDataSource.deleteNav(navId)
//        navsLocalDataSource.deleteNav(navId)
        cachedNavs.remove(navId)
    }

    private fun getNavsFromRemoteDataSource(callback: NavsDataSource.LoadNavsCallback) {
        navsRemoteDataSource.getNavs(object : NavsDataSource.LoadNavsCallback {
            override fun onNavsLoaded(navs: List<Nav>) {
                refreshCache(navs)
                refreshLocalDataSource(navs)
                callback.onNavsLoaded(ArrayList(cachedNavs.values))
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    private fun refreshCache(navs: List<Nav>) {
        cachedNavs.clear()
        navs.forEach {
            cacheAndPerform(it) {}
        }
        cacheIsDirty = false
    }

    private fun refreshLocalDataSource(navs: List<Nav>) {
//        navsLocalDataSource.deleteAllNavs()
//        for (Nav in navs) {
//            navsLocalDataSource.saveNav(Nav)
//        }
    }

    private fun getNavWithId(id: String) = cachedNavs[id]

    private inline fun cacheAndPerform(nav: Nav, perform: (Nav) -> Unit) {
        val cachedNav = Nav(nav.title, nav.description, nav.id).apply {
            isCompleted = nav.isCompleted
        }
        cachedNavs.put(cachedNav.id, cachedNav)
        perform(cachedNav)
    }

    companion object {

        private var INSTANCE: NavsRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.

         * @param navsRemoteDataSource the backend data source
         * *
         * @param NavsLocalDataSource  the device storage data source
         * *
         * @return the [NavsRepository] instance
         */
        @JvmStatic
        fun getInstance(navsRemoteDataSource: NavsDataSource
        ): NavsRepository {
            return INSTANCE ?: NavsRepository(navsRemoteDataSource)
                    .apply { INSTANCE = this }
        }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}