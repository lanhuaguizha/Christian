package com.christian

import com.christian.data.Nav

/**
 * Contains presenter properties. View initializes first
 */
class BaseViewContract {

    /**
     * View of MVP architecture.
     * Responsible for handling view logic.
     */
    interface View<T> {

        var presenter: T

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

        fun deinitView()
    }

    /**
     * Presenter of MVP architecture.
     * Responsible for handling business logic.
     */
    interface Presenter {

        fun start()

        fun end()
    }
}