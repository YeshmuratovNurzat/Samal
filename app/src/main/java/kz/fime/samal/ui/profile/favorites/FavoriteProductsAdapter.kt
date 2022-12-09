package kz.fime.samal.ui.profile.favorites

import kz.fime.samal.databinding.ItemFavoriteBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.InnerItem
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.loadUrl

class FavoriteProductsAdapter constructor(
    private val onProductClick: (item: Item) -> Unit,
    private val onFavoriteToggle: (item: Item, checked: Boolean) -> Unit
): BindingRvAdapter<Item, ItemFavoriteBinding>(ItemFavoriteBinding::inflate) {

    override fun bind(item: Item, binding: ItemFavoriteBinding) {
        binding.run {

            tvTitle.text = item.getOrNull("name", "")
            rb.rating = item.getOrNull<String>("rating")?.toFloat() ?: 0f
            tvPrice.text = item.getOrNull("price", "")
            cb.isChecked = item.getOrNull("favourite", false, safe = true)!!
            cb.setOnCheckedChangeListener { _, isChecked ->
                onFavoriteToggle.invoke(item, isChecked)
            }
            ibCart.setOnClickListener { onProductClick.invoke(item) }
            iv.loadUrl(item.getOrNull("images", listOf<InnerItem>())?.firstOrNull()?.getOrNull("link", ""))
        }
    }

}