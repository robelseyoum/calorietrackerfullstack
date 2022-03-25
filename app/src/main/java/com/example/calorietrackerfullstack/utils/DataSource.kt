package com.example.calorietrackerfullstack.utils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

sealed class DataResult<out T> {
    data class Success<out T>(val value: T): DataResult<T>()
    data class GenericError(val code: Int? = null, val errorMessages: String? = null): DataResult<Nothing>()
    object NetworkError : DataResult<Nothing>()
}

enum class DataResponseStatus { LOADING, ERROR, DONE }

suspend fun <T> safeDataResult(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T? //It takes a function as input and in order to execute the function you have to call cacheCall.invoke() on it
): DataResult<T?> {

    return withContext(dispatcher) {
        try {
//            throws TimeoutCancellationException
            DataResult.Success(apiCall.invoke()) //if it is success then it will pass the return type of the suspend function T (type of T) this -(apiCall: suspend () -> T? )
        }
        catch (throwable: Throwable) {

            throwable.printStackTrace()

            when (throwable) {

                is TimeoutCancellationException -> {
                    val code = 408 //timeout error code
                    DataResult.GenericError(code, Constants.NETWORK_ERROR_TIMEOUT)
                }

                is IOException -> {
                    DataResult.NetworkError
                }

                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse =
                        convertErrorBody(
                            throwable
                        )
                    DataResult.GenericError(
                        code,
                        errorResponse
                    )
                }

                else -> {
                    DataResult.GenericError(
                        null,
                        Constants.NETWORK_ERROR_UNKNOWN
                    )
                }
            }
        }
    }
}

fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        Constants.ERROR_UNKNOWN
    }
}