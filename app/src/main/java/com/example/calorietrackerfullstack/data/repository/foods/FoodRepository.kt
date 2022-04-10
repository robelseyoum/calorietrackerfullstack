package com.example.calorietrackerfullstack.data.repository.foods

import com.example.calorietrackerfullstack.concurrency.AppDispatchers
import com.example.calorietrackerfullstack.data.api.ApiClient
import com.example.calorietrackerfullstack.data.model.FoodResponse
import com.example.calorietrackerfullstack.data.model.FoodsResponse
import com.example.calorietrackerfullstack.utils.DataResult
import com.example.calorietrackerfullstack.utils.safeDataResult
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FoodRepository(
    private val apiClient: ApiClient,
    private val appDispatchers: AppDispatchers
) : IFoodRepository {

    override suspend fun getAllFoods(): DataResult<FoodsResponse?> =
        safeDataResult(appDispatchers.io) { apiClient.getAllFoods() }

    override suspend fun editFood(
        foodId: String,
        foodData: HashMap<String, RequestBody>,
        image: MultipartBody.Part
    ): DataResult<FoodResponse?> =
        safeDataResult(appDispatchers.io) { apiClient.editFood(foodId, foodData, image) }

    override suspend fun getFoods(userId: String): DataResult<FoodsResponse?> =
        safeDataResult(appDispatchers.io) { apiClient.getFoods(userId) }

    override suspend fun addFood(
        foodData: HashMap<String, RequestBody>,
        image: MultipartBody.Part
    ): DataResult<FoodResponse?> =
        safeDataResult(appDispatchers.io) { apiClient.addFood(foodData, image) }

    override suspend fun deleteFood(id: String): DataResult<FoodResponse?> =
        safeDataResult(appDispatchers.io) { apiClient.deleteFood(id) }
}