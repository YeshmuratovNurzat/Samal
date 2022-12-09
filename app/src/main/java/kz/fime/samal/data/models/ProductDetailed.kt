package kz.fime.samal.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kz.fime.samal.utils.extensions.Item

data class ProductDetailed(
    val shop_uuid: String?,
    val name: String?,
    val description: String?,
    val categories: List<Item>,
    val price: Item?,
    val images: List<Image>?,
    val product_variants: List<ProductVariant>?,
    val options: List<ProductOptions>
)

@Parcelize
data class ProductVariant(
    val product_variant_id: Int,
    val price: String
) : Parcelable

data class ProductOptions(
    val name: String?,
    val slug: String?,
    val option_id: Int?,
    var option_selected: Boolean?,
    val data: List<OptionData>?
)

data class OptionData(
    val value_id: Int?,
    val value: String?,
    var selected: Boolean?,
    val product_variant_id: List<Int>?
)