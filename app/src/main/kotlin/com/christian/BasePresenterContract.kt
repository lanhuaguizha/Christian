package com.christian

/**
 * Contains view properties. Presenter initializes first
 */
interface BasePresenterContract {
    interface View {
        fun start()
    }

    interface Presenter<T> {
        var view: T
    }
}