package kz.fime.samal.ui.profile

import kz.fime.samal.data.models.CardsResponse
import kz.fime.samal.databinding.ItemCardsBinding
import kz.fime.samal.utils.binding.BindingRvAdapter

class CardsAdapter(private val onItemClick: (item: CardsResponse.UserCards) -> Unit)
    : BindingRvAdapter<CardsResponse.UserCards, ItemCardsBinding>(ItemCardsBinding::inflate) {

    override fun bind(item: CardsResponse.UserCards, binding: ItemCardsBinding) {
        binding.run {
            tvTitle.text = item.card_id
            tvSubtitle.text = "••••${item.card_hash.substring(12,16)}"
            vgContainer.setOnClickListener { onItemClick.invoke(item) }
        }
    }
}