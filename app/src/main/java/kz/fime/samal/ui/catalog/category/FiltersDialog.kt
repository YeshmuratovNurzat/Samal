package kz.fime.samal.ui.catalog.category

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import kz.fime.samal.R
import kz.fime.samal.databinding.FilterDialogBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.catalog.adapters.FilterAdapter
import kz.fime.samal.ui.home.product.ProductDetailsViewModel
import kz.fime.samal.utils.binding.BindingBottomSheetFragment
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull

class FiltersDialog : BindingBottomSheetFragment<FilterDialogBinding>(FilterDialogBinding::inflate) {

    private val viewModel: ProductDetailsViewModel by activityViewModels()
    private var showPaymentMethod = false
    private var selectedItem: Item? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            viewModel.getSort()
            val filterAdapter = FilterAdapter {
                selectedItem = it
                sortParameter.text = it.getOrNull("name","")
            }

            rvSortOptions.adapter = filterAdapter

            viewModel.getTypeSort.observeState(viewLifecycleOwner, {
                filterAdapter.submitList(it.result)
            })

            showPaymentMethod(showPaymentMethod)
            sortContainer.setOnClickListener {
                showPaymentMethod = !showPaymentMethod
                showPaymentMethod(showPaymentMethod)
            }

            btnApply.setOnClickListener {
                selectedItem?.let {
                    viewModel.fetchProducts.postValue(selectedItem)
                }
                dismiss()
            }
        }
    }

    private fun showPaymentMethod(show: Boolean) {
        binding.run {
            if (show){
                rvSortOptions.visibility = View.VISIBLE
                sortContainer.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray_93))
                ivPaymentMethodDropdown.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.gray_8))
                ivPaymentMethodDropdown.setImageResource(R.drawable.ic_arrow_up_24)
            } else {
                rvSortOptions.visibility = View.GONE
                sortContainer.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray_98))
                ivPaymentMethodDropdown.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.gray_72))
                ivPaymentMethodDropdown.setImageResource(R.drawable.ic_arrow_down_24)
            }
        }
    }

}