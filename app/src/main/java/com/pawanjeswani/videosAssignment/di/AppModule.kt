package com.pawanjeswani.videosAssignment.di

import com.pawanjeswani.videosAssignment.network.APIService
import com.pawanjeswani.videosAssignment.network.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApi(
        remoteDataSource: RemoteDataSource
    ): APIService {
        return remoteDataSource.buildApi(APIService::class.java)
    }
}