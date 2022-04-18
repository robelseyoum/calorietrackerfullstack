package com.example.calorietrackerfullstack.foodlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.net.toUri
import com.example.calorietrackerfullstack.TestDispatchers
import com.example.calorietrackerfullstack.data.model.FoodResponse
import com.example.calorietrackerfullstack.data.model.FoodsResponse
import com.example.calorietrackerfullstack.data.repository.foods.FoodRepository
import com.example.calorietrackerfullstack.ui.main.foodlist.FoodListViewModel
import com.example.calorietrackerfullstack.utils.DataResult
import com.example.calorietrackerfullstack.utils.fileToMultiPart
import com.example.calorietrackerfullstack.utils.stringToRequestBody
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.MockKAnnotations
import junit.framework.Assert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.RequestBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class FoodListViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: FoodListViewModel
    private val repository = mock<FoodRepository>()
    private val id = "1"


    @Before
    fun setUpTest() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)
        viewModel = FoodListViewModel(TestDispatchers, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    /***
     * Test cases:
     * Getting Food list Successfully
     */
    @Test
    fun `when getting list of foods it should return the add success as result`() = runTest {
        val result = mock<FoodsResponse>()
        whenever(repository.getFoods(id)).thenReturn(DataResult.Success(result))
        viewModel.getFoods(id)
        Assert.assertEquals(DataResult.Success(result), viewModel.foodList.value)
    }

    /***
     * Test cases:
     * Getting Food list throws IOException as NetworkError
     */
    @Test
    fun `when getting list of foods it should return throws IOException then it should emit the result as NetworkError`() =
        runTest {
            whenever(repository.getFoods(id)).thenReturn(DataResult.NetworkError("Network error"))
            viewModel.getFoods(id)
            Assert.assertEquals(DataResult.NetworkError("Network error"), viewModel.foodList.value)
        }


    /***
     * Test cases:
     * Editing Food and throws throws IOException as GenericError
     */
    @Test
    fun `when getting list of food should return throws IOException then it should emit the result as GenericError`() =
        runTest {
            whenever(repository.getFoods(id)).thenReturn(
                DataResult.GenericError(
                    433,
                    "error message"
                )
            )
            viewModel.getFoods(id)
            Assert.assertEquals(
                DataResult.GenericError(
                    433,
                    "error message"
                ), viewModel.foodList.value
            )
        }
}