package com.example.tableorderingapp.di

import android.content.Context
import com.example.tableorderingapp.domain.network.NetworkBuilder
import com.example.tableorderingapp.domain.network.NetworkService
import com.example.tableorderingapp.domain.repository.NetworkRepositoryImpl
import com.example.tableorderingapp.util.GlobalVariable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideNetworkService(@ApplicationContext context: Context): NetworkService {
        return NetworkBuilder.create(
            GlobalVariable.API_BASE_URL,
            context.cacheDir,
            (10 * 1024 * 1024).toLong()
        )
    }

    @Provides
    @Singleton
    fun provideTaskRepository(
        networkService: NetworkService,
    ): NetworkRepositoryImpl {
        return NetworkRepositoryImpl(
            networkService
        )
    }
}