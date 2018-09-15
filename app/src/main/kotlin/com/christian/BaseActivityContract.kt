package com.christian

import com.christian.data.Nav
import com.christian.nav.NavContract

class BaseActivityContract {

    interface View : BaseView<NavContract.Presenter> {
        fun createPresenter()

        /**
         * Summary of init view,
         *
         * initCl()
         *
         * initAbl()
         *
         * initTb()
         *
         * initSb()
         *
         * initSrl()
         *
         * initRv()
         *
         * initPb()
         *
         * initFab()
         *
         * initBnv()
         */
        fun initView(navs: List<Nav>)
    }

    interface Presenter : BasePresenter
}