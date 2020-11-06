package com.jess.cleanarchitecture.data.entity


data class MovieEntity(
    val lastBuildDate: String?,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<ItemEntity>?
)

data class ItemEntity(
    val title: String?,
    val link: String?,
    val image: String?,
    val subtitle: String?,
    val pubDate: String?,
    val director: String?,
    val actor: String?,
    val userRating: String?
)