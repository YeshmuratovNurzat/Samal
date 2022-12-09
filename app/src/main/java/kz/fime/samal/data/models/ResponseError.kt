package kz.fime.samal.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class ResponseError(
    val timestamp: Date?,
    val status: String? = null,
    val error: String?,
    val exception: String?,
    val message: String?,
    val path: String?,
    val supported: Boolean?,

    val code: String?,
    val url: String?,
    val text: String,
    val errors: ArrayList<String>?
) : Parcelable