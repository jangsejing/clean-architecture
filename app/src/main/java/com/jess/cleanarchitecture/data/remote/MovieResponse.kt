package com.jess.cleanarchitecture.data.remote

import com.jess.cleanarchitecture.data.entity.ItemEntity
import com.jess.cleanarchitecture.data.entity.MovieEntity


data class MovieResponse(
    val lastBuildDate: String?,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<ItemResponse>?
) {
    fun toEntity(): MovieEntity {
        return MovieEntity(
            lastBuildDate = lastBuildDate,
            total = total,
            start = start,
            display = display,
            items = items?.map { it.toEntity() }
        )
    }
}

data class ItemResponse(
    val title: String?,
    val link: String?,
    val image: String?,
    val subtitle: String?,
    val pubDate: String?,
    val director: String?,
    val actor: String?,
    val userRating: String?
) {
    fun toEntity(): ItemEntity {
        return ItemEntity(
            title,
            link,
            image,
            subtitle,
            pubDate,
            director,
            actor,
            userRating
        )
    }

}