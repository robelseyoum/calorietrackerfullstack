package com.example.calorietrackerfullstack.ui.main.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calorietrackerfullstack.databinding.FragmentFoodReportListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodReportListFragments : Fragment() {

    private lateinit var binding: FragmentFoodReportListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFoodReportListBinding.inflate(inflater)
        return binding.root
    }


}