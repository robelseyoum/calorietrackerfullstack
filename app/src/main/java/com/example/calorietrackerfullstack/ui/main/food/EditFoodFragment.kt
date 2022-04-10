package com.example.calorietrackerfullstack.ui.main.food

import android.Manifest.*
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.calorietrackerfullstack.R
import com.example.calorietrackerfullstack.data.model.Food
import com.example.calorietrackerfullstack.databinding.FragmentEditFoodBinding
import com.example.calorietrackerfullstack.utils.*
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import kotlin.collections.HashMap

@AndroidEntryPoint
class EditFoodFragment : Fragment(), TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var foodItem: Food
    private lateinit var binding: FragmentEditFoodBinding
    private lateinit var mPhotoUri: Uri
    private lateinit var startForSelectImageResult: ActivityResultLauncher<Intent>
    private val viewModel: FoodViewModel by viewModels()

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
        initLauncher()
        getFoodData()
        setFoodData()
        updateFood()
        attachUpdateFoodData()
        attachProgressBar()
        setUpAddImage()
        setUpDateAndTime()
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
                binding.imgGallery.load(
                    "${Constants.IMAGE_BASE_URL}${foodItem.foodImage}"
                ) { placeholder(R.drawable.baseline_photo_24) }
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

    private fun getImage(): MultipartBody.Part {
        context!!.contentResolver.openInputStream(mPhotoUri)
        Log.d("TAG", "getImage: $mPhotoUri ${mPhotoUri.path}")
        return File(mPhotoUri.getFilePath(context!!)).fileToMultiPart("foodImage")
    }

    private fun updateFood() {
        binding.submitBtn.setOnClickListener {
            viewModel.editFood(
                foodItem.userId,
                getFoodUI(),
                getImage()
            )
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
                        Log.d("EditFoodFragment", "network error message- ${result.networkError}")
                        Toast.makeText(
                            context,
                            "Network error message- ${result.networkError}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is DataResult.Success -> {
                        if (result.value.success) {
                            navBackAdminReportList()
                        }
                    }
                }
            }
        })
    }

    private fun setUpAddImage() {
        binding.fabAddGalleryPhoto.setOnClickListener {
            requestPermission()
        }
    }

    /******************************
     * ****************************
     * *******Permission Stuff *****
     * ****************************
     * ****************************
     * */
    private var permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                pickImage()
            } else {
                unAvailableFeature()
            }
        }

    private fun unAvailableFeature() {
        MaterialAlertDialogBuilder(context!!)
            .setTitle("Unavailable Feature")
            .setMessage(
                "Uploading image to the Application isn't available. " +
                        "Pleas, grant us the permission so you can upload images!!"
            )
            .setPositiveButton("Ok") { _, _ ->
                askStoragePermission()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(context!!, permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED -> {
                pickImage()
            }
            shouldShowRequestPermissionRationale(permission.READ_EXTERNAL_STORAGE) -> {
                permissionExplanation()
            }
            else -> {
                askStoragePermission()
            }
        }
    }

    private fun permissionExplanation() {
        MaterialAlertDialogBuilder(context!!)
            .setTitle("Permission needed")
            .setMessage(
                "This permissioin is needed to allow us help you use your image in our app."
            )
            .setPositiveButton("Ok") { _, _ ->
                askStoragePermission()
            }
            .setNegativeButton("Noo thanks") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun askStoragePermission() {
        permissionLauncher.launch(permission.READ_EXTERNAL_STORAGE)
    }

    private fun pickImage() {
        ImagePicker.with(this)
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                startForSelectImageResult.launch(intent)
            }
    }

    private fun initLauncher() {
        startForSelectImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        val fileUri = data?.data!!
                        mPhotoUri = fileUri
                        binding.imgGallery.setImageURI(fileUri)
                    }
                    ImagePicker.RESULT_ERROR -> {
                        Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> {
                        Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                    }
                }
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

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
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
            findNavController()
                .navigate(R.id.action_editFoodFragment_to_adminFoodReportListFragments)
        }
    }
}

const val FOOD = "FOOD"