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
         * Set the Nav page View initialization parameters, including
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
        fun initView()

        /**
         *
         */
        fun setupToolbar(title: String)

        fun setupSearchbar(searchHint: String)

        fun startSwipeRefreshLayout()

        fun showProgressBar()

        fun hideProgressBar()

        /**
         * passing a list to a recycler view and let it show
         */
        fun showRecyclerView(navs: List<Nav>)

        fun hideRecyclerView()

        fun showFloatingActionButton()

        fun hideFloatingActionButton()

        fun activeFloatingActionButton()

        fun scrollToPosition()

    }

    /**
     * Write Nav view and presenter separately, first of all you think there are ways to
     * play the Nav business logic
     */
    interface Presenter : BasePresenter {

        /**
         *(1)insert
         *
         *(2)delete
         *
         *(3)update
         *
         *(4)select
         */

        fun insertData(itemId: Int)

        fun updateData(navs: List<Nav>)

    }

}