package com.christian.data.source

import com.christian.data.NavBean
import org.jetbrains.anko.AnkoLogger
import retrofit2.Call

/**
 * Write NavBean model, first of all you think there are ways to
 * play the NavBean model
 */
interface NavsDataSource : AnkoLogger {

    interface LoadNavsCallback {

        fun onNavsLoaded(navBeans: List<NavBean>)

        fun onDataNotAvailable()
    }

    interface GetNavCallback {

        fun onNavLoaded(navBean: NavBean)

        fun onDataNotAvailable()
    }

    fun getNavs(call: Call<List<NavBean>>, callback: LoadNavsCallback)

    fun getNav(navId: String, callback: GetNavCallback)

    fun saveNav(navBean: NavBean)

    fun completeNav(navBean: NavBean)

    fun completeNav(navId: String)

    fun activateNav(navBean: NavBean)

    fun activateNav(navId: String)

    fun clearCompletedNavs()

    fun refreshNavs()

    fun deleteAllNavs()

    fun deleteNav(navId: String)
}