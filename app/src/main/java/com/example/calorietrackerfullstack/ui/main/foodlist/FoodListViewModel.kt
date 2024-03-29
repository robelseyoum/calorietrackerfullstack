package com.example.calorietrackerfullstack.ui.main.foodlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietrackerfullstack.concurrency.IAppDispatchers
import com.example.calorietrackerfullstack.data.model.FoodsResponse
import com.example.calorietrackerfullstack.data.repository.foods.IFoodRepository
import com.example.calorietrackerfullstack.utils.DataResult
import com.example.calorietrackerfullstack.utils.DataResult.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class FoodListViewModel @Inject constructor(
    private val appDispatchers: IAppDispatchers,
    private val repository: IFoodRepository,
) : ViewModel() {

    private val _foodList: MutableLiveData<DataResult<FoodsResponse>?> = MutableLiveData()
    val foodList: LiveData<DataResult<FoodsResponse>?> = _foodList
    val loading = MutableLiveData<Boolean>()

    fun getFoods(userId: String) = viewModelScope.launch {
        loading.value = true
        val result = withContext(appDispatchers.io) { repository.getFoods(userId) }
        when (result) {
            is GenericError -> {
                loading.value = false
                _foodList.value = GenericError(result.code, result.errorMessages)
            }
            is NetworkError -> {
                loading.value = false
                _foodList.value = NetworkError(result.networkError)
            }
            is Success -> {
                loading.value = false
                _foodList.value = Success(result.value!!)
            }
        }
    }
}