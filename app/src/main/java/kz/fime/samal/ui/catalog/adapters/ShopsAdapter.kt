package kz.fime.samal.ui.catalog.adapters


import kz.fime.samal.data.models.Widget
import kz.fime.samal.databinding.ItemCategoryBinding
import kz.fime.samal.databinding.ItemShopBinding
import kz.fime.samal.databinding.ItemWidgetBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.loadUrl

class ShopsAdapter constructor(
    private val onItemClick: (item: Item) -> Unit
): BindingRvAdapter<Item, ItemShopBinding>(ItemShopBinding::inflate) {

    override fun bind(item: Item, binding: ItemShopBinding) {
        binding.run {
            tvTitle.text = item["name"]?.toString()
            tvSubtitle.text = item["address"]?.toString()
            iv.loadUrl(item["logo"]?.toString())
            root.setOnClickListener { onItemClick.invoke(item) }
        }
    }

}