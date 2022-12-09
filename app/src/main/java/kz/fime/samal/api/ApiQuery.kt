package kz.fime.samal.api

import java.util.ArrayList

data class ApiQuery<T> (
        val count: Int?,
        val next: String?,
        val previous: String?,
        val results: ArrayList<T>?
)