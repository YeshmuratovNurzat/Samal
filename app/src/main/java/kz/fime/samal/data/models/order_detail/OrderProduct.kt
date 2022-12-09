package kz.fime.samal.data.models.order_detail

data class OrderProduct(
    val barcode: Any,
    val brand: Any,
    val cost: String,
    val count: String,
    val depth: String,
    val description: String,
    val height: String,
    val id: Int,
    val images: List<Image>,
    val is_adult: Boolean,
    val is_recipe: Boolean,
    val name: String,
    val old_price: String,
    val options: Any,
    val price: String,
    val product_id: Int,
    val product_variant_id: Int,
    val properties: List<Property>,
    val sku: Any,
    val unit: Unit,
    val uuid: String,
    val weight: String,
    val width: String
)