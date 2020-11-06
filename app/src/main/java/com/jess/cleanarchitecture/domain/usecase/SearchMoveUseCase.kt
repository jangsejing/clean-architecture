package com.jess.cleanarchitecture.domain.usecase

import com.jess.cleanarchitecture.data.entity.MovieEntity
import com.jess.cleanarchitecture.domain.repository.NaverRepository
import javax.inject.Inject

class SearchMoveUseCase(private val repository: NaverRepository) {

    suspend operator fun invoke(query: String): MovieEntity {
        return repository.getSearchMovie(query)
    }

}