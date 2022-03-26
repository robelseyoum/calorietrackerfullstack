package com.example.calorietrackerfullstack.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResponse(
    @SerializedName("data")
    val data: User,
    @SerializedName("success")
    val success: Boolean
): Parcelable

@Parcelize
data class RegisterResponse(
    @SerializedName("data")
    val data: String,
    @SerializedName("success")
    val success: Boolean
): Parcelable

@Parcelize
data class User(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("userPassword")
    val userPassword: String,
    @SerializedName("userType")
    val userType: Int? = 0
): Parcelable


data class UserAuth(
    val userName: String,
    val userPassword: String,
)