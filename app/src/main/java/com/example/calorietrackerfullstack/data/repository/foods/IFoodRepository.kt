package com.example.calorietrackerfullstack.data.repository.foods

import com.example.calorietrackerfullstack.data.model.FoodResponse
import com.example.calorietrackerfullstack.data.model.FoodsResponse
import com.example.calorietrackerfullstack.utils.DataResult
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface IFoodRepository {

    suspend fun getAllFoods(): DataResult<FoodsResponse?>

    suspend fun editFood(
        foodId: String,
        foodData: HashMap<String, RequestBody>,
        image: MultipartBody.Part?
    ): DataResult<FoodResponse?>

    suspend fun getFoods(userId: String): DataResult<FoodsResponse?>

    suspend fun addFood(
        foodData: HashMap<String, RequestBody>,
        image: MultipartBody.Part?
    ): DataResult<FoodResponse?>

    suspend fun deleteFood(id: String): DataResult<FoodResponse?>
}