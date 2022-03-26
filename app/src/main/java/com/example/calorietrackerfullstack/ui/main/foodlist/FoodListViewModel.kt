package com.example.calorietrackerfullstack.ui.main.foodlist

import androidx.lifecycle.ViewModel
import com.example.calorietrackerfullstack.concurrency.IAppDispatchers
import com.example.calorietrackerfullstack.data.repository.foods.IFoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class FoodListViewModel @Inject constructor(
    private val appDispatchers: IAppDispatchers,
    private val repository: IFoodRepository,
) : ViewModel() {




}