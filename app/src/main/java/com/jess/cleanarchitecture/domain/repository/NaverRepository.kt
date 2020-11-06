package com.jess.cleanarchitecture.domain.repository

import com.jess.cleanarchitecture.data.entity.MovieEntity
import javax.inject.Inject

interface NaverRepository {
    suspend fun getSearchMovie(query: String): MovieEntity
}

class NaverRepositoryImpl @Inject constructor(
    private val remote: NaverService
) : NaverRepository {

    override suspend fun getSearchMovie(query: String): MovieEntity {
        return remote.getMovies(query).toEntity()
    }
}