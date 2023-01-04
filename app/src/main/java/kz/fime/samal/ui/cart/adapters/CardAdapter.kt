package kz.fime.samal.ui.cart.adapters

import kz.fime.samal.R
import kz.fime.samal.databinding.ItemCardBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull

class CardAdapter(private val onItemClick: (item: Item) -> Unit)
    : BindingRvAdapter<Item, ItemCardBinding>(ItemCardBinding::inflate) {

    override fun bind(item: Item, binding: ItemCardBinding) {
        binding.run {
            cardDescription.text = "Карта •••• ${item.getOrNull("card_hash","")?.substring(12,16)}"
            iv.setImageResource(R.drawable.ic_master_card)
            vgContainer.setOnClickListener { onItemClick.invoke(item) }
        }
    }
}