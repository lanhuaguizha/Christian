package com.christian.nav

import android.util.Log
import com.christian.R
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

    // dTAG represents debugging tag
    private val dTAG = "NavPresenter"

    init {
        navView.presenter = this
    }

    override fun start() {

        navView.initView()
    }

    override fun getData(itemId: Int) {
        when (itemId) {
            R.id.navigation_home -> {
                Log.i(dTAG, "request Home data")
            }
            R.id.navigation_gospel -> {
                Log.i(dTAG, "request Gospel data")
            }
            R.id.navigation_me -> {
                Log.i(dTAG, "request Me data")
            }
        }
        Log.i(dTAG, itemId.toString())
    }

    override fun loadData(nav: Nav) {
//        val dataSet: Array<String> = arrayOf("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
//        for (i in 0 until 30) {
//            dataSet[i] = navView.getString(R.string.next_week) + i
//        }
//
//        loadView(dataSet)
        navView.showRecyclerView(nav)
    }
}