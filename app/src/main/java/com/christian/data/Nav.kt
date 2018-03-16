package com.christian.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Nav Activity model class.
 * I think one data class is enough.
 */
@Entity(tableName = "navs")
data class Nav @JvmOverloads constructor(
        @ColumnInfo(name = "subtitle") var subtitle: String = "John",
        @ColumnInfo(name = "title") var title: String = "3:16",
        @ColumnInfo(name = "detail") var detail: String = "For God so loved the world, that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life.",
        @ColumnInfo(name = "relation") var relation: String = "",
        @ColumnInfo(name = "author") var author: String = "Holy Spirit",
        @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
) {

    /**
     * True if the task is completed, false if it's active.
     */
    @ColumnInfo(name = "completed")
    var isCompleted = false

    val titleForList: String
        get() = if (title.isNotEmpty()) title else relation

    val isActive
        get() = !isCompleted

    val isEmpty
        get() = title.isEmpty() && relation.isEmpty()
}