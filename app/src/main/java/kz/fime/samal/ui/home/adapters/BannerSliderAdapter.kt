package kz.fime.samal.ui.home.adapters

import kz.fime.samal.data.models.Banner
import kz.fime.samal.databinding.ItemBannerBinding
import kz.fime.samal.utils.binding.BindingSliderRvAdapter
import kz.fime.samal.utils.extensions.loadUrl

class BannerSliderAdapter constructor(
    private val onItemClick: (item: Banner) -> Unit
): BindingSliderRvAdapter<Banner, ItemBannerBinding>(ItemBannerBinding::inflate) {


    override fun bind(item: Banner, binding: ItemBannerBinding) {
        binding.run {
//            tvTitle.text = item.title
//            tvCategory.text = item.subtitle
//            tvPrice.text = item.price?.let { "${item.price}₸" } ?: ""
            iv.loadUrl(item.image)
            root.setOnClickListener { onItemClick.invoke(item) }
        }
    }

}