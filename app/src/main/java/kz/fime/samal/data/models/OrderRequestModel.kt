package kz.fime.samal.data.models

import kz.fime.samal.data.models.order_detail.Order

data class OrderRequestModel(
    val status: String?,
    val message: String?,
    val data: List<Order>?,
    val links: HashMap<String, Any>,
    val meta: HashMap<String, Any>
) {
}