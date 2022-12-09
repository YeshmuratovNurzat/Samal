package kz.fime.samal.data.base

import com.google.gson.Gson
import kotlinx.coroutines.*
import kz.fime.samal.api.ApiBuilder
import kz.fime.samal.api.ApiResponse
import kz.fime.samal.api.SamalApi
import kz.fime.samal.data.SessionManager
import kz.fime.samal.utils.extensions.getOrNull
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.lang.Exception

private const val REGULAR_REQUEST = 0
private const val REFRESH_TOKEN = 1
private const val SIGN_IN = 2

suspend fun <T> call(dispatcher: CoroutineDispatcher = Dispatchers.IO, type: Int = REGULAR_REQUEST, doOnSuccess: (T?) -> Unit = {}, apiCall: suspend () -> Response<ApiResponse<T>>?): State<T> {
    return withContext(dispatcher) {
        try {
            val res = apiCall.invoke()
            if (res is Response<*>) {
                if (res.isSuccessful) {
                    doOnSuccess.invoke(res.body()?.data)
                    State.Success(res.code(), res.message(), res.body()?.data)
                } else {
                    Timber.d("ResponseError")
                    if (res.code() == 401 && type==REGULAR_REQUEST && SessionManager.token.isNotEmpty()) {
                        parseTokenExpiration { apiCall.invoke() }
                    } else {
                        State.ServerError(res.code(), parseErrorBody(res.errorBody()))
                    }
                }
            } else {
                Timber.d("ResponseUnknownError")
                State.UnknownError
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                is IOException -> State.NetworkError
                is HttpException -> {
                    val code = throwable.code()
                    if (code == 401 && type==REGULAR_REQUEST && SessionManager.token.isNotEmpty()) {
                        parseTokenExpiration { apiCall.invoke() }
                    } else {
                        State.ServerError(code, parseErrorResponse(throwable))
                    }
                }
                else -> State.UnknownError
            }
        }
    }
}

private suspend fun<T> parseTokenExpiration(dispatcher: CoroutineDispatcher = Dispatchers.IO, apiCall: suspend () -> Response<ApiResponse<T>>?): State<T> {
    return when(val refresh = call(dispatcher, REFRESH_TOKEN) { ApiBuilder().buildService(SamalApi::class.java).refreshToken() }) {
        State.Loading -> { State.Loading }
        State.NetworkError -> { State.NetworkError }
        is State.ServerError -> {
            if (refresh.code == 401 && !SessionManager.getPhone().isNullOrEmpty() && !SessionManager.getPassword().isNullOrEmpty()) {
                parseReAuthorization { apiCall.invoke() }
            } else {
                refresh
            }
        }
        is State.Success -> {
            // TODO: Update token and refresh
            refresh.result?.getOrNull<String>("token")?.let {
                SessionManager.token = it
            }
            call { apiCall.invoke() } }
        State.UnknownError -> { State.UnknownError }
    }
}

private suspend fun<T> parseReAuthorization(dispatcher: CoroutineDispatcher = Dispatchers.IO, apiCall: suspend () -> Response<ApiResponse<T>>?): State<T> {
    return when(val refresh = call(dispatcher, SIGN_IN) { ApiBuilder().buildService(SamalApi::class.java, true).signIn(
        hashMapOf(Pair("phone", SessionManager.getPhone()!!), Pair("password", SessionManager.getPassword()!!))) }) {
        State.Loading -> { State.Loading }
        State.NetworkError -> { State.NetworkError }
        is State.ServerError -> { refresh }
        is State.Success -> {
            // TODO: Update token and refresh
            refresh.result?.getOrNull<String>("token")?.let {
                SessionManager.token = it
            }
            call { apiCall.invoke() } }
        State.UnknownError -> { State.UnknownError }
    }
}

private fun parseErrorResponse(e: HttpException): String? {
    return try {
        val errorMessage = e.response()?.errorBody()?.string()
        val response = Gson().fromJson(errorMessage, ApiResponse::class.java)
        response.message
    } catch (ignored: Exception) {
        e.message()
    }
}

private fun parseErrorBody(e: ResponseBody?): String? {
    return try {
        val errorMessage = e?.string()
        val response = Gson().fromJson(errorMessage, ApiResponse::class.java)
        response.message
    } catch (ignored: Exception) {
        e?.string()
    }
}