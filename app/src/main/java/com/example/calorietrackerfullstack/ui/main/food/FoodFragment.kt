package com.example.calorietrackerfullstack.ui.main.food

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.calorietrackerfullstack.R
import com.example.calorietrackerfullstack.databinding.FragmentFoodBinding
import com.example.calorietrackerfullstack.ui.main.foodlist.USER_ID
import com.example.calorietrackerfullstack.utils.*
import com.github.dhaval2404.imagepicker.ImagePicker
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import kotlin.collections.HashMap

@AndroidEntryPoint
class FoodFragment : Fragment(), TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentFoodBinding
    private lateinit var mPhotoUri: Uri
    private lateinit var userID: String
    private lateinit var file: File
    private val viewModel: FoodViewModel by viewModels()
    private var map = HashMap<String, RequestBody>()
    private lateinit var startForSelectImageResult: ActivityResultLauncher<Intent>
    var food = ""
    var calories = ""
    var date = ""
    var time = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFoodBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setFoodData()
        setUploadedPhotoGallery()
//        attachUploadImageData()
//        attachSubmitFoodData()
//        initLauncher()
        setBackReportList()
        setUpDateAndTime()
        setUpAddFood()
//        setUpAddImage()
        attachAddFoodData()
        getUserID()
        attachProgressBar()
    }

    private fun setUploadedPhotoGallery() {
        binding.fabAddGalleryPhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(COMPRESS_MAX_SIZE)  //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    MAX_RESULT_SIZE,
                    MAX_RESULT_SIZE
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }
    }


    private fun getUserID() {
        arguments?.let { arg -> userID = (arg[USER_ID] as String) }
    }

    private fun setUpAddFood() {
        binding.submitBtn.setOnClickListener {

            if (this::mPhotoUri.isInitialized) {
                if (mPhotoUri.toString().isNotEmpty() && checkFormIsNotEmpty()) {
                    viewModel.addFood(
                        getFoodUI(),
                        getImage(mPhotoUri)
                    )
                }
            } else {
                prepareImage("Image is not added")
            }
        }
    }

    private fun checkFormIsNotEmpty(): Boolean {
        binding.apply {
            food = etFoodName.text.toString()
            calories = etCaloriesValue.text.toString()
            date = etDate.text.toString()
            time = etTime.text.toString()

            return if (
                food.isNotEmpty() &&
                calories.isNotEmpty() &&
                date.isNotEmpty() &&
                time.isNotEmpty()
            ) {
                true
            } else {
                Toast.makeText(context, "Please fill food form", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    private fun getFoodUI(): HashMap<String, RequestBody> {
        if (
            checkFormIsNotEmpty()
        ) {
            map["foodName"] = food.stringToRequestBody()
            map["calorie"] = calories.stringToRequestBody()
            map["date"] = date.stringToRequestBody()
            map["time"] = time.stringToRequestBody()
            map["userId"] = userID.stringToRequestBody()
        }
        return map
    }

    private fun getImage(mImageUri: Uri): MultipartBody.Part {
        context!!.contentResolver.openInputStream(mImageUri)
        Log.d("TAG", "getImage: ${mImageUri} ${mImageUri.path}")
        return File(mImageUri.getFilePath(context!!)).fileToMultiPart("foodImage")
    }

    private fun prepareImage(mImageUri: String) {
        Toast.makeText(
            context,
            "Please add Image: $mImageUri",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun attachAddFoodData() {
        viewModel.addFoodStatus.observe(viewLifecycleOwner, Observer { response ->
            response?.let { result ->
                when (result) {
                    is DataResult.GenericError -> {
                        Log.d(
                            "FoodFragment",
                            "code- ${result.code} error message- ${result.errorMessages}"
                        )
                        Toast.makeText(
                            context,
                            "GenericError code- ${result.code} error message- ${result.errorMessages}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is DataResult.NetworkError -> {
                        Log.d("FoodFragment", "network error message- ${result.networkError}")
                        Toast.makeText(
                            context,
                            "Network error message- ${result.networkError}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is DataResult.Success -> {
                        if (result.value.success) {
                            navToFoodList()
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

    /******************************
     * ****************************
     * *******Permission Stuff *****
     * ****************************
     * ****************************
     * */

    private fun setBackReportList() {
        binding.apply {
            textBack.setOnClickListener {
                navToFoodList()
            }
        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                mPhotoUri = data?.data!!
                binding!!.imgGallery.setImageURI(mPhotoUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun navToFoodList() {
        findNavController().navigate(R.id.action_foodFragment_to_foodListFragment)
    }

    private fun setUpDateAndTime() {
        binding.apply {
            etDate.setOnClickListener { funDatePicker() }
            etTime.setOnClickListener { funTimePicker() }
        }
    }

    private fun funTimePicker() {
        val now: Calendar = Calendar.getInstance()
        val dpd = TimePickerDialog.newInstance(
            this,
            now.get(Calendar.HOUR_OF_DAY),
            now.get(Calendar.MINUTE), true // Initial month selection
        )
        dpd.show(fragmentManager!!, TIME_PICKER_DIALOG)
    }

    private fun funDatePicker() {
        val now: Calendar = Calendar.getInstance()
        val dpd = DatePickerDialog.newInstance(
            this,
            now.get(Calendar.YEAR), // Initial year selection
            now.get(Calendar.MONTH), // Initial year selection
            now.get(Calendar.DAY_OF_MONTH) // Initial year selection
        )
        // If you're calling this from a support Fragment
        dpd.show(fragmentManager!!, DATE_PICKER_DIALOG)
    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        val time = "$hourOfDay:$minute"
        binding.etTime.setText(time)
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val date = dayOfMonth.toString() + SLASH + (monthOfYear + 1) + SLASH + year
        binding.etDate.setText(date)
    }

}

const val SLASH = "-"
const val TIME_PICKER_DIALOG = "TimepickerDialog"
const val DATE_PICKER_DIALOG = "Datepickerdialog"
const val COMPRESS_MAX_SIZE = 1024
const val MAX_RESULT_SIZE = 1080