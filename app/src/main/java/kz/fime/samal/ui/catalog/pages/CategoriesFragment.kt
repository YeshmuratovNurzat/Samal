package kz.fime.samal.ui.catalog.pages

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.databinding.FragmentCatalogCategoriesBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.catalog.adapters.CategoriesAdapter
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.components.BottomBar
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.setShimmerView
import timber.log.Timber

@AndroidEntryPoint
class CategoriesFragment: BindingFragment<FragmentCatalogCategoriesBinding>(FragmentCatalogCategoriesBinding::inflate) {

    private val viewModel: CategoriesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        BottomBar.getBottomBar()?.showMain()
        binding.run {
            setShimmerView(contentLoading.root)

            srl.setOnRefreshListener { viewModel.getCategories() }

            etSearch.setOnClickListener {
                findNavController().navigate(R.id.search, bundleOf(Pair("type", "product")))
            }
            val categoriesAdapter = CategoriesAdapter {
                Timber.d("Got: %s", it)
//                findNavController().navigate(R.id.action_global_category_products, bundleOf(Pair("name", it.getOrNull("name", "")), Pair("slug", it.getOrNull("category_slug", ""))))
                findNavController().navigate(R.id.action_global_subcategories, bundleOf(Pair("name", it.getOrNull("name", "")), Pair("slug", it.getOrNull("category_slug", ""))))
            }
            rvCategories.adapter = categoriesAdapter

            viewModel.categories.observeState(viewLifecycleOwner, {
                categoriesAdapter.submitList(it.result)
            }, {

            }, {
                srl.isRefreshing = it
                if (it) {
                    rvCategories.visibility = View.GONE
                    contentLoading.root.visibility = View.VISIBLE
                } else {
                    rvCategories.visibility = View.VISIBLE
                    contentLoading.root.stopShimmer()
                    contentLoading.root.visibility = View.GONE
                }
            })

        }
    }

}