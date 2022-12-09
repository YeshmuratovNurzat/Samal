package kz.fime.samal.ui.catalog.adapters

import com.google.gson.internal.LinkedTreeMap
import kz.fime.samal.data.models.Product
import kz.fime.samal.databinding.ItemProduct2Binding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.InnerItem
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.loadUrl

class CategoryProductsAdapter constructor(
    private val onItemClick: (item: Item) -> Unit
): BindingRvAdapter<Item, ItemProduct2Binding>(ItemProduct2Binding::inflate) {

    override fun bind(item: Item, binding: ItemProduct2Binding) {
        binding.run {

            tvTitle.text = item.getOrNull("name", "")
            tvPrice.text = item.getOrNull("min_price", "") + "₸"
            iv.loadUrl(item.getOrNull("images", listOf<InnerItem>())?.firstOrNull()?.getOrNull("link", ""))
            rb.rating = item.getOrNull<String>("rating")?.toFloat() ?: 0f

            container.setOnClickListener { onItemClick.invoke(item) }
        }
    }

}