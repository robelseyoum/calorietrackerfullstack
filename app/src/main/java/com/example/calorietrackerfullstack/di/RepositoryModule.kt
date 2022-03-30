package com.example.calorietrackerfullstack.di

import com.example.calorietrackerfullstack.concurrency.AppDispatchers
import com.example.calorietrackerfullstack.concurrency.IAppDispatchers
import com.example.calorietrackerfullstack.data.api.ApiClient
import com.example.calorietrackerfullstack.data.repository.auth.AuthRepository
import com.example.calorietrackerfullstack.data.repository.auth.IAuthRepository
import com.example.calorietrackerfullstack.data.repository.foods.FoodRepository
import com.example.calorietrackerfullstack.data.repository.foods.IFoodRepository
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
    fun provideAppDispatchers(): IAppDispatchers {
        return AppDispatchers()
    }

    @Singleton
    @Provides
    fun provideAuthRepository(
        apiClient: ApiClient,
        appDispatchers: AppDispatchers
    ): IAuthRepository = AuthRepository(apiClient, appDispatchers)


    @Singleton
    @Provides
    fun provideFoodRepository(
        apiClient: ApiClient,
        appDispatchers: AppDispatchers
    ): IFoodRepository = FoodRepository(apiClient, appDispatchers)

}