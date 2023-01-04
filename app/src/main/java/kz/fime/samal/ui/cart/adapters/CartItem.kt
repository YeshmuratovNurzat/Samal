package kz.fime.samal.ui.cart.adapters

data class CartItem(
    val count: Int = 0,
    val title: String?,
    val price: Int = 0,
    val installment: Boolean = true,
    var priceWithPercent: Float = 0f
)
