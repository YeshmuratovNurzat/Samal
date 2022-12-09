package kz.fime.samal.ui.catalog.shop


import kz.fime.samal.data.models.Image
import kz.fime.samal.databinding.ItemImageBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.InnerItem
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.loadUrl

class ImagesAdapter : BindingRvAdapter<InnerItem, ItemImageBinding>(ItemImageBinding::inflate) {

    override fun bind(item: InnerItem, binding: ItemImageBinding) {
        binding.run {
            iv.loadUrl(item.getOrNull("link"))
        }
    }

}