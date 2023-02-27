package kz.fime.samal.data.models.order_detail

import kz.fime.samal.data.models.PickUpAddress

data class OrderDetailResponse(
    val client_address: ClientAddress,
    val order: Order,
    val order_products: List<OrderProduct>,
    val pick_up_point: PickUpAddress
)