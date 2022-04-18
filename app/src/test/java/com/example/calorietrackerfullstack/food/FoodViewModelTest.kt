package com.example.calorietrackerfullstack.food

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.calorietrackerfullstack.TestDispatchers
import com.example.calorietrackerfullstack.data.model.FoodResponse
import com.example.calorietrackerfullstack.data.repository.foods.FoodRepository
import com.example.calorietrackerfullstack.ui.main.food.FoodViewModel
import com.example.calorietrackerfullstack.utils.DataResult
import com.example.calorietrackerfullstack.utils.stringToRequestBody
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.MockKAnnotations
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class FoodViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val repository = mock<FoodRepository>()
    private lateinit var viewModel: FoodViewModel

    private val id = "1"
    private var mapData: HashMap<String, RequestBody> =
        hashMapOf(
            "foodName" to "Steak".stringToRequestBody(),
            "calorie" to "3.4".stringToRequestBody(),
            "date" to "12/03/2022".stringToRequestBody(),
            "time" to "03:20".stringToRequestBody(),
            "userId" to "1".stringToRequestBody(),
        )

    // Context of the app under test.
    private val multipartBody: MultipartBody.Part? = null // can be null since just a test


    @Before
    fun setUpTest() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)
        viewModel = FoodViewModel(TestDispatchers, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /***
     * Test cases:
     * Add Food Successfully
     */
    @Test
    fun `when adding food and it should return the edit success as result`() = runTest {
        val result = mock<FoodResponse>()
        whenever(repository.addFood(mapData, multipartBody)).thenReturn(DataResult.Success(result))
        viewModel.addFood(mapData, multipartBody)
        Assert.assertEquals(DataResult.Success(result), viewModel.addFoodStatus.value)
    }

    /***
     * Test cases:
     * Adding Food throws IOException as NetworkError
     */
    @Test
    fun `when adding food it should return throws IOException then it should emit the result as NetworkError`() =
        runTest {
            whenever(repository.addFood(mapData, multipartBody)).thenReturn(
                DataResult.NetworkError(
                    "Network error"
                )
            )
            viewModel.addFood(mapData, multipartBody)
            assertEquals(DataResult.NetworkError("Network error"), viewModel.addFoodStatus.value)
        }

    /***
     * Test cases:
     * Adding Food throws IOException as GenericError
     */
    @Test
    fun `when adding food it should return throws IOException then it should emit the result as GenericError`() =
        runTest {
            whenever(repository.addFood(mapData, multipartBody)).thenReturn(
                DataResult.GenericError(
                    433,
                    "error message"
                )
            )
            viewModel.addFood(mapData, multipartBody)
            assertEquals(
                DataResult.GenericError(
                    433,
                    "error message"
                ), viewModel.addFoodStatus.value
            )
        }


    /***
     * Test cases:
     * Edit Food Successfully
     */
    @Test
    fun `when editing foods and it should return the edit success as result`() = runTest {
        val result = mock<FoodResponse>()
        whenever(repository.editFood(id, mapData, multipartBody)).thenReturn(
            DataResult.Success(
                result
            )
        )
        viewModel.editFood(id, mapData, multipartBody)
        Assert.assertEquals(DataResult.Success(result), viewModel.updateFoodStatus.value)
    }

    /***
     * Test cases:
     * Editing Food throws IOException as NetworkError
     */
    @Test
    fun `when editing foods it should return throws IOException then it should emit the result as NetworkError`() =
        runTest {
            whenever(repository.editFood(id, mapData, multipartBody)).thenReturn(
                DataResult.NetworkError(
                    "Network error"
                )
            )
            viewModel.editFood(id, mapData, multipartBody)
            assertEquals(DataResult.NetworkError("Network error"), viewModel.updateFoodStatus.value)
        }


    /***
     * Test cases:
     * Editing Food throws IOException as GenericError
     */
    @Test
    fun `when editing foods it should return throws IOException then it should emit the result as GenericError`() =
        runTest {
            whenever(repository.editFood(id, mapData, multipartBody)).thenReturn(
                DataResult.GenericError(
                    433,
                    "error message"
                )
            )
            viewModel.editFood(id, mapData, multipartBody)
            assertEquals(
                DataResult.GenericError(
                    433,
                    "error message"
                ), viewModel.updateFoodStatus.value
            )
        }

}