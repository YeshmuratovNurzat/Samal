package kz.fime.samal.ui.home.adapters

import androidx.core.view.ViewCompat
import kz.fime.samal.data.models.Product
import kz.fime.samal.data.models.Widget
import kz.fime.samal.databinding.ItemWidgetBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.gridItemDecorator

class WidgetsAdapter constructor(
    private val onWidgetClick: (item: Widget) -> Unit,
    private val onProductClick: (item: Product) -> Unit
): BindingRvAdapter<Widget, ItemWidgetBinding>(ItemWidgetBinding::inflate) {

    override fun bind(item: Widget, binding: ItemWidgetBinding) {
        binding.run {

            vgCategory.setOnClickListener { onWidgetClick.invoke(item) }
            tvCategory.text = item.name

            val productsAdapter = ProductsAdapter { item -> onProductClick.invoke(item)}
            ViewCompat.setNestedScrollingEnabled(rvProducts, false)
            rvProducts.removeItemDecoration(gridItemDecorator)
            rvProducts.addItemDecoration(gridItemDecorator)
            rvProducts.adapter = productsAdapter
            productsAdapter.submitList(item.products)
        }
    }

}