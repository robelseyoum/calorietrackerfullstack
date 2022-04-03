package com.example.calorietrackerfullstack.ui.main.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietrackerfullstack.concurrency.IAppDispatchers
import com.example.calorietrackerfullstack.data.model.FoodsResponse
import com.example.calorietrackerfullstack.data.repository.foods.IFoodRepository
import com.example.calorietrackerfullstack.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FoodReportListViewModel  @Inject constructor(
    private val appDispatchers: IAppDispatchers,
    private val repository: IFoodRepository,
) : ViewModel() {

    private val _foodList: MutableLiveData<DataResult<FoodsResponse>?> = MutableLiveData()
    val foodList: LiveData<DataResult<FoodsResponse>?> = _foodList
    val showProgress: MutableLiveData<Boolean> = MutableLiveData()

    fun getAllFoods() = viewModelScope.launch {
        showProgress.value = true
        val result = withContext(appDispatchers.io) { repository.getAllFoods() }
        when (result) {
            is DataResult.GenericError -> {
                showProgress.value = false
                _foodList.value = DataResult.GenericError(result.code, result.errorMessages)
            }
            is DataResult.NetworkError -> {
                showProgress.value = false
                _foodList.value = DataResult.NetworkError(result.networkError)
            }
            is DataResult.Success -> {
                showProgress.value = false
                _foodList.value = DataResult.Success(result.value!!)
            }
        }
    }
}