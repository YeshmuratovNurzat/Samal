package kz.fime.samal.ui.profile.orders

import kz.fime.samal.databinding.ItemOrderDetailBinding
import kz.fime.samal.utils.binding.BindingRvAdapter

class OrderDetailAdapter : BindingRvAdapter<ProductItem, ItemOrderDetailBinding>(ItemOrderDetailBinding::inflate) {

    override fun bind(item: ProductItem, binding: ItemOrderDetailBinding) {
        binding.run {
            if (item.name == "delivery") {
                orderName.text = "Доставка"
            } else {
                orderName.text = item.count.toString() + " x " + item.name
            }
            orderPrice.text = item.cost.toString() + "₸"
        }
    }

}