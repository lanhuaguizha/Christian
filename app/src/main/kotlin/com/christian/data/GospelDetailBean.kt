package com.christian.data

data class GospelDetailBean(val title: String, val detailList: ArrayList<Detail>)
data class Detail(val subtitle: String, val image: String, val content: String)