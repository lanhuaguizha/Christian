package com.christian.data
import java.util.*

/**
 * Nav Activity model class.
 * I think one data class is enough.
 */
data class NavBean @JvmOverloads constructor(
        var subtitle: String = "",
        var title: String = "",
        var detail: String = "",
        var relation: String = "",
        var author: String = "",
        var id: String = UUID.randomUUID().toString()
) {

    /**
     * True if the task is completed, false if it's active.
     */
    var isCompleted = false

    val titleForList: String
        get() = if (title.isNotEmpty()) title else relation

    val isActive
        get() = !isCompleted

    val isEmpty
        get() = title.isEmpty() && relation.isEmpty()
}