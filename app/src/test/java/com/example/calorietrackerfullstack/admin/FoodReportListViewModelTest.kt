package com.example.calorietrackerfullstack.admin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.calorietrackerfullstack.TestDispatchers
import com.example.calorietrackerfullstack.data.model.FoodResponse
import com.example.calorietrackerfullstack.data.model.FoodsResponse
import com.example.calorietrackerfullstack.data.repository.foods.FoodRepository
import com.example.calorietrackerfullstack.ui.main.admin.FoodReportListViewModel
import com.example.calorietrackerfullstack.utils.DataResult
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.MockKAnnotations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class FoodReportListViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: FoodReportListViewModel
    private val repository = mock<FoodRepository>()
    private val id = "1"


    @Before
    fun setUpTest() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)
        viewModel = FoodReportListViewModel(TestDispatchers, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /***
     * Test cases:
     * Getting all Food list Successfully
     */
    @Test
    fun `when getting all list of foods it should return the add success as result`() =
        runTest {
            val result = mock<FoodsResponse>()
            whenever(repository.getAllFoods()).thenReturn(DataResult.Success(result))
            viewModel.getAllFoods()
            Assert.assertEquals(DataResult.Success(result), viewModel.foodList.value)

        }

    /***
     * Test cases:
     * Getting all Food list throws IOException as NetworkError
     */

    @Test
    fun `when getting all list of foods it should return throws IOException then it should emit the result as NetworkError`() =
        runTest {
            whenever(repository.getAllFoods()).thenReturn(
                DataResult.NetworkError(
                    "Network error"
                )
            )
            viewModel.getAllFoods()
            junit.framework.Assert.assertEquals(
                DataResult.NetworkError("Network error"),
                viewModel.foodList.value
            )
        }

    /***
     * Test cases:
     * Getting all Food and throws throws IOException as GenericError
     */
    @Test
    fun `when getting all list of food should return throws IOException then it should emit the result as GenericError`() =
        runTest {
            whenever(repository.getAllFoods()).thenReturn(
                DataResult.GenericError(
                    433,
                    "error message"
                )
            )
            viewModel.getAllFoods()

            junit.framework.Assert.assertEquals(
                DataResult.GenericError(
                    433,
                    "error message"
                ), viewModel.foodList.value
            )
        }


    /***
     * Test cases:
     * Getting all Food list Successfully
     */
    @Test
    fun `when deleting a food should return the success as result`() =
        runTest {
            val result = mock<FoodResponse>()
            whenever(repository.deleteFood(id)).thenReturn(DataResult.Success(result))
            viewModel.deleteFood(id)
            Assert.assertEquals(DataResult.Success(result), viewModel.foodDeleteStatus.value)

        }

    /***
     * Test cases:
     * Getting all Food list throws IOException as NetworkError
     */

    @Test
    fun `when deleting a food should return throws IOException then it should emit the result as NetworkError`() =
        runTest {
            whenever(repository.deleteFood(id)).thenReturn(
                DataResult.NetworkError(
                    "Network error"
                )
            )
            viewModel.deleteFood(id)
            junit.framework.Assert.assertEquals(
                DataResult.NetworkError("Network error"),
                viewModel.foodDeleteStatus.value
            )
        }

    /***
     * Test cases:
     * Getting all Food and throws throws IOException as GenericError
     */
    @Test
    fun `when deleting a food should return throws IOException then it should emit the result as GenericError`() =
        runTest {
            whenever(repository.deleteFood(id)).thenReturn(
                DataResult.GenericError(
                    433,
                    "error message"
                )
            )
            viewModel.deleteFood(id)

            junit.framework.Assert.assertEquals(
                DataResult.GenericError(
                    433,
                    "error message"
                ), viewModel.foodDeleteStatus.value
            )
        }
}