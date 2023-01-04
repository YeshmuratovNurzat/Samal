package kz.fime.samal.ui.cart.adapters

import kz.fime.samal.R
import kz.fime.samal.databinding.ItemInstallmentBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.loadUrl

class InstallmentAdapter(private val onItemClick: (item: Item) -> Unit) : BindingRvAdapter<Item, ItemInstallmentBinding>(ItemInstallmentBinding::inflate) {

    override fun bind(item: Item, binding: ItemInstallmentBinding) {
        binding.run {
            tvDescription.text = item.getOrNull("short_description", "")
            iv.setImageResource(R.drawable.ic_home_credit_bank)
            vgContainer.setOnClickListener { onItemClick.invoke(item) }
        }
    }
}