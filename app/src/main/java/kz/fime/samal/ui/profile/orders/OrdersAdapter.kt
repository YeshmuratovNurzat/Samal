package kz.fime.samal.ui.profile.orders

import android.content.res.ColorStateList
import android.graphics.Color
import kz.fime.samal.databinding.ItemOrderBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.InnerItem
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull

class OrdersAdapter(
    private val onItemClick: (Item) -> Unit
): BindingRvAdapter<Item, ItemOrderBinding>(ItemOrderBinding::inflate) {

    override fun bind(item: Item, binding: ItemOrderBinding) {
        binding.run {
            tvTitle.text = "Заказ №${item.getOrNull("number", 0)}"
            tvSubtitle.text = "${item.getOrNull("total_cost", 0)}₸ • ${item.getOrNull<InnerItem>("delivery").getOrNull("name", "")}"
            tvStatus.text = "${item.getOrNull<InnerItem>("status").getOrNull("name", "")}"
            tvStatus.backgroundTintList = ColorStateList.valueOf(Color.parseColor(item.getOrNull<InnerItem>("status").getOrNull("color", "#FFFFFF")))
            root.setOnClickListener { onItemClick.invoke(item) }
        }
    }

}