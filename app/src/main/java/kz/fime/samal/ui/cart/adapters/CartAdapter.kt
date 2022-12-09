package kz.fime.samal.ui.cart.adapters

import kz.fime.samal.databinding.ItemCartBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.InnerItem
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.loadUrl

class CartAdapter constructor(
    private val onShopClick: (item: InnerItem) -> Unit,
    private val onProductClick: (item: InnerItem) -> Unit,
    private val onIncrement: (item: InnerItem, adapter: CartProductsAdapter) -> Unit,
    private val onDecrement: (item: InnerItem, adapter: CartProductsAdapter) -> Unit
): BindingRvAdapter<Item, ItemCartBinding>(ItemCartBinding::inflate) {

    override fun bind(item: Item, binding: ItemCartBinding) {
        binding.run {
            item.getOrNull<InnerItem>("shop")?.let { shop ->
                tvTitle.text = shop.getOrNull("name", "")
                tvSubtitle.text = shop.getOrNull("address", "")
                iv.loadUrl(shop.getOrNull("logo"))
                vgShop.setOnClickListener { onShopClick.invoke(shop) }
            }

            val productsAdapter = CartProductsAdapter(onProductClick, onIncrement, onDecrement)
            rvProducts.adapter = productsAdapter
            productsAdapter.submitList(item.getOrNull<List<InnerItem>>("products"))
        }
    }

}