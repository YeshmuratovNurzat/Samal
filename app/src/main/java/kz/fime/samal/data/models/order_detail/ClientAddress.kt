package kz.fime.samal.data.models.order_detail

data class ClientAddress(
    val address_id: Int,
    val address_slug: String,
    val apartment: String,
    val city: City,
    val default: Boolean,
    val house_number: String,
    val name: String,
    val street: String
)