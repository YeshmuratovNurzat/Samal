package kz.fime.samal.ui.catalog.subcategory

import kz.fime.samal.data.models.Categories
import kz.fime.samal.databinding.ItemSubcategoryBinding
import kz.fime.samal.utils.binding.BindingRvAdapter

class SubcategoryAdapter(
    private val onItemClick: (item: Categories) -> Unit
) : BindingRvAdapter<Categories, ItemSubcategoryBinding>(ItemSubcategoryBinding::inflate) {

    override fun bind(item: Categories, binding: ItemSubcategoryBinding) {
        binding.run {
            subcategoryTitle.text = item.name
            subcategoryTitle.setOnClickListener {
                onItemClick.invoke(item)
            }
        }
    }

}