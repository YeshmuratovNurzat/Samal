package kz.fime.samal.ui.home.adapters

import kz.fime.samal.data.models.ProductProperties
import kz.fime.samal.databinding.ItemProductPropertiesBinding
import kz.fime.samal.utils.binding.BindingRvAdapter

class ProductPropertiesAdapter :
    BindingRvAdapter<ProductProperties, ItemProductPropertiesBinding>(ItemProductPropertiesBinding::inflate) {

    override fun bind(item: ProductProperties, binding: ItemProductPropertiesBinding) {
        binding.run {
            tvProperties.text = item.getProperties()
        }
    }
}