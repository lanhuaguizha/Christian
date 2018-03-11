package com.christian.nav

import android.util.Log
import com.christian.data.Nav
import com.christian.data.source.NavsDataSource
import com.christian.data.source.NavsRepository

/**
 * This contains all the NAV business logic, and the MVP control center. We'll write the code here first.
 * Hold references to View and Model, implementation of View is NavActivity, implementation of Model
 * is Nav
 */
class NavPresenter(
        private val navId: String,
        private val navsRepository: NavsRepository,
        private val navView: NavContract.View) : NavContract.Presenter {

    init {
        navView.presenter = this
    }

    override fun start() {

        navView.initView()

    }

    override fun insertNav(itemId: Int) {

        navsRepository.getNavs(object : NavsDataSource.LoadNavsCallback {

            override fun onNavsLoaded(navs: List<Nav>) {
                Log.i("home clicked", navs.toString())
                navView.showRecyclerView(navs)
            }

            override fun onDataNotAvailable() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

    }

    override fun updateNav(navs: List<Nav>) {

        navView.showRecyclerView(navs)

    }

    override fun queryNav() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}