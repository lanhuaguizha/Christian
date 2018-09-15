package com.christian.nav

import com.christian.BaseViewContract
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
    interface View : BaseViewContract.View<NavContract.Presenter> {

        fun setupSb(searchHint: String)

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

        /**
         * Hide before each display to meet the material design specification.
         */
        fun showFab(drawableId: Int)

        fun hideFloatingActionButton()

        fun activeFloatingActionButton()
    }

    /**
     * Write Nav view and presenter separately, first of all you think there are ways to
     * play the Nav business logic
     */
    interface Presenter : BaseViewContract.Presenter {
        /**
         * Summary of business logic,
         *
         * insert()
         *
         * delete()
         *
         * update()
         *
         * select()
         */

        fun insertNav(itemId: Int, isSrl: Boolean = false)

        fun updateNav(navs: List<Nav>)

        fun queryNav()
    }
}
