package com.example.calorietrackerfullstack.ui.auth.register

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.calorietrackerfullstack.R
import com.example.calorietrackerfullstack.data.model.UserAuth
import com.example.calorietrackerfullstack.databinding.FragmentRegisterBinding
import com.example.calorietrackerfullstack.utils.DataResult.*
import com.example.calorietrackerfullstack.utils.Prefs
import com.example.calorietrackerfullstack.utils.isEmailValid
import com.example.calorietrackerfullstack.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    var prefs: Prefs? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = Prefs(context!!)
        binding.signInTextView.setOnClickListener { navLogin() }
        setUpRegister()
        attachRegisterAuth()
        backNav()
        attachProgressBar()
    }

    private fun setUpRegister() {
        binding.apply {
            regButton.setOnClickListener {
                Log.d(
                    "RegisterFragment",
                    "Sign up clicked"
                )

                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                val userName = userNameEdit.text.toString().trim()
                val confirmUserName = confirmUserNameEdit.text.toString().trim()
                val password = editUserPassword.text.toString()
                val isUsernameEqual = userName == confirmUserName
                if (!isUsernameEqual)
                    confirmUserNameContainer.error = getString(R.string.confirm_username_equal)

                val isUsernameValid = validateUsername(userName)
                if (!isUsernameValid) userNameContainer.error = getString(R.string.invalid_email)

                val isPasswordValid = validateUserPassword(password)
                if (!isPasswordValid) { userPasswordInputContainer.error = getString(R.string.less_password) }

                if (isUsernameValid && isPasswordValid && isUsernameEqual) {
                    val userCredential = UserAuth(userName, password)
                    viewModel.setUpRegister(userCredential)
                    userNameContainer.error = null
                    confirmUserNameContainer.error = null
                    userPasswordInputContainer.error = null
                } else {
                    Toast.makeText(
                        context,
                        "Either Username or Password input is not valid",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                imm.hideSoftInputFromWindow(it.windowToken, 0)
                it.clearFocus()
            }
        }
    }

    private fun validateUsername(username: String): Boolean {
        var valid = false
        if (username.isNotEmpty() && !username.startsWith(" ") &&
            username.contains('@') || username.contains('.')
        ) {
            valid = username.isEmailValid()
        } else if (username.isNotEmpty() && username.length >= 6 && !username.startsWith(" ")) {
            valid = true
        }
        return valid
    }

    private fun validateUserPassword(password: String): Boolean {
        var valid = false
        if (password.isNotEmpty() && password.length >= 6 && !password.startsWith(" ")) {
            valid = true
        }
        return valid
    }

    private fun attachRegisterAuth() {
        viewModel.authUser.observe(viewLifecycleOwner, Observer { response ->
            response?.let { result ->
                when (result) {
                    is GenericError -> {
                        Log.d(
                            "RegisterFragment",
                            "code- ${result.code} error message- ${result.errorMessages}"
                        )
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "GenericError code- ${result.code} error message- ${result.errorMessages}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is NetworkError -> {
                        Log.d("RegisterFragment", "network error message- ${result.networkError}")
                        Toast.makeText(
                            context,
                            "Network error message- ${result.networkError}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Success -> {
                        if (result.value.success) {
                            prefs!!.isLoggedIn = true
                            prefs!!.isAdminPref = result.value.data.userType!!
                            prefs!!.userIdPref = result.value.data.userId
                            navFoodList()
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

    private fun backNav() {
        binding.textBack.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun navFoodList() {
        findNavController().navigate(R.id.action_registerFragment_to_foodListFragment)
    }

    private fun navLogin() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }
}