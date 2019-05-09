package com.christian.data

data class Gospel(
        var title: String = "",
        var subtitle: String = "",
        var detail: List<GospelDetails> = arrayListOf(),
        var date: String = "",
        var author: String = ""
)

data class GospelDetails(
        val content: String = "",
        val image: String = ""
)

data class Disciple(
        val id: String = "",
        val name: String = ""
)

data class Message(
        val id: String,
        var text: String = "",
        var name: String = "",
        var photoUrl: String = "",
        var imageUrl: String = "")

data class MeBean(
        val id: String = "",
        val name: String = "",
        val desc: String = "",
        var detail: List<MeDetails> = arrayListOf()
)

data class MeDetails(
        val category: String = "",
        val type: String = "",
        val card: String = "",
        val desc: String = "",
        val name: String = "",
        val url: String = ""
)

data class Setting(
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