package com.example.calorietrackerfullstack.data.repository.auth

import com.example.calorietrackerfullstack.concurrency.AppDispatchers
import com.example.calorietrackerfullstack.concurrency.IAppDispatchers
import com.example.calorietrackerfullstack.data.api.ApiClient
import com.example.calorietrackerfullstack.data.model.AuthResponse
import com.example.calorietrackerfullstack.data.model.UserAuth
import com.example.calorietrackerfullstack.utils.DataResult
import com.example.calorietrackerfullstack.utils.safeDataResult

class AuthRepository(
    private val apiClient: ApiClient,
    private val appDispatchers: IAppDispatchers
) : IAuthRepository {

    override suspend fun login(userAuth: UserAuth): DataResult<AuthResponse?> =
        safeDataResult(appDispatchers.io) { apiClient.login(userAuth) }

    override suspend fun register(userAuth: UserAuth): DataResult<AuthResponse?> =
        safeDataResult(appDispatchers.io) { apiClient.register(userAuth) }

}