package com.example.calorietrackerfullstack.ui.auth.login

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
import com.example.calorietrackerfullstack.databinding.FragmentLoginBinding
import com.example.calorietrackerfullstack.utils.DataResponseStatus.*
import com.example.calorietrackerfullstack.utils.Prefs
import com.example.calorietrackerfullstack.utils.isEmailValid
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
                    LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    ERROR -> {
                        binding.progressBar.visibility = View.GONE
                    }
                    DONE -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun navFoodList() {
        findNavController().navigate(R.id.action_to_foodListFragment)
    }

    private fun navSignup() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }
}