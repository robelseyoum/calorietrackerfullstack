package com.example.calorietrackerfullstack.ui.main.foodlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.calorietrackerfullstack.databinding.FragmentCalorieBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodListFragment : Fragment()  {

    val maxCaloriesLimit = 2100
    private lateinit var binding: FragmentCalorieBinding
    private val viewModel: FoodListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCalorieBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        checkIsAdmin()
//        setOpenAdminFeatures()
//        setLogoutFunction()
//        setAddFood()
//        setHistoryFeature()
//        getFoodListData(DateUtils.currentDate())
//        setupPieChart()
//        maxCalories()
//        calculateDailyLimit(DateUtils.currentDate())
//        attachListOfData()
        val successUserResponse = FoodListFragmentArgs.fromBundle(requireArguments()).successUserResponse
        Log.d("FoodListFragment", "$successUserResponse")
    }
}