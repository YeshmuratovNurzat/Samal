package kz.fime.samal.data.repositories

import android.util.Log
import com.google.gson.Gson
import kz.fime.samal.data.models.ResponseError
import kz.fime.samal.data.models.custom.Resource
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

open class BaseRepository(val gson: Gson) {
    protected fun getError(e: Throwable) =
        Resource.error(msg = null, data = null, exception = handleError(e))

    private fun handleError(e: Throwable): Exception = when (e) {
        is HttpException -> {
            try {
                val response = e.response()

                if (response != null) {
                    if (response.code() == 401) {
                        val responseError: ResponseError? =
                            gson.fromJson(response.errorBody()?.string(), ResponseError::class.java)
                        if (responseError?.status != null && responseError.status == "error" && responseError.message == "Unauthorized") {
                            LogoutException(responseError)
                        } else {
                            TokenException(responseError)
                        }
                    } else {
                        val responseError: ResponseError? =
                            gson.fromJson(
                                response.errorBody()?.string(),
                                ResponseError::class.java
                            )
                        ApiException(responseError)
                    }
                } else {
                    if (response?.code() == 204) {
                        EmptyException()
                    } else {
                        Log.d("ERROR", e.localizedMessage.toString())
                        UnknownException(e)
                    }
                }
            } catch (e1: Exception) {
                UnknownException(e)
            }
        }
        is SocketTimeoutException -> TimeoutException(e)
        is IOException -> NetworkException(e)
        is EmptyException -> EmptyException()
        else -> UnknownException(e)
    }

}