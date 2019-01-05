package com.christian.data

const val NAV_DETAIL_TYPE_TITLE = 0 // 就要使用专为该类型设计的viewType，比如字体蓝色
const val NAV_DETAIL_TYPE_SUB_TITLE = 1

const val NAV_DETAIL_TYPE_IMAGE = 10 // 一张图片的布局样式

const val NAV_DETAIL_TYPE_MUSIC = 20

const val NAV_DETAIL_TYPE_VIDEO = 30

const val NAV_DETAIL_TYPE_TEXT = 40 // 就要使用专为该类型设计的viewType，比如没有elevation
const val NAV_DETAIL_TYPE_TEXT_ITALIC = 41 // 用于展示圣经原文

data class NavDetailBean(val type: Int, val text: String)