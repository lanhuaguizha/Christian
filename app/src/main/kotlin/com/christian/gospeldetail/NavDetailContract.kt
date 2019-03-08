package com.christian.gospeldetail

import com.christian.BaseContract

/**
 * Including View logic, Business logic and write respectively,
 * In particular, don't mistake the View for a strong correlation with a Presenter, even though they are written in a file,
 * Just for the convenience of viewing.
 */
class NavDetailContract {

    /**
     * All view logic about NavDetail
     */
    interface IView : BaseContract.IView<IPresenter> {

    }

    interface IPresenter: BaseContract.IPresenter<IView>
}