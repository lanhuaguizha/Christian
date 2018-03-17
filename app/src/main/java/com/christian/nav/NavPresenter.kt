package com.christian.nav

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

        navView.initView(navs = listOf(Nav()))

    }

    override fun insertNav(itemId: Int) {

        navView.startPb()

        navsRepository.getNavs(object : NavsDataSource.LoadNavsCallback {

            override fun onNavsLoaded(navs: List<Nav>) {

                navView.invalidateRv(navs)

                navView.stopPb()

            }

            override fun onDataNotAvailable() {

                navView.stopPb()

            }

        })

    }

    override fun updateNav(navs: List<Nav>) {
    }

    override fun queryNav() {
    }

}