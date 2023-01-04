package kz.fime.samal.ui.cart.adapters

import kz.fime.samal.R
import kz.fime.samal.databinding.ItemPaymentOptionBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull

class PaymentOptionAdapter(
    private val onItemClick: (item: Item) -> Unit
): BindingRvAdapter<Item, ItemPaymentOptionBinding>(ItemPaymentOptionBinding::inflate) {

    override fun bind(item: Item, binding: ItemPaymentOptionBinding) {
        binding.run {
            tvDescription.text = item.getOrNull("name", "")

            when(item.getOrNull("payment_id",1.0)){

                1.0 -> { iv.setImageResource(R.drawable.ic_cash) }

                2.0 -> { iv.setImageResource(R.drawable.ic_baseline_add_24) }

                5.0 -> { iv.setImageResource(R.drawable.ic_installment) }
            }
            vgContainer.setOnClickListener { onItemClick.invoke(item) }
        }
    }
}