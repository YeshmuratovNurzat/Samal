package kz.fime.samal.ui.profile.notifications

import kz.fime.samal.R
import kz.fime.samal.databinding.ItemNotificationBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.loadUrl

class NotificationsAdapter(
    private val onItemClick: (Item) -> Unit
    ): BindingRvAdapter<Item, ItemNotificationBinding>(ItemNotificationBinding::inflate) {

    override fun bind(item: Item, binding: ItemNotificationBinding) {
        binding.run {
            tvTitle.text = item.getOrNull("title","")
            tvSubtitle.text = item.getOrNull("message","")
            root.setOnClickListener { onItemClick.invoke(item) }

            if(item.getOrNull("type","") == "order_status"){
                iv.setImageResource(R.drawable.ic_orders_yellow)
            }
        }
    }
}