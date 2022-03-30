package com.example.calorietrackerfullstack.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.calorietrackerfullstack.TestDispatchers
import com.example.calorietrackerfullstack.data.model.AuthResponse
import com.example.calorietrackerfullstack.data.model.User
import com.example.calorietrackerfullstack.data.model.UserAuth
import com.example.calorietrackerfullstack.data.repository.auth.AuthRepository
import com.example.calorietrackerfullstack.ui.auth.register.RegisterViewModel
import com.example.calorietrackerfullstack.utils.DataResponseStatus
import com.example.calorietrackerfullstack.utils.DataResult
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.MockKAnnotations
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class RegisterViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RegisterViewModel
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
        viewModel = RegisterViewModel(TestDispatchers, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    /***
     * Test cases:
     * register Successfully
     */
    @Test
    fun `when register it should return the register success as result`() = runTest {
        val result = mock<AuthResponse>()
        whenever(repository.register(userCredential)).thenReturn(DataResult.Success(result))
        viewModel.setUpRegister(userCredential)
        assertEquals(result, viewModel.authUserSuccess.value)
    }

    /***
     * Test cases:
     * register and throws IOException as NetworkError
     */
    @Test
    fun `when register it should return throws IOException then it should emit the result as NetworkError`() =
        runTest {
            whenever(repository.register(userCredential)).thenReturn(DataResult.NetworkError)
            viewModel.setUpRegister(userCredential)
            assertEquals(DataResponseStatus.ERROR, viewModel.dataResponseStatus.value)
        }


    /***
     * Test cases:
     * register and throws throws IOException as GenericError
     */
    @Test
    fun `when register food it should return throws IOException then it should emit the result as GenericError`() =
        runTest {
            whenever(repository.register(userCredential)).thenReturn(
                DataResult.GenericError(
                    433,
                    "error message"
                )
            )
            viewModel.setUpRegister(userCredential)
            assertEquals(DataResponseStatus.ERROR, viewModel.dataResponseStatus.value)
        }
}