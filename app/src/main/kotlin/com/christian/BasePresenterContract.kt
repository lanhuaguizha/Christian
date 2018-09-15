package com.christian

/**
 * View of MVP architecture.
 * Responsible for handling view logic.
 */
interface BaseView<T> {
    var presenter: T
}

/**
 * Presenter of MVP architecture.
 * Responsible for handling business logic.
 */
interface BasePresenter {
    fun start()
}

/**
 * Contains view properties. Presenter initializes first
 */
interface BasePresenterContract {

    interface View {
//        fun start()
    }

    interface Presenter<T> {
//        var navItemView: T
    }
}