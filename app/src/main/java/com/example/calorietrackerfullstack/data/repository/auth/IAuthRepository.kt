package com.example.calorietrackerfullstack.data.repository.auth

import com.example.calorietrackerfullstack.data.model.LoginResponse
import com.example.calorietrackerfullstack.data.model.RegisterResponse
import com.example.calorietrackerfullstack.utils.DataResult

interface IAuthRepository {
    suspend fun login(username: String, password: String): DataResult<LoginResponse?>
    suspend fun register(username: String, password: String): DataResult<RegisterResponse?>
}