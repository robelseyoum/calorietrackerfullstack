package com.example.calorietrackerfullstack.ui.main.food

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.calorietrackerfullstack.R
import com.example.calorietrackerfullstack.data.model.Food
import com.example.calorietrackerfullstack.databinding.FragmentEditFoodBinding
import dagger.hilt.android.AndroidEntryPoint

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
    }

    private fun navBack() {
        binding.textBack.setOnClickListener {
            findNavController()
                .navigate(R.id.action_editFoodFragment_to_adminFoodReportListFragments)
        }
    }
}