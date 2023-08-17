package com.seumulseumul.cld.di

import com.seumulseumul.data.repository.remote.ClDRemoteDataSource
import com.seumulseumul.data.repository.remote.ClDRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideClDRemoteDataSource(clDRemoteDataSource: ClDRemoteDataSourceImpl): ClDRemoteDataSource = clDRemoteDataSource

}