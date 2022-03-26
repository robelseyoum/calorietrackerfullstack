package com.example.calorietrackerfullstack.ui.auth.register

import androidx.lifecycle.ViewModel
import com.example.calorietrackerfullstack.concurrency.IAppDispatchers
import com.example.calorietrackerfullstack.data.repository.auth.IAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val appDispatchers: IAppDispatchers,
    private val repository: IAuthRepository,
) : ViewModel() {



}