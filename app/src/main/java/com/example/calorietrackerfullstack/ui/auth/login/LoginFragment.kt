package com.example.calorietrackerfullstack.ui.auth.login

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.calorietrackerfullstack.R
import com.example.calorietrackerfullstack.data.model.UserAuth
import com.example.calorietrackerfullstack.databinding.FragmentLoginBinding
import com.example.calorietrackerfullstack.utils.DataResult.*
import com.example.calorietrackerfullstack.utils.Prefs
import com.example.calorietrackerfullstack.utils.isEmailValid
import com.example.calorietrackerfullstack.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    var prefs: Prefs? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = Prefs(context!!)
        binding.textSignup.setOnClickListener { navSignup() }
        setUpLogin()
        attachLoginAuth()
        attachProgressBar()
    }

    private fun setUpLogin() {
        binding.apply {
            loginBtn.setOnClickListener {

                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                //Toast.makeText(context, "loginBtn", Toast.LENGTH_SHORT).show()
                var isEmailValid = false
                lateinit var errorMessage: String

                val userName = userNameEdit.text.toString().trim()
                val password = passwordEdit.text.toString()

                val isUsernameValid = validateUsername(userName)
                if (!isUsernameValid) userNameContainer.error = getString(R.string.invalid_email)

                val isPasswordValid = validateUserPassword(password)
                if (!isPasswordValid) {
                    passwordContainer.error = getString(R.string.less_password)
                }

                if (isUsernameValid && isPasswordValid) {
                    val userCredential = UserAuth(userName, password)
                    viewModel.logInUser(userCredential)
                    userNameContainer.error = null
                    passwordContainer.error = null
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

    private fun attachLoginAuth() {
        viewModel.currentUser.observe(viewLifecycleOwner, Observer { response ->
            response?.let { result ->
                when (result) {
                    is GenericError -> {
                        Log.d(
                            "LoginFragment",
                            "code- ${result.code} error message- ${result.errorMessages}"
                        )
                        Toast.makeText(
                            context,
                            "Username or Password is Wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is NetworkError -> {
                        Log.d("LoginFragment", "network error message- ${result.networkError}")
                        Toast.makeText(
                            context,
                            "No Internet - ${result.networkError}",
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

    private fun navFoodList() {
        findNavController().navigate(R.id.action_to_foodListFragment)
    }

    private fun navSignup() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }
}