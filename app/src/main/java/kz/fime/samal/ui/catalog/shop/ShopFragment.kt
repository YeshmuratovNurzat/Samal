package kz.fime.samal.ui.catalog.shop

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.databinding.FragmentShopDetailsBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.catalog.adapters.CategoriesAdapter
import kz.fime.samal.utils.EditTextUtils
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.extensions.InnerItem
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.setUpVpAdapter
import timber.log.Timber

@AndroidEntryPoint
class ShopFragment: BindingFragment<FragmentShopDetailsBinding>(FragmentShopDetailsBinding::inflate) {

    private val viewModel: ShopViewModel by viewModels()
    private var shopPhone : String? = null
    private var shopUuid : String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            viewModel.getShop(arguments?.getString("id", "")!!)

            val shopAdapter = ShopAdapter {
                Timber.d("Got: %s", it)
                findNavController().navigate(R.id.action_global_subcategories,
                    bundleOf(
                        Pair("name", it.getOrNull("name", "")),
                        Pair("slug", it.getOrNull("category_slug", "")),
                        Pair("shop_id", shopUuid)))
            }
            rvCategories.adapter = shopAdapter

            phone.setOnClickListener {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel: $shopPhone")
                startActivity(callIntent)
            }

            val imagesAdapter = ImagesAdapter()
            setUpVpAdapter(vpImages, imagesAdapter, dotsImages)

            viewModel.shop.observeState(viewLifecycleOwner, {
                it.result?.let {
                    tvName.text = it.getOrNull("name")
                    tvAddress.text = it.getOrNull("address")
                    shopPhone = EditTextUtils.getPhoneMasked(it.getOrNull<String>("phone").toString())
                    imagesAdapter.submitList(it.getOrNull<List<InnerItem>>("images"))
                    shopUuid = it.getOrNull("shop_uuid","")
                }
            }, {}, {})

            viewModel.shopCategories.observeState(viewLifecycleOwner, {
                shopAdapter.submitList(it.result)
            }, {}, {})
        }
    }

}