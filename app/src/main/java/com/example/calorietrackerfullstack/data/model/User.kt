package com.example.calorietrackerfullstack.data.model

import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @SerializedName("data")
    val data: List<User>,
    @SerializedName("success")
    val success: Boolean
)


data class RegisterResponse(
    @SerializedName("data")
    val data: String,
    @SerializedName("success")
    val success: Boolean
)


data class User(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("userPassword")
    val userPassword: String,
    @SerializedName("userType")
    val userType: Int? = 0
)
