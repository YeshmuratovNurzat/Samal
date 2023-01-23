package kz.fime.samal.ui.cart.adapters

import kz.fime.samal.databinding.ItemPickUpBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull

class PickUpAdapter(private val onItemClick: (item: Item) -> Unit)
    : BindingRvAdapter<Item, ItemPickUpBinding>(ItemPickUpBinding::inflate) {

    override fun bind(item: Item, binding: ItemPickUpBinding) {
        binding.run {
            tvTitle.text = item.getOrNull("name","")
            tvSubtitle.text = item.getOrNull("address","")
            vgContainer.setOnClickListener { onItemClick.invoke(item) }
        }
    }
}