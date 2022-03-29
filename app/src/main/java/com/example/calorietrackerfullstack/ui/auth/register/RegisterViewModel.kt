package com.example.calorietrackerfullstack.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietrackerfullstack.concurrency.IAppDispatchers
import com.example.calorietrackerfullstack.data.model.AuthResponse
import com.example.calorietrackerfullstack.data.model.UserAuth
import com.example.calorietrackerfullstack.data.repository.auth.IAuthRepository
import com.example.calorietrackerfullstack.utils.DataResponseStatus
import com.example.calorietrackerfullstack.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val appDispatchers: IAppDispatchers,
    private val repository: IAuthRepository,
) : ViewModel() {


    private val _authUserSuccess: MutableLiveData<AuthResponse?> = MutableLiveData()
    val authUserSuccess: LiveData<AuthResponse?> = _authUserSuccess
    private val _dataResponseStatus = MutableLiveData<DataResponseStatus>()
    val dataResponseStatus: LiveData<DataResponseStatus> = _dataResponseStatus

    fun setUpRegister(userAuth: UserAuth) = viewModelScope.launch {
        _dataResponseStatus.value = DataResponseStatus.LOADING

        val result = withContext(appDispatchers.io) { repository.register(userAuth) }

        when(result){
            is DataResult.GenericError -> _dataResponseStatus.value = DataResponseStatus.ERROR
            is DataResult.NetworkError -> _dataResponseStatus.value = DataResponseStatus.ERROR
            is DataResult.Success -> {
                _authUserSuccess.value = result.value
                _dataResponseStatus.value = DataResponseStatus.DONE
            }
        }
    }

}