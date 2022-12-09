package kz.fime.samal.ui.home.adapters

import kz.fime.samal.data.models.Product
import kz.fime.samal.databinding.ItemProduct2Binding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.loadUrl

class ProductsAdapter constructor(
    private val onItemClick: (item: Product) -> Unit
): BindingRvAdapter<Product, ItemProduct2Binding>(ItemProduct2Binding::inflate) {

    override fun bind(item: Product, binding: ItemProduct2Binding) {
        binding.run {

            tvTitle.text = item.name
            tvPrice.text = item.price.toString() + "₸"
            iv.loadUrl(item.image)
            rb.rating = item.rating ?: 0f

            container.setOnClickListener { onItemClick.invoke(item) }
        }
    }

}