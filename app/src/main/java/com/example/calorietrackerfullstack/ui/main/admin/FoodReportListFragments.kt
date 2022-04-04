package com.example.calorietrackerfullstack.ui.main.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calorietrackerfullstack.R
import com.example.calorietrackerfullstack.data.model.Food
import com.example.calorietrackerfullstack.databinding.FragmentFoodReportListBinding
import com.example.calorietrackerfullstack.ui.main.adapter.AdminFoodsListAdapter
import com.example.calorietrackerfullstack.utils.DataResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodReportListFragments : Fragment() {

    private lateinit var binding: FragmentFoodReportListBinding
    private val viewModel: FoodReportListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFoodReportListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setFoodListFunction()
//        setReportFunction()
        getAllFoodListData()
        attachProgressBar()
        attachAllListOfData()
        navBackFoodList()
        initOnDeleteFood()
        initEditFood()
    }


    private fun getAllFoodListData() {
        viewModel.getAllFoods()
    }

    private fun attachAllListOfData() {
        viewModel.foodList.observe(viewLifecycleOwner, Observer { response ->
            response?.let { foods ->
                when (foods) {
                    is DataResult.GenericError -> {
                        Log.d(
                            "LoginFragment",
                            "code- ${foods.code} error message- ${foods.errorMessages}"
                        )
                        Toast.makeText(
                            context,
                            "GenericError code- ${foods.code} error message- ${foods.errorMessages}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is DataResult.NetworkError -> {
                        Log.d("LoginFragment", "network error message- ${foods.networkError}")
                        Toast.makeText(
                            context,
                            "Network error message- ${foods.networkError}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is DataResult.Success -> {
                        if (foods.value.success) {
                            foods.value.data?.let { data ->
                                setupAdaptor(data)
                            }
                        } else {
                            binding.textError.visibility = View.VISIBLE
                            binding.recycler.visibility = View.GONE
                        }
                    }
                }
            }
        })
    }

    private fun attachProgressBar() {
        viewModel.showProgress.observe(viewLifecycleOwner, Observer { show ->
            if (show) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun setupAdaptor(data: List<Food>) {
        binding.recycler.visibility = View.VISIBLE
        val adapter =
            AdminFoodsListAdapter(data, object : AdminFoodsListAdapter.FoodAdapterListener {
                override fun onDeleteClick(foodData: Food) {
                    Log.d("on__DeleteClick", "foods - $foodData")
                }

                override fun onEditClick(foodData: Food) {
                    Log.d("on__EditClick", "foods - $foodData")
                    findNavController()
                        .navigate(R.id.action_adminFoodReportListFragments_to_editFoodFragment)
                }
            })
        binding.recycler.layoutManager = LinearLayoutManager(context)
        binding.recycler.adapter = adapter
    }

    private fun initEditFood() {
        //TODO edit food
    }
    private fun initOnDeleteFood() {
        //TODO delete food
    }

    private fun navBackFoodList() {
        binding.apply { textBack.setOnClickListener { navToFoodList() } }
    }

    private fun navToFoodList() {
        findNavController().navigate(R.id.action_adminFoodReportListFragments_to_foodListFragment)
    }

}