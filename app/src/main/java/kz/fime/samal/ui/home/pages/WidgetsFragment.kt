package kz.fime.samal.ui.home.pages

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.databinding.FragmentHomeWidgetsBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.home.adapters.ProductsAdapter
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.components.BottomBar
import kz.fime.samal.utils.extensions.ITEM_ARG
import kz.fime.samal.utils.gridItemDecorator
import timber.log.Timber

@AndroidEntryPoint
class WidgetsFragment: BindingFragment<FragmentHomeWidgetsBinding>(FragmentHomeWidgetsBinding::inflate) {

    private val viewModel: WidgetsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BottomBar.getBottomBar()?.showMain()
        binding.run {
//            val widget = arguments?.getParcelable<Widget>("item")!!
            toolbar.setTitle(arguments?.getString("name"))
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

            etSearch.setOnClickListener {
                findNavController().navigate(R.id.search, bundleOf(Pair("type", "product")))
            }

            val slug = arguments?.getString("slug")!!

            viewModel.getProducts(slug)

            val productsAdapter = ProductsAdapter {
                findNavController().navigate(R.id.action_global_product_details, bundleOf(Pair(ITEM_ARG, it)))
            }
            rvProducts.addItemDecoration(gridItemDecorator)
            rvProducts.adapter = productsAdapter
            viewModel.products.observe(viewLifecycleOwner) {
                Timber.d("Got: %s", it)
            }
            viewModel.products.observeState(viewLifecycleOwner, {
                productsAdapter.submitList(it.result)
            })
        }
    }
}