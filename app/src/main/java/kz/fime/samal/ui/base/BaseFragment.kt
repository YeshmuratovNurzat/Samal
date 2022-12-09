package kz.fime.samal.ui.base

import android.util.Log
import androidx.fragment.app.Fragment
import kz.fime.samal.data.repositories.*

fun handleException(exception: Exception?) {
    when (exception) {
        is ApiException -> {
            if (exception.responseError != null) {
            }
        }
        is TimeoutException -> {
        }
        is NetworkException -> {
        }
        is UnknownException -> {
            Log.e("handleException", "UnknownException ${exception.t.message ?: ""}")
        }
        is TokenException -> {
            Log.e("HANDLE EXCEPTION", "Token exception")
        }
        is EmptyException -> {
            Log.e("EMPTY EXCEPTION", "STATUS 204")
        }
    }
}