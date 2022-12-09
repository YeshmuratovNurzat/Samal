package kz.fime.samal.data.models.order_detail

data class Order(
    val created_at: String,
    val delivery: Delivery,
    val delivery_sum: String,
    val number: Int,
    val order_uuid: String,
    val paid: Boolean,
    val payment: Payment,
    val status: Status,
    val total_cost: String
)