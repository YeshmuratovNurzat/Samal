package kz.fime.samal.ui.profile.favorites

import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull

data class FavItem(
    val shopId: String,
    val productSlug: String
)

object Favorites {

    private var favorites = mutableListOf<FavItem>()

    fun updateList(list: List<Item>?){
        val items = mutableListOf<FavItem>()
        list?.forEach {
            items.add(FavItem(it.getOrNull("shop_uuid", "")!!, it.getOrNull("product_slug", "")!!))
        }
        favorites = items
    }

    fun isFavorite(shopId: String, productSlug: String): Boolean {
        val item = FavItem(shopId, productSlug)
        return favorites.contains(item)
    }
}