package com.christian.nav

import com.christian.BasePresenter
import com.christian.BaseView
import com.christian.data.Nav

/**
 * Contract of Nav View & Presenter.
 * You can see all the interfaces of the Nav here,
 * also defining everything.
 */
class NavContract {

    /**
     * Write Nav view and presenter separately, first of all you think there are ways to
     * play the Nav view
     */
    interface View : BaseView<Presenter> {

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

        fun setupToolbar(title: String)

        fun setupSearchbar(searchHint: String)

        fun startSwipeRefreshLayout()

        /**
         * Stop the scale down animation after inserting the data.
         */
        fun stopSrl()

        /**
         * Start the loading animation before inserting the data.
         */
        fun startPb()

        /**
         * Stop the loading animation after inserting the data.
         */
        fun stopPb()

        /**
         * You have to show the view in init view and invalidate view in the callback.
         */
        fun invalidateRv(navs: List<Nav>)

        fun restoreRvPos()

        fun showFloatingActionButton()

        fun hideFloatingActionButton()

        fun activeFloatingActionButton()

    }

    /**
     * Write Nav view and presenter separately, first of all you think there are ways to
     * play the Nav business logic
     */
    interface Presenter : BasePresenter {

        /**
         * Summary of business logic,
         *
         * insert()
         *
         * update()
         *
         * delete()
         *
         * query()
         */

        fun insertNav(itemId: Int)

        fun insertNav(itemId: Int, isSrl: Boolean)

        fun updateNav(navs: List<Nav>)

        fun queryNav()

    }

}