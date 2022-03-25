package com.example.calorietrackerfullstack.data.api

import com.example.calorietrackerfullstack.data.model.FoodResponse
import com.example.calorietrackerfullstack.data.model.FoodsResponse
import com.example.calorietrackerfullstack.data.model.LoginResponse
import com.example.calorietrackerfullstack.data.model.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiClient {

    @POST("food/account/login")
    @FormUrlEncoded
    fun login(
        @Field("username") email: String,
        @Field("password") password: String
    ): LoginResponse


    @POST("food/account/register")
    @FormUrlEncoded
    fun register(
        @Field("username") username: String,
        @Field("password") password: String,
    ): RegisterResponse

    @GET("food/food_list")
    suspend fun getAllFoods(): FoodsResponse

    @Multipart
    @POST("food/add_food")
    suspend fun addFoods(
        @PartMap foodData: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part
    ): FoodResponse


}