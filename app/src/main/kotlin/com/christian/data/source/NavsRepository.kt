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

import com.christian.data.Gospels
import retrofit2.Call
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
        private val navsRemoteDataSource: NavsDataSource

) : NavsDataSource {

    /**
     * This variable has public visibility so it can be accessed from tests.
     */
    var cachedNavs: LinkedHashMap<String, Gospels> = LinkedHashMap()

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
    override fun getNavs(call: Call<List<Gospels>>, callback: NavsDataSource.LoadNavsCallback) {
        // Respond immediately with cache if available and not dirty
//        if (cachedNavs.isNotEmpty() && !cacheIsDirty) {
//            callback.onNavsLoaded(ArrayList(cachedNavs.values))
//            return
//        }

        getNavsFromRemoteDataSource(call, callback)
//        if (cacheIsDirty) {
//            // If the cache is dirty we need to fetch new data from the network.
//            getNavsFromRemoteDataSource(callback)
//        } else {
//            // Query the local storage if available. If not, query the network.
//            navsLocalDataSource.getBean(object : NavsDataSource.LoadNavsCallback {
//                override fun onNavsLoaded(bean: List<Gospels>) {
//                    refreshCache(bean)
//                    callback.onNavsLoaded(ArrayList(cachedNavs.values))
//                }
//
//                override fun onDataNotAvailable() {
//                    getNavsFromRemoteDataSource(callback)
//                }
//            })
//        }
    }

    override fun saveNav(gospels: Gospels) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(gospels) {
            navsRemoteDataSource.saveNav(it)
//            navsLocalDataSource.saveNav(it)
        }
    }

    override fun completeNav(gospels: Gospels) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(gospels) {
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

    override fun activateNav(gospels: Gospels) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(gospels) {
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
        } as LinkedHashMap<String, Gospels>
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

        // Is the Gospels in the local data source? If not, query the network.
//        navsLocalDataSource.getNav(navId, object : NavsDataSource.GetNavCallback {
//            override fun onNavLoaded(nav: Gospels) {
//                // Do in memory cache update to keep the app UI up to date
//                cacheAndPerform(nav) {
//                    callback.onNavLoaded(it)
//                }
//            }
//
//            override fun onDataNotAvailable() {
//                navsRemoteDataSource.getNav(navId, object : NavsDataSource.GetNavCallback {
//                    override fun onNavLoaded(nav: Gospels) {
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

    private fun getNavsFromRemoteDataSource(call: Call<List<Gospels>>, callback: NavsDataSource.LoadNavsCallback) {
        navsRemoteDataSource.getNavs(call, object : NavsDataSource.LoadNavsCallback {
            override fun onNavsLoaded(gospels: List<Gospels>) {
                refreshCache(gospels)
                refreshLocalDataSource(gospels)
//                callback.onNavsLoaded(ArrayList(cachedNavs.values))
                callback.onNavsLoaded(gospels)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    private fun refreshCache(gospels: List<Gospels>) {
        cachedNavs.clear()
        gospels.forEach {
            cacheAndPerform(it) {}
        }
        cacheIsDirty = false
    }

    private fun refreshLocalDataSource(gospels: List<Gospels>) {
//        navsLocalDataSource.deleteAllNavs()
//        for (Gospels in gospels) {
//            navsLocalDataSource.saveNav(Gospels)
//        }
        System.out.print(gospels)
    }

    private fun getNavWithId(id: String) = cachedNavs[id]

    private inline fun cacheAndPerform(gospels: Gospels, perform: (Gospels) -> Unit) {
        val cachedNav = Gospels(gospels.title, gospels.relation, id = gospels.id).apply {
            isCompleted = gospels.isCompleted
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