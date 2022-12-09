package kz.fime.samal.data.models

data class Banner (
    val id: Int?,
    val type: String?,
    val category_slug: String?,
    val shop_uuid: String?,
    val product_slug: String?,
    val title: String?,
    val subtitle: String?,
    val price: Int?,
    val image: String?
)
