package kz.fime.samal.ui.catalog.shop

import kz.fime.samal.databinding.ItemCategoryBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.loadUrl

class ShopAdapter constructor(
    private val onItemClick: (item: Item) -> Unit
): BindingRvAdapter<Item, ItemCategoryBinding>(ItemCategoryBinding::inflate) {

    override fun bind(item: Item, binding: ItemCategoryBinding) {
        binding.run {
            tvTitle.text = item.getOrNull("name","")
            iv.loadUrl(item["image"]?.toString())
            root.setOnClickListener { onItemClick.invoke(item) }
        }
    }
}