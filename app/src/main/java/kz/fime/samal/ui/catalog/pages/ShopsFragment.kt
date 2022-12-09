package kz.fime.samal.ui.catalog.pages

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.databinding.FragmentCatalogShopsBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.catalog.adapters.ShopsAdapter
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.components.BottomBar
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.setShimmerView

@AndroidEntryPoint
class ShopsFragment: BindingFragment<FragmentCatalogShopsBinding>(FragmentCatalogShopsBinding::inflate) {

    private val viewModel: ShopsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        BottomBar.getBottomBar()?.showMain()
        binding.run {
            setShimmerView(contentLoading.root)
            srl.setOnRefreshListener { viewModel.getShops() }

            val shopsAdapter = ShopsAdapter {
                findNavController().navigate(R.id.action_global_shop, bundleOf(Pair("id", it.getOrNull("shop_uuid", ""))))
            }
            rvShops.adapter = shopsAdapter

            etSearch.setOnClickListener {
                findNavController().navigate(R.id.search, bundleOf(Pair("type", "shop")))
            }
            viewModel.shops.observeState(viewLifecycleOwner, {
                shopsAdapter.submitList(it.result)
            }, {

            }, {
                srl.isRefreshing = it
                if (it) {
                    rvShops.visibility = View.GONE
                    contentLoading.root.visibility = View.VISIBLE
                } else {
                    rvShops.visibility = View.VISIBLE
                    contentLoading.root.stopShimmer()
                    contentLoading.root.visibility = View.GONE
                }
            })

        }
    }

}