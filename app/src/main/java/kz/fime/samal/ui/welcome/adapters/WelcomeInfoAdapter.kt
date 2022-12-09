package kz.fime.samal.ui.welcome.adapters

import kz.fime.samal.databinding.ItemWelcomeInfoBinding
import kz.fime.samal.utils.binding.BindingSliderRvAdapter

class WelcomeInfoAdapter: BindingSliderRvAdapter<WelcomeInfo, ItemWelcomeInfoBinding>(ItemWelcomeInfoBinding::inflate) {

    override fun bind(item: WelcomeInfo, binding: ItemWelcomeInfoBinding) {
        binding.run {
            tvTitle.text = item.title
            tvContent.text = item.content
        }
    }

}