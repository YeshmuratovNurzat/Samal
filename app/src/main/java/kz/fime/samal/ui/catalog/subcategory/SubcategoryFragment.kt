package kz.fime.samal.ui.catalog.subcategory

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.databinding.FragmentSubcategoryBinding
import kz.fime.samal.ui.catalog.category.CategoryProductsViewModel
import kz.fime.samal.utils.binding.BindingFragment

@AndroidEntryPoint
class SubcategoryFragment: BindingFragment<FragmentSubcategoryBinding>(FragmentSubcategoryBinding::inflate) {

    private val viewModel: CategoryProductsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            toolbar.title = arguments?.getString("name")
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

            val slug = arguments?.getString("slug")!!

            viewModel.getSubcategory(slug)

            observeViewModel()
        }
    }

    private fun observeViewModel() {
        viewModel.resultSubcategory.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    it.data?.let {
                        val adapter = SubcategoryAdapter {
                            findNavController().navigate(R.id.action_global_category_products, bundleOf(Pair("name", it.name), Pair("slug", it.category_slug)))
                        }
                        adapter.submitList(it.data)
                        binding.run {
                            subcategoryRv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                            subcategoryRv.adapter = adapter
                        }
                    }
                }
                Status.ERROR -> {
                }
            }
        })
    }

}