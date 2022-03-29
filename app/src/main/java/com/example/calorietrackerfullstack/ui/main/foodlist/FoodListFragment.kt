package com.example.calorietrackerfullstack.ui.main.foodlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.calorietrackerfullstack.R
import com.example.calorietrackerfullstack.databinding.FragmentCalorieBinding
import com.example.calorietrackerfullstack.prefs
import com.example.calorietrackerfullstack.utils.Prefs
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

        checkIsAdmin()
//        setOpenAdminFeatures()
        setLogout()
//        setAddFood()
//        setHistoryFeature()
//        getFoodListData(DateUtils.currentDate())
//        setupPieChart()
//        maxCalories()
//        calculateDailyLimit(DateUtils.currentDate())
//        attachListOfData()
        val userId = Prefs(context!!).userIdPref
        Log.d("FoodListFragmentUserId", "$userId")
    }

    private fun checkIsAdmin() {
        if(Prefs(context!!).isAdminPref == 1){
            binding.textAdmin.visibility = View.VISIBLE
        }
    }

    private fun setLogout() {
        binding.textLogout.setOnClickListener {
            prefs.isLoggedIn = false
            prefs.isAdminPref = 0
            prefs.userIdPref = 0
            navToStartScreen()
        }
    }

    private fun navToStartScreen() {
        findNavController().navigate(R.id.action_foodListFragment_to_authFragment)
    }
}