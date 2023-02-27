package kz.fime.samal.ui.home.product.pages

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kz.fime.samal.R
import kz.fime.samal.databinding.FragmentProductDetailsDescriptionBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.home.adapters.ProductPropertiesAdapter
import kz.fime.samal.ui.home.product.ProductDetailsViewModel
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.extensions.asHtml

class DescriptionFragment: BindingFragment<FragmentProductDetailsDescriptionBinding>(FragmentProductDetailsDescriptionBinding::inflate) {

    private val viewModel: ProductDetailsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {

            val productPropertiesAdapter = ProductPropertiesAdapter()
            rv.adapter = productPropertiesAdapter

            viewModel.productDetailed.observeState(viewLifecycleOwner, {
                it.result?.let { product ->
                    productPropertiesAdapter.submitList(product.properties)
                    tvContent.text = product.description?.trim().asHtml()
                    viewStoreProducts.text = "Посмотреть все товары магазина"
                    shopAddress.text = "Адрес магазина"

                    shopAddress.setOnClickListener {
                        val shopAddress = product.shop.two_gis
                        val twoGis = Uri.parse(shopAddress)
                        val intent = Intent(Intent.ACTION_VIEW, twoGis)
                        startActivity(intent)
                    }

                    viewStoreProducts.setOnClickListener {
                        findNavController().navigate(
                            R.id.action_global_shop,
                            bundleOf(Pair("id", product.shop.uuid))
                        )
                    }
                }
            }, {

            }, {

            })
        }
    }

}