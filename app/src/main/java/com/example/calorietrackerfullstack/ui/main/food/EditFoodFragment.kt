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
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.calorietrackerfullstack.R
import com.example.calorietrackerfullstack.data.model.Food
import com.example.calorietrackerfullstack.databinding.FragmentEditFoodBinding
import com.example.calorietrackerfullstack.utils.*
import com.github.dhaval2404.imagepicker.ImagePicker
import com.squareup.picasso.Picasso
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*
import kotlin.collections.HashMap

@AndroidEntryPoint
class EditFoodFragment : Fragment(), TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var foodItem: Food
    private lateinit var binding: FragmentEditFoodBinding
    private lateinit var mPhotoUri: Uri
    private val viewModel: FoodViewModel by viewModels()
    private var map = HashMap<String, RequestBody>()
    private lateinit var startForSelectImageResult: ActivityResultLauncher<Intent>
    var multipartBody: MultipartBody.Part? = null
    private lateinit var file: File
    var food = ""
    var calories = ""
    var date = ""
    var time = ""
    var foodImage = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentEditFoodBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navBackAdminReportList()
        getFoodData()
        setFoodData()
        setUpAddFood()
        attachUpdateFoodData()
        attachProgressBar()
        setUpDateAndTime()
        addImageToGallery()
    }

    private fun getFoodData() {
        arguments?.let { arg -> foodItem = (arg[FOOD] as Food) }
    }

    private fun setFoodData() {
        binding.apply {
            etDate.setText(foodItem.date)
            etTime.setText(foodItem.time)
            etFoodName.setText(foodItem.foodName)
            etCaloriesValue.setText(foodItem.calorieValue)

            if (foodItem.foodImage.isNotEmpty()) {
                Picasso.get().load(
                    "${Constants.IMAGE_BASE_URL}${foodItem.foodImage}"
                ).into(binding.imgGallery)
            }
        }
    }

    private fun setUpAddFood() {
        binding.submitBtn.setOnClickListener {
            if (checkFormIsNotEmpty()) {
                viewModel.editFood(
                    foodItem.foodId.toString(),
                    getFoodUI(),
                    getImage()
                )
            } else {
                noImageMessage("Image is not added")
            }
        }
    }

    private fun setUploadedPhotoGallery() {
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

    private fun addImageToGallery() {
        binding.fabAddGalleryPhoto.setOnClickListener {
            setUploadedPhotoGallery()
        }
    }

    private fun getImage(): MultipartBody.Part? {
        if (this::mPhotoUri.isInitialized) {
            if (mPhotoUri.path!!.isNotEmpty()) {
                context!!.contentResolver.openInputStream(mPhotoUri)
                multipartBody = File(mPhotoUri.getFilePath(context!!)).fileToMultiPart("foodImage")
                Log.d("TAG", "getImage: ${mPhotoUri} ${mPhotoUri.path}")
            }
        } else {
            Picasso.get().load(
                "${Constants.IMAGE_BASE_URL}${foodItem.foodImage}"
            ).into(binding.imgGallery)
        }
        return multipartBody
    }

    private fun noImageMessage(mImageUri: String) {
        Toast.makeText(
            context,
            "Please add Image: $mImageUri",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun checkFormIsNotEmpty(): Boolean {
        binding.apply {
            food = etFoodName.text.toString()
            calories = etCaloriesValue.text.toString()
            date = etDate.text.toString()
            time = etTime.text.toString()
            foodImage = foodItem.foodImage

            return if (
                food.isNotEmpty() &&
                calories.isNotEmpty() &&
                date.isNotEmpty() &&
                time.isNotEmpty() &&
                foodImage.isNotEmpty()
            ) {
                true
            } else {
                Toast.makeText(context, "Please fill food form", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    private fun getFoodUI(): HashMap<String, RequestBody> {
        binding.apply {
            val food = etFoodName.text.toString()
            val calories = etCaloriesValue.text.toString()
            val date = etDate.text.toString()
            val time = etTime.text.toString()

            val map = HashMap<String, RequestBody>()
            map["foodName"] = food.stringToRequestBody()
            map["calorie"] = calories.stringToRequestBody()
            map["date"] = date.stringToRequestBody()
            map["time"] = time.stringToRequestBody()
            map["userId"] = foodItem.userId.stringToRequestBody()

            return map
        }
    }

    private fun attachUpdateFoodData() {
        viewModel.updateFoodStatus.observe(viewLifecycleOwner, Observer { response ->
            response?.let { result ->
                when (result) {
                    is DataResult.GenericError -> {
                        Log.d(
                            "EditFoodFragment",
                            "code- ${result.code} error message- ${result.errorMessages}"
                        )
                        Toast.makeText(
                            context,
                            "GenericError code- ${result.code} error message- ${result.errorMessages}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is DataResult.NetworkError -> {
                        Log.d(
                            "EditFoodFragment",
                            "network error message- ${result.networkError}"
                        )
                        Toast.makeText(
                            context,
                            "Network error message- ${result.networkError}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is DataResult.Success -> {
                        if (result.value.success) {
                            navToAdminAddFoodList()
                        }
                    }
                }
            }
        })
    }


    /******************************
     * ****************************
     * ******* Permission Stuff ***
     * ****************************
     * ****************************
     * */
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                mPhotoUri = data?.data!!
                binding!!.imgGallery.setImageURI(mPhotoUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(activity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
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

    override fun onDateSet(
        view: DatePickerDialog?,
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int
    ) {
        val date = dayOfMonth.toString() + SLASH + (monthOfYear + 1) + SLASH + year
        binding.etDate.setText(date)
    }

    private fun attachProgressBar() {
        viewModel.loading.observe(viewLifecycleOwner, Observer { show ->
            binding.progressBar.show(show)
        })
    }

    private fun navBackAdminReportList() {
        binding.textBack.setOnClickListener {
            navToAdminAddFoodList()
        }
    }

    private fun navToAdminAddFoodList() {
        findNavController()
            .navigate(R.id.action_editFoodFragment_to_adminFoodReportListFragments)
    }
}

const val FOOD = "FOOD"