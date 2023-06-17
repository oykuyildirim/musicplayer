package com.kotlinegitim.vize3_kotlin

data class Music (
    val musicCategories: List<MusicCategory>
)

data class MusicCategory (
    val baseTitle: String? = "",
    val items: List<Item>?= arrayListOf()
)

data class Item (
    val baseCat: Long?=0,
    val title: String?="",
    val url: String?=""
)


