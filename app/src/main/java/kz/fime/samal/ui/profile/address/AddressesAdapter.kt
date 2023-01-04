package kz.fime.samal.ui.profile.address

import android.content.res.ColorStateList
import android.graphics.Color
import kz.fime.samal.databinding.ItemAddressBinding
import kz.fime.samal.databinding.ItemOrderBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.InnerItem
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull

class AddressesAdapter(
    private val onItemClick: (Item) -> Unit
): BindingRvAdapter<Item, ItemAddressBinding>(ItemAddressBinding::inflate) {

    override fun bind(item: Item, binding: ItemAddressBinding) {
        binding.run {
            tvTitle.text = item.getOrNull("name", "")
            tvSubtitle.text = "${item.getOrNull("street", "")}, ${item.getOrNull("house_number", "")}, ${item.getOrNull("apartment", "")}"
            root.setOnClickListener { onItemClick.invoke(item) }
        }
    }
}