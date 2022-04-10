package com.example.calorietrackerfullstack.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Food(
    @SerializedName("foodId")
    val foodId: Int,
    @SerializedName("foodName")
    val foodName: String,
    @SerializedName("calorieValue")
    val calorieValue: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("foodImage")
    val foodImage: String,
    @SerializedName("userId")
    val userId: String
) : Parcelable

data class FoodsResponse(
    @SerializedName("error")
    val error: String?,
    @SerializedName("data")
    val data: List<Food>?,
    @SerializedName("success")
    val success: Boolean
)

data class FoodResponse(
    @SerializedName("data")
    val data: String,
    @SerializedName("success")
    val success: Boolean
)

data class FoodRequest(
    val foodId: Int,
    val foodName: String,
    val calorieValue: String,
    val date: String,
    val time: String,
    val foodImage: String,
    val userId: String
)

