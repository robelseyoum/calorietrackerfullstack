package com.example.calorietrackerfullstack.ui.main.foodlist

import android.graphics.Color
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
import com.example.calorietrackerfullstack.utils.DateUtils
import com.example.calorietrackerfullstack.utils.Prefs
import com.example.calorietrackerfullstack.utils.show
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.android.material.datepicker.MaterialDatePicker
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class FoodListFragment : Fragment(),
    DatePickerDialog.OnDateSetListener {

    val maxCaloriesLimit = 2100
    private lateinit var binding: FragmentCalorieBinding
    private val viewModel: FoodListViewModel by viewModels()
    private lateinit var foodList: List<Food>
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
        setHistoryFeature()
        setupPieChart()
        maxCalories()
        attachListOfData()
        getFoodList()
        attachProgressBar()
    }

    private fun setHistoryFeature() {
        binding.apply {
            tvHistory.setOnClickListener {
                showDataRangePicker()
            }
        }
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
                                foodList = data
                                setupTodayFoodCalories()
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

    private fun setupTodayFoodCalories() {
        val foodDateList = foodList.filter {
            (it.date == DateUtils.currentDate())
        }
        setupAdaptor(foodDateList)
        calculateCalories(foodDateList)
    }

    private fun showDataRangePicker() {
        val dateRangePicker =
            MaterialDatePicker
                .Builder.dateRangePicker()
                .setTitleText("Select Date")
                .build()

        dateRangePicker.show(
            fragmentManager!!,
            DATE_RANGE_PICKER
        )

        dateRangePicker.addOnPositiveButtonClickListener { dateSelected ->
            val startDate = dateSelected.first
            val endDate = dateSelected.second

            if (startDate != null && endDate != null) {
                val foodDateList = foodList.filter {
                    (it.date >= DateUtils.convertLongToStringTime(startDate)) &&
                    (it.date <= DateUtils.convertLongToStringTime(endDate))
                }
                setupAdaptor(foodDateList)
            }
        }
    }


    private fun calculateCalories(data: List<Food>) {
        var total = 0.0
        data.forEach { total += it.calorieValue.toDouble() }
        binding.tvEatenCalories.text = total.toString()
        if (total >= maxCaloriesLimit.toDouble()) {
            binding.textMaxCalorieLeft.show(true)
            binding.textMaxCalorieRight.show(true)
        }
        loadPieChartData(total)
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

    private fun setupPieChart() {
        binding.apply {
            pieChart.apply {
                isDrawHoleEnabled = true
                setUsePercentValues(false)
                setEntryLabelTextSize(10F)
                setEntryLabelColor(Color.TRANSPARENT)
                centerText = maxCaloriesLimit.toString()
                setCenterTextSize(18F)
                description.isEnabled = false
            }
        }
    }

    private fun maxCalories() {
        binding.tvMaxCalories.text = maxCaloriesLimit.toString()
    }

    private fun loadPieChartData(total: Double) {
        val minThresholdLimit = maxCaloriesLimit - total
        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(total.toFloat(), EATEN))
        entries.add(PieEntry(minThresholdLimit.toFloat(), BUDGET_CALORIES))

        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.RED)
        colors.add(Color.GREEN)

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        val data = PieData(dataSet)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(binding.pieChart))
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.TRANSPARENT)
        binding.pieChart.setData(data)
        binding.pieChart.invalidate()
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)
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

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {}
}


const val USER_ID = "USER_ID"
const val EATEN = "EATEN"
const val BUDGET_CALORIES = "BUDGET_CALORIES"
const val DATE_RANGE_PICKER = "DATE_RANGE_PICKER"