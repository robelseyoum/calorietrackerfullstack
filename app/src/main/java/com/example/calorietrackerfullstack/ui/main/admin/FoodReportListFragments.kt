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
import com.example.calorietrackerfullstack.utils.show
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
        setReportFunction()
        getAllFoodListData()
        attachAllListOfData()
        navBackFoodList()
        attachOnDeleteFood()
    }

    private fun getAllFoodListData() {
        viewModel.getAllFoods()
        attachProgressBar()
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
        viewModel.loading.observe(viewLifecycleOwner, Observer { show ->
            binding.progressBar.show(show)
        })
    }

    private fun setupAdaptor(data: List<Food>) {
        binding.recycler.visibility = View.VISIBLE
        val adapter =
            AdminFoodsListAdapter(data, object : AdminFoodsListAdapter.FoodAdapterListener {
                override fun onDeleteClick(id: String) {
                    Log.d("on__DeleteClick", "foods - $id")
                    viewModel.deleteFood(id)
                }

                override fun onEditClick(foodData: Food) {
                    Log.d("on__EditClick", "foods - $foodData")
                    goToEditFragment(foodData)
                }
            })
        binding.recycler.layoutManager = LinearLayoutManager(context)
        binding.recycler.adapter = adapter
    }

    private fun goToEditFragment(foodData: Food) {
        val bundle = Bundle()
        bundle.putParcelable(FOOD, foodData)
        navToEditFood(bundle)
    }

    private fun attachOnDeleteFood() {
        viewModel.foodDeleteStatus.observe(viewLifecycleOwner, Observer { response ->
            response?.let { delete ->
                when (delete) {
                    is DataResult.GenericError -> {
                        Log.d(
                            "LoginFragment",
                            "code- ${delete.code} error message- ${delete.errorMessages}"
                        )
                        Toast.makeText(
                            context,
                            "GenericError code- ${delete.code} error message- ${delete.errorMessages}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is DataResult.NetworkError -> {
                        Log.d("LoginFragment", "network error message- ${delete.networkError}")
                        Toast.makeText(
                            context,
                            "Network error message- ${delete.networkError}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is DataResult.Success -> {
                        if (delete.value.success) {
                            viewModel.getAllFoods()
                        } else {
                            binding.textError.visibility = View.VISIBLE
                            binding.recycler.visibility = View.GONE
                        }
                    }
                }
            }
        })
    }

    private fun setReportFunction() {
        binding.apply { textReport.setOnClickListener { navToReport() } }
    }

    private fun navToEditFood(bundle: Bundle) {
        findNavController()
            .navigate(R.id.action_adminFoodReportListFragments_to_editFoodFragment, bundle)
    }

    private fun navBackFoodList() {
        binding.apply { textBack.setOnClickListener { navToFoodList() } }
    }

    private fun navToFoodList() {
        findNavController().navigate(R.id.action_adminFoodReportListFragments_to_foodListFragment)
    }

    private fun navToReport() {
        findNavController().navigate(R.id.action_adminFoodReportListFragments_to_reportFragment)
    }
}
const val FOOD = "FOOD"