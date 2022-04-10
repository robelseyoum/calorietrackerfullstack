package com.example.calorietrackerfullstack.ui.main.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietrackerfullstack.concurrency.IAppDispatchers
import com.example.calorietrackerfullstack.data.model.FoodResponse
import com.example.calorietrackerfullstack.data.repository.foods.IFoodRepository
import com.example.calorietrackerfullstack.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


@HiltViewModel
class FoodViewModel @Inject constructor(
    private val appDispatchers: IAppDispatchers,
    private val repository: IFoodRepository,
) : ViewModel() {

    private val _addFoodStatus: MutableLiveData<DataResult<FoodResponse>?> = MutableLiveData()
    val addFoodStatus: LiveData<DataResult<FoodResponse>?> = _addFoodStatus
    private val _updateFoodStatus: MutableLiveData<DataResult<FoodResponse>?> = MutableLiveData()
    val updateFoodStatus: LiveData<DataResult<FoodResponse>?> = _updateFoodStatus
    val loading = MutableLiveData<Boolean>()

    fun addFood(
        foodData: HashMap<String, RequestBody>,
        image: MultipartBody.Part
    ) = viewModelScope.launch {
        loading.value = true
        val result = withContext(appDispatchers.io) { repository.addFood(foodData, image) }
        when (result) {
            is DataResult.GenericError -> {
                loading.value = false
                _addFoodStatus.value = DataResult.GenericError(result.code, result.errorMessages)
            }
            is DataResult.NetworkError -> {
                loading.value = false
                _addFoodStatus.value = DataResult.NetworkError(result.networkError)
            }
            is DataResult.Success -> {
                loading.value = false
                _addFoodStatus.value = DataResult.Success(result.value!!)
            }
        }
    }

    fun editFood(
        id: String,
        foodData: HashMap<String, RequestBody>,
        image: MultipartBody.Part
    ) = viewModelScope.launch {
        loading.value = true
        val result = withContext(appDispatchers.io) { repository.editFood(id, foodData, image) }
        when (result) {
            is DataResult.GenericError -> {
                loading.value = false
                _updateFoodStatus.value = DataResult.GenericError(result.code, result.errorMessages)
            }
            is DataResult.NetworkError -> {
                loading.value = false
                _updateFoodStatus.value = DataResult.NetworkError(result.networkError)
            }
            is DataResult.Success -> {
                loading.value = false
                _updateFoodStatus.value = DataResult.Success(result.value!!)
            }
        }
    }
}