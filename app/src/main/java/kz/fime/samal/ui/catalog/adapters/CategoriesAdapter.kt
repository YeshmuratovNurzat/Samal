package kz.fime.samal.ui.catalog.adapters


import kz.fime.samal.data.models.Widget
import kz.fime.samal.databinding.ItemCategoryBinding
import kz.fime.samal.databinding.ItemCategoryGridBinding
import kz.fime.samal.databinding.ItemWidgetBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.loadUrl

class CategoriesAdapter constructor(
    private val onItemClick: (item: Item) -> Unit
): BindingRvAdapter<Item, ItemCategoryGridBinding>(ItemCategoryGridBinding::inflate) {

    override fun bind(item: Item, binding: ItemCategoryGridBinding) {
        binding.run {
            iv.loadUrl(item["image"]?.toString())
            root.setOnClickListener { onItemClick.invoke(item) }
        }
    }

}