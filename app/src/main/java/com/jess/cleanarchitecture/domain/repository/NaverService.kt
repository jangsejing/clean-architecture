package com.jess.cleanarchitecture.domain.repository

import com.jess.cleanarchitecture.data.remote.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author jess
 * @since 2020.06.12
 */
interface NaverService {

    /**
     * 영화 조회
     */
    @GET("/v1/search/movie.json")
    suspend fun getMovies(
        @Query("query") query: String?,
        @Query("start") start: Int = 1,
        @Query("display") display: Int = 1000
    ): MovieResponse

}