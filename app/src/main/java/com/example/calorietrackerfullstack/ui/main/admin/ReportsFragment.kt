package com.example.calorietrackerfullstack.ui.main.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.calorietrackerfullstack.R
import com.example.calorietrackerfullstack.databinding.FragmentReportsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportsFragment : Fragment() {

    private lateinit var binding: FragmentReportsBinding
    private lateinit var weekDate: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentReportsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navBackToAdminList()
    }

    private fun navBackToAdminList() {
        binding.textBack.setOnClickListener {
            findNavController().navigate(R.id.action_reportFragment_to_adminFoodReportListFragments)
        }
    }
}