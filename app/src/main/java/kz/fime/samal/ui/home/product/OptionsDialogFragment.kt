package kz.fime.samal.ui.home.product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import kz.fime.samal.data.models.ProductVariant
import kz.fime.samal.databinding.DialogCartBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.home.adapters.ProductOptionsAdapter
import kz.fime.samal.utils.binding.BindingBottomSheetFragment

class OptionsDialogFragment :
    BindingBottomSheetFragment<DialogCartBinding>(DialogCartBinding::inflate) {

    private val viewModel: ProductDetailsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shopUuid = arguments?.getString("shop_uuid", null)
        val productSlug = arguments?.getString("product_slug", null)
        val variants = arguments?.getParcelableArrayList<ProductVariant>("variants")

        binding.run {
            val productOptionsAdapter = ProductOptionsAdapter()
            rv.adapter = productOptionsAdapter

            viewModel.productDetailed.observeState(viewLifecycleOwner, { productDetailed ->
                productOptionsAdapter.submitList(productDetailed.result?.options)
                productDetailed.result?.product_variants?.map { it.product_variant_id }?.toSet()!!
                    .let {
                        productOptionsAdapter.productVariantId = it
                    }
            }, {}, {})

            productOptionsAdapter.complete.observe(viewLifecycleOwner, {
                btnAddToCart.isEnabled = it
                val price =
                    variants?.first { it.product_variant_id == productOptionsAdapter.productVariantId.first() }?.price

               if(it) btnAddToCart.text = "В корзину за $price ₸"
                else btnAddToCart.text = "Добавить в корзину"

            })

            btnAddToCart.setOnClickListener {
                if (shopUuid != null) {
                    if (productSlug != null) {
                        viewModel.addCartItem(
                            shopUuid,
                            productSlug,
                            productOptionsAdapter.productVariantId.first()
                        )
                    }
                }
                dismiss()
            }

        }
    }
}