package com.christian.navdetail

import com.christian.BasePresenter
import com.christian.BaseView

/**
 * Including View logic, Business logic and write respectively,
 * In particular, don't mistake the View for a strong correlation with a Presenter, even though they are written in a file,
 * Just for the convenience of viewing.
 */
class NavDetailContract {

    /**
     * All view logic about NavDetail
     */
    interface View : BaseView<Presenter> {

    }

    /**
     * All business logic about NavDetail
     */
    interface Presenter : BasePresenter {

    }
}