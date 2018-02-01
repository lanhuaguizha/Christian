package com.christian

/**
 * View of MVP architecture.
 * Responsible for handling view logic.
 */
interface BaseView<T> {

    var presenter: T

}