package kz.fime.samal.ui.cart.adapters

import kz.fime.samal.databinding.ItemCartProductBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.InnerItem
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.loadUrl

class CartProductsAdapter constructor(
    private val onProductClick: (item: InnerItem) -> Unit,
    private val onIncrement: (item: InnerItem, adapter: CartProductsAdapter) -> Unit,
    private val onDecrement: (item: InnerItem, adapter: CartProductsAdapter) -> Unit
): BindingRvAdapter<InnerItem, ItemCartProductBinding>(ItemCartProductBinding::inflate) {

    override fun bind(item: InnerItem, binding: ItemCartProductBinding) {
        binding.run {

            tvTitle.text = item.getOrNull("name", "")
            tvCount.text= item.getOrNull<Int>("count", safe = true).toString()
            val count = item.getOrNull("count", 0)!!
            val price = count * item.getOrNull("price", 0)!!
            tvDescription.text = ""
            tvPrice.text = "$price₸"
            iv.loadUrl(item.getOrNull("images", listOf<InnerItem>())?.firstOrNull()?.getOrNull("link", ""))
            ibIncrement.setOnClickListener { onIncrement.invoke(item, this@CartProductsAdapter) }
            ibDecrement.setOnClickListener { onDecrement.invoke(item, this@CartProductsAdapter) }
        }
    }

}