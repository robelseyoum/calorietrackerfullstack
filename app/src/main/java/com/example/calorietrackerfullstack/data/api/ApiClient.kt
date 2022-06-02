package com.example.calorietrackerfullstack.data.api

import com.example.calorietrackerfullstack.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiClient {

    @POST("/food/account/login")
    suspend fun login(
        @Body user: UserAuth
    ): AuthResponse

    @POST("food/account/register")
    suspend fun register(
        @Body user: UserAuth
    ): AuthResponse

    @GET("food/foods_list")
    suspend fun getAllFoods(): FoodsResponse

    @GET("food/single_food/{id}")
    suspend fun getFood(
        @Path("id") id: String,
    ): FoodsResponse

    @Multipart
    @PUT( "food/food_update/{id}")
    suspend fun editFood(
        @Path("id") id: String,
        @PartMap foodData: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part?
    ): FoodResponse

    @Multipart
    @POST("food/add_food")
    suspend fun addFood(
        @PartMap foodData: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part?
    ): FoodResponse

    @DELETE("food/food_delete/{id}")
    suspend fun deleteFood(@Path("id") id: String): FoodResponse




}