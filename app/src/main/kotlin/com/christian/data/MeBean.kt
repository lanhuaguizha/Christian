package com.christian.data

data class MeBean (
    var id: Int = 0,
    var url: String = "",
    var name: String = "",
    var nickName: String = "",
    var address: Address = Address(),
    var settings: List<Settings> = arrayListOf()
)

data class Settings(
        var name: String = "",
        var url: String = "",
        var desc: String = ""
)

// 头像点击开后的详情
data class Address(
        var street: String = "",
        var city: String = "",
        var country: String = ""
)