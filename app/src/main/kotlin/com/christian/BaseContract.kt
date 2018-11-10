package com.christian

import org.jetbrains.anko.AnkoLogger

/**
 * The contract top-level abstraction
 */
class BaseContract {

    /**
     * View of MVP architecture.
     * Responsible for handling view logic.
     */
    interface IView<Presenter> : AnkoLogger {
        var presenter: Presenter
    }

    /**
     * Presenter of MVP architecture.
     * Responsible for handling business logic.
     */
    interface IPresenter<View> : AnkoLogger {
        var view: View
    }
}