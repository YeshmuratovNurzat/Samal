package kz.fime.samal.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val shop_slug: String?,
    val price: Int?,
    val product_slug: String?,
    val name: String?,
    val rating: Float?,
    val people_voted: Int?,
    val image: String?
): Parcelable
