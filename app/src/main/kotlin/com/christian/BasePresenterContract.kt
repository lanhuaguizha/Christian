package com.christian

import org.jetbrains.anko.AnkoLogger

/**
 * Contains view properties. Presenter initializes first
 */
interface BasePresenterContract {

    /**
     * View of MVP architecture.
     * Responsible for handling view logic.
     */
    interface View : AnkoLogger {
    }

    /**
     * Presenter of MVP architecture.
     * Responsible for handling business logic.
     */
    interface Presenter<T> : AnkoLogger {
    }
}