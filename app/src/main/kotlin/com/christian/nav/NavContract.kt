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
        fun showPb()

        /**
         * Stop the loading animation after inserting the data.
         */
        fun hidePb()

        /**
         * Hide before each display to meet the material design specification.
         */
        fun showFab(drawableId: Int)

        fun hideFab()
    }

    interface INavFragment : BaseViewContract.IView<NavContract.IPresenter> {

        fun initSrl()

        fun initFs()

        fun initRv(navs: List<Nav>)

        fun showSrl()

        fun hideSrl()

        /**
         * You have to show the view in init view and invalidate view in the callback.
         */
        fun invalidateRv(navs: List<Nav>)
    }

    /**
     * Write Nav view and presenter separately, first of all you think there are ways to
     * play the Nav business logic
     */
    interface IPresenter : BaseViewContract.IPresenter {

        /**
         * Summary of business logic is db's CRUD
         *
         * Create()
         *
         * Delete()
         *
         * Update()
         *
         * Read()
         */

        fun createNav(navId: String = "0", isSrl: Boolean = false, navFragment: NavFragment): Boolean

        /**
         * The next view has been pressed
         */
        fun deleteNav(navId: String)

        fun updateNav(navs: List<Nav>)

        fun readNav()
    }
}
