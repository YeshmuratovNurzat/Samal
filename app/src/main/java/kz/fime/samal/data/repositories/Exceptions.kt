package kz.fime.samal.data.repositories

import kz.fime.samal.data.models.ResponseError

data class ApiException(val responseError: ResponseError?) : Exception()

class TimeoutException(val t: Throwable) : Exception()

class NetworkException(val t: Throwable) : Exception()

class UnknownException(val t: Throwable) : Exception()

data class TokenException(val responseError: ResponseError?) : Exception()

data class LogoutException(val responseError: ResponseError?) : Exception()

class EmptyException() : Exception()