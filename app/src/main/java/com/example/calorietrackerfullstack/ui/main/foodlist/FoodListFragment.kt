package com.example.calorietrackerfullstack.ui.main.foodlist

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
import com.example.calorietrackerfullstack.databinding.FragmentCalorieBinding
import com.example.calorietrackerfullstack.prefs
import com.example.calorietrackerfullstack.ui.main.adapter.FoodsListAdapter
import com.example.calorietrackerfullstack.utils.DataResult
import com.example.calorietrackerfullstack.utils.Prefs
import com.example.calorietrackerfullstack.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodListFragment : Fragment() {

    val maxCaloriesLimit = 2100
    private lateinit var binding: FragmentCalorieBinding
    private val viewModel: FoodListViewModel by viewModels()
    val bundle = Bundle()
    var userId: Int = 0

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
        getUserId()
        checkIsAdmin()
        setOpenAdminFeatures()
        setLogout()
        setAddFood()
//        setHistoryFeature()
//        getFoodListData(DateUtils.currentDate())
//        setupPieChart()
//        maxCalories()
//        calculateDailyLimit(DateUtils.currentDate())
        attachListOfData()
        getFoodList()
        attachProgressBar()
    }

    private fun getUserId() {
        userId = Prefs(context!!).userIdPref
    }

    private fun setAddFood() {
        binding.fab.setOnClickListener { navAddFood() }
    }

    private fun navAddFood() {
        bundle.putString(USER_ID, userId.toString())
        findNavController().navigate(R.id.action_foodListFragment_to_foodFragment, bundle)
    }

    private fun getFoodList() {
        viewModel.getFoods(userId.toString())
    }

    private fun attachListOfData() {
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
                            context, "Network error message- ${foods.networkError}",
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
        val adapter = FoodsListAdapter(data, object : FoodsListAdapter.OnClickListener {
            override fun onClick(foodData: Food) {
                Log.d("onClick", "foods - $foodData")
            }
        })
        binding.recycler.layoutManager = LinearLayoutManager(context)
        binding.recycler.adapter = adapter
    }

    private fun setOpenAdminFeatures() {
        binding.apply { textAdmin.setOnClickListener { navReportList() } }
    }

    private fun checkIsAdmin() {
        if (Prefs(context!!).isAdminPref == 1) {
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

    private fun navReportList() {
        findNavController().navigate(R.id.action_foodListFragment_to_adminFoodReportListFragments)
    }
}


const val USER_ID = "USER_ID"