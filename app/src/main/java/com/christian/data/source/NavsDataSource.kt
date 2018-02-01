package com.christian.data.source

import com.christian.data.Nav

/**
 * Write Nav model, first of all you think there are ways to
 * play the Nav model
 */
interface NavsDataSource {

    interface LoadNavsCallback {

        fun onNavsLoaded(navs: List<Nav>)

        fun onDataNotAvailable()
    }

    interface GetNavCallback {

        fun onNavLoaded(nav: Nav)

        fun onDataNotAvailable()
    }

    fun getNavs(callback: LoadNavsCallback)

    fun getNav(navId: String, callback: GetNavCallback)

    fun saveNav(nav: Nav)

    fun completeNav(nav: Nav)

    fun completeNav(navId: String)

    fun activateNav(nav: Nav)

    fun activateNav(navId: String)

    fun clearCompletedNavs()

    fun refreshNavs()

    fun deleteAllNavs()

    fun deleteNav(navId: String)
}