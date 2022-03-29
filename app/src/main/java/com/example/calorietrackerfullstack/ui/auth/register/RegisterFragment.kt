package com.example.calorietrackerfullstack.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.calorietrackerfullstack.R
import com.example.calorietrackerfullstack.data.model.UserAuth
import com.example.calorietrackerfullstack.databinding.FragmentRegisterBinding
import com.example.calorietrackerfullstack.utils.DataResponseStatus
import com.example.calorietrackerfullstack.utils.Prefs
import com.example.calorietrackerfullstack.utils.isEmailValid
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
    }


    private fun setUpRegister() {
        binding.apply {
            regButton.setOnClickListener {
                val email = regEmail.text.toString().trim()
                val password = regPassword.text.toString()

                val userCredential = UserAuth(email, password)
                if (
                    email.isNotEmpty() &&
                    password.isNotEmpty()
                ) {
                    if (password.length < 6 && !email.isEmailValid()) {
                        passwordInput.error = getString(R.string.less_password)
                        emailTextInput.error = getString(R.string.invalid_email)
                    } else {
                        viewModel.setUpRegister(userCredential)
                        emailTextInput.error = null
                        passwordInput.error = null
                    }
                }
            }
        }
    }

    private fun attachRegisterAuth() {
        viewModel.authUserSuccess.observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                if (it.success) {
                    prefs!!.isLoggedIn = true
                    prefs!!.isAdminPref = it.data.userType!!
                    prefs!!.userIdPref = it.data.userId
                    navFoodList()
                }
            }
        })

        viewModel.dataResponseStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let {
                when (it) {
                    DataResponseStatus.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    DataResponseStatus.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                    }
                    DataResponseStatus.DONE -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
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