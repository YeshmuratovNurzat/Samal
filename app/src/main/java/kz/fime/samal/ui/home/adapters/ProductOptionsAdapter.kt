package kz.fime.samal.ui.home.adapters

import android.view.LayoutInflater
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.chip.Chip
import kz.fime.samal.R
import kz.fime.samal.data.models.ProductOptions
import kz.fime.samal.databinding.ItemProductOptionsBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import okhttp3.internal.notifyAll
import timber.log.Timber

class ProductOptionsAdapter :
    BindingRvAdapter<ProductOptions, ItemProductOptionsBinding>(ItemProductOptionsBinding::inflate) {

    var productVariantId = listOf<Int>().toSet()
    var complete = MutableLiveData<Boolean>()

    override fun bind(item: ProductOptions, binding: ItemProductOptionsBinding) {
        binding.run {
            tvTitle.text = item.name
            cg.removeAllViews()
            complete.value = isComplete()

            item.data?.forEach {
                val chip =
                    LayoutInflater.from(cg.context).inflate(R.layout.item_chip, null, false) as Chip
                chip.text = it.value
                chip.isCheckable = true

                chip.isEnabled =
                    it.product_variant_id?.let { it1 ->
                        productVariantId.intersect(it1).isNotEmpty()
                    } == true

                chip.isChecked = it.selected == true

                chip.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        if (item.option_selected == true) {
                            item.data
                                .flatMap { it.product_variant_id!! }
                                .let { it1 ->
                                    productVariantId = productVariantId.union(it1)
                                    Timber.d("PRODID 2 %s", it1)
                                }
                            item.data.filter { it.selected == true }.forEach { it.selected = false }
                        }
                    } else {
                        item.data.flatMap { it.product_variant_id!! }
                            .let { it1 ->
                                productVariantId = productVariantId.union(it1)
                            }
                        Timber.d("PRODID 3 %s", productVariantId)
                    }
                    it.selected = isChecked
                    item.option_selected = isChecked
                    Timber.d("itemOption %s", item.option_selected)
                    notifyDataSetChanged()
                }
                cg.addView(chip)
            }
        }
        if (item.option_selected == true) {
            item.data?.filter { it.selected == true }?.flatMap { it.product_variant_id!! }!!
                .let {
                    productVariantId = productVariantId.intersect(it)
                }
            Timber.d("PRODID 1 %s", productVariantId)
        }
    }

    private fun isComplete(): Boolean {
        if (items.filter { it.option_selected == true }.size == items.size) return true
        return false
    }
}
