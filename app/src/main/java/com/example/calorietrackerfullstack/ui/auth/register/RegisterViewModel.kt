package com.example.calorietrackerfullstack.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietrackerfullstack.concurrency.IAppDispatchers
import com.example.calorietrackerfullstack.data.model.AuthResponse
import com.example.calorietrackerfullstack.data.model.UserAuth
import com.example.calorietrackerfullstack.data.repository.auth.IAuthRepository
import com.example.calorietrackerfullstack.utils.DataResult
import com.example.calorietrackerfullstack.utils.DataResult.GenericError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val appDispatchers: IAppDispatchers,
    private val repository: IAuthRepository,
) : ViewModel() {


    private val _authUser: MutableLiveData<DataResult<AuthResponse>?> = MutableLiveData()
    val authUser: LiveData<DataResult<AuthResponse>?> = _authUser
    val showProgress: MutableLiveData<Boolean> = MutableLiveData()

    fun setUpRegister(userAuth: UserAuth) = viewModelScope.launch {
        showProgress.value = true
        val result = withContext(appDispatchers.io) { repository.register(userAuth) }

        when (result) {
            is GenericError -> {
                showProgress.value = false
                _authUser.value = GenericError(result.code, result.errorMessages)
            }
            is DataResult.NetworkError -> {
                showProgress.value = false
                _authUser.value = DataResult.NetworkError(result.networkError)
            }
            is DataResult.Success -> {
                showProgress.value = false
                _authUser.value = DataResult.Success(result.value!!)
            }
        }
    }

}