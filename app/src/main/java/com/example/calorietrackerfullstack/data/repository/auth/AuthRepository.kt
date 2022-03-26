package com.example.calorietrackerfullstack.data.repository.auth

import com.example.calorietrackerfullstack.concurrency.AppDispatchers
import com.example.calorietrackerfullstack.data.api.ApiClient
import com.example.calorietrackerfullstack.data.model.LoginResponse
import com.example.calorietrackerfullstack.data.model.RegisterResponse
import com.example.calorietrackerfullstack.data.model.User
import com.example.calorietrackerfullstack.data.model.UserAuth
import com.example.calorietrackerfullstack.utils.DataResult
import com.example.calorietrackerfullstack.utils.safeDataResult

class AuthRepository(
    private val apiClient: ApiClient,
    private val appDispatchers: AppDispatchers
) : IAuthRepository {

    override suspend fun login(userAuth: UserAuth): DataResult<LoginResponse?> =
        safeDataResult(appDispatchers.io) { apiClient.login(userAuth) }

    override suspend fun register(
        username: String,
        password: String
    ): DataResult<RegisterResponse?> =
        safeDataResult(appDispatchers.io) { apiClient.register(username, password) }

}