package kz.fime.samal.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Widget(
    val slug: String?,
    val name: String?,
    val products: List<Product>?
): Parcelable
