package com.jess.cleanarchitecture.di.module

import com.jess.cleanarchitecture.domain.repository.NaverRepositoryImpl
import com.jess.cleanarchitecture.domain.usecase.SearchMoveUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class UseCaseModule {

    /**
     * 투자현황
     */
    @Provides
    fun provideSearchMoveUseCase(
        repository: NaverRepositoryImpl
    ): SearchMoveUseCase {
        return SearchMoveUseCase(repository)
    }

}