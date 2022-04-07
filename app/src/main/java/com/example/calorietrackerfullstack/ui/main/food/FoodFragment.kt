package com.example.calorietrackerfullstack.ui.main.food

import android.Manifest
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.calorietrackerfullstack.R
import com.example.calorietrackerfullstack.data.model.Food
import com.example.calorietrackerfullstack.databinding.FragmentFoodBinding
import com.example.calorietrackerfullstack.ui.main.foodlist.USER_ID
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
class FoodFragment : Fragment(), TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentFoodBinding
    private lateinit var mImageUri: Uri
    private lateinit var userID: String
    private val viewModel: FoodViewModel by viewModels()
    private lateinit var startForSelectImageResult: ActivityResultLauncher<Intent>

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
//        setUploadedPhotoGallery()
//        attachUploadImageData()
//        attachSubmitFoodData()
        setBackReportList()
        setUpDateAndTime()
        initLauncher()
        setUpAddFood()
        setUpAddImage()
        attachAddFoodData()
        getUserID()
        attachProgressBar()
    }

    private fun getUserID() {
        arguments?.let { arg -> userID = (arg[USER_ID] as String) }
    }

    private fun setUpAddFood() {
        binding.submitBtn.setOnClickListener {
            viewModel.addFood(
                getDataFromUi(),
                getImage()
            )
        }
    }

    private fun getDataFromUi(): HashMap<String, RequestBody> {
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
            map["time"] = time.stringToRequestBody()
            map["userId"] = userID.stringToRequestBody()

            return map
        }
    }

    private fun getImage(): MultipartBody.Part {
        context!!.contentResolver.openInputStream(mImageUri)
        Log.d("TAG", "getImage: $mImageUri ${mImageUri.path}")
        return File(mImageUri.getFilePath(context!!)).fileToMultiPart("foodImage")
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

    private fun setUpAddImage() {
        binding.fabAddGalleryPhoto.setOnClickListener {
            requestPermission()
        }
    }

    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED -> {
                pickImage()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
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
                "This permission is needed to allow us help you use your images in our app."
            )
            .setPositiveButton("OK") { _, _ ->
                askStoragePermission()
            }
            .setNegativeButton("No thanks") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun askStoragePermission() {
        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
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
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        val fileUri = data?.data!!
                        mImageUri = fileUri
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

    private fun setBackReportList() {
        binding.apply {
            textBack.setOnClickListener {
                navToFoodList()
            }
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

const val SLASH = "/"
const val TIME_PICKER_DIALOG = "TimepickerDialog"
const val DATE_PICKER_DIALOG = "Datepickerdialog"