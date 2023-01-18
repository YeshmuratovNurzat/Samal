package kz.fime.samal.ui.catalog.category

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.data.models.Product
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.databinding.FragmentCatalogProductsBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.catalog.adapters.CategoryProductsAdapter
import kz.fime.samal.ui.home.product.ProductDetailsViewModel
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.components.BottomBar
import kz.fime.samal.utils.extensions.ITEM_ARG
import kz.fime.samal.utils.extensions.InnerItem
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.gridItemDecorator
import timber.log.Timber

@AndroidEntryPoint
class CategoryProductsFragment: BindingFragment<FragmentCatalogProductsBinding>(FragmentCatalogProductsBinding::inflate) {

    private val viewModel: CategoryProductsViewModel by viewModels()
    private val activityViewModel: ProductDetailsViewModel by activityViewModels()

    private var isLoading = false
    private var objects: MutableList<Item> = mutableListOf()
    private var sort: Item = hashMapOf(Pair("sortDir", "asc"), Pair("sortBy", "min_price"))
    private lateinit var slug: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BottomBar.getBottomBar()?.showMain()
        binding.run {
            toolbar.setTitle(arguments?.getString("name"))
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

            filterIcon.setOnClickListener {
                findNavController().navigate(R.id.filtersDialog)
            }

            slug = arguments?.getString("slug")!!

            viewModel.getProducts(slug, null, sort)

            val productsAdapter = CategoryProductsAdapter {
                findNavController().navigate(
                    R.id.action_global_product_details, bundleOf(Pair(
                        ITEM_ARG, Product(
                            shop_slug = it.getOrNull("shop_uuid", ""),
                            product_slug = it.getOrNull("product_slug", ""),
                            price = null, name = null, rating = null, people_voted = null, image = null
                        )))
                )
            }
            rvProducts.addItemDecoration(gridItemDecorator)
            rvProducts.adapter = productsAdapter
            viewModel.products.observe(viewLifecycleOwner) {
                Timber.d("Got: %s", it)
            }
            viewModel.products.observeState(viewLifecycleOwner, {
                productsAdapter.submitList(it.result)
            })
            viewModel.resultProducts.observe(viewLifecycleOwner) {
                Timber.d("resultProducts: %s", it)
                when (it.status) {
                    Status.LOADING -> {

                    }
                    Status.SUCCESS -> {
                        it.data?.let {
                            it.data?.let { it1 -> objects.addAll(it1)}
                            productsAdapter.submitList(objects)
                            isLoading = false
                            rvProducts.addOnScrollListener(object :
                                RecyclerView.OnScrollListener() {
                                override fun onScrolled(
                                    recyclerView: RecyclerView,
                                    dx: Int,
                                    dy: Int
                                ) {
                                    super.onScrolled(recyclerView, dx, dy)
                                    if (dy > 0) {
                                        val linearLayoutManager =
                                            recyclerView.layoutManager as LinearLayoutManager?
                                        val visibleItemCount = linearLayoutManager?.childCount
                                        val totalItemCount = linearLayoutManager?.itemCount
                                        val pastVisibleItems =
                                            linearLayoutManager?.findFirstVisibleItemPosition()
                                        val nextPage = it.links.getOrNull("next", "")
                                        if (nextPage != null) {
                                            if (!isLoading) {
                                                if (visibleItemCount != null && pastVisibleItems != null && totalItemCount != null) {
                                                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                                        val pages = nextPage.split("?")
                                                        var page = pages[pages.lastIndex]
                                                        page = page.substring(5)
                                                        viewModel.getProducts(slug, page, sort)
                                                        isLoading = true
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            })
                        }
                    }
                    Status.ERROR -> {
                    }
                }
            }

            activityViewModel.fetchProducts.observe(viewLifecycleOwner) {
                objects.clear()
                val sBy: Any = it.getOrNull("sortBy", "") ?: "min_price"
                val sDir: Any = it.getOrNull("sortDir", "") ?: "desc"
                sort = hashMapOf(Pair("sortDir", sDir), Pair("sortBy", sBy))
                viewModel.getProducts(slug, null, sort)
            }

        }
    }
}