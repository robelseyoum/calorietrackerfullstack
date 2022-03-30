package com.example.calorietrackerfullstack.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.calorietrackerfullstack.TestDispatchers
import com.example.calorietrackerfullstack.data.model.AuthResponse
import com.example.calorietrackerfullstack.data.model.User
import com.example.calorietrackerfullstack.data.model.UserAuth
import com.example.calorietrackerfullstack.data.repository.auth.AuthRepository
import com.example.calorietrackerfullstack.ui.auth.login.LoginViewModel
import com.example.calorietrackerfullstack.utils.DataResponseStatus
import com.example.calorietrackerfullstack.utils.DataResult
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.MockKAnnotations
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class LoginViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel
    private val repository = mock<AuthRepository>()

    private val userCredential = UserAuth(userName = "testuser", userPassword = "testuser3")
    private val user = User(
        userId = 1,
        userName = "testuser",
        userPassword = "testuser3",
        userType = 0
    )

    private val authResponse = AuthResponse(
        data = user,
        success = true
    )

    @Before
    fun setUpTest() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)
        viewModel = LoginViewModel(TestDispatchers, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    /***
     * Test cases:
     * Login Successfully
     */
    @Test
    fun `when login it should return the login success as result`() = runTest {
        val result = mock<AuthResponse>()
        whenever(repository.login(userCredential)).thenReturn(DataResult.Success(result))
        viewModel.logInUser(userCredential)
        assertEquals(result, viewModel.currentUser.value)
    }

    /***
     * Test cases:
     * Login and throws IOException as NetworkError
     */
    @Test
    fun `when login it should return throws IOException then it should emit the result as NetworkError`() =
        runTest {
            whenever(repository.login(userCredential)).thenReturn(DataResult.NetworkError)
            viewModel.logInUser(userCredential)
            assertEquals(DataResponseStatus.ERROR, viewModel.dataResponseStatus.value)
        }


    /***
     * Test cases:
     * Login and throws throws IOException as GenericError
     */
    @Test
    fun `when Login food it should return throws IOException then it should emit the result as GenericError`() =
        runTest {
            whenever(repository.login(userCredential)).thenReturn(
                DataResult.GenericError(
                    433,
                    "error message"
                )
            )
            viewModel.logInUser(userCredential)
            assertEquals(DataResponseStatus.ERROR, viewModel.dataResponseStatus.value)
        }
}