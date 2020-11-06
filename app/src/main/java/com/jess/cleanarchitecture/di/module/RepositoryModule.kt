package com.jess.cleanarchitecture.di.module

import com.jess.cleanarchitecture.domain.repository.NaverRepository
import com.jess.cleanarchitecture.domain.repository.NaverRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindNaverRepository(repository: NaverRepositoryImpl): NaverRepository
}