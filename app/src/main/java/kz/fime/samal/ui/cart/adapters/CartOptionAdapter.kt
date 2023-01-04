package kz.fime.samal.ui.cart.adapters

import kz.fime.samal.databinding.ItemCartOptionBinding
import kz.fime.samal.databinding.ItemCartProductBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.InnerItem
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.loadUrl

class CartOptionAdapter: BindingRvAdapter<CartItem, ItemCartOptionBinding>(ItemCartOptionBinding::inflate) {

    fun getList() = items

    override fun bind(item: CartItem, binding: ItemCartOptionBinding) {
        binding.run {

            when(item.count){

                -1 -> {
                    tvPrice.text = "${item.price}₸"
                    tvProduct.text = "${item.title}"
                }

                -2 -> {
                    tvPrice.text = "${item.price}₸"
                    tvProduct.text = "${item.title}"
                }

                else -> {
                    tvPrice.text = "${item.count*item.price}₸"
                    tvProduct.text = "${item.count} x ${item.title}"
                }
            }


//            if (item.count == -1) {
//                tvPrice.text = "${item.price}₸"
//                tvProduct.text = "${item.title}"
//            }else if(item.count == -2){
//
//            } else {
//                tvPrice.text = "${item.count*item.price}₸"
//                tvProduct.text = "${item.count} x ${item.title}"
//            }
        }
    }
}