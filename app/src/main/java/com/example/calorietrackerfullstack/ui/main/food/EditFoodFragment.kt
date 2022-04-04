package com.example.calorietrackerfullstack.ui.main.food

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.calorietrackerfullstack.R
import com.example.calorietrackerfullstack.data.model.Food
import com.example.calorietrackerfullstack.databinding.FragmentEditFoodBinding
import com.example.calorietrackerfullstack.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.food_item_raw.view.*
import kotlinx.android.synthetic.main.item_admin_food_list.*

@AndroidEntryPoint
class EditFoodFragment : Fragment() {

    private lateinit var docID: String
    private lateinit var foodItem: Food
    private lateinit var binding: FragmentEditFoodBinding
    private lateinit var mPhotoUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentEditFoodBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navBack()
        arguments?.let { arg -> foodItem = (arg["Food"] as Food) }
        setFoodData()
    }

    private fun setFoodData() {
        binding.apply {
            etDate.setText(foodItem.date)
            etTime.setText(foodItem.time)
            etFoodName.setText(foodItem.foodName)
            etCaloriesValue.setText(foodItem.calorieValue)

            if (foodItem.foodImage.isNotEmpty()) {
                binding.imgGallery.load(
                    "${Constants.IMAGE_BASE_URL}${foodItem.foodImage}"
                ) { placeholder(R.drawable.baseline_photo_24) }
            }
        }
    }

    private fun navBack() {
        binding.textBack.setOnClickListener {
            findNavController()
                .navigate(R.id.action_editFoodFragment_to_adminFoodReportListFragments)
        }
    }
}