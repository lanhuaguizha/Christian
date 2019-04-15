package com.christian.data.source

import com.christian.data.Gospels
import org.jetbrains.anko.AnkoLogger
import retrofit2.Call

/**
 * Write Gospels model, first of all you think there are ways to
 * play the Gospels model
 */
interface NavsDataSource : AnkoLogger {

    interface LoadNavsCallback {

        fun onNavsLoaded(gospels: List<Gospels>)

        fun onDataNotAvailable()
    }

    interface GetNavCallback {

        fun onNavLoaded(gospels: Gospels)

        fun onDataNotAvailable()
    }

    fun getNavs(call: Call<List<Gospels>>, callback: LoadNavsCallback)

    fun getNav(navId: String, callback: GetNavCallback)

    fun saveNav(gospels: Gospels)

    fun completeNav(gospels: Gospels)

    fun completeNav(navId: String)

    fun activateNav(gospels: Gospels)

    fun activateNav(navId: String)

    fun clearCompletedNavs()

    fun refreshNavs()

    fun deleteAllNavs()

    fun deleteNav(navId: String)
}