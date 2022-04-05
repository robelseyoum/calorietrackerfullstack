package com.example.calorietrackerfullstack.ui.auth.login

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
//                Toast.makeText(context, "loginBtn", Toast.LENGTH_SHORT).show()
                val email = logEmail.text.toString()
                val password = logPassword.text.toString()
                val userCredential = UserAuth(email, password)

//                Log.d("loginBtn", email)

                if (
                    email.isNotEmpty() &&
                    password.isNotEmpty()
                ) {
                    if (password.length < 6 && !email.isEmailValid()) {
                        textInputPassword.error = getString(R.string.less_password)
                        textInputEmail.error = getString(R.string.invalid_email)
                    } else {
                        viewModel.logInUser(userCredential)
                        textInputPassword.error = null
                        textInputEmail.error = null
                    }
                }
            }
        }
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
                            "GenericError code- ${result.code} error message- ${result.errorMessages}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is NetworkError -> {
                        Log.d("LoginFragment", "network error message- ${result.networkError}")
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

    private fun navFoodList() {
        findNavController().navigate(R.id.action_to_foodListFragment)
    }

    private fun navSignup() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }
}