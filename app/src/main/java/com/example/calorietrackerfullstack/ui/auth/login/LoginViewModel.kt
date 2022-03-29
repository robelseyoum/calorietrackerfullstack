package com.example.calorietrackerfullstack.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietrackerfullstack.concurrency.IAppDispatchers
import com.example.calorietrackerfullstack.data.model.AuthResponse
import com.example.calorietrackerfullstack.data.model.UserAuth
import com.example.calorietrackerfullstack.data.repository.auth.IAuthRepository
import com.example.calorietrackerfullstack.utils.DataResponseStatus
import com.example.calorietrackerfullstack.utils.DataResponseStatus.*
import com.example.calorietrackerfullstack.utils.DataResult.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val appDispatchers: IAppDispatchers,
    private val repository: IAuthRepository,
) : ViewModel() {

    private val _user: MutableLiveData<AuthResponse?> = MutableLiveData()
    val currentUser: LiveData<AuthResponse?>  = _user
    private val _dataResponseStatus = MutableLiveData<DataResponseStatus>()
    val dataResponseStatus: LiveData<DataResponseStatus> = _dataResponseStatus

    fun logInUser(userAuth: UserAuth) = viewModelScope.launch {
        _dataResponseStatus.value = LOADING

        val result = withContext(appDispatchers.io) { repository.login(userAuth) }

        when(result){
            is GenericError -> _dataResponseStatus.value = ERROR
            is NetworkError -> _dataResponseStatus.value = ERROR
            is Success -> {
                _user.value = result.value
                _dataResponseStatus.value = DONE
            }
        }
    }
}