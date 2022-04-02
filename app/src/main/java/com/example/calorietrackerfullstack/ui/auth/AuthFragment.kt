package com.example.calorietrackerfullstack.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.calorietrackerfullstack.R
import com.example.calorietrackerfullstack.databinding.FragmentAuthBinding
import com.example.calorietrackerfullstack.utils.Prefs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private lateinit var binding: FragmentAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkIfUserLoggedIn()
        binding.loginBtn.setOnClickListener { navLogin() }
        binding.textSignup.setOnClickListener { navSignup() }
    }

    private fun checkIfUserLoggedIn() {
        if (Prefs(context!!).isLoggedIn) {
            navFoodList()
        }
    }

    private fun navFoodList() {
        findNavController().navigate(R.id.action_authFragment_to_foodListFragment)
    }

    private fun navLogin() {
        findNavController().navigate(R.id.action_authFragment_to_loginFragment)
    }

    private fun navSignup() {
        findNavController().navigate(R.id.action_authFragment_to_registerFragment)
    }
}