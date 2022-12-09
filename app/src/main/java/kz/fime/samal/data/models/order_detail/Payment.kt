package kz.fime.samal.data.models.order_detail

data class Payment(
    val name: String,
    val payment_id: Int,
    val slug: String
)