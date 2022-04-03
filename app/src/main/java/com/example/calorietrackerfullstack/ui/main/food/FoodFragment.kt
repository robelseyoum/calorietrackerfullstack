package com.example.calorietrackerfullstack.ui.main.food

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.calorietrackerfullstack.R
import com.example.calorietrackerfullstack.databinding.FragmentFoodBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodFragment : Fragment() {

    private lateinit var mPhotoUri: Uri
    private lateinit var binding: FragmentFoodBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFoodBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackReportList()
//        setFoodData()
//        setUploadedPhotoGallery()
//        setUpDateAndTime()
//        attachUploadImageData()
//        attachSubmitFoodData()
//        setUpAddFood()
    }


    private fun navToReportList() {
        findNavController().navigate(R.id.action_foodFragment_to_foodListFragment)
    }

    private fun setBackReportList() {
        binding.apply {
            textBack.setOnClickListener {
                navToReportList()
            }
        }
    }

}