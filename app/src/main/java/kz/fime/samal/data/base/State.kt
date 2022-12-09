package kz.fime.samal.data.base

import kz.fime.samal.api.ApiResponse

sealed class State<out T> {
    data class Success<out T>(val code: Int? = null, val message: String? = null, val result: T? = null): State<T>()
    data class ServerError(val code: Int, val message: String? = null): State<Nothing>()
    object NetworkError: State<Nothing>()
    object UnknownError: State<Nothing>()
    object Loading: State<Nothing>()
}