package com.seumulseumul.cld.di

import com.seumulseumul.data.repository.ClDRepositoryImpl
import com.seumulseumul.domain.repository.ClDRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideClDRepository(repository: ClDRepositoryImpl): ClDRepository = repository
}