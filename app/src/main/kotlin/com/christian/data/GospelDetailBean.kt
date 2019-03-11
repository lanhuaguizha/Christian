package com.christian.data

data class GospelDetailBean(var title: String, var listDetail: List<Detail>)
data class Detail(var subtitle: String, var image: String, var content: String)