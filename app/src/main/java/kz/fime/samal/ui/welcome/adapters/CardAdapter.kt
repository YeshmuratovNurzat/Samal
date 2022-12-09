package kz.fime.samal.ui.welcome.adapters

import android.view.View
import kz.fime.samal.databinding.ItemSplashCardBinding
import kz.fime.samal.databinding.ItemWelcomeInfoBinding
import kz.fime.samal.utils.binding.BindingSliderRvAdapter

class CardAdapter: BindingSliderRvAdapter<Int, ItemSplashCardBinding>(ItemSplashCardBinding::inflate) {

    override fun bind(item: Int, binding: ItemSplashCardBinding) {
        if (item == 0) {
            binding.container.visibility = View.INVISIBLE
        } else {
            binding.container.visibility = View.VISIBLE
            binding.iv.setImageResource(item)
        }
    }

}