package com.example.calorietrackerfullstack.data.repository.auth

import com.example.calorietrackerfullstack.data.model.AuthResponse
import com.example.calorietrackerfullstack.data.model.UserAuth
import com.example.calorietrackerfullstack.utils.DataResult

interface IAuthRepository {
    suspend fun login(userAuth: UserAuth): DataResult<AuthResponse?>
    suspend fun register(userAuth: UserAuth): DataResult<AuthResponse?>
}