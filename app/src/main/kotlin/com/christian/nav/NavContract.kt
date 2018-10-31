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
    interface INavActivity : BaseViewContract.IView<NavContract.IPresenter> {

        fun initSb(searchHint: String)

        /**
         * Start the loading animation before inserting the data.
         */
        fun startPb()

        /**
         * Stop the loading animation after inserting the data.
         */
        fun stopPb()

        /**
         * Hide before each display to meet the material design specification.
         */
        fun showFab(drawableId: Int)

        fun hideFloatingActionButton()

        fun activeFloatingActionButton()
    }

    interface INavFragment : BaseViewContract.IView<NavContract.IPresenter> {

        fun initSrl()

        fun initFs()

        fun initRv(navs: List<Nav>)

        fun startSrl()

        fun stopSrl()

        /**
         * You have to show the view in init view and invalidate view in the callback.
         */
        fun invalidateRv(navs: List<Nav>)

        fun restoreRvPos()
    }

    /**
     * Write Nav view and presenter separately, first of all you think there are ways to
     * play the Nav business logic
     */
    interface IPresenter : BaseViewContract.IPresenter {

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

        fun insertNav(navId: String = "0", isSrl: Boolean = false, navFragment: NavFragment): Boolean

        fun updateNav(navs: List<Nav>)

        fun queryNav()
        /**
         * The next view has been pressed
         */
        fun deleteNav(navId: String)

        fun generateNavId(itemId: Int) : String
    }
}
