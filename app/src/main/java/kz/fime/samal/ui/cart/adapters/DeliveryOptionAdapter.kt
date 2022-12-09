package kz.fime.samal.ui.cart.adapters

import kz.fime.samal.databinding.ItemCartOptionBinding
import kz.fime.samal.databinding.ItemCartProductBinding
import kz.fime.samal.databinding.ItemPaymentOptionBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.InnerItem
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.loadUrl

class DeliveryOptionAdapter(
    private val onItemClick: (item: Item) -> Unit
): BindingRvAdapter<Item, ItemPaymentOptionBinding>(ItemPaymentOptionBinding::inflate) {

    override fun bind(item: Item, binding: ItemPaymentOptionBinding) {
        binding.run {
            tvDescription.text = item.getOrNull("name", "")
            vgContainer.setOnClickListener { onItemClick.invoke(item) }
        }
    }

}