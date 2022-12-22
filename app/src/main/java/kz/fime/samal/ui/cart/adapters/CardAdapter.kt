package kz.fime.samal.ui.cart.adapters

import kz.fime.samal.R
import kz.fime.samal.databinding.ItemCardBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.loadUrl

class CardAdapter(private val onItemClick: (item: Item) -> Unit)
    : BindingRvAdapter<Item, ItemCardBinding>(ItemCardBinding::inflate) {

    override fun bind(item: Item, binding: ItemCardBinding) {
        binding.run {
            cardDescription.text = "Card ${item.getOrNull("card_hash","")}"
            iv.setImageResource(R.drawable.ic_master_card)
            vgContainer.setOnClickListener { onItemClick.invoke(item) }
        }
    }
}