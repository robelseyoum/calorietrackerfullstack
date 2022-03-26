package com.example.calorietrackerfullstack.ui.main.food

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calorietrackerfullstack.data.model.Food
import com.example.calorietrackerfullstack.databinding.FragmentEditFoodBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditFoodFragment: Fragment() {

    private lateinit var docID: String
    private lateinit var foodItem: Food
    private lateinit var binding: FragmentEditFoodBinding
    private lateinit var mPhotoUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentEditFoodBinding.inflate(inflater)
        return binding.root
    }
}