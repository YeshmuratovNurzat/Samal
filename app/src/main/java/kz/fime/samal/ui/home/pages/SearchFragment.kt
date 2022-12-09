package kz.fime.samal.ui.home.pages

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.data.models.Product
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.databinding.FragmentHomeSearchBinding
import kz.fime.samal.ui.catalog.adapters.CategoryProductsAdapter
import kz.fime.samal.ui.catalog.adapters.ShopsAdapter
import kz.fime.samal.ui.catalog.category.CategoryProductsViewModel
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.components.BottomBar
import kz.fime.samal.utils.extensions.ITEM_ARG
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.requestFocusAndShowKeyboard
import kz.fime.samal.utils.gridItemDecorator

@AndroidEntryPoint
class SearchFragment :
    BindingFragment<FragmentHomeSearchBinding>(FragmentHomeSearchBinding::inflate) {

    private val viewModel: SearchViewModel by viewModels()

    private var objects: MutableList<Item> = mutableListOf()

    private lateinit var productsAdapter: CategoryProductsAdapter
    private lateinit var shopsAdapter: ShopsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BottomBar.getBottomBar()?.hide()
        val type = arguments?.getString("type", "")!!
        binding.run {
            etSearch.requestFocusAndShowKeyboard()
            etSearch.doAfterTextChanged {
                if (type == "product")
                    viewModel.searchProduct(etSearch.text.toString())
                else if (type == "shop")
                    viewModel.searchShop(etSearch.text.toString())
            }
            productsAdapter = CategoryProductsAdapter {
                findNavController().navigate(
                    R.id.action_global_product_details, bundleOf(
                        Pair(
                            ITEM_ARG, Product(
                                shop_slug = it.getOrNull("shop_uuid", ""),
                                product_slug = it.getOrNull("product_slug", ""),
                                price = null,
                                name = null,
                                rating = null,
                                people_voted = null,
                                image = null
                            )
                        )
                    )
                )
            }
            shopsAdapter = ShopsAdapter {
                findNavController().navigate(
                    R.id.action_global_shop,
                    bundleOf(Pair("id", it.getOrNull("shop_uuid", "")))
                )
            }
            searchRv.addItemDecoration(gridItemDecorator)
            if (type == "product") {
                searchRv.adapter = productsAdapter
                val layoutManager = GridLayoutManager(requireContext(), 2)
                searchRv.layoutManager = layoutManager
            }
            else if (type == "shop") {
                searchRv.adapter = shopsAdapter
                val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                searchRv.layoutManager = layoutManager
            }
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.resultSearch.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    it.data?.let {
                        it.data?.let { it1 ->
                            objects.clear()
                            objects.addAll(it1) }
                        productsAdapter.submitList(objects)

                    }
                }
                Status.ERROR -> {
                }
            }
        }
        viewModel.resultSearchShop.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    it.data?.let {
                        it.data?.let { it1 ->
                            objects.clear()
                            objects.addAll(it1) }
                        shopsAdapter.submitList(objects)

                    }
                }
                Status.ERROR -> {
                }
            }
        }
    }

}