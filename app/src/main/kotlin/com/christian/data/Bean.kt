package com.christian.data

import java.util.*

sealed class Bean

data class GospelBean(val gospels: List<Gospels>
) : Bean()

data class Gospels @JvmOverloads constructor(
        val title: String = "",
        val subtitle: String = "",
        val detail: List<GospelDetailBean> = arrayListOf(),
        val relation: String = "",
        val author: String = "",
        val id: String = UUID.randomUUID().toString()
) {

    /**
     * True if the task is completed, false if it's active.
     */
    var isCompleted = false
}

data class MeBean(
        val id: Int = 0,
        val url: String = "",
        val name: String = "",
        val nickName: String = "",
        val address: Address = Address(),
        val settings: List<Settings> = arrayListOf()
) : Bean()

data class Settings(
        val name: String = "",
        val url: String = "",
        val desc: String = ""
)

// 头像点击开后的详情
data class Address(
        val street: String = "",
        val city: String = "",
        val country: String = ""
)

data class GospelDetailBean(val content: String, val image: String)