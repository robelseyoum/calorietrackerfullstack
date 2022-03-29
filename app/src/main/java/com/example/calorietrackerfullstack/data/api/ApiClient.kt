package com.example.calorietrackerfullstack.data.api

import com.example.calorietrackerfullstack.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiClient {

    /**

    @Field("userName") email: String,
    @Field("userPassword") password: String

    @POST("/food/account/login")
    @FormUrlEncoded
    suspend fun login(
    @FieldMap fields: Map<String, String>
    ): LoginResponse


    @POST("food/account/register")
    @FormUrlEncoded
    suspend fun register(
    @Field("username") username: String,
    @Field("password") password: String,
    ): RegisterResponse

     */

    @POST("/food/account/login")
    suspend fun login(
        @Body user: UserAuth
    ): AuthResponse

    @POST("food/account/register")
    suspend fun register(
        @Body user: UserAuth
    ): AuthResponse

    @GET("food/food_list")
    suspend fun getAllFoods(): FoodsResponse

    @Multipart
    @POST("food/add_food")
    suspend fun addFoods(
        @PartMap foodData: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part
    ): FoodResponse


}