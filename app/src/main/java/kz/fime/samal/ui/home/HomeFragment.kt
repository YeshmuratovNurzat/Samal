package kz.fime.samal.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sm.dinio.utils.extensions.removeOverScroll
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.data.models.Product
import kz.fime.samal.databinding.FragmentHomeBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.home.adapters.BannerSliderAdapter
import kz.fime.samal.ui.home.adapters.WidgetsAdapter
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.components.BottomBar
import kz.fime.samal.utils.extensions.ITEM_ARG
import kz.fime.samal.utils.extensions.setUpMenu
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment: BindingFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BottomBar.getBottomBar()?.showMain()
        binding.run {
            srl.setOnRefreshListener {
                viewModel.loadData()
            }

            searchIcon.setOnClickListener {
                findNavController().navigate(R.id.search, bundleOf(Pair("type", "product")))
            }

            viewModel.state.observeState(viewLifecycleOwner, {}, {
                Timber.d("Error: %s", it)
                // TODO: Show network error placeholder
            }, {
                srl.isRefreshing = it
            })

            val bannersAdapter = BannerSliderAdapter { item ->
                when (item.type) {
                    "category" -> {
                        findNavController().navigate(R.id.action_global_category_products,
                            bundleOf(Pair("name", item.title),
                                Pair("slug", item.category_slug)))
                    }
                    "shop" -> {
                        findNavController().navigate(R.id.action_global_shop, bundleOf(Pair("id", item.shop_uuid)))
                    }
                    "product" -> {
                        findNavController().navigate(
                            R.id.action_global_product_details, bundleOf(Pair(
                                ITEM_ARG, Product(
                                    shop_slug = item.shop_uuid,
                                    product_slug = item.product_slug,
                                    price = null, name = null, rating = null, people_voted = null, image = null
                                )
                            ))
                        )
                    }
                }
            }
            cvpBanners.adapter = bannersAdapter
            cvpBanners.removeOverScroll()
            viewModel.banners.observeState(viewLifecycleOwner, {
                Timber.d("Banners: %s", it)
                it.result?.let {
                    binding.toolbar.title = it.title
                    it.banners.let {
                        bannersAdapter.submitList(it)
                    }
                }
            })

            val widgetsAdapter = WidgetsAdapter({ widget ->
                findNavController().navigate(R.id.action_global_home_widgets, bundleOf(Pair("name", widget.name), Pair("slug", widget.slug)))
            }, { product ->
//                findNavController().navigate(R.id.action_global_home_details, bundleOf(Pair(ITEM_ARG, product)))
                findNavController().navigate(R.id.action_global_product_details, bundleOf(Pair(ITEM_ARG, product)))
            })
            rvWidgets.adapter = widgetsAdapter
            viewModel.widgets.observeState(viewLifecycleOwner, {
                Timber.d("Widgets: %s", it)
                widgetsAdapter.submitList(it.result)
            })
        }
    }
}