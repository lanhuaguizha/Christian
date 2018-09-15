package com.christian

import com.christian.data.Nav

/**
 * View of MVP architecture.
 * Responsible for handling view logic.
 */
interface BaseView<T> {
    var presenter: T
}

/**
 * Presenter of MVP architecture.
 * Responsible for handling business logic.
 */
interface BasePresenter {
    fun start()
}

/**
 * Contains presenter properties. View initializes first
 */
class BaseViewContract {

    interface View<T> : BaseView<T> {
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