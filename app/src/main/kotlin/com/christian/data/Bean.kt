package com.christian.data

sealed class Bean

data class GospelBean(val gospels: List<Gospels>
) : Bean()

data class Gospels @JvmOverloads constructor(
        var title: String = "",
        var subtitle: String = "",
        var gospelDetails: List<GospelDetails> = arrayListOf(),
        var date: String = "",
        var author: String = ""
)

data class GospelDetailBean(val gospelDetails: List<GospelDetails>)

data class GospelDetails(val content: String, val image: String)

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