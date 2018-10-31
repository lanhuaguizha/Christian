package com.christian.navdetail

import com.christian.BaseViewContract
import com.christian.nav.NavContract

/**
 * Including View logic, Business logic and write respectively,
 * In particular, don't mistake the View for a strong correlation with a Presenter, even though they are written in a file,
 * Just for the convenience of viewing.
 */
class NavDetailContract {

    /**
     * All view logic about NavDetail
     */
    interface View : BaseViewContract.IView<NavContract.IPresenter> {

        /**
         * Show comment view
         */
        fun showCv()

        /**
         * Hide comment view
         */
        fun hideCv()
    }
}