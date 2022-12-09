package kz.fime.samal.ui.catalog.shop

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.databinding.FragmentShopDetailsBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.catalog.adapters.CategoriesAdapter
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.extensions.InnerItem
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.setUpVpAdapter
import timber.log.Timber

@AndroidEntryPoint
class ShopFragment: BindingFragment<FragmentShopDetailsBinding>(FragmentShopDetailsBinding::inflate) {

    private val viewModel: ShopViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            viewModel.getShop(arguments?.getString("id", "")!!)

            val categoriesAdapter = CategoriesAdapter {
                Timber.d("Got: %s", it)
                findNavController().navigate(R.id.action_global_category_products, bundleOf(Pair("name", it.getOrNull("name", "")), Pair("slug", it.getOrNull("category_slug", ""))))
            }
            rvCategories.adapter = categoriesAdapter

            val imagesAdapter = ImagesAdapter()
            setUpVpAdapter(vpImages, imagesAdapter, dotsImages)

            viewModel.shop.observeState(viewLifecycleOwner, {
                it.result?.let {
                    tvName.text = it.getOrNull("name")
                    tvAddress.text = it.getOrNull("address")
                    imagesAdapter.submitList(it.getOrNull<List<InnerItem>>("images"))
                }
            }, {}, {})

            viewModel.shopCategories.observeState(viewLifecycleOwner, {
                categoriesAdapter.submitList(it.result)
            }, {}, {})
        }
    }

}