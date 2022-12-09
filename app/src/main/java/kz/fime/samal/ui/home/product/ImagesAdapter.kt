package kz.fime.samal.ui.home.product


import kz.fime.samal.data.models.Image
import kz.fime.samal.databinding.ItemImageBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.loadUrl

class ImagesAdapter : BindingRvAdapter<Image, ItemImageBinding>(ItemImageBinding::inflate) {

    override fun bind(item: Image, binding: ItemImageBinding) {
        binding.run {
            iv.loadUrl(item.url)
        }
    }

}