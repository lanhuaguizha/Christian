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
        @ColumnInfo(name = "subtitle") var subtitle: String = "约翰福音",
        @ColumnInfo(name = "title") var title: String = "叫一切信他的，不至灭亡，反得永生。",
        @ColumnInfo(name = "detail") var detail: String = "神爱世人，甚至将他的独生子赐给他们，叫一切信他的，不至灭亡，反得永生",
        @ColumnInfo(name = "relation") var relation: String = "",
        @ColumnInfo(name = "author") var author: String = "神",
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