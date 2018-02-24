package com.christian.nav

import android.util.Log
import com.christian.R
import com.christian.data.Nav
import com.christian.data.source.NavsDataSource
import com.christian.data.source.NavsRepository

/**
 * This contains all the NAV business logic, and the MVP control center. We'll write the code here first.
 * Hold references to View and Model, implementation of View is ActNav, implementation of Model
 * is Nav
 */
class NavPresenter(
        private val navId: String,
        private val navsRepository: NavsRepository,
        private val navView: NavContract.View) : NavContract.Presenter {

    // dTAG represents debugging tag
    private val TAG = "NavPresenter"

    init {
        navView.presenter = this
    }

    override fun start() {

        navView.initView()
    }

    override fun getData(itemId: Int) {
        when (itemId) {
            R.id.navigation_home -> {
                Log.i(TAG, "request Home data")
                navsRepository.getNavs(object : NavsDataSource.LoadNavsCallback {
                    override fun onNavsLoaded(navs: List<Nav>) {
                        Log.i(TAG, navs.toString())
                    }

                    override fun onDataNotAvailable() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                })
            }
            R.id.navigation_gospel -> {
                Log.i(TAG, "request Gospel data")
            }
            R.id.navigation_me -> {
                Log.i(TAG, "request Me data")
            }
        }
        Log.i(TAG, itemId.toString())
    }

    override fun loadData(nav: Nav) {
        navView.showRecyclerView(nav)
    }
}